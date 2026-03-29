package at.ac.htlinn.hamsterbackend.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@EntityScan("at.ac.htlinn.hamsterbackend.*")
@ComponentScan(basePackages = { "at.ac.htlinn.hamsterbackend.*" })
@EnableJpaRepositories(basePackages = "at.ac.htlinn.hamsterbackend.*")
@AutoConfigureMockMvc
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { UserController.class })
@Sql({"/testdata/user_controller_testdata.sql", "/testdata/user_controller_testdata.sql"})
class UserControllerTest {

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

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetUsers() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/api/user/users"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();

		output(result.getResponse().getStatus()); 
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetFirstUserByID() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/user/users/1"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		output(result.getResponse().getStatus()); 
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testGetAdminUserByUsername() throws Exception {
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/user/users?username=admin"))
				.andExpect(status().is(HttpStatus.OK.value())).andReturn();
		output(result.getResponse().getStatus()); 
	}
	
	@Disabled // Activate when you want to try it out - currently deactivated!
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testCreateUser() throws Exception {
		  User user = new User();
		  user.setUsername("new_user");
		  user.setPassword("new_password");

		  JsonNode node = objectMapper.valueToTree(user); 
		  ObjectNode objectNode = objectMapper.createObjectNode();
		  objectNode.set("user", node);

		  String json = objectMapper.writeValueAsString(objectNode);
		  
		  MvcResult result = mockMvc.perform(post("https://localhost:" + port + "/user/users", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		  output(result.getResponse().getStatus()); 
	}
	// Set correct id for user you want to change!
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testUpdateUser() throws Exception {
		  User user = new User();
		  user.setId(2);
		  user.setUsername("new_user_updated");
		  user.setPassword("new_password_updated");

		  JsonNode node = objectMapper.valueToTree(user); 
		  ObjectNode objectNode = objectMapper.createObjectNode();
		  objectNode.set("user", node);

		  String json = objectMapper.writeValueAsString(objectNode);
		  MvcResult result = mockMvc.perform(put("https://localhost:" + port + "/user/users", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		  output(result.getResponse().getStatus()); 
	}
	
//	@Disabled // Activate when you want to try it out - currently deactivated!
	@Test
	@WithMockUser(authorities = "ADMIN")
	public void testDeleteUserByID() throws Exception {
		  User user = new User();
		  user.setId(2);
		  
		  JsonNode node = objectMapper.valueToTree(user); 
		  ObjectNode objectNode = objectMapper.createObjectNode();
		  objectNode.set("user", node);

		  String json = objectMapper.writeValueAsString(objectNode);
		  MvcResult result = mockMvc.perform(delete("https://localhost:" + port + "/user/users", 1)
		                                       .content(json)
		                                       .contentType(MediaType.APPLICATION_JSON))
		                            .andExpect(status().is(HttpStatus.OK.value()))
		                            .andReturn();
		  output(result.getResponse().getStatus()); 
	}
}
