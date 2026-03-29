package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.terrain.Field;
import at.ac.htlinn.hamsterbackend.terrain.HamsterObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@RunWith(SpringRunner.class)
public class SolutionIntegrationTest {

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
	private final SolutionDto solution = SolutionDto.builder()
			.studentName("admin")
			.code("abc")
			.build();

    private int courseId;
    private long terrainObjectId;
    private int activityId;
    private int solutionId;
	
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
		solution.setActivityId(activityId);
		
		// create solution
		objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", objectMapper.valueToTree(solution));
		requestBody = objectMapper.writeValueAsString(objectNode);

		result = mockMvc.perform(put("/solutions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andReturn();
		solutionId = objectMapper.readValue(result.getResponse().getContentAsString(), SolutionDto.class).getId();
		
		// create exercise 2
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
		solution.setActivityId(activityId);
	}
	
	@AfterEach
	public void teardown() throws Exception {
		// delete solution
        mockMvc.perform(delete("/solutions/" + solutionId)
	            .secure(true))
				.andExpect(status().isNoContent());
		
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
		// get solution by id
        mockMvc.perform(get("/solutions/" + solutionId)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.solution_id", is(solutionId)))
				.andDo(document("getSolution"));
    	
    	// create solution
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", objectMapper.valueToTree(solution));
		String requestBody = objectMapper.writeValueAsString(objectNode);

		MvcResult result = mockMvc.perform(put("/solutions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andDo(document("createSolution"))
          		.andReturn();
		int id = objectMapper.readValue(result.getResponse().getContentAsString(), SolutionDto.class).getId();
		
		// get solutions by activity
		mockMvc.perform(get("/solutions?activity_id=" + activityId)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(1)))
				.andDo(document("getSolutionsByActivity"));
		
		// get solutions by student/course
		mockMvc.perform(get("/solutions?student_id=1&course_id=" + courseId)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
          		.andExpect(jsonPath("$", hasSize(2)))
				.andDo(document("getSolutionsByStudent"));
		
		// update solution
		String updatedCode = "updated code";
		solution.setId(id);
		solution.setCode(updatedCode);
		objectNode = objectMapper.createObjectNode();
		objectNode.set("solution", objectMapper.valueToTree(solution));
		requestBody = objectMapper.writeValueAsString(objectNode);
		
		mockMvc.perform(put("/solutions")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
          		.andDo(document("updateSolution"));
        // check if solution was updated
        mockMvc.perform(get("/solutions/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(jsonPath("$.code", is(updatedCode)));
		
		// feedback solution
        String feedback = "Feedback from teacher";
		objectNode = objectMapper.createObjectNode();
		objectNode.set("feedback", objectMapper.valueToTree(feedback));
		requestBody = objectMapper.writeValueAsString(objectNode);
		
		mockMvc.perform(patch("/solutions/" + id + "/feedback")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(requestBody)
        		.secure(true))
          		.andExpect(status().isOk())
				.andExpect(jsonPath("$.feedback", is(feedback)))
          		.andDo(document("feedbackSolution"));

        // delete activity
        mockMvc.perform(delete("/solutions/" + id)
	            .secure(true))
				.andExpect(status().isNoContent())
		        .andDo(document("deleteSolution"));
    }
}
