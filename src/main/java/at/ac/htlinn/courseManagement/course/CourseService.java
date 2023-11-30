package at.ac.htlinn.courseManagement.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseViewDTO;
import at.ac.htlinn.courseManagement.courseUser.CourseUserService;
import at.ac.htlinn.courseManagement.exercise.ExerciseService;
import at.ac.htlinn.courseManagement.exercise.model.Exercise;
import at.ac.htlinn.courseManagement.exercise.model.ExerciseViewDTO;
import at.ac.htlinn.courseManagement.solution.SolutionService;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionDTO;
import at.ac.htlinn.user.model.User;

@Service
public class CourseService {

	private CourseRepository courseRepository;
	
	private CourseUserService courseUserService;
	private ExerciseService exerciseService;
	private SolutionService solutionService;

	public CourseService(CourseRepository courseRepository, CourseUserService studentService,
			ExerciseService exerciseService, SolutionService solutionService) {
		
		this.courseRepository = courseRepository;
		this.courseUserService = studentService;
		this.exerciseService = exerciseService;
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
		int exerciseId = solution.getExerciseId();
		return courseRepository.getByExerciseId(exerciseId);
	}
	

	
	public List<CourseViewDTO> getStudentCourseViews(int studentId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get exercises for each course
		for (Course course : getCoursesByStudentId(studentId)) {
			List<ExerciseViewDTO> exerciseViews = new ArrayList<ExerciseViewDTO>();
			// get exercise view for each exercise
			for (Exercise exercise : exerciseService.getAllExercisesInCourse(course.getId())) {
				Solution solution = solutionService.getSolutionByExerciseAndStudentId(exercise.getId(), studentId);
				
				if(solution != null)
					exerciseViews.add(new ExerciseViewDTO(exercise, solution));
				else
					exerciseViews.add(new ExerciseViewDTO(exercise));
			}
			courseViews.add(new CourseViewDTO(course, exerciseViews));
		}
		
		return courseViews;
	}
	
	public List<CourseViewDTO> getTeacherCourseViews(int teacherId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get exercises for each course
		for (Course course : getCoursesByTeacherId(teacherId)) {
			List<ExerciseViewDTO> exerciseViews = new ArrayList<ExerciseViewDTO>();
			// get exercise view for each exercise
			for (Exercise exercise : exerciseService.getAllExercisesInCourse(course.getId())) {
				exerciseViews.add(new ExerciseViewDTO(exercise));
			}
			courseViews.add(new CourseViewDTO(course, exerciseViews));
		}
		
		return courseViews;
	}
}
