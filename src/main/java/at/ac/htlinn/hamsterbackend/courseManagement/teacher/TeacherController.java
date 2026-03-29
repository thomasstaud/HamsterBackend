package at.ac.htlinn.hamsterbackend.courseManagement.teacher;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.teacher.dto.TeacherCourseDto;
import at.ac.htlinn.hamsterbackend.user.model.User;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.dto.UserDto;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private UserService userService;

	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getTeacherByCourseId(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		User teacher = teacherService.getCourseTeacher(courseId);
		return ResponseEntity.ok(new UserDto(teacher));
	}
	
	@GetMapping("my-view")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getViewForLoggedInTeacher(
			@RequestParam(name = "course_id", required = false) Integer courseId, Principal principal) {

		User user = userService.findUserByUsername(principal.getName());
		
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
