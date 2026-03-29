package at.ac.htlinn.hamsterbackend.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.role.model.Role;
import at.ac.htlinn.hamsterbackend.user.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userServiceImpl;

    @Override
    @Transactional
	public UserDetails loadUserByUsername(String username) {
        User user = userServiceImpl.findUserByUsername(username);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }
    
    public List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : userRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities; 
    }

    public UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
    	return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getActive(), true, true, true, authorities);
    }
    
}
