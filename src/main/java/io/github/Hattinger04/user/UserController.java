package io.github.Hattinger04.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService service; 
	
	@GetMapping(value={"/user", "/user/index"})
	public String getUser(Model model) {
		return "user/index";
	}
	
	
	@PostMapping(value={"/user", "/user/index"})
	public String personDbRequest(@RequestParam("id") String str, Model model) {
		int id = Integer.parseInt(str); 
		User user = service.findOne(id); 
		if(user == null) {
			model.addAttribute("error", "nothing found!"); 
			return "redirect:/user/index"; 
		}
		model.addAttribute("id", user.getUserId()); 
		model.addAttribute("username", user.getUsername()); 
		model.addAttribute("password", user.getPassword()); 
		return "redirect:/user/index"; 
	}
	
	@GetMapping("/user/login")
	public String getLogin(Model model) {
		return "user/login";
	}

	@PostMapping("/user/login")
	public String getLogin(@ModelAttribute(name = "loginForm") User user, Model m) {
		String uname = user.getUsername();
		String pass = user.getPassword();
		if (uname.equals("Admin") && pass.equals("Admin@123")) {
			m.addAttribute("uname", uname);
			m.addAttribute("pass", pass);
			return "home/index";
		}
		m.addAttribute("error", "Incorrect Username & Password");
		return "redirect:/user/login";
	}
}
