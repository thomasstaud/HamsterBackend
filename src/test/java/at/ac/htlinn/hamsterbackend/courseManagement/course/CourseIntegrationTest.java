package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@RunWith(SpringRunner.class)
public class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    
    private final CourseDto course = CourseDto.builder()
			.name("Course")
			.build();
    
    private int courseId;
    
    @BeforeEach
    public void setup() throws Exception {
		// create course
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(course));
		String requestBody = objectMapper.writeValueAsString(objectNode);
		
		MvcResult result = mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
	            .secure(true))
		        .andExpect(status().isOk())
		        .andReturn();
		courseId = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
    }
    
    @AfterEach
    public void teardown() throws Exception {
        // delete course
        mockMvc.perform(delete("/courses/" + courseId)
	            .secure(true))
				.andExpect(status().isNoContent());
    }
    
    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "ADMIN")
    public void test() throws Exception {
		// get course by id
        mockMvc.perform(get("/courses/" + courseId)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(courseId)))
				.andDo(document("getCourse"));
        
    	// create course
    	ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(course));
		String requestBody = objectMapper.writeValueAsString(objectNode);	
		
		MvcResult result = mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
	            .secure(true))
		        .andExpect(status().isOk())
		        .andDo(document("createCourse"))
		        .andReturn();
		int id = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
        
        // get all courses
        mockMvc.perform(get("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andDo(document("getCourses"));
        
        // update course
    	String updatedName = "updated name";
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedName);
		requestBody = objectMapper.writeValueAsString(fields);

        mockMvc.perform(patch("/courses/" + id)
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
		        .secure(true))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$", is(id)))
		        .andDo(document("updateCourse"));
        // check if course was updated
        mockMvc.perform(get("/courses/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
        		.andExpect(jsonPath("$.name", is(updatedName)));
        
        // delete course
        mockMvc.perform(delete("/courses/" + id)
	            .secure(true))
				.andExpect(status().isNoContent())
		        .andDo(document("deleteCourse"));
    }
}
