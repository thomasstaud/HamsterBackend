package io.github.Hattinger04;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

	// TODO: remove this
	public void output(int status) {
		if (status == HttpStatus.OK.value()) {
			System.out.println("\033[32mTest passed!\033[0m");
		} else {
			System.out.println("\033[31mTest failed!\033[0m");
		}
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllStudents_moreThanZeroResults() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		User[] users = objectMapper.readValue(response, User[].class);
		
		assertTrue(users.length > 0);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getFirstStudent_hasUsernameAdmin() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses/students/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		Student s = objectMapper.readValue(response, Student.class);
		
		assertTrue(s.getUser().getUsername().equals("admin"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getThousandthStudent_return404() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses/students/1000"))
				.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllStudentsFromFirstCourse_moreThanZeroResults() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses/1/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		Student[] students = objectMapper.readValue(response, Student[].class);
		
		assertTrue(students.length > 0);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void getAllStudentsFromThousandthCourse_zeroResults() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/courses/1000/students"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		String response = result.getResponse().getContentAsString();
		Student[] students = objectMapper.readValue(response, Student[].class);
		
		assertTrue(students.length == 0);
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
	public void testGetCourseByExistantName_returns200() throws Exception {
		mockMvc.perform(get("https://localhost:" + port + "/courses?course_name=Hamster-Basics"))
				.andExpect(status().is(HttpStatus.OK.value()));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetCourseByNonexistantName_returns404() throws Exception {
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
	
	
	
	
	// TODO: fix tests below to actually make them test things
	//			currently they are just checking the return code
	
	
	
	
	// currently adds student to database -> i am probably going to change this
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

	// currently removes student from database but doesn't remove user from course
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
}
