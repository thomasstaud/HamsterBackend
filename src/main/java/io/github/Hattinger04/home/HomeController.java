package io.github.Hattinger04.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserForm;
import io.github.Hattinger04.user.model.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping(value = { "/", "/home", "/home/index" })
	public String getHome(Model model) { 
		List<User> userList = userService.selectMany();
		model.addAttribute("userlist", userList);
		long count = userService.count();
		model.addAttribute("userListCount", count);
		return "home/index";
	}
	
	@GetMapping("/home/userDetail/{id:.+}")
	public String getUserDetail(@RequestParam(name="passwordRepeat", required = false) String passwordRepeat, @ModelAttribute UserForm form, Model model, @PathVariable("id") String userID) {
		if(userID.equals("-1")) {
			form.setUser_id(String.valueOf(userService.getFutureID()));
			model.addAttribute("userForm", form); 
			model.addAttribute("insert", true); 
		}
		else if (userID != null && userID.length() > 0) {
			User user = userService.findUserByID(Integer.valueOf(userID)); 
			form.setUser_id(String.valueOf(user.getId())); 
			form.setUsername(user.getUsername());
			model.addAttribute("passwordRepeat", "check if password is the same"); 
			model.addAttribute("userForm", form); 
		}
		return "home/userDetail";
	}
	
	@PostMapping(value = "/home/userDetail", params = "update")
	public String postUserDetailUpdate(@ModelAttribute("passwordRepeat") String passwordRepeat, @ModelAttribute UserForm form, Model model) {
		User user = new User(); 
		if(!form.getPassword().equals(passwordRepeat)) {
			model.addAttribute("result", "Passwords not the same");
			return getUserDetail(passwordRepeat, form, model, String.valueOf(form.getUser_id())); 
		}
		user.setId(Integer.valueOf(form.getUser_id())); 
		user.setUsername(form.getUsername()); 
		user.setPassword(form.getPassword()); 
		boolean result = userService.updateUser(user);
		if (result == true) {
			model.addAttribute("result", "Update success");
			return "redirect:/home/index";
		}
		return getUserDetail(passwordRepeat, form, model, String.valueOf(user.getId())); 	
	}

	@PostMapping(value = "/home/userDetail", params = "delete")
	public String postUserDetailDelete(@ModelAttribute UserForm form, Model model, RedirectAttributes redirectAtt) {
		User user = new User();
		user.setId(Integer.valueOf(form.getUser_id())); 
		user.setUsername(form.getUsername()); 
		user.setPassword(form.getPassword()); 
		boolean result = userService.deleteUser(user);
		if (result == true) {
			redirectAtt.addFlashAttribute("result", "Delete success");
		} else {
			redirectAtt.addFlashAttribute("result", "Delete failed");
		}
		return "redirect:/home/index";
	}
	
	@PostMapping(value = "/home/userDetail", params = "insert")
	public String postUserDetailInsert(@ModelAttribute("passwordRepeat") String passwordRepeat, @ModelAttribute UserForm form, Model model) {
		User user = new User(); 
		if(!form.getPassword().equals(passwordRepeat)) {
			model.addAttribute("error", "Passwords not the same");
			return getUserDetail(passwordRepeat, form, model, String.valueOf(form.getUser_id())); 
		}
		user.setId(Integer.valueOf(form.getUser_id())); 
		user.setUsername(form.getUsername()); 
		user.setPassword(form.getPassword()); 
		user.setEmail(form.getEmail()); 
		user.setLastName(form.getLastname()); 
		user.setName(form.getName()); 
		boolean result = userService.saveUser(user);
		if(result) {
			model.addAttribute("result", "Insert success");
			return "redirect:/home/index";
		}
		model.addAttribute("error", "Das Formular ist nicht korrekt ausgefüllt!"); 
		return getUserDetail(passwordRepeat, form, model, "-1"); 	
	}
}