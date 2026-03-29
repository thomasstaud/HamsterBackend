package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ActivityDto;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestDto;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestParticipant;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestResults;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ExerciseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.SolutionService;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActivityService {

	private SolutionService solutionService;
	private CourseService courseService;
	private TerrainObjectService terrainObjectService;
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
	
	private Map<String, String> getFieldNameMap(Class<?> cls) {
	    Map<String, String> map = new HashMap<>();
	    
	    Field[] fields = cls.getDeclaredFields();
	    for (Field field : fields) {
	        if (field.isAnnotationPresent(JsonProperty.class)) {
	            String annotationValue = field.getAnnotation(JsonProperty.class).value();
	            map.put(annotationValue, field.getName());
	        } else {
	        	map.put(field.getName(), field.getName());
	        }
	    }
	    
	    return map;
	}
	
	public Activity updateActivity(Activity activity, Map<String, Object> fields) throws NoSuchFieldException, Exception {
		// attempt to update all specified fields

		ActivityDto activityDto = activity instanceof Exercise ? 
				new ExerciseDto((Exercise) activity) : new ContestDto((Contest) activity);
		
		// create maps to replace @JsonProperty values with actual field names
		Map<String, String> activityMap = getFieldNameMap(ActivityDto.class);
		Map<String, String> subclassMap = activityDto instanceof ExerciseDto ?
				getFieldNameMap(ExerciseDto.class) : getFieldNameMap(ContestDto.class);
		
		for (Map.Entry<String, Object> set : fields.entrySet()) {
			String key = set.getKey();
			Field field;
			
			// the field is looked for in the super- & subclass
			if (activityMap.containsKey(key)) {
				field = ActivityDto.class.getDeclaredField(activityMap.get(key));
			} else if (subclassMap.containsKey(key)) {
				field = activityDto instanceof ExerciseDto ?
					ExerciseDto.class.getDeclaredField(subclassMap.get(key))
					: ContestDto.class.getDeclaredField(subclassMap.get(key));
			} else {
				System.err.println("attempted to update invalid field " + set.getKey());
				throw new NoSuchFieldException(set.getKey());
			}
			
			// attempt to update field
			try {
		    	field.setAccessible(true);
		    	// TODO: updating Date fields does not work since Strings cannot be automatically casted to Dates
		    	field.set(activityDto, set.getValue());
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new Exception(set.getKey());
			}
		}
		
		activity = activity instanceof Exercise ? 
				new Exercise((ExerciseDto) activityDto, courseService, terrainObjectService)
				: new Contest((ContestDto) activityDto, courseService, terrainObjectService);
		
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
	
	public ContestResults getContestResults(Contest contest, int userId) {
		List<SolutionDto> solutions = solutionService.getSolutionsByActivityId(contest.getId());
		
		// remove all solutions that have not yet been submitted
		solutions.stream()
	    	      .filter(solution -> solution.isSubmitted())
	    	      .collect(Collectors.toList());
		
		Collections.sort(solutions, new Comparator<SolutionDto>() {
		   public int compare(SolutionDto o1, SolutionDto o2){
			  return o1.getSubmissionDate().compareTo(o2.getSubmissionDate());
		   }
		});
		
		Date start = contest.getStart();
		ContestResults results = new ContestResults();

		ContestParticipant firstPlace = new ContestParticipant();
		firstPlace.setUsername(solutions.get(0).getStudentName());
		firstPlace.setTime((solutions.get(0).getSubmissionDate().getTime() - start.getTime()) / 1000);
		results.setFirstPlace(firstPlace);
		
		if (solutions.size() > 1) {
			ContestParticipant secondPlace = new ContestParticipant();
			secondPlace.setUsername(solutions.get(1).getStudentName());
			secondPlace.setTime((solutions.get(1).getSubmissionDate().getTime() - start.getTime()) / 1000);
			results.setSecondPlace(secondPlace);
		}
		
		if (solutions.size() > 2) {
			ContestParticipant thirdPlace = new ContestParticipant();
			thirdPlace.setUsername(solutions.get(2).getStudentName());
			thirdPlace.setTime((solutions.get(2).getSubmissionDate().getTime() - start.getTime()) / 1000);
			results.setThirdPlace(thirdPlace);
		}
		
		SolutionDto userSolution = new SolutionDto(solutionService.getSolutionByActivityAndStudentId(contest.getId(), userId));
		int userPlace = solutions.indexOf(userSolution);
		long userTime = (userSolution.getSubmissionDate().getTime() - start.getTime()) / 1000;
		results.setUserPlace(userPlace + 1);
		results.setUserTime(userTime);
		
		return results;
	}
}
