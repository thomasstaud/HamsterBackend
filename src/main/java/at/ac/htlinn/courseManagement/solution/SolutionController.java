package at.ac.htlinn.courseManagement.solution;

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

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.courseUser.CourseUserService;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionDTO;
import at.ac.htlinn.role.Role;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ActivityService exerciseService;
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private CourseUserService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * GET solution by id
	 * requires @PathVariable solutionId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getSolutionById(@PathVariable int solutionId) {
		Solution solution = solutionService.getSolutionById(solutionId); 
		if (solution == null) return new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.getCurrentUser();
		if (!(user.getRoles().contains(new Role(1, "ADMIN")) || user.getRoles().contains(new Role(2, "DEV")))) {
			// check if user is in course (either as student or as teacher)
			if (!studentService.isUserInCourse(user.getId(), solution.getActivity().getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its solutions.", HttpStatus.FORBIDDEN);
	
			// if user is a student, they must have created the solution
			for (Role role : user.getRoles()) {
				if (role.getRole().equals("USER") && solution.getStudent().getId() != user.getId())
					return new ResponseEntity<>("You cannot view other student's solutions.", HttpStatus.FORBIDDEN);
			}
		}
		
		return new ResponseEntity<>(new SolutionDTO(solution), HttpStatus.OK);
	}
	
	// TODO: clean up
	/**
	 * GET all solutions
	 * optional @RequestParam exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllSolutions(
			@RequestParam(name = "exercise_id", required = false) Integer exerciseId,
			@RequestParam(name = "student_id", required = false) Integer studentId,
			@RequestParam(name = "course_id", required = false) Integer courseId) {
		
		if (exerciseId != null) {
			// get all solutions for 1 exercise
			
		    Exercise exercise = exerciseService.getActivityById(exerciseId);
		    if (exercise == null) return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);

			// if user is a teacher, they must be teacher of the specified course
			User user = userService.getCurrentUser();
			for (Role role : user.getRoles()) {
				if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId()) {
					return new ResponseEntity<>("You must be this courses teacher to view its exercises.", HttpStatus.FORBIDDEN);
				}
			}
			
			List<SolutionDTO> solutions = solutionService.getSolutionsByExerciseId(exerciseId);
			return new ResponseEntity<>(solutions, HttpStatus.OK);
		}
		
		if (studentId != null || courseId != null) {
			// get all solutions for 1 students and 1 course
			
			if (studentId == null)
				return new ResponseEntity<>("You must include a student id", HttpStatus.BAD_REQUEST);
			if (courseId == null)
				return new ResponseEntity<>("You must include a course id", HttpStatus.BAD_REQUEST);
			
			// check if course exists and student is in course
			Course course = courseService.getCourseById(courseId);
			if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
			if (!studentService.isUserStudent(studentId, courseId))
				return new ResponseEntity<>("Student does not exist or is not in course!", HttpStatus.BAD_REQUEST);
			
			// if user is a teacher, they must be teacher of the specified course
			User user = userService.getCurrentUser();
			for (Role role : user.getRoles()) {
				if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
					return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
			}
			
			List<SolutionDTO> solutions = solutionService.getSolutionsByStudentId(studentId, courseId);
			return new ResponseEntity<>(solutions, HttpStatus.OK);
		}
		
		// get all solutions
		List<SolutionDTO> solutions = solutionService.getAllSolutions();
		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}
	
	/**
	 * PUT solution for an exercise
	 * requires in @RequestBody solution object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addSolutionToExercise(@RequestBody JsonNode node) {
		SolutionDTO solutionDTO = mapper.convertValue(node.get("solution"), SolutionDTO.class);
		User user = userService.getCurrentUser();
		solutionDTO.setStudentId(user.getId());
		// set submissionDate to current Date
		solutionDTO.setSubmissionDate(new Date());
		
		Solution solution = new Solution(solutionDTO, exerciseService, userService);

		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is student in course
			if (!studentService.isUserStudent(user.getId(), solution.getActivity().getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to create solutions.", HttpStatus.FORBIDDEN);
		}

		if (solution.getId() != 0) {
			// update existing solution
			Solution existingSolution = solutionService.getSolutionById(solution.getId());
			
			// check if id is correct
			if (existingSolution.getId() != solution.getId())
				return new ResponseEntity<>("Solution already exists with different ID!", HttpStatus.BAD_REQUEST);
			// check if there is already feedback
			if (existingSolution.getFeedback() != null)
				return new ResponseEntity<>("Can't update solution: It was already feedbacked!", HttpStatus.BAD_REQUEST);
			
			// update fields
			existingSolution.setCode(solution.getCode());
			existingSolution.setSubmitted(solution.isSubmitted());
			solution = existingSolution;
		} else {
			// create new solution

			// check if solution already exists for this exercise/student combination
			int exerciseId = solutionDTO.getActivityId();
			int userId = user.getId();
			if (solutionService.getSolutionByExerciseAndStudentId(exerciseId, userId) != null)
				return new ResponseEntity<>("Solution already exists!", HttpStatus.BAD_REQUEST);
		}
		
		solution = solutionService.saveSolution(solution);
		return solution != null ? new ResponseEntity<>(new SolutionDTO(solution), HttpStatus.OK)
				: new ResponseEntity<>("Could not update solution!", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * PATCH feedback to a solution
	 * requires @PathVariable solutionId + in @RequestBody feedback object
	 * 
	 * @param json
	 * @return
	 */
	@PatchMapping("{solutionId}/feedback")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> feedbackSolution(@PathVariable int solutionId, @RequestBody JsonNode node) {
		Solution solution = solutionService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		if (!solution.isSubmitted()) new ResponseEntity<>("Solution was not submitted!", HttpStatus.BAD_REQUEST);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && solution.getActivity().getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to give feedback.", HttpStatus.FORBIDDEN);
		}
		
		String feedback = mapper.convertValue(node.get("feedback"), String.class);
		solution.setFeedback(feedback);
		
		System.out.println(feedback);
		
		solutionService.saveSolution(solution);
	    return new ResponseEntity<>(new SolutionDTO(solution), HttpStatus.OK);
	}
	
	/**
	 * DELETE existing solution
	 * requires @PathVariable solutionId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> deleteSolution(@PathVariable int solutionId) {
		Solution solution = solutionService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		
		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if solution has already been given feedback
			if (solution.getFeedback() != null)
				return new ResponseEntity<>("You cannot delete solutions that were already feedbacked.", HttpStatus.FORBIDDEN);
			
			// check if user created the solution
			if (solution.getStudent().getId() != user.getId())
				return new ResponseEntity<>("You cannot delete other student's solutions.", HttpStatus.FORBIDDEN);
		}
		
		return solutionService.deleteSolution(solutionId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}
}
