package at.ac.htlinn.courseManagement.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.ActivityViewDTO;
import at.ac.htlinn.courseManagement.activity.model.ExerciseViewDTO;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseViewDTO;
import at.ac.htlinn.courseManagement.courseUser.CourseUserService;
import at.ac.htlinn.courseManagement.solution.SolutionService;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionDTO;
import at.ac.htlinn.user.model.User;

@Service
public class CourseService {

	private CourseRepository courseRepository;
	
	private CourseUserService courseUserService;
	private ActivityService activityService;
	private SolutionService solutionService;

	public CourseService(CourseRepository courseRepository, CourseUserService studentService,
			ActivityService activityService, SolutionService solutionService) {
		
		this.courseRepository = courseRepository;
		this.courseUserService = studentService;
		this.activityService = activityService;
		this.solutionService = solutionService;
	}
	
	
	
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
			List<User> students = courseUserService.getAllStudentsInCourse(courseId);
			if (students != null) {
				for (User student : students) {
					courseUserService.removeStudentFromCourse(courseId, student.getId());
				}
			}
			
			Course course = getCourseById(courseId);
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return false;
		}
	}

	public Course getCourseById(int id) {
		return courseRepository.getById(id);
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

	public Course getCourseBySolution(SolutionDTO solution) {
		int exerciseId = solution.getActivityId();
		return courseRepository.getByExerciseId(exerciseId);
	}
	

	
	public List<CourseViewDTO> getStudentCourseViews(int studentId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get exercises for each course
		for (Course course : getCoursesByStudentId(studentId)) {
			List<ActivityViewDTO> activityViews = new ArrayList<ActivityViewDTO>();
			// get exercise view for each exercise
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				Solution solution = solutionService.getSolutionByExerciseAndStudentId(activity.getId(), studentId);
				
				/*
				if(solution != null)
					activityViews.add(new ExerciseViewDTO(exercise, solution));
				else
					activityViews.add(new ExerciseViewDTO(exercise));
				*/
			}
			courseViews.add(new CourseViewDTO(course, activityViews));
		}
		
		return courseViews;
	}
	
	public List<CourseViewDTO> getTeacherCourseViews(int teacherId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get exercises for each course
		for (Course course : getCoursesByTeacherId(teacherId)) {
			List<ActivityViewDTO> activityViews = new ArrayList<ActivityViewDTO>();
			// get exercise view for each exercise
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				// activityViews.add(new ExerciseViewDTO(exercise));
			}
			courseViews.add(new CourseViewDTO(course, activityViews));
		}
		
		return courseViews;
	}
}
