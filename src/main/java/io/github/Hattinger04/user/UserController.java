package io.github.Hattinger04.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	@GetMapping(value={"/user/", "/user/index"})
	public String getUser(Model model) {
		return "user/index";
	}
}
