package io.github.Hattinger04.course.model.solution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long>{

	Solution findById(int id); 
	// TODO: SQL not tested yet!
	@Query(value = "SELECT * FROM SOLUTION s JOIN exercise e USING (exercise_id) where exercise_id=:exercise_id and e.name=:name", nativeQuery = true)
	Solution findByExercise(@Param("exercise_id")int exercise_id, @Param("name")String name);
}
