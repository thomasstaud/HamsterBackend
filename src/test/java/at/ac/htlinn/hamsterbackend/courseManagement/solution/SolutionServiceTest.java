package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class SolutionServiceTest {
	
	@Autowired
	SolutionService solutionService;
	
    @MockBean
    SolutionRepository solutionRepository;
    
	private final User user = User.builder()
			.id(1)
			.build();
	private final Course course = Course.builder()
			.id(2)
			.name("Hamster")
			.teacher(user)
			.build();
	private final Exercise exercise = Exercise.builder()
			.id(3)
			.name("HamsterSolution")
			.course(course)
			.build();
	private final Solution solution = Solution.builder()
			.id(4)
			.activity(exercise)
			.student(user)
			.code("abc")
			.build();
	
    @Test
    public void getSolutionByIdTest() {
    	when(solutionRepository.getById(solution.getId())).thenReturn(solution);
    	Solution found = solutionService.getSolutionById(solution.getId());
    	assertEquals(found.getCode(), solution.getCode());
    }
    
    @Test
    public void saveSolutionTest() {
    	when(solutionRepository.save(solution)).thenReturn(solution);
    	Solution saved = solutionService.saveSolution(solution);
    	assertEquals(saved.getCode(), solution.getCode());
    }
    
    @Test
    public void deleteSolutionTest() {
    	boolean success = solutionService.deleteSolution(solution.getId());
    	assertTrue(success);
    }
    
    @Test
    public void getSolutionsByActivityIdTest() {
        List<Solution> solutions = Arrays.asList(solution);
        
        when(solutionRepository.getByActivityId(exercise.getId())).thenReturn(solutions);
        List<SolutionDto> found = solutionService.getSolutionsByActivityId(exercise.getId());
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getCode(), solution.getCode());
    }
    
    @Test
    public void getSolutionsByStudentIdTest() {
        List<Solution> solutions = Arrays.asList(solution);
        
        when(solutionRepository.getByStudentIdInSpecifiedCourse(user.getId(), course.getId())).thenReturn(solutions);
        List<SolutionDto> found = solutionService.getSolutionsByStudentId(user.getId(), course.getId());
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getCode(), solution.getCode());
    }
}
