package at.ac.htlinn.courseManagement.course;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.solution.model.SolutionDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

	private CourseRepository courseRepository;
	
	
	
	public List<Course> getAllCourses() {
		try {
			return courseRepository.findAll();
		} catch (Exception e) {
			return null;
		}
	}

	public Course saveCourse(Course course) {
		try {
			return courseRepository.save(course);
		} catch (Exception e) {
			return null;
		}

	}

	public boolean deleteCourse(int courseId) {
		try {
			Course course = getCourseById(courseId);
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return false;
		}
	}

	public Course getCourseById(int id) {
		try {
			return (Course)Hibernate.unproxy(courseRepository.getById(id));
		}
		catch (EntityNotFoundException e) {
			return null;
		}
	}

	public Course getCourseByName(String name) {
		return courseRepository.getByName(name);
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
}
