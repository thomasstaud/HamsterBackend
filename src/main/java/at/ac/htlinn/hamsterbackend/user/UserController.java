package at.ac.htlinn.hamsterbackend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.hamsterbackend.user.model.User;

@RestController
@RequestMapping("/users")
public class UserController {

	static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper mapper;

	/**
	 * GET all users or one user by username
	 * optional @RequestParam username
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username", required = false) String username) {
		if(username == null) {
			return new ResponseEntity<>(userService.selectMany(), HttpStatus.OK);
		}
		log.info("XXXXUSERNAME:" + username + "|");
		User userFound = userService.findUserByUsername(username);
		log.info("USER:" + userFound);

		if (userFound == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userFound, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Get user by id. Needs as @PathVariable id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("{id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getUserById(@PathVariable long id) {
		User userFound = userService.findUserByID(id);
		if (userFound == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(userFound, HttpStatus.OK);
	}

	/**
	 * Creating new user if not exist Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */

	@PostMapping
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> createUser(@RequestBody JsonNode node) {

		User user = mapper.convertValue(node.get("user"), User.class);
		if (user == null) return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
		
		user = userService.saveUser(user);
		return user != null ? ResponseEntity.ok(user.getId())
				: new ResponseEntity<>("Could not create user!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Updating user Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> updateUser(@RequestBody JsonNode node) {
		User user = mapper.convertValue(node.get("user"), User.class);
		if (!userService.updateUser(user)) {
			return new ResponseEntity<>("Could not update user!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Deleting user Needs as RequestBody user object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("{userId}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> deleteUser(@PathVariable int userId) {
		if (!userService.deleteUser(userId)) {
			return new ResponseEntity<>("Could not delete user!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 
	 * Adding role to user. Needs as PathVariable role_id and userId
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("{userId}/roles/{roleId}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> addRole(@PathVariable int userId, @PathVariable int roleId) {
		if (!userService.insertUserRole(userId, roleId)) {
			return new ResponseEntity<>("Could not insert new Role!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * 
	 * Removing role from user. Needs as PathVariable role_id and user_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("{userId}/roles/{roleId}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> removeRole(@PathVariable int userId, @PathVariable int roleId) {
		if (!userService.removeUserRole(userId, roleId)) {
			return new ResponseEntity<>("Could not remove Role!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
