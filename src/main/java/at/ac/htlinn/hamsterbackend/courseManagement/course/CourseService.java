package at.ac.htlinn.hamsterbackend.courseManagement.course;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

	private CourseRepository courseRepository;

	public Course getCourseById(int id) {
		try {
			return (Course)Hibernate.unproxy(courseRepository.getById(id));
		}
		catch (EntityNotFoundException e) {
			return null;
		}
	}
	
	public List<Course> getAllCourses() {
		try {
			return courseRepository.findAll();
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Course> getCoursesByStudentId(int studentId) {
		return courseRepository.getCoursesByStudentId(studentId);
	}
	
	public List<Course> getCoursesByTeacherId(int teacherId) {
		return courseRepository.getCoursesByTeacherId(teacherId);
	}

	public Course getCourseBySolution(SolutionDto solution) {
		int activityId = solution.getActivityId();
		return courseRepository.getByActivityId(activityId);
	}

	public Course saveCourse(Course course) {
		try {
			return courseRepository.save(course);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Course updateCourse(Course course, Map<String, Object> fields) throws NoSuchFieldException, Exception {
		// attempt to update all specified fields
		for (Map.Entry<String, Object> set : fields.entrySet()) {
			try {
				Field field = Course.class.getDeclaredField(set.getKey());
		    	field.setAccessible(true);
		    	field.set(course, set.getValue());
			}
			catch (NoSuchFieldException e) {
				System.out.println(e);
				throw new NoSuchFieldException(set.getKey());
			}
			catch (Exception e) {
				throw new Exception(set.getKey());
			}
		}
		
		return saveCourse(course);
	}

	public boolean deleteCourse(Course course) {
		try {
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return false;
		}
	}
}
