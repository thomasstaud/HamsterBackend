package io.github.Hattinger04;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ProjektWebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjektWebsiteApplication.class, args);
	}

}
