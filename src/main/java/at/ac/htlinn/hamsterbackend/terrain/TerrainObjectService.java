package at.ac.htlinn.hamsterbackend.terrain;

import at.ac.htlinn.hamsterbackend.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TerrainObjectService {
    @Autowired
    TerrainObjectRepository terrainObjectRepository;

    @Autowired
    UserService userService;


    
    public Set<TerrainObject> getTerrainObjectBasicData(long id) {
        return terrainObjectRepository.findAllTerrainBasicDataByUserID(id);
    }

    
    public boolean delete(long id) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(id);
        if (o.isPresent()) {
            TerrainObject t = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == t.getUserId()) {
                terrainObjectRepository.delete(t);
                Optional<TerrainObject> o2 = terrainObjectRepository.findById(id);
                return o2.isEmpty();
            }
        }
        return false;
    }

    
    public TerrainObject getTerrainObject(long id) {
        return terrainObjectRepository.findTerrainObjectByTerrainId(id);
    }

    
    public TerrainObject save(TerrainObject terrainObject) {
        return terrainObjectRepository.save(terrainObject);
    }

    
    public boolean updatePath(TerrainObject terrainObject) {
        return terrainObjectRepository.updatePath(terrainObject.getTerrainId(), terrainObject.getTerrainPath());
    }

    
    public boolean updateName(TerrainObject terrainObject) {
        return terrainObjectRepository.updateName(terrainObject.getTerrainId(), terrainObject.getTerrainName());
    }

    
    public boolean update(TerrainObject terrainObject) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(terrainObject.getTerrainId());
        if (o.isPresent()) {
            TerrainObject t = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == t.getUserId()) {
                return terrainObjectRepository.update(t.getTerrainId(), t.getCustomFields(), t.getDefaultHamster(), t.getHeight(), t.getWidth());
            }
        }
        return false;
    }
}
