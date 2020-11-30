/**
 * 
 */
package com.fss.openbanking.service;

import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ResponseDetails;
import com.fss.openbanking.bean.UserDetails;

public interface AuthenticationService {

	ResponseDetails tppCreateAccount(AuthenticationBean authenticationBean);

	UserDetails fetchUserDetailsbyUserId(String userId);

	String generateQRUrl(UserDetails userDetails);

	boolean verifyCode(String authenticatorCode, String secret);
}
