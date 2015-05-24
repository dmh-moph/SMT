package smt.config;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import smt.auth.service.CustomAuthenticationProvider;
import smt.auth.service.CustomUserDetailsService;
import smt.auth.service.SecUserEntityService;
import smt.auth.service.SecUserEntityServiceJPA;


@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth        	
        	.userDetailsService(userDetailService());
    }

	protected void configure(HttpSecurity http) throws Exception {
	    http
	    	.headers()
	    		.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
	    			.and()
	    	.csrf().disable()
			.authorizeRequests()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/login**").permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
			.formLogin()
				.loginPage("/login").permitAll()
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/")
				.and()
			.logout()
			 	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout_successful=1")
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint());
				
	}
	
	
	@Bean 
	public DelegatingAuthenticationEntryPoint restAuthenticationEntryPoint() {
		LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = 
				new LinkedHashMap<RequestMatcher, AuthenticationEntryPoint>();
		
		entryPoints.put(
				new RegexRequestMatcher("^/REST/.*", null), 
				new Http403ForbiddenEntryPoint());
		
		
		DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint =
				new DelegatingAuthenticationEntryPoint(entryPoints);
		
		delegatingAuthenticationEntryPoint
			.setDefaultEntryPoint(new LoginUrlAuthenticationEntryPoint("/login") );
		
		return delegatingAuthenticationEntryPoint;
	}
	
	
	@Bean 
	public UserDetailsService userDetailService() {
		return new CustomUserDetailsService();
	}
	
}
