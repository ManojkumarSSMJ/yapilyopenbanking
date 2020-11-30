/**
 * 
 */
package com.fss.openbanking.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ResponseDetails;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.service.AuthenticationService;
import com.fss.openbanking.service.SettingsService;

@Controller
public class AuthenticationController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private SettingsService settingsService;
	
	
	@RequestMapping("loginSuccess")
	public ModelAndView tppAuthenticate(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("page.qr");
		try {
			
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			HttpSession session = request.getSession();
			if (session!=null && !session.isNew()) {
			    session.invalidate();
			}
			
			HttpSession newsession = request.getSession(true); // create the session
			newsession.setAttribute("userDetails",userDetails);
			
			String loginAuthenticatorFlag = settingsService.fetchLoginAuthenticatorFlag(userDetails.getUserId());
			
			if("yes".equalsIgnoreCase(loginAuthenticatorFlag))
			{
				mav = new ModelAndView("page.qr");
				mav.addObject("userId", userDetails.getUserId());
			}
			
			if("no".equalsIgnoreCase(loginAuthenticatorFlag))
			{
				if("2".equals(userDetails.getRole()))
				mav = new ModelAndView("page.tppHome");
				if("1".equals(userDetails.getRole()))
				mav = new ModelAndView("page.tppAdminHome");
			}
			
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return mav;
	}
	
	@RequestMapping("loginFail")
	public ModelAndView tppAuthenticate1(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("page.bankredirect");
		try {
			mav.addObject("responseFlag", "0");
			mav.addObject("url", "tppLogin");
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return mav;
	}
	
	 @RequestMapping(value = "generateQrCode", method = RequestMethod.GET)
	    @ResponseBody
	    public Map<String, String> getQRUrl(@RequestParam("userId") final String userId) throws UnsupportedEncodingException {
	      Map<String, String> result = new HashMap<String, String>();
	        try {
	        	
		        UserDetails userDetails = authenticationService.fetchUserDetailsbyUserId(userId);
		        result.put("url", authenticationService.generateQRUrl(userDetails));
		        result.put("secret", userDetails.getSecret());
		        
	        } catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
	        }
	        return result;
	    }
	 
	 @RequestMapping("verifyQrCode")
	 @ResponseBody
	 public Map<String, String> verifyQrCode(HttpServletRequest request,@RequestParam("authenticatorCode") final String authenticatorCode,@RequestParam("secret") final String secret) {
		 Map<String, String> result = new HashMap<String, String>();
			try {
				
				boolean verify = authenticationService.verifyCode(authenticatorCode, secret);
				
				if(verify)
					result.put("result", "success");
				
				if(!verify)
					result.put("result", "failed");
				
			} catch(Exception e) {
				LOGGER.error("catch block");
				LOGGER.error("Failed!", e);
			}
			return result;
		}
	 
	 @RequestMapping("tppAuthenticator")
	 public ModelAndView tppAuthenticator(HttpServletRequest request) {
			ModelAndView mav = null;
			try {
				
				HttpSession session = request.getSession();
				UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
				
				if("2".equals(userDetails.getRole()))
				mav = new ModelAndView("page.tppHome");
				if("1".equals(userDetails.getRole()))
				mav = new ModelAndView("page.tppAdminHome");
				
			} catch(Exception e) {
				LOGGER.error("catch block");
				LOGGER.error("Failed!", e);
			}
			return mav;
		}

	@RequestMapping("tppCreateAccount")
	public ModelAndView tppCreateAccount(AuthenticationBean authenticationBean, HttpServletRequest request) {
		ModelAndView mav = null;
		try {
			ResponseDetails responseDetails = authenticationService.tppCreateAccount(authenticationBean);
		    mav = new ModelAndView("page.bankredirect");
			mav.addObject("responseFlag", responseDetails.getResponseFlag());
			mav.addObject("responseMessage", responseDetails.getResponseMessage());
			mav.addObject("url", "tppSingUP");
			
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return mav;
	}
	
	  @RequestMapping("logout")
	    public ModelAndView getLogoutPage(HttpServletRequest request, HttpServletResponse response){

			LOGGER.info("logout");
			HttpSession session = request.getSession();
			UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
	        if(userDetails != null) {
	        	session.setAttribute("userDetails", null);	        	
	        } 
	        if(session != null)
	        	session.invalidate();
	       
	        return new ModelAndView(new RedirectView("/index", true));
	    }
}
