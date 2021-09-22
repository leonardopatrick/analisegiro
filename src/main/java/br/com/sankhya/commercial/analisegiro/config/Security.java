package br.com.sankhya.commercial.analisegiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/*@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) // THIS !!!

 */

public class Security extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("leonardo").password("123").roles("USER","ADMIN")
		.and().withUser("roberto").password("123").roles("USER");
	}
	
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	 @Override
		public void configure(HttpSecurity http) throws Exception {
			  http.authorizeRequests()
		        .antMatchers("/admin/**").hasRole("ADMIN")
		        .antMatchers("/api/produtos/**","/api/produtos").hasAnyRole("ADMIN")
		        .anyRequest().authenticated()
		        .and().formLogin()
		        .and().logout().logoutSuccessUrl("/login").permitAll()
		        .and().csrf().disable();
		  }
		  
		  @Override
	      public void configure(WebSecurity web) throws Exception {
	              web.ignoring()
					 .antMatchers("/v3/api-docs",
							  "/configuration/ui",
							  "/swagger-resources/**",
							  "/swagger-ui/**",
							  "/configuration/security",
							  "/swagger-ui.html",
							  "/webjars/**"
							  ,"/participant/**"
							  ,"/error/**"
							  ,"/swagger-ui/index.html");
	      }

}
