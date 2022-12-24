package io.github.Hattinger04.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:settings.properties")
public class CustomPasswordEncoder implements PasswordEncoder {

	@Value("${password.pepper}")
	private String pepper;

	@Value("${password.rounds}")
	private int rounds;

	
	public CustomPasswordEncoder() {}
	
	@Override
	public String encode(CharSequence rawPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(rounds);
		String saltedPassword = pepper + rawPassword + pepper;
		return encoder.encode(saltedPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String saltedPassword = pepper + rawPassword + pepper;
		return encoder.matches(saltedPassword, encodedPassword);
	}
}
