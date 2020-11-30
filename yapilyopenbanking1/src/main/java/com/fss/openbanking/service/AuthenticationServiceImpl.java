/**
 * 
 */
package com.fss.openbanking.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fss.openbanking.adapter.RestAdapter;
import com.fss.openbanking.bean.AccountAdapter;
import com.fss.openbanking.bean.AdapterResponse;
import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ResponseDetails;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.constants.TppConstants;
import com.fss.openbanking.dao.DaoRepository;
import com.fss.openbanking.responseformatter.AccountsResponseFormatter;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired
	private DaoRepository daoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RestAdapter restAdapter;
	
	@Autowired
	private AccountsResponseFormatter accountsResponseFormatter; 
	
	public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "FSSAuthenticator";
	

	@Override
	public ResponseDetails tppCreateAccount(AuthenticationBean authenticationBean) {
		ResponseDetails responseDetails = new ResponseDetails();
		responseDetails.setResponseFlag("0");
    	responseDetails.setResponseMessage("User creation failed");
		try {
			    boolean createUserDetails = false;
			    
			    createUserDetails = daoRepository.checkAleardyCreatedAccount(authenticationBean);
			    
			    if(!createUserDetails)
				{
					responseDetails.setResponseFlag("2");
			    	throw new Exception("User already linked with FSS OpenBanking HUB");
				}
			    
			    AccountAdapter accountAdapter = new AccountAdapter();
				accountAdapter.setUrl( TppConstants.yapilyUrl + "/" + TppConstants.createuserUrl);
				accountAdapter.setCustomerName(authenticationBean.getCustomerName());
				accountAdapter.setMobileNumber(authenticationBean.getMobileNumber());
				AdapterResponse adapterResponse = restAdapter.callCreateUser(accountAdapter);
				
				if(!"201".equalsIgnoreCase(adapterResponse.getStatusCode()))
					createUserDetails = false;
				
			    if("409".equalsIgnoreCase(adapterResponse.getStatusCode())) {
					responseDetails.setResponseFlag("2");
			    	throw new Exception("User already linked with FSS OpenBanking HUB");
			    }
				
				if(!createUserDetails)
				{
					responseDetails.setResponseFlag("0");
			    	throw new Exception("User creation failed");
				}
				
				createUserDetails = accountsResponseFormatter.formatCreateUserDetails(adapterResponse.getData(), authenticationBean);
				
				authenticationBean.setEncodedPassword(passwordEncoder.encode((CharSequence)authenticationBean.getPassword()));
				
				createUserDetails = daoRepository.tppCreateAccount(authenticationBean);
				
				if(!createUserDetails)
				{
					responseDetails.setResponseFlag("0");
			    	throw new Exception("User creation failed");
				}
				
				responseDetails.setResponseFlag("1");
		    	responseDetails.setResponseMessage("User Created Successfully");
				
				
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return responseDetails;
	}


	@Override
	public UserDetails fetchUserDetailsbyUserId(String userId) {
		UserDetails userDetails = null;
		try {
			userDetails = daoRepository.fetchUserDetailsbyUserId(userId);
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return userDetails;
	}


	@Override
	public String generateQRUrl(UserDetails userDetails) {
		String qrURl = null;
		try {
			if (userDetails == null) 
				qrURl = "";
			if (userDetails != null)
			{
				userDetails.setSecret(Base32.random());
				qrURl = generateQRUrl(userDetails.getSecret(), userDetails.getMobileNumber());
			}
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return qrURl;
	}
	
	private String generateQRUrl(String secret, String mobileNumber) throws UnsupportedEncodingException {
        return QR_PREFIX + URLEncoder.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", APP_NAME, mobileNumber, secret , APP_NAME), "UTF-8");
    }


	@Override
	public boolean verifyCode(String authenticatorCode, String secret) {
		boolean verify = false;
		try {
			if(authenticatorCode !=null && secret !=null)
			{
			 final Totp totp = new Totp(secret);
			 
	            if (totp.verify(authenticatorCode))
	            	verify = true;
			}
	            
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return verify;
	}

}
