package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunConfig {
    TerrainObject terrainObject;
    private Set<Program> programs = new HashSet<>();
}
