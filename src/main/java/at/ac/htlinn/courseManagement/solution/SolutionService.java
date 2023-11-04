package at.ac.htlinn.courseManagement.solution;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionDTO;

@Service
public class SolutionService {
	private SolutionRepository solutionRepository;
	
	public SolutionService(SolutionRepository solutionRepository) {
		
		this.solutionRepository = solutionRepository;
	}
	
	
	
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

	public List<SolutionDTO> getAllSolutions() {

		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : solutionRepository.findAll()) {
			solutions.add(new SolutionDTO(solution));
		}
		
		return solutions;
	}

	// get all solutions for one exercise
	public List<SolutionDTO> getSolutionsByExerciseId(int exerciseId) {

		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : solutionRepository.getByExerciseId(exerciseId)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		return solutions;
	}

	// get solution for one exercise for a specified student
	public Solution getSolutionByExerciseAndStudentId(int exerciseId, int studentId) {
		return solutionRepository.getByExerciseAndStudentId(exerciseId, studentId);
	}

	// get all solutions for one student in a specific course
	public List<SolutionDTO> getSolutionsByStudentId(int studentId, int courseId) {
		
		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : solutionRepository.getByStudentIdInSpecifiedCourse(studentId, courseId)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		return solutions;
	}
}
