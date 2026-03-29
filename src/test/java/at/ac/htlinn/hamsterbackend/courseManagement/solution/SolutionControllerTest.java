package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import at.ac.htlinn.hamsterbackend.courseManagement.activity.ActivityService;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@WebMvcTest(SolutionController.class)
public class SolutionControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;
    @MockBean
    private ActivityService activityService;
    @MockBean
    private SolutionService solutionService;
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
			.name("Hamster")
			.teacher(user)
			.build();
	private final Exercise exercise = Exercise.builder()
			.id(3)
			.name("HamsterExercise")
			.course(course)
			.build();
	private final Solution solution = Solution.builder()
			.id(4)
			.activity(exercise)
			.student(user)
			.code("abc")
			.build();
	
	@BeforeEach
	public void Setup() {
		when(userService.findUserByID(user.getId())).thenReturn(user);
		when(userService.findUserByUsername("admin")).thenReturn(user);
		when(userService.isUserPrivileged(user)).thenReturn(true);
	}

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getSolutionByIdTest() throws Exception {
        when(solutionService.getSolutionById(solution.getId())).thenReturn(solution);

        mockMvc.perform(get("/solutions/" + solution.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$.code", is(solution.getCode())));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getSolutionsByActivityIdTest() throws Exception {
        when(activityService.getActivityById(exercise.getId())).thenReturn(exercise);
        List<SolutionDto> solutions = Arrays.asList(new SolutionDto(solution));
        when(solutionService.getSolutionsByActivityId(exercise.getId())).thenReturn(solutions);

        mockMvc.perform(get("/solutions?activity_id=" + exercise.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(1)))
          		.andExpect(jsonPath("$[0].code", is(solution.getCode())));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getSolutionsByStudentIdTest() throws Exception {
        List<SolutionDto> solutions = Arrays.asList(new SolutionDto(solution));
        when(courseService.getCourseById(course.getId())).thenReturn(course);
        when(solutionService.getSolutionsByStudentId(user.getId(), course.getId())).thenReturn(solutions);

        mockMvc.perform(get("/solutions?student_id=" + user.getId() + "&course_id=" + course.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(1)))
          		.andExpect(jsonPath("$[0].code", is(solution.getCode())));
    }
    
    /* TODO: doesn't work for some reason?
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void createSolutionTest() throws Exception {
    	Solution newSolution = Solution.builder()
    			.id(0)
    			.activity(exercise)
    			.student(user)
    			.code("def")
    			.build();
    	
        when(solutionService.getSolutionByActivityAndStudentId(exercise.getId(), user.getId())).thenReturn(null);
		when(activityService.getActivityById(exercise.getId())).thenReturn(exercise);
		when(solutionService.saveSolution(newSolution)).thenReturn(newSolution);
    	
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", objectMapper.valueToTree(new SolutionDto(newSolution)));
		String requestBody = objectMapper.writeValueAsString(objectNode);

        mockMvc.perform(put("/solutions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.principal(principal)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$.code", is(solution.getCode())));
    }
    */
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void updateSolutionTest() throws Exception {
    	Solution updatedSolution = Solution.builder()
    			.id(solution.getId())
    			.activity(exercise)
    			.student(user)
    			.code("def")
    			.build();
    	
        when(solutionService.getSolutionById(solution.getId())).thenReturn(solution);
		when(solutionService.saveSolution(updatedSolution)).thenReturn(updatedSolution);
    	
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", objectMapper.valueToTree(new SolutionDto(updatedSolution)));
		String requestBody = objectMapper.writeValueAsString(objectNode);

        mockMvc.perform(put("/solutions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.principal(principal)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andExpect(jsonPath("$.code", is(solution.getCode())));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void feedbackSolutionTest() throws Exception {
        when(solutionService.getSolutionById(solution.getId())).thenReturn(solution);
		when(solutionService.saveSolution(solution)).thenReturn(solution);
        
        String feedback = "Feedback from teacher";
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("feedback", objectMapper.valueToTree(feedback));
		String requestBody = objectMapper.writeValueAsString(objectNode);
		
		mockMvc.perform(patch("/solutions/" + solution.getId() + "/feedback")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
				.andExpect(jsonPath("$.feedback", is(feedback)));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void deleteSolutionTest() throws Exception {
        when(solutionService.getSolutionById(solution.getId())).thenReturn(solution);
    	when(solutionService.deleteSolution(solution.getId())).thenReturn(true);

        mockMvc.perform(delete("/solutions/" + solution.getId())
        		.contentType(MediaType.APPLICATION_JSON)
        		.principal(principal)
        		.secure(true))
          		.andExpect(status().isNoContent());
    }
}
