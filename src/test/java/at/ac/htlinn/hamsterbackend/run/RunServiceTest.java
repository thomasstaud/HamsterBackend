package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.*;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RunServiceTest {
    @Autowired
    RunService runService;

    @MockBean
    ProgramService programService;

    @MockBean
    TerrainObjectService terrainObjectService;

    @Test
    public void createTerrainTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        HamsterObject defaultHamster = HamsterObject.builder()
                .hamster_id(123)
                .yCord(4)
                .xCord(4)
                .cntCornInMouth(10)
                .viewDirection(ViewDirection.NORTH)
                .build();

        Field field1 = Field.builder()
                .field_id(123L)
                .wall(true)
                .xCord(1)
                .yCord(1)
                .build();
        Field field2 = Field.builder()
                .field_id(124L)
                .cntCorn(5)
                .xCord(2)
                .yCord(2)
                .build();

        List<Field> fields = new ArrayList<>();
        fields.add(field1);
        fields.add(field2);

        TerrainObject terrain = TerrainObject.builder()
                .terrainId(123)
                .terrainName("TestTerrain")
                .terrainPath("")
                .defaultHamster(defaultHamster)
                .height(12)
                .width(12)
                .userId(user.getId())
                .customFields(fields)
                .build();

        String terrainPath = "src" +
                File.separator + "test" +
                File.separator + "resources" +
                File.separator + "RunDir" +
                File.separator + user.getId() +
                File.separator + "TerrainDir" +
                File.separator + terrain.getTerrainName() + ".ter";

        assertTrue(runService.createTerrainFile(terrain, terrainPath));
    }

    @Test
    public void getTerrainPathTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        HamsterObject defaultHamster = HamsterObject.builder()
                .hamster_id(123)
                .yCord(4)
                .xCord(4)
                .cntCornInMouth(10)
                .viewDirection(ViewDirection.NORTH)
                .build();

        Field field1 = Field.builder()
                .field_id(123L)
                .wall(true)
                .xCord(1)
                .yCord(1)
                .build();
        Field field2 = Field.builder()
                .field_id(124L)
                .cntCorn(5)
                .xCord(2)
                .yCord(2)
                .build();

        List<Field> fields = new ArrayList<>();
        fields.add(field1);
        fields.add(field2);

        TerrainObject terrain = TerrainObject.builder()
                .terrainId(123)
                .terrainName("TestTerrain")
                .terrainPath("")
                .defaultHamster(defaultHamster)
                .height(12)
                .width(12)
                .userId(user.getId())
                .customFields(fields)
                .build();

        assertEquals(runService.buildTerrainFilePath(user, terrain),
                "src" +
                        File.separator + "main" +
                        File.separator + "resources" +
                        File.separator + "RunDir" +
                        File.separator + user.getId() +
                        File.separator + "TerrainDir" +
                        File.separator + terrain.getTerrainName() + ".ter"
        );
    }

    @Test
    public void createHamFileOnFileSystemTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        Program program = Program.builder()
                .programId(123)
                .programName("TestProgram1")
                .userId(user.getId())
                .programPath("")
                .sourceCode("class A {\n}")
                .build();

        String programPath = "src" +
                File.separator + "test" +
                File.separator + "resources" +
                File.separator + "RunDir" +
                File.separator + user.getId() +
                File.separator + "HamsterFiles" +
                File.separator + program.getProgramName() + ".ham";

        assertTrue(runService.createHamFileOnFileSystem(programPath));
    }

    @Test
    public void getHamsterPathTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        Program program = Program.builder()
                .programId(123)
                .programName("TestProgram1")
                .userId(user.getId())
                .programPath("")
                .sourceCode("class A {\n}")
                .build();
        assertEquals(runService.buildHamFilePath(user, program),
                "src" +
                        File.separator + "main" +
                        File.separator + "resources" +
                        File.separator + "RunDir" +
                        File.separator + user.getId() +
                        File.separator + "HamsterFiles" +
                        File.separator + program.getProgramName() + ".ham");
    }

    @Test
    public void precompileHamFileTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        Program program = Program.builder()
                .programId(123)
                .programName("TestProgram1")
                .userId(user.getId())
                .programPath("")
                .sourceCode("class SammelHamster extends Hamster {\n" +
                        "    SammelHamster(int r, int s, int b, int k) {\n" +
                        "        super(r, s, b, k);\n" +
                        "    }\n" +
                        "\n" +
                        "\n" +
                        "    void sammle() {\n" +
                        "        while (this.kornDa()) {\n" +
                        "            this.nimm();\n" +
                        "        }\n" +
                        "    }\n" +
                        "    \n" +
                        "    void laufeZurMauerUndSammle() {\n" +
                        "        this.sammle();\n" +
                        "        while (this.vornFrei()) {\n" +
                        "            this.vor();\n" +
                        "            this.sammle();\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "void main() {\n" +
                        "    Hamster willi = Hamster.getStandardHamster();\n" +
                        "    while (willi.vornFrei()) {\n" +
                        "        willi.vor();\n" +
                        "    }\n" +
                        "\n" +
                        "    SammelHamster paul = new SammelHamster(1, 0,\n" +
                        "            Hamster.OST, 0);\n" +
                        "    paul.laufeZurMauerUndSammle();\n" +
                        "}\n")
                .build();

        String programPath = runService.buildHamFilePath(user, program);
        runService.createHamFileOnFileSystem(programPath);
        assertTrue(runService.precompileHamFile(runService.getHamsterFileObject(program, programPath)));

    }
}
