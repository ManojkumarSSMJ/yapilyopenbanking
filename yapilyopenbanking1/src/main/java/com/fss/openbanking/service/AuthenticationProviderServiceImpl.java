/**
 * 
 */
package com.fss.openbanking.service;
import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fss.openbanking.bean.UserDetails;
@Component("authenticationProvider")
public class AuthenticationProviderServiceImpl implements AuthenticationProvider {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userId = null;
        String password = null;
        String role = null;
        UserDetails userDetails = null;
		userId = authentication.getName();
		password = authentication.getCredentials().toString();
		
		userDetails = authenticationService.fetchUserDetailsbyUserId(userId);
		
		if(userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword()))
			 throw new BadCredentialsException("Invalid username or password");
		
		if("1".equals(userDetails.getRole()))
			role = "ROLE_ADMIN";
		if("2".equals(userDetails.getRole()))
			role = "ROLE_USER";
			
        return new UsernamePasswordAuthenticationToken(userDetails, password, Arrays.asList(new SimpleGrantedAuthority(role)));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
