/**
 * 
 */
package com.fss.openbanking.restcontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fss.openbanking.bean.InstitutionDetails;
import com.fss.openbanking.bean.TppAccountsResponse;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.bean.UserResponse;
import com.fss.openbanking.service.ApiService;

@RestController
public class ApiController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	private ApiService apiService;

	@RequestMapping("updateBankDetailsByBank")
	public @ResponseBody String updateBankDetailsByBank(HttpServletRequest request,InstitutionDetails institutionDetails) {
		String response = "failed";
		try {
			LOGGER.info("Tpp Accounts");
			HttpSession session = request.getSession();
			UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
			TppAccountsResponse tppAccountsResponse = apiService.updateBankDetailsByBank(userDetails, institutionDetails);
			
			if("1".equals(tppAccountsResponse.getResponseFlag()))
				response = "uptodate";
			else if("2".equals(tppAccountsResponse.getResponseFlag()))
				response = "tknNotFound";
			else if(!"0".equals(tppAccountsResponse.getResponseFlag()))
				response = "success";
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return response;
	}
	
	@RequestMapping("updateBankDetails")
	public @ResponseBody String updateBankDetails(HttpServletRequest request) {
		String response = "failed";
		try {
			LOGGER.info("Tpp Accounts");
			HttpSession session = request.getSession();
			UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
			TppAccountsResponse tppAccountsResponse = apiService.tppUpdateBankDetails(userDetails);
			
			if(!"0".equals(tppAccountsResponse.getResponseFlag()))
				response = "success";
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return response;
	}
	
	@RequestMapping(value = "updateInstitutions", method = RequestMethod.GET)
	public UserResponse getInstitutions() {

		return apiService.getInstitutions();

	}
}
