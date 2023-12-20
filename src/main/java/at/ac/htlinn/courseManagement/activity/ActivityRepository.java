package at.ac.htlinn.courseManagement.activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.courseManagement.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{
	
	@Query(value = "SELECT e.* FROM EXERCISE e where course_id=:course_id", nativeQuery = true)
	public List<Activity> getByCourseId(@Param("course_id") int course_id);
}
