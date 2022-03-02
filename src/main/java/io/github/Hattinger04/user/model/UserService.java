package io.github.Hattinger04.user.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.Hattinger04.role.Role;
import io.github.Hattinger04.role.RoleRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	public User findUserByID(int id) {
		return userRepository.findById(id);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean saveUser(User user) {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

	public boolean deleteUser(User user) {
		userRepository.delete(user);
		return true;
	}

	public List<User> selectMany() {
		return userRepository.findAll();
	}

	public long count() {
		return userRepository.count();
	}

}
