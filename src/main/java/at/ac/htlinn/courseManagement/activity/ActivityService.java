package at.ac.htlinn.courseManagement.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActivityService {

	private ActivityRepository activityRepository;

	public Activity getActivityById(int activityId) { 
		return (Activity)Hibernate.unproxy(activityRepository.getById(activityId));
	}
	
	public Activity saveActivity(Activity activity) {
		try {
			return activityRepository.save(activity);
		} catch(Exception e) {
			return null;
		}
	}
	
	public Activity updateActivity(Activity activity, Map<String, Object> fields) throws NoSuchFieldException, Exception {
		// attempt to update all specified fields
		// TODO: key currently needs to match actual field names instead of JSON field names
		for (Map.Entry<String, Object> set : fields.entrySet()) {
			try {
				Field field = activity instanceof Exercise ?
						Exercise.class.getDeclaredField(set.getKey()) : Contest.class.getDeclaredField(set.getKey());
		    	field.setAccessible(true);
		    	field.set(activity, set.getValue());
			}
			catch (NoSuchFieldException e) {
				System.out.println(e);
				throw new NoSuchFieldException(set.getKey());
			}
			catch (Exception e) {
				throw new Exception(set.getKey());
			}
		}
		
		return saveActivity(activity);
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

	public List<Activity> getAllActivitiesInCourse(int courseId) {
		
		// get exercises
		List<Activity> activities = new ArrayList<Activity>();
		for(Activity activity : activityRepository.getByCourseId(courseId)) {
			activities.add(activity);
		}
		
		return activities;
	}
}
