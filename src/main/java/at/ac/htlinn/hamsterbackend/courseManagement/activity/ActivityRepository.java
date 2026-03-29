package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Integer>{
	
	@Query(value = "SELECT a FROM Activity a WHERE course_id=:course_id")
	public List<Activity> getByCourseId(@Param("course_id") int course_id);
}
