package at.ac.htlinn.hamsterbackend.program;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProgramServiceTest {
    @MockBean
    ProgramRepository programRepository;

    @Autowired
    ProgramService programService;

    @Test
    public void getAllNeededProgramToRunTest() {
        Program mainProgram = Program.builder()
                .programId(100)
                .userId(100)
                .programName("First")
                .programPath("/root/dir1")
                .sourceCode("public static void main(){" +
                        "Second s = new Second();" +
                        "}")
                .build();

        Program p2 = Program.builder()
                .programId(101)
                .userId(100)
                .programName("Second")
                .programPath("/root/dir1")
                .sourceCode("class Second{" +
                        "public void doSth(){" +
                        "Third t = new Third();" +
                        "}" +
                        "}")
                .build();

        Program p3 = Program.builder()
                .programId(102)
                .userId(100)
                .programName("Third")
                .programPath("/root/dir1")
                .sourceCode("class Third{" +
                        "public doSthElse(){" +
                        "vor();" +
                        "}" +
                        "}")
                .build();

        when(programRepository.findProgramByProgramName("Second")).thenReturn(p2);
        when(programRepository.findProgramByProgramName("Third")).thenReturn(p3);

        Set<Program> programsNeeded = programService.getAllNeededProgramsToRun(mainProgram);

        Set<Program> expectedProgramsNeeded = new HashSet<>();
        expectedProgramsNeeded.add(p2);
        expectedProgramsNeeded.add(p3);


        assertEquals(expectedProgramsNeeded, programsNeeded);
    }
}
