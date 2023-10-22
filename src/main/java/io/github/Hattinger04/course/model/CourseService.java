package io.github.Hattinger04.course.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseRepository;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseRepository;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionRepository;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@Service
public class CourseService {

	private CourseRepository courseRepository;
	private ExerciseRepository exerciseRepository;
	private SolutionRepository solutionRepository;
	private UserService userService;

	public CourseService(CourseRepository courseRepository, ExerciseRepository exerciseRepository,
			SolutionRepository solutionRepository, UserService userService) {
		this.courseRepository = courseRepository;
		this.exerciseRepository = exerciseRepository;
		this.solutionRepository = solutionRepository;
		this.userService = userService;
	}
	
	public List<User> getAllStudentsInCourse(int course_id) {
		try {
			List<String[]> studentsString = courseRepository.getAllStudents(course_id);
			return studentsString.stream().map(
					x -> new User(Integer.parseInt(x[0]), x[1])).collect(Collectors.toList()
			);
		} catch (Exception e) {
			return null;
		}
	}

	public User getCourseTeacher(int course_id) {
		List<User> teachers = new ArrayList<>();
		for (String[] s : courseRepository.getCourseTeacher(course_id)) {
			teachers.add(new User(Integer.valueOf(s[0]), s[1]));
		}
		
		// There has to be exactly one teacher per course
		if (teachers.size() != 1) {
			return null;
		}
		
		return teachers.get(0);
	}

	public boolean addStudentToCourse(int course_id, int student_id) {
		try {
			// check if course and student exist
			if (courseRepository.doesCourseExist(course_id) == 0
					|| userService.findUserByID(student_id) == null) {
				return false;
			}
			// check if student is already in course
			if (isUserStudent(student_id, course_id)) {
				return false;
			}
			// check if student is already teacher
			if (isUserTeacher(student_id, course_id)) {
				return false;
			}
			courseRepository.addUserToCourse(student_id, course_id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean removeStudentFromCourse(int course_id, int student_id) {
		try {
			courseRepository.removeUserFromCourse(student_id, course_id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isUserTeacher(int user_id, int course_id) {
		return courseRepository.isUserTeacher(user_id, course_id) != 0;
	}

	public boolean isUserStudent(int user_id, int course_id) {
		return courseRepository.isUserStudent(user_id, course_id) != 0;
	}

	public boolean isUserInCourse(int user_id, int course_id) {
		return courseRepository.isUserStudent(user_id, course_id) != 0
				|| courseRepository.isUserTeacher(user_id, course_id) != 0;
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

	public boolean deleteCourse(int course_id) {
		try {
			List<User> students = getAllStudentsInCourse(course_id);
			if (students != null) {
				for (User student : students) {
					removeStudentFromCourse(course_id, student.getId());
				}
			}
			
			Course course = getCourseById(course_id);
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return false;
		}
	}

	public Course getCourseById(int id) {
		return courseRepository.findById(id);
	}

	public Course getCourseByName(String name) {
		return courseRepository.findByName(name);
	}
	
	public List<Course> getCoursesByStudentId(int student_id) {
		return courseRepository.getCoursesByStudentId(student_id);
	}
	
	
	
	public Exercise saveExercise(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}

	public boolean deleteExercise(int exercise_id) {
		try {
			Exercise exercise = getExerciseById(exercise_id);
			exerciseRepository.delete(exercise);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Exercise getExerciseById(int id) { 
		return exerciseRepository.findById(id);
	}

	public List<Exercise> getAllExercisesInCourse(int course_id) {
		return exerciseRepository.findByCourseId(course_id);
	}

	
	
	public Solution saveSolution(Solution solution) {
		return solutionRepository.save(solution);
	}

	public boolean deleteSolution(int solution_id) {
		try {
			Solution solution = getSolutionById(solution_id);
			solutionRepository.delete(solution);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Solution getSolutionById(int id) {
		return solutionRepository.findById(id);
	}

	// get all solutions for one exercise
	public List<Solution> getSolutionsByExerciseId(int exercise_id) {
		return solutionRepository.findByExerciseId(exercise_id);
	}

	// get solution for one exercise for a specified student
	public Solution getSolutionByExerciseAndStudentId(int exercise_id, int student_id) {
		return solutionRepository.findByExerciseAndStudentId(exercise_id, student_id);
	}

	// get all solutions for one student in a specific course
	public List<Solution> getSolutionsByStudentId(int student_id, int course_id) {
		return solutionRepository.findByStudentIdInSpecifiedCourse(student_id, course_id);
	}
}
