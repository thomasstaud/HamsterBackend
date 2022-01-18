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

import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@GetMapping(value = { "/", "/home", "/home/index" })
	public String getHome(Model model) {
		model.addAttribute("contents", "home/userList :: userList_contents");
		List<User> userList = userService.selectMany();
		model.addAttribute("userlist", userList);
		long count = userService.count();
		model.addAttribute("userListCount", count);
		return "home/index";
	}

	@GetMapping("/home/userDetail")
	public String getUserList(Model model) {
		model.addAttribute("contents", "home/index :: userDetail_contents");
		return "redirect:/home/index";
	}
	
	@GetMapping("/home/userDetail/{id:.+}")
	public String getUserDetail(@RequestParam(name="passwordRepeat", required = false) String passwordRepeat, @ModelAttribute User user, Model model, @PathVariable("id") String userID) {
		model.addAttribute("contents", "home/userDetail :: userDetail_contents");
		if (userID != null && userID.length() > 0) {
			model.addAttribute("user", user);
			model.addAttribute("passwordRepeat", "check if password is the same"); 
		}
		return "home/userDetail";
	}
	
	@PostMapping(value = "/home/userDetail", params = "update")
	public String postUserDetailUpdate(@ModelAttribute("passwordRepeat") String passwordRepeat, @ModelAttribute User user, Model model) {
		if(!user.getPassword().equals(passwordRepeat)) {
			model.addAttribute("result", "Passwords not the same");
			return getUserDetail(passwordRepeat, user, model, String.valueOf(user.getId())); 
		}
		boolean result = userService.updateUser(user);
		if (result == true) {
			model.addAttribute("result", "Update success");
			return getUserList(model);
		}
		return getUserDetail(passwordRepeat, user, model, String.valueOf(user.getId())); 	
	}

	@PostMapping(value = "/home/userDetail", params = "delete")
	public String postUserDetailDelete(@ModelAttribute User user, Model model) {
		boolean result = userService.deleteUser(user);
		if (result == true) {
			model.addAttribute("result", "Delete success");
		} else {
			model.addAttribute("result", "Delete failed");
		}
		return getUserList(model);
	}

}
