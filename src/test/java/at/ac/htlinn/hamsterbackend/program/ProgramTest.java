package at.ac.htlinn.hamsterbackend.program;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;


public class ProgramTest {
    @Test
    public void extractUsedExternalClassesTest() {
        String sourceCode = "class PacManHamster{" + "public void goAround(){" + "RunHamster abc = new   RunHamster(12);" + "Felix dumb =new Felix ('Sigma',231,True);" + "}" + "}";
        Program p = Program.builder().sourceCode(sourceCode).build();
        Set<String> usedExternalClasses = p.extractUsedExternalClasses();

        Set<String> expectedClasses = new HashSet<>();
        expectedClasses.add("Felix");
        expectedClasses.add("RunHamster");

        assertEquals(expectedClasses, usedExternalClasses);
    }

    @Test
    public void compareToTest() {
        Program p1 = Program.builder()
                .programId(12)
                .programPath("/path/Peter")
                .programName("Peter")
                .userId(100)
                .sourceCode("class Peter{}")
                .build();

        Program p2 = Program.builder()
                .programId(13)
                .programPath("/path/Johann")
                .programName("Johann")
                .userId(100)
                .sourceCode("class Johann{" +
                        "public void yellow(int cnt){" +
                        "Peter p = new Peter ();" +
                        "}" +
                        "}")
                .build();

        int result = p1.compareTo(p2);

        assertEquals(-1, result);

        Program p3 = Program.builder()
                .programId(12)
                .programPath("/path/Peter")
                .programName("Peter")
                .userId(100)
                .sourceCode("class Peter{}")
                .build();

        Program p4 = Program.builder()
                .programId(13)
                .programPath("/path/Johann")
                .programName("Johann")
                .userId(100)
                .sourceCode("class Johann{}")
                .build();

        int result2 = p3.compareTo(p4);

        assertEquals(0, result2);

        Program p5 = Program.builder()
                .programId(12)
                .programPath("/path/Peter")
                .programName("Peter")
                .userId(100)
                .sourceCode("class Peter{}")
                .build();

        Program p6 = Program.builder()
                .programId(13)
                .programPath("/path/Johann")
                .programName("Johann")
                .userId(100)
                .sourceCode("class Johann{" +
                        "public void yellow(int cnt){" +
                        "Peter p = new Peter ();" +
                        "}" +
                        "}")
                .build();

        int result3 = p6.compareTo(p5);

        assertEquals(1, result3);

    }
}
