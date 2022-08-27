package io.github.Hattinger04.course.model.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long>{

	Solution findById(int id); 
}
