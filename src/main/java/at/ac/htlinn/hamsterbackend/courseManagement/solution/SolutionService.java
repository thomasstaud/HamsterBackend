package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SolutionService {
	
	private SolutionRepository solutionRepository;
	
	public Solution saveSolution(Solution solution) {
		return solutionRepository.save(solution);
	}

	public boolean deleteSolution(int solutionId) {
		try {
			Solution solution = solutionRepository.getById(solutionId);
			solutionRepository.delete(solution);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	

	
	public Solution getSolutionById(int solutionId) {
		return solutionRepository.getById(solutionId);
	}

	// get all solutions for one activity
	public List<SolutionDto> getSolutionsByActivityId(int activityId) {

		// get solutions and convert to DTOs
		List<SolutionDto> solutions = new ArrayList<SolutionDto>();
		for (Solution solution : solutionRepository.getByActivityId(activityId)) {
			solutions.add(new SolutionDto(solution));
		}
		
		return solutions;
	}

	// get solution for one activity for a specified student
	public Solution getSolutionByActivityAndStudentId(int activityId, int studentId) {
		return solutionRepository.getByActivityAndStudentId(activityId, studentId);
	}

	// get all solutions for one student in a specific course
	public List<SolutionDto> getSolutionsByStudentId(int studentId, int courseId) {
		
		// get solutions and convert to DTOs
		List<SolutionDto> solutions = new ArrayList<SolutionDto>();
		for (Solution solution : solutionRepository.getByStudentIdInSpecifiedCourse(studentId, courseId)) {
			solutions.add(new SolutionDto(solution));
		}
		
		return solutions;
	}
	
	
	
	public int getNumberOfSubmittedSolutions(int activityId) {
		return solutionRepository.getNumberOfSubmittedSolutions(activityId);
	}
	
	public int getNumberOfFeedbackedSolutions(int activityId) {
		return solutionRepository.getNumberOfFeedbackedSolutions(activityId);
	}
}
