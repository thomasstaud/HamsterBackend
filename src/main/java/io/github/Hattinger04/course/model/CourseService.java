package io.github.Hattinger04.course.model;

import java.util.ArrayList;
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
			List<User> students = getAllStudents(course);
			Student s;
			for(User user : students) {
				if ((s = studentRepository.findByUserId(user.getId()).get(0)) != null) {
					removeStudentFromCourse(course, s);
				}
			}
			User teacher = getCourseTeachers(course).get(0); 
			if(teacher != null) {
				deleteCourseTeacher(course, teacherRepository.findByUserId(teacher.getId())); 
			}
			courseRepository.delete(course);
			return true;
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return false;
		}
	}

	public Course getCourseByID(int id) {
		return courseRepository.findById(id);
	}

	public Course getCourseByName(String name) {
		return courseRepository.findByName(name);
	}

	/**
	 * Returns user by student
	 * 
	 * @param student
	 * @return
	 */
	public User getUserByStudent(Student student) {
		User user = getUserByStudentID(student);
		return user == null ? getUserByStudentUsername(student) : user;
	}

	private User getUserByStudentID(Student student) {
		try {
			Student s = studentRepository.findByUserId(student.getUser().getId()).get(0);
			return new User(s.getUser().getId(), s.getUser().getUsername());
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			return null;
		}
	}

	private User getUserByStudentUsername(Student student) {
		try {
			Student s = studentRepository.findByName(student.getUser().getUsername()).get(0);
			return new User(s.getUser().getId(), s.getUser().getUsername());
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			return null;
		}
	}

	public List<User> getAllStudents(Course course) {
		List<User> users = new ArrayList<>();
		for (String[] s : courseRepository.getAllStudents(course.getId())) {
			users.add(new User(Integer.valueOf(s[0]), s[1]));
		}
		return users;
	}

	public List<User> getCourseTeachers(Course course) {
		List<User> users = new ArrayList<>();
		for (String[] s : courseRepository.getCourseTeacher(course.getId())) {
			users.add(new User(Integer.valueOf(s[0]), s[1]));
		}
		return users; 
	}

	public User getUserByTeacher(Teacher teacher) {
		User user = getUserByTeacherID(teacher);
		return user == null ? getUserByTeacherUsername(teacher) : user;
	}

	private User getUserByTeacherID(Teacher teacher) {
		try {
			Teacher t = teacherRepository.findByUserId(teacher.getUser().getId());
			return new User(t.getUser().getId(), t.getUser().getUsername());
		} catch (NullPointerException e) {
			return null;
		}
	}

	private User getUserByTeacherUsername(Teacher teacher) {
		try {
			Teacher t = teacherRepository.findByName(teacher.getUser().getUsername());
			return new User(t.getUser().getId(), t.getUser().getUsername());
		} catch (NullPointerException e) {
			return null;
		}
	}
	public boolean setCourseTeacher(Course course, Teacher teacher) {
		try {
			// check if course is exisiting
			if (courseRepository.doesCourseExist(course.getId()) == 0) {
				return false; 
			}
			User user = getUserByStudent(new Student(teacher.getId(), teacher.getUser())); 
			// check if user is already in course
			if(user != null && courseRepository.isUserInCourse(user.getId(), course.getId()) != 0) {
				return false; 
			}
			// check if user is already student
			if (user != null && courseRepository.isUserStudent(user.getId(), course.getId()) != 0) {
				return false;
			}
			Teacher t = teacherRepository.save(teacher);
			courseRepository.addUserToCourse(t.getUser().getId(), course.getId());
			return true;
		} catch (IllegalArgumentException | NullPointerException e) {
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

	public boolean addStudentToCourse(Course course, Student student) {
		try {
			// check if course is existing
			if (courseRepository.doesCourseExist(course.getId()) == 0) {
				return false; 
			}
			// check if student is already in course
			User user = getUserByStudent(student);
			if(user != null && courseRepository.isUserInCourse(user.getId(), course.getId()) != 0) {
				return false; 
			}
			// check if user is already teacher
			user = getUserByTeacher(new Teacher(student.getId(), student.getUser()));
			if (user != null && courseRepository.isUserTeacher(user.getId(), course.getId()) != 0) {
				return false;
			}
			Student s = studentRepository.save(student);
			courseRepository.addUserToCourse(s.getUser().getId(), course.getId());
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
