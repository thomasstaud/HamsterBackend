package io.github.Hattinger04.hamster;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.hamster.model.HamsterForm;
import io.github.Hattinger04.hamsterEvaluation.workbench.Workbench;

@Controller
public class HamsterController {
	
	@GetMapping(value = "/home/hamster")
	public String getHamster(@ModelAttribute HamsterForm form) {
		return "home/hamster"; 
	}
	@PostMapping(value = "/home/hamster")
	public String postHamster(@ModelAttribute HamsterForm form, Model model) {
		Workbench wb = Workbench.getWorkbench(); 
		wb.startProgram("src/main/resources/hamster/testuser/data.ham");
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			model.addAttribute("json", objectMapper.writeValueAsString(Workbench.getWorkbench().getJsonObject().entrySet()));
			return "home/hamster"; 
		} catch (JsonProcessingException e) {
			return "home/hamster"; 
		} 
	}
}
