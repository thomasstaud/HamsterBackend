package io.github.Hattinger04;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Disabled;
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
import io.github.Hattinger04.course.model.student.Student;
import io.github.Hattinger04.course.model.teacher.Teacher;

@RunWith(SpringRunner.class)
@EntityScan("io.github.Hattinger04.*")
@ComponentScan(basePackages = { "io.github.Hattinger04.*" })
@EnableJpaRepositories(basePackages = "io.github.Hattinger04.*")
@AutoConfigureMockMvc
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { CourseController.class })
class CourseControllerTest {

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

	public void output(int status) {
		if (status == HttpStatus.OK.value()) {
			System.out.println("\033[32mTest passed!\033[0m");
		} else {
			System.out.println("\033[31mTest failed!\033[0m");
		}
	}

	
	
	// TODO: fix tests to actually make them test things
	//			currently they are just checking the return code
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetStudents() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetFirstStudent() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/students/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAllStudentsByCourseId() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetFirstStudentByCourseId() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1/students/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAllStudentsByCourseName() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/students?course_name=Hamster-Basics"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	// currently adds student to database -> probably wrong
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testAddStudentToCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get second student from DB (first is already in course)
		result = mockMvc.perform(get("https://localhost:" + port + "/course/students/2"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Student student = objectMapper.readValue(response, Student.class);
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		JsonNode studentNode = objectMapper.valueToTree(student);
		JsonNode courseNode = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("student", studentNode);
		objectNode.set("course", courseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(post("https://localhost:" + port + "/course/courses/students", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}

	// currently removes student from database but doesn't remove user from course -> probably wrong
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testRemoveStudentFromCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get second student from DB (first is already in course)
		result = mockMvc.perform(get("https://localhost:" + port + "/course/students/2"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Student student = objectMapper.readValue(response, Student.class);
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		JsonNode studentNode = objectMapper.valueToTree(student);
		JsonNode courseNode = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("student", studentNode);
		objectNode.set("course", courseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(delete("https://localhost:" + port + "/course/courses/students", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAllTeachersByCourseId() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1/teachers"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAllTeachersByCourseName() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/teachers?course_name=Hamster-Basics"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetCourseById() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetCourseByName() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses?course_name=Hamster-Basics"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAllCourses() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/course/courses"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}
	
	// TODO: error, repair this
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testCreateCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get first teacher from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1/teachers"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Teacher teacher = objectMapper.readValue(response, Teacher[].class)[0];
		
		Course course = new Course();
		course.setName("new_course");
		
		JsonNode teacherNode = objectMapper.valueToTree(teacher);
		JsonNode courseNode = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("teacher", teacherNode);
		objectNode.set("course", courseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(post("https://localhost:" + port + "/course/courses", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	// TODO: test properly
	// currently removes teacher from database but doesn't remove teacher user from (deleted) course -> probably wrong
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDeleteCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get second course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/2"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		JsonNode courseNode = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", courseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(delete("https://localhost:" + port + "/course/courses", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testCreateExercise() throws Exception {
		MvcResult result;
		String response;
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		Exercise exercise = new Exercise();
		exercise.setName("new_exercise");
		exercise.setHamster("new_hamster");
		exercise.setCourse(course);
		
		JsonNode exerciseNode = objectMapper.valueToTree(exercise);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", exerciseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(post("https://localhost:" + port + "/course/exercises", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	// does not work (creates new exercise)
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testUpdateExercise() throws Exception {
		MvcResult result;
		String response;
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		Exercise exercise = new Exercise();
		exercise.setName("new_exercise");
		exercise.setHamster("new_hamster");
		exercise.setCourse(course);
		
		JsonNode exerciseNode = objectMapper.valueToTree(exercise);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", exerciseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(put("https://localhost:" + port + "/course/exercises", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	// does not work (does nothing)
	@Disabled
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDeleteExercise() throws Exception {
		MvcResult result;
		String response;
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		Exercise exercise = new Exercise();
		exercise.setName("new_exercise");
		exercise.setHamster("new_hamster");
		exercise.setCourse(course);
		
		JsonNode exerciseNode = objectMapper.valueToTree(exercise);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", exerciseNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(delete("https://localhost:" + port + "/course/exercises", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		output(result.getResponse().getStatus());  
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testIsUserInCourse_InCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		// get first user from DB (in first course)
		result = mockMvc.perform(get("https://localhost:" + port + "/user/users/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		User user = objectMapper.readValue(response, User.class);
		
		JsonNode courseNode = objectMapper.valueToTree(course);
		JsonNode userNode = objectMapper.valueToTree(user);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", courseNode);
		objectNode.set("user", userNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(post("https://localhost:" + port + "/course/isUserInCourse", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
	}
	
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testIsUserInCourse_NotInCourse() throws Exception {
		MvcResult result;
		String response;
		
		// get first course from DB
		result = mockMvc.perform(get("https://localhost:" + port + "/course/courses/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		Course course = objectMapper.readValue(response, Course.class);
		
		// get second user from DB (not in first course)
		result = mockMvc.perform(get("https://localhost:" + port + "/user/users/2"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		response = result.getResponse().getContentAsString();
		User user = objectMapper.readValue(response, User.class);
		
		JsonNode courseNode = objectMapper.valueToTree(course);
		JsonNode userNode = objectMapper.valueToTree(user);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", courseNode);
		objectNode.set("user", userNode);
		
		String json = objectMapper.writeValueAsString(objectNode);
		  
		result = mockMvc.perform(post("https://localhost:" + port + "/course/isUserInCourse", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
		                            .andReturn();
	}
}
