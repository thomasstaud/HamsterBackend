package io.github.Hattinger04;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.github.Hattinger04.user.UserController;

@RunWith(SpringRunner.class)
@EntityScan("io.github.Hattinger04.*")
@ComponentScan(basePackages = { "io.github.Hattinger04.*" })
@EnableJpaRepositories(basePackages = "io.github.Hattinger04.*")
@AutoConfigureMockMvc
@EnableAutoConfiguration

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { UserController.class })
class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mockMvc;

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
		MvcResult result = mockMvc.perform(get("https://localhost:" + port + "/user/users"))
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
}
