package at.ac.htlinn.hamsterbackend.courseManagement.student;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Arrays;
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

import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
	private StudentService studentService;
    @MockBean
    private CourseService courseService;
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
			.name("Hamster")
			.teacher(user)
			.build();
	
	@BeforeEach
	public void Setup() {
		when(userService.findUserByID(user.getId())).thenReturn(user);
		when(userService.findUserByUsername("admin")).thenReturn(user);
		when(userService.isUserPrivileged(user)).thenReturn(true);
	}

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getAllStudentsTest() throws Exception {
        List<User> users = Arrays.asList(user);
        when(studentService.getAllStudents()).thenReturn(users);

        mockMvc.perform(get("/students")
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(1)))
          		.andExpect(jsonPath("$[0].id", is(user.getId())));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getAllStudentsInACourseTest() throws Exception {
        List<User> users = Arrays.asList(user);
        when(courseService.getCourseById(course.getId())).thenReturn(course);
        when(studentService.getAllStudentsInCourse(course.getId())).thenReturn(users);

        mockMvc.perform(get("/students?course_id=" + course.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(1)))
          		.andExpect(jsonPath("$[0].id", is(user.getId())));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void addStudentsToCourseTest() throws Exception {
    	List<Integer> users = Arrays.asList(1, 2, 3);
    	
        when(courseService.getCourseById(course.getId())).thenReturn(course);
        // only one of the three users can be added successfully
        when(studentService.addStudentToCourse(course.getId(), 2)).thenReturn(true);
    	
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("users", objectMapper.valueToTree(users));
		String requestBody = objectMapper.writeValueAsString(objectNode);

        mockMvc.perform(post("/students?course_id=" + course.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.principal(principal)
        		.secure(true))
          		.andExpect(status().isBadRequest())
          		// 2 users could not be added
          		.andExpect(jsonPath("$.failed_users", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void removeStudentsFromCourseTest() throws Exception {
    	when(courseService.getCourseById(course.getId())).thenReturn(course);
        when(studentService.removeStudentFromCourse(course.getId(), user.getId())).thenReturn(true);

        mockMvc.perform(delete("/students/" + user.getId() + "?course_id=" + course.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.principal(principal)
        		.secure(true))
          		.andExpect(status().isNoContent());
    }
}
