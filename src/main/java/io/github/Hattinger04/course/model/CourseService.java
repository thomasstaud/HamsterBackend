package io.github.Hattinger04.course.model;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseRepository;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseRepository;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionRepository;
import io.github.Hattinger04.course.model.student.Student;
import io.github.Hattinger04.course.model.student.StudentRepository;
import io.github.Hattinger04.course.model.teacher.Teacher;
import io.github.Hattinger04.course.model.teacher.TeacherRepository;
import io.github.Hattinger04.user.model.User;

@Service
public class CourseService {

	// TODO: Nothing(!) tested yet
	// TODO: making void - boolean

	private CourseRepository courseRepository;
	private ExerciseRepository exerciseRepository;
	private SolutionRepository solutionRepository;
	private TeacherRepository teacherRepository;
	private StudentRepository studentRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, ExerciseRepository exerciseRepository,
			SolutionRepository solutionRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository) {
		this.courseRepository = courseRepository;
		this.exerciseRepository = exerciseRepository;
		this.solutionRepository = solutionRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
	}

	public Course createCourse(Course course) {
		return courseRepository.save(course);
	}

	public boolean deleteCourse(Course course) {
		try {
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Course getCourseByID(int id) {
		return courseRepository.findById(id);
	}

	public Course getCourseByName(String name) {
		return courseRepository.findByName(name);
	}

	public Student getStudent(Course course, Student student) {
		Student s = null;
		try {
			if ((s = studentRepository.findById(student.getId())) == null) { // TODO: if not tested!
				s = studentRepository.findByName(student.getUser().getUsername());
			}
		} catch (NullPointerException e) {
			return s;
		}
		return s;
	}

	public List<User> getAllStudents(Course course) {
		return courseRepository.getAllStudents(course.getId());
	}

	public User getCourseTeacher(Course course) {
		return courseRepository.getCourseTeacher(course.getId());
	}

	public boolean setCourseTeacher(Course course, Teacher teacher) {
		try {
			teacherRepository.save(teacher);
			courseRepository.addUserToCourse(teacher.getId(), course.getId());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean deleteCourseTeacher(Course course, Teacher teacher) {
		try {
			teacherRepository.delete(teacher);
			courseRepository.removeUserFromCourse(teacher.getId(), course.getId());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	// TODO: working with student / teacher table
	public boolean addStudentToCourse(Course course, Student student) {
		try {
			studentRepository.save(student);
			courseRepository.addUserToCourse(student.getUser().getId(), course.getId());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean addStudentsToCourse(Course course, Set<Student> students) {
		try {
			for (Student student : students) {
				studentRepository.save(student);
				courseRepository.addUserToCourse(student.getId(), course.getId());
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean removeStudentFromCourse(Course course, Student student) {
		try {
			studentRepository.delete(student);
			courseRepository.removeUserFromCourse(student.getId(), student.getId());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean removeStudentsFromCourse(Course course, Set<Student> students) {
		try {
			for (Student student : students) {
				studentRepository.delete(student);
				courseRepository.removeUserFromCourse(student.getId(), student.getId());
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isUserTeacher(Course course, User user) {
		return courseRepository.isUserTeacher(user.getId(), course.getId()) != 0;
	}

	public boolean isUserStudent(Course course, User user) {
		return courseRepository.isUserStudent(user.getId(), course.getId()) != 0;
	}

	public boolean isUserInCourse(Course course, User user) {
		return courseRepository.isUserStudent(user.getId(), course.getId()) != 0
				|| courseRepository.isUserTeacher(user.getId(), course.getId()) != 0;
	}

	public Exercise createExercise(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}

	public boolean deleteExercise(Exercise exercise) {
		try {
			exerciseRepository.delete(exercise);
			return true; 
		} catch (IllegalArgumentException e) {
			return false; 
		}
	}

	public Exercise getExerciseByID(int id) {
		return exerciseRepository.findById(id);
	}

	/**
	 * Get exercise by course_id and course name
	 * 
	 * @param course_id
	 * @param name
	 * @return
	 */
	public Exercise getExerciseByCourse(int course_id, String name) {
		return exerciseRepository.findByCourse(course_id, name);
	}

	public Solution createSolution(Solution solution) {
		return solutionRepository.save(solution);
	}

	public boolean deleteSolution(Solution solution) {
		try {
			solutionRepository.delete(solution);
			return true; 
		} catch (IllegalArgumentException e) {
			return false; 
		}
	}

	public Solution getSolutionByID(int id) {
		return solutionRepository.findById(id);
	}

	/**
	 * Get solution by exercise_id and exercise name
	 * 
	 * @param exercise_id
	 * @param name
	 * @return
	 */
	public Solution getSolutionByExercise(int exercise_id, String name) {
		return solutionRepository.findByExercise(exercise_id, name);
	}

	// TODO: teacher correcting students work
}
