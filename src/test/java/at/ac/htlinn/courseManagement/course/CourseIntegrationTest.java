package at.ac.htlinn.courseManagement.course;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.courseManagement.course.model.Course;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class CourseIntegrationTest {

	@LocalServerPort
	private int port;
	private String url = "https://localhost:" + port;

    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void createCourse() throws Exception {
    	Course course = Course.builder()
    			.name("hans")
    			.build();
    	
        String requestBody = objectMapper.writeValueAsString(course);
        
        var result = mockMvc.perform(post(url + "/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
		int id = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
        
        // remove course
        mockMvc.perform(delete(url + "/courses/" + id))
		.andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getCourse() {
        var result = mockMvc.perform(post(url + "/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
    }
}
