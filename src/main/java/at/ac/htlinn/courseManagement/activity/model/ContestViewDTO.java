package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionViewDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContestViewDTO extends ActivityViewDTO {
	public ContestViewDTO(Contest contest, Solution solution) {
		super(contest.getId(), contest.getName(),
				contest.getDetails(), new SolutionViewDTO(solution));
		this.start = contest.getStart();
		this.hamsters = contest.getHamsters();
	}
	
	public ContestViewDTO(Contest contest) {
		super(contest.getId(), contest.getName(), contest.getDetails(), null);
		this.start = contest.getStart();
		this.hamsters = contest.getHamsters();
	}

	private Date start;
	private String[] hamsters;
}
