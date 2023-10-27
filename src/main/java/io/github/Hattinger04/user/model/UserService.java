package io.github.Hattinger04.user.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.github.Hattinger04.configuration.CustomPasswordEncoder;
import io.github.Hattinger04.role.Role;
import io.github.Hattinger04.role.RoleRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private CustomPasswordEncoder customPasswordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			CustomPasswordEncoder customPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.customPasswordEncoder = customPasswordEncoder;
	}

	public User findUserByID(long id) {
		return userRepository.findById((int)id);
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean saveUser(User user) {
		try {
			user.setPassword(customPasswordEncoder.encode(user.getPassword()));
			user.setActive(true);
			Role userRole = roleRepository.findByRole("USER");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userRepository.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean updateUser(User user) {
		try {
			User u = userRepository.findById(user.getId());
			u.setUsername(user.getUsername());
			u.setPassword(user.getPassword());
			saveUser(u);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public long getFutureID() {
		return userRepository.getNextSeriesId().get(1);
	}

	public boolean deleteUser(long id) {
		userRepository.delete(findUserByID(id));
		return true;
	}

	public List<User> selectMany() {
		return userRepository.findAll();
	}

	public long count() {
		return userRepository.count();
	}
	
	public boolean insertUserRole(long user_id, long role_id) {
		try {
			userRepository.insertUserRole(user_id, role_id); 
			return true; 
		} catch (Exception e) {
			return false; 
		}
	}
	
	public boolean removeUserRole(long user_id, long role_id) {
		try {
			userRepository.removeUserRole(user_id, role_id); 
			return true; 
		} catch (Exception e) {
			return false; 
		}
	}
	
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String username;
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		System.out.println(principal);
		
		// check for mock user
		if (username.equals("user")) {
			User user = new User();
			HashSet<Role> roles = new HashSet<Role>();
			// TODO: check for actual mock user roles instead of setting it to admin
			roles.add(new Role(1, "ADMIN"));
			user.setRoles(roles);
			return user;
		}
		
		return findUserByUsername(username);
	}
}
