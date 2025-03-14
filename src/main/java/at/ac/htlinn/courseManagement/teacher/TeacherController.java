package at.ac.htlinn.courseManagement.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.htlinn.courseManagement.course.model.CourseDto;
import at.ac.htlinn.courseManagement.teacher.model.TeacherCourseDto;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDto;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private UserService userService;

	/**
	 * GET teacher by course id
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseTeacherByCourseId(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		User teacher = teacherService.getCourseTeacher(courseId);
		return ResponseEntity.ok(new UserDto(teacher));
	}
	
	/**
	 * GET all courses for active user (must be teacher)
	 * 
	 * or with optional @RequestParam course_id:
	 * GET info for one course
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("my-view")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getViewForLoggedInTeacher(
			@RequestParam(name = "course_id", required = false) Integer courseId) {
		
		User user = userService.getCurrentUser();
		
		if (courseId != null) {
			// return info for one course
			
			if (!userService.isUserPrivileged(user) && !teacherService.isUserTeacher(user.getId(), courseId))
				// check if user is teacher of the specified course
				return new ResponseEntity<>("You must be this courses teacher to view its details.", HttpStatus.FORBIDDEN);
			
			TeacherCourseDto courseView = teacherService.getCourseView(courseId);
			return ResponseEntity.ok(courseView);
		}
		
		// return all courses
		List<CourseDto> courses = teacherService.getTeacherView(user.getId());
		return ResponseEntity.ok(courses);
	}
}
