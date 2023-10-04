package io.github.Hattinger04.course.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public CourseService(CourseRepository courseRepository, ExerciseRepository exerciseRepository,
			SolutionRepository solutionRepository, TeacherRepository teacherRepository,
			StudentRepository studentRepository) {
		this.courseRepository = courseRepository;
		this.exerciseRepository = exerciseRepository;
		this.solutionRepository = solutionRepository;
		this.teacherRepository = teacherRepository;
		this.studentRepository = studentRepository;
	}

	public List<Course> getAllCourses() {
		try {
			return courseRepository.findAll();
		} catch (Exception e) {
			return null;
		}
	}

	public Course createCourse(Course course) {
		try {
			return courseRepository.save(course);
		} catch (Exception e) {
			return null;
		}

	}

	public boolean deleteCourse(int course_id) {
		try {
			List<User> students = getAllUsersInCourse(course_id);
			Student s;
			if (students != null) {
				for (User user : students) {
					if ((s = studentRepository.findByUserId(user.getId()).get(0)) != null) {
						removeStudentFromCourse(course_id, s.getId());
					}
				}
			}
			User teacher = getCourseTeachers(course_id).get(0);
			if (teacher != null) {
				removeTeacherFromCourse(course_id, teacher.getId());
			}
			Course course = getCourseByID(course_id);
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
	
	public List<Course> getCoursesByStudentId(int student_id) {
		return courseRepository.findCourseByStudentId(student_id);
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

	public List<User> getAllStudents() {
		List<User> users = new ArrayList<>();
		for (Student s : studentRepository.findAll()) {
			users.add(getUserByStudent(s));
		}
		return users;
	}

	
	public List<Student> getAllStudentsInCourse(int course_id) {
		try {
			List<String[]> studentsString = courseRepository.getAllStudents(course_id);
			return studentsString.stream().map(x -> new Student(Integer.parseInt(x[0]), Integer.parseInt(x[0]))).collect(Collectors.toList());
		} catch (Exception e) {
			return null; 
		}
	}

	public List<User> getAllUsersInCourse(int course_id) {
		List<Student> students = getAllStudentsInCourse(course_id); 
		if(students == null) {
			return null;
		}
		return students.stream().map(x -> getUserByStudent(x)).collect(Collectors.toList());
	}

	public List<User> getCourseTeachers(int course_id) {
		List<User> users = new ArrayList<>();
		for (String[] s : courseRepository.getCourseTeacher(course_id)) {
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
			// check if course exists
			if (courseRepository.doesCourseExist(course.getId()) == 0) {
				return false;
			}
			User user = getUserByStudent(new Student(teacher.getId(), teacher.getUser()));
			// check if user is already in course
			if (user != null && courseRepository.isUserInCourse(user.getId(), course.getId()) != 0) {
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

	public boolean removeTeacherFromCourse(int course_id, int teacher_id) {
		try {
			courseRepository.removeUserFromCourse(teacher_id, course_id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean addStudentToCourse(int course_id, Student student) {
		try {
			// check if course exists
			if (courseRepository.doesCourseExist(course_id) == 0) {
				return false;
			}
			// check if student is already in course
			User user = getUserByStudent(student);
			if (user != null && courseRepository.isUserInCourse(user.getId(), course_id) != 0) {
				return false;
			}
			// check if user is already teacher
			user = getUserByTeacher(new Teacher(student.getId(), student.getUser()));
			if (user != null && courseRepository.isUserTeacher(user.getId(), course_id) != 0) {
				return false;
			}
			Student s = studentRepository.save(student);
			courseRepository.addUserToCourse(s.getUser().getId(), course_id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Student getStudentByStudentId(int student_id) {
		return studentRepository.findById(student_id);
	}

	public Student getStudentByUserId(int user_id) {
		return studentRepository.findByUserId(user_id).get(0);
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

	public boolean removeStudentFromCourse(int course_id, int student_id) {
		try {
			courseRepository.removeUserFromCourse(student_id, course_id);
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

	public boolean deleteExercise(int exercise_id) {
		try {
			Exercise exercise = getExerciseByID(exercise_id);
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
	 * Get exercise by course_id
	 * 
	 * @param course_id
	 * @return
	 */
	public List<Exercise> getAllExercisesInCourse(int course_id) {
		return exerciseRepository.findByCourseId(course_id);
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

	// get all solutions for one exercise
	public List<Solution> getSolutionsByExerciseId(int exercise_id) {
		return solutionRepository.findByExerciseId(exercise_id);
	}

	// get all solutions for one student in a specific course
	public List<Solution> getSolutionsByStudentId(int student_id, int course_id) {
		return solutionRepository.findByStudentIdInSpecifiedCourse(student_id, course_id);
	}

	// TODO: teacher correcting students work
}
