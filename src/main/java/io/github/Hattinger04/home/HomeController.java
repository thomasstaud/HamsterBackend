package io.github.Hattinger04.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@GetMapping(value={"/", "/index"})
	public String getHome(Model model) {
		return "home/index";
	}
	@PostMapping("/")
	public String postRequest(@RequestParam("text1") String s, Model model) {
		model.addAttribute("usereingabe", s); 
		return "user/index"; 
	}
}
