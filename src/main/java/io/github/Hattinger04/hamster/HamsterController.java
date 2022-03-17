package io.github.Hattinger04.hamster;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.Hattinger04.hamster.model.HamsterForm;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;

@Controller
public class HamsterController {
	
	@GetMapping(value = "/home/hamster")
	public String getHamster(@ModelAttribute HamsterForm form	) {
		return "home/hamster"; 
	}
	@PostMapping(value = "/home/hamster")
	public String postHamster(@ModelAttribute HamsterForm form, Model model) {
		Workbench wb = Workbench.getWorkbench(); 
		wb.startProgram("src/main/resources/hamster/testuser/data.ham");
		System.out.println("ArrayList: ");
		for(Map.Entry<String, String> entry : Workbench.jsonObject.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		return "home/hamster"; 
	}
}
