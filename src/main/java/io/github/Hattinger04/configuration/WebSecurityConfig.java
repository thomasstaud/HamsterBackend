
package io.github.Hattinger04.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import io.github.Hattinger04.user.model.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String loginPage = "/user/login"; 
		String registrationPage = "/user/registration"; 
		

		http.authorizeRequests()
				.antMatchers(loginPage, registrationPage, "/user/**", "/hamster/**").permitAll()
				.anyRequest().authenticated()
				.and().csrf().disable().httpBasic().and()
				.requiresChannel().antMatchers("/**").requiresSecure().and()
				.formLogin().loginPage(loginPage).failureUrl("/user/login?error=true").defaultSuccessUrl("/user/home")
				.usernameParameter("username").passwordParameter("password").and().logout()
				.logoutSuccessUrl(loginPage).and()
				.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and()
				.exceptionHandling().authenticationEntryPoint((request, response, exception) -> {
	                response.setStatus(401);
	            })  
	            .accessDeniedHandler((request, response, exception) -> {
	                response.setStatus(403);
	            });
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/webjars/**");
	}

}