package io.github.Hattinger04.hamster.model;

import org.springframework.stereotype.Service;

@Service
public class HamsterService {

	public Hamster getHamsterFromHamsterForm(HamsterForm form) {
		Hamster hamster = new Hamster(); 
		hamster.setHamster_id(Integer.valueOf(form.getHamster_id()));
		hamster.setProgram(form.getProgram());
		return hamster; 
	}
}
