package at.ac.htlinn.hamsterbackend.terrain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "HAMSTER")
public class HamsterObject {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hamster_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TERRAIN_ID", unique = true)
    @JsonIgnore
    private TerrainObject terrainObject;

    @Column(name = "X_CORD")
    private int xCord;

    @Column(name = "Y_CORD")
    private int yCord;

    @Column(name = "CNT_CORN_IN_MOUTH")
    private int cntCornInMouth;

    @Enumerated(EnumType.STRING)
    @Column(name = "VIEW_DIRECTION")
    private ViewDirection viewDirection;
}
