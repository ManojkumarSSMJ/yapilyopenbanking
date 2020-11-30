/**
 * 
 */
package com.fss.openbanking.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.PaymentRequest;
import com.fss.openbanking.bean.TppPaymentsResponse;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.service.PaymentService;

@Controller
public class PaymentController {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping("tppPayments")
	public ModelAndView payementsPage(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("page.tpp.payments");
		try {
			HttpSession session = request.getSession();
			UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
			if(request.getParameter("responseFlag") == null)
			{
				TppPaymentsResponse tppPaymentsResponse = paymentService.fetchPaymentAccountsDetails(userDetails);
				mav.addObject("responseFlag", tppPaymentsResponse.getResponseFlag());
				if("1".equals(tppPaymentsResponse.getResponseFlag())) {
					mav.addObject("responseFlag", "3");
				}
				mav.addObject("responseMessage", tppPaymentsResponse.getResponseMessage());
				mav.addObject("tppPaymentsResponse", tppPaymentsResponse);
			}
			else
			{
				mav.addObject("responseFlag", request.getParameter("responseFlag"));
			    mav.addObject("userId", request.getParameter("userId"));
				mav.addObject("accountId", request.getParameter("accountId"));
				mav.addObject("bankName", request.getParameter("bankName"));
				mav.addObject("provider", request.getParameter("provider"));
				mav.addObject("mobileNumber", request.getParameter("mobileNumber"));
				mav.addObject("amount", request.getParameter("amount"));
				mav.addObject("transactionId", request.getParameter("transactionId"));
			}
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return mav;
	}
	
	@RequestMapping("tppRecharge")
	public ModelAndView tppRedirect(HttpServletRequest request, PaymentRequest paymentRequest) {
		ModelAndView mav = new ModelAndView("page.paymentVerification");
		try {
				HttpSession session = request.getSession();
				UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
				String[] accountAndBank = paymentRequest.getAccountId().split("\\|");
				String provider = paymentRequest.getProvider();
				String mobileNumber = paymentRequest.getMobileNumber();
				String amount = paymentRequest.getAmount();
				mav.addObject("userId", userDetails.getUserId());
				mav.addObject("bankName", accountAndBank[1]);
				mav.addObject("provider", provider);
				mav.addObject("mobileNumber", mobileNumber);
				mav.addObject("amount", amount);
				mav.addObject("accountId", accountAndBank[0]);
				mav.addObject("logoUrl", accountAndBank[2]);
				mav.addObject("iconUrl", accountAndBank[3]);
				mav.addObject("bankId", accountAndBank[4]);
				mav.addObject("moduleName", "PSP");
				mav.addObject("last4DigitMobNo", userDetails.getMobileNumber().substring(userDetails.getMobileNumber().length()-4, userDetails.getMobileNumber().length()));
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return mav;
	}
	
	@RequestMapping("paymentProcess")
	public ModelAndView paymentProcess(HttpServletRequest request, AuthenticationBean authenticationBean) {
		ModelAndView mav = null;
		HttpSession session = request.getSession();
		UserDetails userDetails = (UserDetails) session.getAttribute("userDetails");
	    TppPaymentsResponse tppPaymentsResponse = paymentService.createPaymentAuthorization(authenticationBean, userDetails);
	    
	    if("1".equals(tppPaymentsResponse.getResponseFlag()))
	    {
			userDetails.setPaymentData(tppPaymentsResponse.getPaymentData());
			userDetails.setConsentTokenFlag("2");
			request.getSession().setAttribute("userDetails", userDetails);
			
	    	return  new ModelAndView("redirect:" + tppPaymentsResponse.getAuthorizationUrl());
	    }
	    else
	    {
	    	mav = new ModelAndView("page.mpayredirect");
		    mav.addObject("responseFlag", tppPaymentsResponse.getResponseFlag());
		    mav.addObject("userId", authenticationBean.getUserId());
			mav.addObject("accountId", authenticationBean.getAccountId());
			mav.addObject("bankName", authenticationBean.getBankName());
			mav.addObject("provider", authenticationBean.getProvider());
			mav.addObject("mobileNumber", authenticationBean.getMobileNumber());
			mav.addObject("amount", authenticationBean.getAmount());
			mav.addObject("url", "tppPayments");
			
	    }
		return mav;
	}
	
}
