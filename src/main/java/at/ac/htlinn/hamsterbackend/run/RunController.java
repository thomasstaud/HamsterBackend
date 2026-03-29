package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/run")
public class RunController {
    @Autowired
    UserService userService;

    @Autowired
    RunService runService;

    @Autowired
    TerrainObjectService terrainObjectService;

    @Autowired
    ProgramService programService;

    private final Workbench wb = Workbench.getWorkbench();

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/runProgram/{program_id}/{terrain_id}")
    @ResponseBody
    public ResponseEntity<?> runProgram(@PathVariable("program_id") long programId, @PathVariable("terrain_id") long terrainId, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());

        String terrainPath = runService.manageTerrain(user, programId);
        String programPath = runService.manageMainProgram(user, programId);

        List errors = runService.manageObjectOrientatedClasses(user, programId);
        if (!errors.isEmpty()) return ResponseEntity.ok(errors);

        return new ResponseEntity<>(wb.startProgram(programPath, terrainPath), HttpStatus.OK);
    }
}
