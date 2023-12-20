package at.ac.htlinn.courseManagement.activity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.model.Activity;

@Service
public class ActivityService {

	private ActivityRepository activityRepository;
	
	public ActivityService(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}
	
	
	
	public Activity saveActivity(Activity activity) {
		return activityRepository.save(activity);
	}

	public boolean deleteActivity(int activityId) {
		try {
			Activity activity = activityRepository.getById(activityId);
			activityRepository.delete(activity);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Activity getActivityById(int activityId) { 
		return activityRepository.getById(activityId);
	}

	public List<Activity> getAllActivitiesInCourse(int courseId) {
		
		// get exercises and convert to DTOs
		List<Activity> activities = new ArrayList<Activity>();
		for(Activity activity : activityRepository.getByCourseId(courseId)) {
			activities.add(activity);
		}
		
		return activities;
	}
}
