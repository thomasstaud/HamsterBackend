package at.ac.htlinn.hamsterbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/files/**")
	    .setCachePeriod(0)
	    .resourceChain(true)
	    .addResolver(new PathResourceResolver());
    }
    
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO: delete not needed origins when publishing 
		registry.addMapping("/**").allowedOrigins("http://localhost:8081").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
		.allowCredentials(true).allowedHeaders("*").exposedHeaders("set-cookie").maxAge(3600); 
	}
	
	@Bean
	public RoleHierarchy roleHierarchy() {
	    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
	    String hierarchy = "ADMIN > DEV \n DEV > TEACHER \n TEACHER > USER";
	    roleHierarchy.setHierarchy(hierarchy);
	    return roleHierarchy;
	}

}
