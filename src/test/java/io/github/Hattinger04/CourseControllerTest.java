package io.github.Hattinger04;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.Hattinger04.course.CourseController;
import io.github.Hattinger04.user.UserController;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.solution.Solution;

@RunWith(SpringRunner.class)
@EntityScan("io.github.Hattinger04.*")
@ComponentScan(basePackages = { "io.github.Hattinger04.*" })
@EnableJpaRepositories(basePackages = "io.github.Hattinger04.*")
@AutoConfigureMockMvc
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { CourseController.class })
class CourseControllerTest {
	
	// TODO: setup database so that unit tests can be executed properly

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllStudentsFromFirstCourse_moreThanZeroResults() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses/1/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		User[] students = objectMapper.readValue(response, User[].class);
		
		assertTrue(students.length > 0);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllStudentsFromThousandthCourse_return404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1000/students"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getFirstStudentFromFirstCourse_returns200() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/students/1"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getThousandthStudentFromFirstCourse_returns404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/students/1000"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getFirstStudentFromThousandthCourse_returns404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1000/students/1"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getTeacherFromFirstCourse_return200() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/teacher"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getTeacherFromThousandthCourse_return500() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1000/teacher"))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getFirstCourse_returns200() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getThousandthCourse_returns404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/1000"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getCourseByExistantName_returns200() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses?course_name=Hamster-Basics"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getCourseByNonexistantName_returns404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses?course_name=Nonexistant-Course"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllCourses_moreThanZeroResults() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		Course[] courses = objectMapper.readValue(response, Course[].class);
		
		assertTrue(courses.length > 0);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void addAndRemoveSecondStudentFromFirstCourse() throws Exception {
		
		// assert that student is currently not in course
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/students/2"))
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
		
		// add student to course
		mockMvc.perform(post("https://localhost:" + port + "/courses/1/students/2"))
				.andExpect(status().is(HttpStatus.OK.value())); 
		
		// assert that student is now in course
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/students/2"))
		.andExpect(status().is(HttpStatus.OK.value()));
		
		// remove student from course
		mockMvc.perform(delete("https://localhost:" + port + "/courses/1/students/2"))
		.andExpect(status().is(HttpStatus.OK.value())); 
		
		// assert that student is not in course anymore
		mockMvc.perform(get("https://localhost:" + port + "/courses/1/students/2"))
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createAndDeleteCourse() throws Exception {
		MvcResult result;
		String response;
		
		// create JSON-String with course data
		result = mockMvc.perform(get("https://localhost:" + port + "/user/users/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		User teacher = objectMapper.readValue(response, User.class);
		
		Course course = new Course();
		course.setName("new_course");
		course.setTeacher(teacher);
		
		JsonNode node = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", node);
		
		String json = objectMapper.writeValueAsString(objectNode);
		
		// send post request
		result = mockMvc.perform(post("https://localhost:" + port + "/courses")
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		int courseId = objectMapper.readValue(response, int.class);
		
		// assert that course with returned ID exists
		mockMvc.perform(get("https://localhost:" + port + "/courses/" + courseId))
		.andExpect(status().is(HttpStatus.OK.value()));
		
		// send delete request
		mockMvc.perform(delete("https://localhost:" + port + "/courses/" + courseId))
		.andExpect(status().is(HttpStatus.OK.value())); 
		
		// assert that course does not exist anymore
		mockMvc.perform(get("https://localhost:" + port + "/courses/" + courseId))
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createAndDeleteExercise() throws Exception {
		MvcResult result;
		String response;
		
		// create JSON-String with exercise data
		result = mockMvc.perform(get("https://localhost:" + port + "/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		Exercise exercise = new Exercise();
		exercise.setName("new_course");
		exercise.setHamster("hamster");
		exercise.setCourse(course);
		
		JsonNode node = objectMapper.valueToTree(exercise);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", node);
		
		String json = objectMapper.writeValueAsString(objectNode);
		
		// send post request
		result = mockMvc.perform(post("https://localhost:" + port + "/courses/exercises")
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		int exerciseId = objectMapper.readValue(response, int.class);
		
		// assert that solution with returned ID exists
		mockMvc.perform(get("https://localhost:" + port + "/courses/exercises/" + exerciseId))
		.andExpect(status().is(HttpStatus.OK.value()));
		
		// send delete request
		mockMvc.perform(delete("https://localhost:" + port + "/courses/exercises/" + exerciseId))
		.andExpect(status().is(HttpStatus.OK.value())); 
		
		// assert that solution does not exist anymore
		mockMvc.perform(get("https://localhost:" + port + "/courses/exercises/" + exerciseId))
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
	
	// TODO: test exercise PUT
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void createAndDeleteSolution() throws Exception {
		MvcResult result;
		String response;
		
		// create JSON-String with exercise data
		result = mockMvc.perform(get("https://localhost:" + port + "/user/users/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		User student = objectMapper.readValue(response, User.class);
		
		result = mockMvc.perform(get("https://localhost:" + port + "/courses/exercises/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Exercise exercise = objectMapper.readValue(response, Exercise.class);
		
		Solution solution = new Solution();
		solution.setHamster("hamster");
		solution.setStudent(student);
		solution.setExercise(exercise);
		
		JsonNode node = objectMapper.valueToTree(solution);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", node);
		
		String json = objectMapper.writeValueAsString(objectNode);
		
		// send post request
		result = mockMvc.perform(post("https://localhost:" + port + "/courses/solutions")
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		int solutionId = objectMapper.readValue(response, int.class);
		
		// assert that exercise with returned ID exists
		mockMvc.perform(get("https://localhost:" + port + "/courses/solutions/" + solutionId))
		.andExpect(status().is(HttpStatus.OK.value()));
		
		// send delete request
		mockMvc.perform(delete("https://localhost:" + port + "/courses/solutions/" + solutionId))
		.andExpect(status().is(HttpStatus.OK.value())); 
		
		// assert that exercise does not exist anymore
		mockMvc.perform(get("https://localhost:" + port + "/courses/solutions/" + solutionId))
		.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}
}
