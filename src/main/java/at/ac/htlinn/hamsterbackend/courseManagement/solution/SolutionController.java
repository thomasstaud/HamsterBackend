package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.ActivityService;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ActivityService activityService;
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping("{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getSolutionById(@PathVariable int solutionId, Principal principal) {
		Solution solution = solutionService.getSolutionById(solutionId); 
		if (solution == null) return new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user)) {
			// if user is not teacher of this course, they must have created the solution
			if ((user.getId() != solution.getActivity().getCourse().getTeacher().getId())
					&& user.getId() != solution.getStudent().getId())
				return new ResponseEntity<>("You cannot view other student's solutions.", HttpStatus.FORBIDDEN);
		}
		
		return ResponseEntity.ok(new SolutionDto(solution));
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllSolutions(
			@RequestParam(name = "activity_id", required = false) Integer activityId,
			@RequestParam(name = "student_id", required = false) Integer studentId,
			@RequestParam(name = "course_id", required = false) Integer courseId,
			Principal principal) {
		
		if (activityId != null) {
			// get all solutions for 1 activity
			
		    Activity activity = activityService.getActivityById(activityId);
		    if (activity == null) return new ResponseEntity<>("Activity does not exist!", HttpStatus.NOT_FOUND);

			// if user is a teacher, they must be teacher of the specified course
			User user = userService.findUserByUsername(principal.getName());
			if (!userService.isUserPrivileged(user) && activity.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its solutions.", HttpStatus.FORBIDDEN);
			
			List<SolutionDto> solutions = solutionService.getSolutionsByActivityId(activityId);
			return ResponseEntity.ok(solutions);
		}
		
		if (studentId != null || courseId != null) {
			// get all solutions for 1 students and 1 course
			
			if (studentId == null)
				return new ResponseEntity<>("Request is missing student id", HttpStatus.BAD_REQUEST);
			if (courseId == null)
				return new ResponseEntity<>("Request is missing course id", HttpStatus.BAD_REQUEST);
			
			// check if course exists
			Course course = courseService.getCourseById(courseId);
			if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
			
			// if user is a teacher, they must be teacher of the specified course
			User user = userService.findUserByUsername(principal.getName());
			if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its solutions.", HttpStatus.FORBIDDEN);
			
			List<SolutionDto> solutions = solutionService.getSolutionsByStudentId(studentId, courseId);
			return ResponseEntity.ok(solutions);
		}
		
		return new ResponseEntity<>("Request must include either activity_id or course_id and student_id!", HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addSolutionToActivity(@RequestBody JsonNode node, Principal principal) {
		SolutionDto solutionDto = mapper.convertValue(node.get("solution"), SolutionDto.class);
		if (solutionDto == null) return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
		
		// build solution from solutionDto
		User user = userService.findUserByUsername(principal.getName());
		solutionDto.setStudentName(user.getUsername());
		// set submissionDate to current Date
		solutionDto.setSubmissionDate(new Date());
		Solution solution = new Solution(solutionDto, activityService, userService);

		if (!userService.isUserPrivileged(user)) {
			// check if user is student in course
			if (!studentService.isUserStudent(user.getId(), solution.getActivity().getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to create a solution!", HttpStatus.FORBIDDEN);
		}

		int activityId = solutionDto.getActivityId();
		int userId = user.getId();

		if (solution.getId() != 0) {
			// update existing solution
			Solution existingSolution = solutionService.getSolutionById(solution.getId());
			
			// validation
			if (existingSolution == null)
				return new ResponseEntity<>("There is no solution with this ID!", HttpStatus.BAD_REQUEST);
			// check if user matches
			if (existingSolution.getStudent().getId() != userId)
				return new ResponseEntity<>("You can't change another student's solution!", HttpStatus.BAD_REQUEST);
			// check if activity matches
			if (existingSolution.getActivity().getId() != activityId)
				return new ResponseEntity<>("This solution belongs to another activity!", HttpStatus.BAD_REQUEST);
			// check if there is already feedback
			if (existingSolution.getFeedback() != null)
				return new ResponseEntity<>("Can't update solution: It was already feedbacked!", HttpStatus.BAD_REQUEST);
			
			// update fields
			existingSolution.setCode(solution.getCode());
			existingSolution.setSubmitted(solution.isSubmitted());
			solution = existingSolution;
		} else {
			// create new solution

			// check if solution already exists for this activity/student combination
			if (solutionService.getSolutionByActivityAndStudentId(activityId, userId) != null)
				return new ResponseEntity<>("Solution already exists!", HttpStatus.BAD_REQUEST);
		}
		
		solution = solutionService.saveSolution(solution);
		return solution != null ? ResponseEntity.ok(new SolutionDto(solution))
				: new ResponseEntity<>("Could not put solution!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PatchMapping("{solutionId}/feedback")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> feedbackSolution(@PathVariable int solutionId, @RequestBody JsonNode node, Principal principal) {
		Solution solution = solutionService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		if (!solution.isSubmitted()) new ResponseEntity<>("Solution was not submitted!", HttpStatus.BAD_REQUEST);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user) && solution.getActivity().getCourse().getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to give feedback!", HttpStatus.FORBIDDEN);
		
		String feedback = mapper.convertValue(node.get("feedback"), String.class);
		if (feedback == null) return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
		
		solution.setFeedback(feedback);
		
	    return solutionService.saveSolution(solution) != null ? ResponseEntity.ok(new SolutionDto(solution))
	    		: new ResponseEntity<>("Could not save feedback!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@DeleteMapping("{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> deleteSolution(@PathVariable int solutionId, Principal principal) {
		Solution solution = solutionService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user)) {
			// check if solution has already been given feedback
			if (solution.getFeedback() != null)
				return new ResponseEntity<>("You cannot delete solutions that were already feedbacked!", HttpStatus.FORBIDDEN);
			
			// check if user created the solution
			if (solution.getStudent().getId() != user.getId())
				return new ResponseEntity<>("You cannot delete another user's solution!", HttpStatus.FORBIDDEN);
		}
		
		return solutionService.deleteSolution(solutionId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete activity!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
