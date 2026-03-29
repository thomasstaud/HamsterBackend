package at.ac.htlinn.hamsterbackend.run;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class DirectoryManagement {
    public static boolean createDirs(String path) {
        //only possible if path does not contain more than one directory with the exact same name
        String[] dirs = path.split(Pattern.quote(File.separator));
        for (String dir : dirs) {
            File f = new File(path.split(dir)[0].trim() + dir);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean moveFileToDir(String filePath, String dirPath) {
        File f = new File(filePath);
        File movedFile = new File(dirPath + "\\" + f.getName());

        if (movedFile.exists()) return true;

        return f.renameTo(movedFile);
    }

    public static boolean createFile(File f) {
        if (!f.exists()) {
            try {
                return f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
