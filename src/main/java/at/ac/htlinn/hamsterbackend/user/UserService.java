package at.ac.htlinn.hamsterbackend.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.role.model.Role;
import at.ac.htlinn.hamsterbackend.role.RoleRepository;
import at.ac.htlinn.hamsterbackend.user.model.User;

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

	public User saveUser(User user) {
		try {
			user.setPassword(customPasswordEncoder.encode(user.getPassword()));
			user.setActive(true);
			Role userRole = roleRepository.findByRole("USER");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			return userRepository.save(user);
		} catch (Exception e) {
			return null;
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
	
	// check if user is privileged (administrator or developer)
	// 		used to e.g. check whether users can edit a course
	//		teachers need to have created the course, administrators and developers can always edit it
	public boolean isUserPrivileged(User user) {
		boolean privileged = false;
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("ADMIN") || role.getRole().equals("DEV")) {
				privileged = true;
			}
		}
		return privileged;
	}
}
