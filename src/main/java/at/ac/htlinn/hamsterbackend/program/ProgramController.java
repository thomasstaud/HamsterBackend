package at.ac.htlinn.hamsterbackend.program;

import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    ProgramService programService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "save")
    public ResponseEntity<?> saveProgram(@RequestBody @Valid Program program, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        program.setUserId(user.getId());
        Program savedProgram = programService.save(program);
        return ResponseEntity.ok(savedProgram);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updatePath")
    public ResponseEntity<?> updatePath(@RequestBody Program program) {
        boolean updateResult = programService.updatePath(program);
        return updateResult ? ResponseEntity.ok("Path update successful") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Path update failed");
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updateName")
    public ResponseEntity<?> updateName(@RequestBody Program program) {
        boolean updateResult = programService.updateName(program);
        return updateResult ? ResponseEntity.ok("Name update successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Name update failed");
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "delete/{program_id}")
    public ResponseEntity<?> deleteProgram(@PathVariable("program_id") long programId) {
        boolean updateResult = programService.delete(programId);
        return updateResult ? ResponseEntity.ok("Delete successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "get/{program_id}")
    public ResponseEntity<Program> getProgram(@PathVariable("program_id") long programId) {
        Program program = programService.getProgram(programId);
        return ResponseEntity.ok(program);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "getBasicData")
    public ResponseEntity<Set<Program>> getProgramBasicData(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        Set<Program> basicData = programService.getProgramBasicData(user.getId());
        return ResponseEntity.ok(basicData);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "update")
    public ResponseEntity<?> updateTerrainObject(@RequestBody @Valid Program program) {
        boolean updateResult = programService.update(program);
        return updateResult ? ResponseEntity.ok("Update successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
    }
}
