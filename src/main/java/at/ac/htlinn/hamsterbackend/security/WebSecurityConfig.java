package at.ac.htlinn.hamsterbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomPasswordEncoder encoder;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String loginPage = "/api/login";
        String registrationPage = "/api/registration";
        String errorPage = "/api/error";

        http.authorizeRequests()
                .antMatchers(loginPage, registrationPage, errorPage).permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable()
                .requiresChannel().antMatchers("/**").requiresSecure().and()
//				.formLogin().loginPage(loginPage).failureUrl("/user/login?error=true").defaultSuccessUrl("/user/home")
//				.usernameParameter("username").passwordParameter("password").and()
//				.logout().logoutSuccessUrl(loginPage).and()
                .cors().and()
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