package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.JavaCompiler;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.Precompiler;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class RunService {
    @Autowired
    ProgramService programService;

    @Autowired
    TerrainObjectService terrainObjectService;

    private final Workbench wb = Workbench.getWorkbench();

    public boolean createTerrainFile(TerrainObject terrainObject, String terrainPath) {
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        return wb.createTerrainFile(terrain, terrainPath);
    }

    public boolean createHamFileOnFileSystem(String hamsterPath) {
        File f = new File(hamsterPath);
        return DirectoryManagement.createFile(f);
    }

    public HamsterFile getHamsterFileObject(Program program, String programPath) {
        program.setProgramTypeAsCommentInSourceCode();
        return new HamsterFile(program.getSourceCode(), program.getProgramType(), programPath);
    }

    public boolean precompileHamFile(HamsterFile hamsterFile) {
        Precompiler precompiler = new Precompiler();
        try {
            return precompiler.precompile(hamsterFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String buildTerrainFilePath(User user, TerrainObject terrainObject) {
        String terrainDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "TerrainDir";
        DirectoryManagement.createDirs(terrainDir);
        if (!DirectoryManagement.createDirs(terrainDir)) return null;
        return String.format(terrainDir + File.separator + "%s.ter", terrainObject.getTerrainName());
    }

    public String buildHamFilePath(User user, Program program) {
        String hamsterDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "HamsterFiles";
        if (!DirectoryManagement.createDirs(hamsterDir)) return null;
        return String.format(hamsterDir + File.separator + "%s.ham", program.getProgramName());
    }

    public ArrayList<Program> getAllNeededPrograms(long programId) {
        Program program = programService.getProgram(programId);
        return new ArrayList<>(programService.getAllNeededProgramsToRun(program));
    }

    public List compileProgram(HamsterFile hamsterFile) {
        JavaCompiler javaCompiler = new JavaCompiler();
        try {
            return javaCompiler.compile(hamsterFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List manageObjectOrientatedClasses(User user, long programId) {
        ArrayList<Program> programs = getAllNeededPrograms(programId);
        List errors = new ArrayList();
        for (Program p : programs) {
            String hamFilePath = buildHamFilePath(user, p);
            createHamFileOnFileSystem(hamFilePath);
            HamsterFile hamsterFile = getHamsterFileObject(p, hamFilePath);
            precompileHamFile(hamsterFile);
            errors.addAll(compileProgram(hamsterFile));
        }
        return errors;
    }

    public String manageTerrain(User user, long terrainId) {
        TerrainObject terrainObject = terrainObjectService.getTerrainObject(terrainId);
        String terrainPath = buildTerrainFilePath(user, terrainObject);
        createTerrainFile(terrainObject, terrainPath);
        return terrainPath;
    }

    public String manageMainProgram(User user, long programId) {
        Program program = programService.getProgram(programId);
        String programPath = buildHamFilePath(user, program);
        createHamFileOnFileSystem(programPath);
        return programPath;
    }
}
