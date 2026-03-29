package at.ac.htlinn.hamsterbackend.terrain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "TERRAIN")
public class TerrainObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long terrainId;

    @JsonIgnore
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "TERRAIN_NAME")
    private String terrainName;

    @Column(name = "WIDTH")
    private int width;

    @Column(name = "HEIGHT")
    private int height;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "terrainObject")
    private HamsterObject defaultHamster;

    @OneToMany(mappedBy = "terrainObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Field> customFields;

    @Column(name = "TERRAIN_PATH")
    private String terrainPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerrainObject that = (TerrainObject) o;
        return Objects.equals(userId, that.userId) && Objects.equals(terrainName, that.terrainName) && Objects.equals(width, that.width)
                && Objects.equals(height, that.height) && Objects.equals(customFields, that.customFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, terrainName, width, height, /*hamsters*/defaultHamster, customFields);
    }

    public TerrainObject(long terrainId, String terrainName, String terrainPath) {
        this.terrainId = terrainId;
        this.terrainName = terrainName;
        this.terrainPath = terrainPath;
    }
}
