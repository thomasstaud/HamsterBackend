package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;
    @MockBean
	private StudentService studentService;
    @MockBean
	private UserService userService;
    
    @MockBean
    private CustomPasswordEncoder customPasswordEncoder;
    @MockBean
    private MyUserDetailsService myUserDetailsService;
    
    @MockBean
    private Principal principal;
    
	private final User user = User.builder()
			.id(1)
			.build();
	private final Course course = Course.builder()
			.id(2)
			.name("Course")
			.teacher(user)
			.build();
	
	@BeforeEach
	public void setup() {
		when(userService.findUserByID(user.getId())).thenReturn(user);
		when(userService.findUserByUsername("admin")).thenReturn(user);
	}

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getCourseByIdTest() throws Exception {
        when(courseService.getCourseById(course.getId())).thenReturn(course);

        mockMvc.perform(get("/courses/" + course.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(course.getName())));
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAllCoursesTest() throws Exception {
        List<Course> allCourses = Arrays.asList(course);
        when(courseService.getAllCourses()).thenReturn(allCourses);

        mockMvc.perform(get("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].name", is(course.getName())));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void createCourseTest() throws Exception {
		when(courseService.saveCourse(course)).thenReturn(course);
    	
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(new CourseDto(course)));
		String requestBody = objectMapper.writeValueAsString(objectNode);

        mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
		        .principal(principal)
		        .secure(true))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$", is(course.getId())));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void updateCourseTest() throws Exception {
    	Course updatedCourse = Course.builder()
    			.id(course.getId())
    			.name("updated name")
    			.teacher(user)
    			.build();
    	
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedCourse.getName());
    	
    	when(courseService.getCourseById(course.getId())).thenReturn(course);
    	when(courseService.updateCourse(course, fields)).thenReturn(updatedCourse);
    	
		String requestBody = objectMapper.writeValueAsString(fields);

        mockMvc.perform(patch("/courses/" + course.getId())
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
		        .principal(principal)
		        .secure(true))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$", is(course.getId())));
	}

	@Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void deleteCourseTest() throws Exception {
    	when(courseService.getCourseById(course.getId())).thenReturn(course);
		when(studentService.removeAllStudentsFromCourse(course.getId())).thenReturn(true);
    	when(courseService.deleteCourse(course)).thenReturn(true);

        mockMvc.perform(delete("/courses/" + course.getId())
		        .contentType(MediaType.APPLICATION_JSON)
		        .principal(principal)
		        .secure(true))
		        .andExpect(status().isNoContent());
    }
}
