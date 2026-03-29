package at.ac.htlinn.hamsterbackend.terrain;

import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/terrainObject")
public class TerrainObjectController {
    @Autowired
    TerrainObjectService terrainObjectService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "save")
    public ResponseEntity<?> saveTerrainObject(@RequestBody @Valid TerrainObject terrainObject, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        terrainObject.setUserId(user.getId());
        terrainObject.getDefaultHamster().setTerrainObject(terrainObject);
        terrainObject.getCustomFields().forEach(field -> {
            field.setTerrainObject(terrainObject);
        });

        TerrainObject savedTerrainObject = terrainObjectService.save(terrainObject);

        return ResponseEntity.ok(savedTerrainObject);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "update")
    public ResponseEntity<?> updateTerrainObject(@RequestBody @Valid TerrainObject terrainObject) {
        boolean updateResult = terrainObjectService.update(terrainObject);
        return updateResult ? ResponseEntity.ok("Update successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
    }


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updatePath")
    public ResponseEntity<?> updatePath(@RequestBody TerrainObject terrainObject) {
        boolean updateResult = terrainObjectService.updatePath(terrainObject);
        return updateResult ? ResponseEntity.ok("Path update successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Path update failed");
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updateName")
    public ResponseEntity<?> updateName(@RequestBody TerrainObject terrainObject) {
        boolean updateResult = terrainObjectService.updateName(terrainObject);
        return updateResult ? ResponseEntity.ok("Name update successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Name update failed");
    }


    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "/delete/{terrainObject_id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("terrainObject_id") long terrainObjectId) {
        boolean deleteResult = terrainObjectService.delete(terrainObjectId);
        return deleteResult ? ResponseEntity.ok("Delete successful") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Delete failed");
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "get/{terrainObject_id}")
    public ResponseEntity<TerrainObject> getProgram(@PathVariable("terrainObject_id") long terrainObject_id) {
        TerrainObject terrainObject = terrainObjectService.getTerrainObject(terrainObject_id);
        return ResponseEntity.ok(terrainObject);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "getBasicData")
    public ResponseEntity<Set<TerrainObject>> getTerrainBasicData(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        Set<TerrainObject> basicData = terrainObjectService.getTerrainObjectBasicData(user.getId());
        return ResponseEntity.ok(basicData);
    }


}
