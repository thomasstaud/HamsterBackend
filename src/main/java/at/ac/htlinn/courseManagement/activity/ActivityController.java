package at.ac.htlinn.courseManagement.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import at.ac.htlinn.courseManagement.activity.model.ActivityDto;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.ContestDto;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.activity.model.ExerciseDto;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.student.StudentService;
import at.ac.htlinn.courseManagement.teacher.TeacherService;
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
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;
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

		// if user is student or teacher, check if user is in course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user))
			if (!studentService.isUserStudent(user.getId(), activity.getCourse().getId())
					&& !teacherService.isUserTeacher(user.getId(), activity.getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its activities.", HttpStatus.FORBIDDEN);

		// create according DTO object
		ActivityDto activityDTO = null;
		if (activity instanceof Exercise)
			activityDTO = new ExerciseDto((Exercise)activity);
		else
			activityDTO = new ContestDto((Contest)activity);
		return ResponseEntity.ok(activityDTO);
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

		// if user is student or teacher, check if user is in course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user))
			if (!studentService.isUserStudent(user.getId(), course.getId())
					&& !teacherService.isUserTeacher(user.getId(), course.getId()))
				return new ResponseEntity<>("You must be in this course to view its activities.", HttpStatus.FORBIDDEN);
		
		List<ActivityDto> activities = new ArrayList<ActivityDto>();
		for (Activity activity : activityService.getAllActivitiesInCourse(courseId)) {
			
			// add exercise or contest to list
			if (activity instanceof Exercise)
				activities.add(new ExerciseDto((Exercise)activity));
			else
				activities.add(new ContestDto((Contest)activity));
		}
		return ResponseEntity.ok(activities);
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
		ExerciseDto exerciseDto = mapper.convertValue(node.get("exercise"), ExerciseDto.class);
		ContestDto contestDto = mapper.convertValue(node.get("contest"), ContestDto.class);
		if (exerciseDto == null && contestDto == null)
			return new ResponseEntity<>("Request body must include either an exercise or a contest!", HttpStatus.BAD_REQUEST);
		if (exerciseDto != null && contestDto != null)
			return new ResponseEntity<>("Request body cannot include both an exercise and a contest!", HttpStatus.BAD_REQUEST);
		
		// parse DTO object accordingly
		Activity activity = null;
		if (exerciseDto != null)
			activity = new Exercise(exerciseDto, courseService);
		else
			activity = new Contest(contestDto, courseService);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && activity.getCourse().getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to add an activity to it!", HttpStatus.FORBIDDEN);
		
		activity = activityService.saveActivity(activity);
		return activity != null ? ResponseEntity.ok(activity.getId())
				: new ResponseEntity<>("Could not create activity!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
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
		if (!userService.isUserPrivileged(user) && activity.getCourse().getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to change its activities.", HttpStatus.FORBIDDEN);
		
		try {
			activity = activityService.updateActivity(activity, fields);
			return activity != null ? ResponseEntity.ok(activity.getId())
					: new ResponseEntity<>("Could not update activity!", HttpStatus.INTERNAL_SERVER_ERROR);
			
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(String.format("Field %s is invalid!", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(String.format("Field %s could not be changed!", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
		if (!userService.isUserPrivileged(user) && activity.getCourse().getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to delete its activities.", HttpStatus.FORBIDDEN);
		
		return activityService.deleteActivity(activityId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete activity!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
