package io.github.Hattinger04.observer;

import java.util.ArrayList;
import java.util.List;

public class Workshop {

	List<Student> students = new ArrayList<Student>(); 
	
	public void addStudent(Student student) {
		students.add(student); 
	}
	
	public void removeStudent(Student student) {
		students.remove(student); 
	}
	
	public void publishExercise(Exercise exercise) {
		for(Student student : students) {
			student.receiveExercise(exercise);
		}
	}
}

class HamsterWorkshop extends Workshop {
	private Exercise exercise;
	
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
		publishExercise(exercise);
	}
	
	public Exercise getExercise() {
		return exercise;
	}
}