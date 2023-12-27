package at.ac.htlinn.courseManagement.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.ActivityDTO;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.ContestDTO;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.activity.model.ExerciseDTO;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.courseUser.CourseUserService;
import at.ac.htlinn.role.Role;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;

@RestController
@RequestMapping("/activities")
public class ActivityController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private CourseUserService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * GET activity by id
	 * requires @PathVariable activityId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("{activityId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getActivityById(@PathVariable int activityId) {
		Activity activity = activityService.getActivityById(activityId);
		if (activity == null) return new ResponseEntity<>("Activity does not exist!", HttpStatus.NOT_FOUND);
		
		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!studentService.isUserInCourse(user.getId(), activity.getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its activities.", HttpStatus.FORBIDDEN);
		}

		// create according DTO object
		ActivityDTO activityDTO = null;
		if (activity instanceof Exercise)
			activityDTO = new ExerciseDTO((Exercise)activity);
		else
			activityDTO = new ContestDTO((Contest)activity);
		return new ResponseEntity<>(activityDTO, HttpStatus.OK);
	}
	
	/**
	 * GET all activities for a course
	 * requires @RequestParam courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllActivitiesByCourseId(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!studentService.isUserInCourse(user.getId(), course.getId()))
				return new ResponseEntity<>("You must be in this course to view its activities.", HttpStatus.FORBIDDEN);
		}
		
		List<ActivityDTO> activities = new ArrayList<ActivityDTO>();
		for (Activity activity : activityService.getAllActivitiesInCourse(courseId)) {
			
			// add exercise or contest to list
			if (activity instanceof Exercise)
				activities.add(new ExerciseDTO((Exercise)activity));
			else
				activities.add(new ContestDTO((Contest)activity));
		}
		return new ResponseEntity<>(activities, HttpStatus.OK);
	}
	
	/**
	 * POST activity for existing course
	 * requires in @RequestBody activity object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createActivity(@RequestBody JsonNode node) {
		// get DTO object from JSON
		ExerciseDTO exerciseDTO = mapper.convertValue(node.get("exercise"), ExerciseDTO.class);
		ContestDTO contestDTO = mapper.convertValue(node.get("contest"), ContestDTO.class);
		if (exerciseDTO != null && contestDTO != null)
			return new ResponseEntity<>("Request body must not include both an exercise and a contest object.",
					HttpStatus.BAD_REQUEST);
		
		// parse DTO object accordingly
		Activity activity = null;
		if (exerciseDTO != null)
			activity = new Exercise(exerciseDTO, courseService);
		else
			activity = new Contest(contestDTO, courseService);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && activity.getCourse().getTeacher().getId() != user.getId()) {
				return new ResponseEntity<>("You must be this courses teacher to add an activity to it.", HttpStatus.FORBIDDEN);
			}
		}
		
		activity = activityService.saveActivity(activity);
		
		return activity != null ? new ResponseEntity<>(activity.getId(), HttpStatus.OK)
				: new ResponseEntity<>("Could not create activity!", HttpStatus.BAD_REQUEST);
	}
	
	// TODO: possibly replace with PUT
	/**
	 * PATCH activity
	 * requires @PathVariable activityId and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PatchMapping("{activityId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateActivity(@PathVariable int activityId, @RequestBody Map<String, Object> fields) {
		
	    // Sanitize and validate the data
	    if (activityId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    
	    Activity activity = activityService.getActivityById(activityId);
	    if (activity == null) return new ResponseEntity<>("Activity does not exist!", HttpStatus.NOT_FOUND);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && activity.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to change its activities.", HttpStatus.FORBIDDEN);
		}
		
		// TODO: improve
		// TODO: does not work: JSON field names do not match actual field names
		if (activity instanceof Exercise)
		    fields.forEach((k, v) -> {
		        Field field = ReflectionUtils.findField(Exercise.class, k); // find field in activity class
		        field.setAccessible(true); 
		        ReflectionUtils.setField(field, activity, v); // set given field for activity object to value V
		    });
		else
		    fields.forEach((k, v) -> {
			    System.out.println(k);
		        Field field = ReflectionUtils.findField(Contest.class, k); // find field in activity class
		        field.setAccessible(true); 
		        ReflectionUtils.setField(field, activity, v); // set given field for activity object to value V
		    });

	    activityService.saveActivity(activity);
	    return new ResponseEntity<>(activity.getId(), HttpStatus.OK);
	}

	/**
	 * DELETE existing activity
	 * requires @PathVariable activityId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("{activityId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteActivity(@PathVariable int activityId) {
	    Activity activity = activityService.getActivityById(activityId);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && activity.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to delete its activities.", HttpStatus.FORBIDDEN);
		}
		
		return activityService.deleteActivity(activityId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete activity!", HttpStatus.BAD_REQUEST);
	}
}
