package at.ac.htlinn.hamsterbackend.terrain;

import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "FIELD")
public class Field {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long field_id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TERRAIN_ID")
    private TerrainObject terrainObject;

    @Column(name = "X_CORD")
    private int xCord;

    @Column(name = "Y_CORD")
    private int yCord;

    @Column(name = "CNT_CORN")
    private int cntCorn;

    @Column(name = "WALL")
    private boolean wall;
}
