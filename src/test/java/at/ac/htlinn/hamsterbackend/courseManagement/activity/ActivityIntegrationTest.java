package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ExerciseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;
import at.ac.htlinn.hamsterbackend.terrain.Field;
import at.ac.htlinn.hamsterbackend.terrain.HamsterObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@RunWith(SpringRunner.class)
public class ActivityIntegrationTest {
	
    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
    private MockMvc mockMvc;
    
    private final CourseDto course = CourseDto.builder()
			.name("Course")
			.build();
    private final HamsterObject hamsterObject = HamsterObject.builder()
            .build();
    private final TerrainObject terrainObject = TerrainObject.builder()
    		.defaultHamster(hamsterObject)
    		.customFields(new ArrayList<Field>())
            .build();
	private final ExerciseDto exercise = ExerciseDto.builder()
			.name("Exercise")
			.build();

    private int courseId;
    private long terrainObjectId;
    private int activityId;
	
    @BeforeEach
	public void setup() throws Exception {
    	ObjectNode objectNode;
    	String requestBody;
    	MvcResult result;
    	
		// create course
		objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(course));
		requestBody = objectMapper.writeValueAsString(objectNode);
		
		result = mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
	            .secure(true))
		        .andExpect(status().isOk())
		        .andReturn();
		courseId = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
		exercise.setCourseId(courseId);
		
		// create terrainObject
		requestBody = objectMapper.writeValueAsString(terrainObject);
		result = mockMvc.perform(post("/terrainObject/save")
		        .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        		.secure(true))
                .andExpect(status().isOk())
                .andReturn();
        terrainObjectId = objectMapper.readValue(result.getResponse().getContentAsString(), TerrainObject.class).getTerrainId();
		exercise.setTerrainId(terrainObjectId);
		
		// create exercise
		objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", objectMapper.valueToTree(exercise));
		requestBody = objectMapper.writeValueAsString(objectNode);

		result = mockMvc.perform(post("/activities")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andReturn();
		activityId = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
	}
	
    @AfterEach
	public void teardown() throws Exception {
        // delete activity
        mockMvc.perform(delete("/activities/" + activityId)
	            .secure(true))
				.andExpect(status().isNoContent());
        
        // delete terrainObject
        mockMvc.perform(delete("/terrainObject/delete/" + terrainObjectId)
	            .secure(true))
				.andExpect(status().isOk());
        
		// delete course
        mockMvc.perform(delete("/courses/" + courseId)
	            .secure(true))
				.andExpect(status().isNoContent());
	}
    
	@Test
    @WithMockUser(username = "admin", password = "admin", authorities = "ADMIN")
    public void test() throws Exception {
		// get activity by id
        mockMvc.perform(get("/activities/" + activityId)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.activity_id", is(activityId)))
				.andDo(document("getActivity"));

    	// create activity
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", objectMapper.valueToTree(exercise));
		String requestBody = objectMapper.writeValueAsString(objectNode);

		MvcResult result = mockMvc.perform(post("/activities")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andDo(document("createActivity"))
          		.andReturn();
		int id = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
		
		// get activities by course id
		mockMvc.perform(get("/activities?course_id=" + courseId)
        		.contentType(MediaType.APPLICATION_JSON)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andDo(document("getActivitiesByCourse"));
		
		// update activity
		String updatedName = "updated Exercise";
		HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedName);
		requestBody = objectMapper.writeValueAsString(fields);

        mockMvc.perform(patch("/activities/" + id)
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
    			.secure(true))
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$", is(id)))
        		.andDo(document("updateActivity"));
        // check if activity was updated
        mockMvc.perform(get("/activities/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(jsonPath("$.name", is(updatedName)));
		
        // delete activity
        mockMvc.perform(delete("/activities/" + id)
	            .secure(true))
				.andExpect(status().isNoContent())
		        .andDo(document("deleteActivity"));
    }
}
