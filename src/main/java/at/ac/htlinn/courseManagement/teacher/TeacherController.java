package at.ac.htlinn.courseManagement.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.ac.htlinn.courseManagement.teacher.model.TeacherCourseDto;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDTO;

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
	public ResponseEntity<?> getCourseTeacherByCourseID(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		User teacher = teacherService.getCourseTeacher(courseId);
		return ResponseEntity.ok(new UserDTO(teacher));
	}
	
	/**
	 * GET all courses and exercises for active user (must be teacher)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInTeacher() {
		int userId = userService.getCurrentUser().getId();
		
		List<TeacherCourseDto> courseViews = teacherService.getTeacherView(userId);
		return ResponseEntity.ok(courseViews);
	}
}
