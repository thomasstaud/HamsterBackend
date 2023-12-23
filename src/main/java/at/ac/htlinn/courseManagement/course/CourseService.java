package at.ac.htlinn.courseManagement.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.ActivityViewDTO;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.ContestViewDTO;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
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
		int activityId = solution.getActivityId();
		return courseRepository.getByActivityId(activityId);
	}
	

	
	public List<CourseViewDTO> getStudentCourseViews(int studentId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get activitys for each course
		for (Course course : getCoursesByStudentId(studentId)) {
			List<ActivityViewDTO> activityViews = new ArrayList<ActivityViewDTO>();
			// get activity view for each activity
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				Solution solution = solutionService.getSolutionByActivityAndStudentId(activity.getId(), studentId);
				
				// add exercise or contest to list
				// TODO: improve this mess
				if(solution != null)
					if (activity instanceof Exercise)
						activityViews.add(new ExerciseViewDTO((Exercise)activity, solution));
					else
						activityViews.add(new ContestViewDTO((Contest)activity, solution));
				else
					if (activity instanceof Exercise)
						activityViews.add(new ExerciseViewDTO((Exercise)activity));
					else
						activityViews.add(new ContestViewDTO((Contest)activity));
			}
			courseViews.add(new CourseViewDTO(course, activityViews));
		}
		
		return courseViews;
	}
	
	public List<CourseViewDTO> getTeacherCourseViews(int teacherId) {
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get activitys for each course
		for (Course course : getCoursesByTeacherId(teacherId)) {
			List<ActivityViewDTO> activityViews = new ArrayList<ActivityViewDTO>();
			// get activity view for each activity
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				
				// add exercise or contest to list
				if (activity instanceof Exercise)
					activityViews.add(new ExerciseViewDTO((Exercise)activity));
				else
					activityViews.add(new ContestViewDTO((Contest)activity));
			}
			courseViews.add(new CourseViewDTO(course, activityViews));
		}
		
		return courseViews;
	}
}
