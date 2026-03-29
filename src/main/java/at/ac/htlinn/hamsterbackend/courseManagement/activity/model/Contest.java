package at.ac.htlinn.hamsterbackend.courseManagement.activity.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestDto;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "contest")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("contest") 
public class Contest extends Activity {
	public static final String type = "contest";
	
	public Contest(ContestDto contest, CourseService courseService, TerrainObjectService terrainObjectService) {
		super(contest.getId(), contest.getName(), contest.getDetails(), contest.isHidden(),
				courseService.getCourseById(contest.getCourseId()));
		this.start = contest.getStart();
		this.startTerrain = terrainObjectService.getTerrainObject(contest.getStartTerrainId());
		this.endTerrain = terrainObjectService.getTerrainObject(contest.getEndTerrainId());
		this.hiddenStartTerrain = terrainObjectService.getTerrainObject(contest.getHiddenStartTerrainId());
		this.hiddenEndTerrain = terrainObjectService.getTerrainObject(contest.getHiddenEndTerrainId());
	}
	
	@Column(name = "start")
	private Date start;
	@Column(name = "ignore_hamster_position")
	private boolean ignoreHamsterPosition;
	
	// initial terrain; visible for students
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "start_terrain") // should not be nullable
	private TerrainObject startTerrain;
	// expected resulting terrain; visible for students
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "end_terrain") // should not be nullable
	private TerrainObject endTerrain;
	
	// initial terrain; hidden for students (optional)
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "hidden_start_terrain")
	private TerrainObject hiddenStartTerrain;
	// expected resulting terrain; hidden for students (optional)
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "hidden_end_terrain")
	private TerrainObject hiddenEndTerrain;
}
