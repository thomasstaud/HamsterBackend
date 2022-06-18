package io.github.Hattinger04.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/user/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/login");
		return modelAndView;
	}

	@GetMapping(value = "/user/registration")
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("user/registration");
		return modelAndView;
	}

	@PostMapping(value = "/user/registration")
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByUsername(user.getUsername());
		if (userExists != null) {
			bindingResult.rejectValue("username", "error.user", "Es gibt bereits einen User mit diesem Namen");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("user/registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User wurde erfogreich registriert!");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("user/registration");
		}
		return modelAndView;
	}
}