package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO extends ActivityDTO {
	public ContestDTO(Contest contest) {
		super(contest.getId(), contest.getCourse().getId(),
				contest.getName(), contest.getDetails());
		this.start = contest.getStart();
		this.hamsters = contest.getHamsters();
	}

	private Date start;
	private String[] hamsters;
}
