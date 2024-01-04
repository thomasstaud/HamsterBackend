package at.ac.htlinn.courseManagement.teacher;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.teacher.model.TeacherActivityDto;
import at.ac.htlinn.courseManagement.teacher.model.TeacherContestDto;
import at.ac.htlinn.courseManagement.teacher.model.TeacherCourseDto;
import at.ac.htlinn.courseManagement.teacher.model.TeacherExerciseDto;
import at.ac.htlinn.user.model.User;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {
	
	private TeacherRepository teacherRepository;
	private CourseService courseService;
	private ActivityService activityService;
	
	
	
	public User getCourseTeacher(int courseId) {
		return teacherRepository.getCourseTeacher(courseId);
	}

	public boolean isUserTeacher(int userId, int courseId) {
		return teacherRepository.isUserTeacher(userId, courseId) != 0;
	}
	
	public List<TeacherCourseDto> getTeacherView(int teacherId) {
		
		List<TeacherCourseDto> courseViews = new ArrayList<TeacherCourseDto>();
		// get activities for each course
		for (Course course : courseService.getCoursesByTeacherId(teacherId)) {
			List<TeacherActivityDto> activityViews = new ArrayList<TeacherActivityDto>();
			// get activity view for each activity
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				
				// add exercise or contest to list
				if (activity instanceof Exercise)
					activityViews.add(new TeacherExerciseDto((Exercise)activity));
				else
					activityViews.add(new TeacherContestDto((Contest)activity));
			}
			courseViews.add(new TeacherCourseDto(course, activityViews));
		}
		
		return courseViews;
	}
}
