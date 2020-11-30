/**
 * 
 */
package com.fss.openbanking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import com.fss.openbanking.service.AuthenticationProviderServiceImpl;

/**
 * @author Selvakumar
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	

	 @Autowired
	 private AuthenticationProviderServiceImpl authenticationProviderServiceImpl;
	 
	 
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
       
		  http
          .authorizeRequests()
              .antMatchers("/","/**","/index","/generateQrCode","/verifyQrCode","/updateInstitutions","/css/**", "/fonts/**","/images/**","/js/**").permitAll() 
              .anyRequest().authenticated()
              
              .and()
              .formLogin()
              	.loginPage("/login")
              	.loginProcessingUrl("/tppAuthenticate")
              	.failureUrl("/loginFail")
              	.defaultSuccessUrl("/loginSuccess")
              	.permitAll()
              	
              .and()
              .logout()
              .logoutUrl("/logout")
              .logoutSuccessUrl("/index")
              .permitAll();
   }
	
  
      
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
   
   @Override
   public void configure(WebSecurity web) throws Exception {
       //@formatter:off
       super.configure(web);
       web.httpFirewall(defaultHttpFirewall());
 
   }
   @Bean
   public HttpFirewall defaultHttpFirewall() {
       return new DefaultHttpFirewall();
   }
   
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider(authenticationProviderServiceImpl);
   }
  
}
