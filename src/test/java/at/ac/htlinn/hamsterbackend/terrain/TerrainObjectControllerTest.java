
package at.ac.htlinn.hamsterbackend.terrain;

import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TerrainObjectController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TerrainObjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TerrainObjectService terrainObjectService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testUser", password = "password", roles = "USER")

    public void testSaveTerrainObject() throws Exception {
        HamsterObject hamsterObject = HamsterObject.builder()
                .hamster_id(123L)
                .xCord(1)
                .yCord(1)
                .cntCornInMouth(10)
                .viewDirection(ViewDirection.NORTH)
                .build();

        TerrainObject terrainObject = TerrainObject.builder()
                .terrainName("testTerrain")
                .terrainPath("root/testPackage/")
                .height(12)
                .width(15)
                .defaultHamster(hamsterObject)
                .build();

        when(userService.findUserByUsername("testUser"))
                .thenReturn(User.builder()
                        .id(123)
                        .username("testUser")
                        .build());

        when(terrainObjectService.save(terrainObject)).thenReturn(terrainObject);

        String requestBody = objectMapper.writeValueAsString(terrainObject);
        String requestURI = "terrainObject" + "/" + "saveTerrainObject";
        mockMvc.perform(post(requestURI)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }


}

