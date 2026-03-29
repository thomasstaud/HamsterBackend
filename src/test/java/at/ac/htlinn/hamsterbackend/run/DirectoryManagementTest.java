package at.ac.htlinn.hamsterbackend.run;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectoryManagementTest {
    @Test
    public void createDirsTest() {
        String path = "src\\test\\resources\\testDir\\1\\test123";

        if (!DirectoryManagement.createDirs(path)) return;


        File f = new File(path);
        assertTrue(f.exists());


    }


    @Test
    public void moveFileToDir() {
        String dirPath = "src\\test\\resources\\RunDir";
        String filePath = "src\\test\\resources\\testFile.java";

        if (DirectoryManagement.moveFileToDir(filePath, dirPath)) return;

        File f = new File("src\\test\\resources\\RunDir\\testFile.java");
        assertTrue(f.exists());

    }
}
