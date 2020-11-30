/**
 * 
 */
package com.fss.openbanking.dao;

import java.util.List;

import org.springframework.context.ApplicationContextAware;

import com.fss.openbanking.bean.AccountDetails;
import com.fss.openbanking.bean.AccountIdentifications;
import com.fss.openbanking.bean.AccountNames;
import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ConsentDetails;
import com.fss.openbanking.bean.ConsentResponse;
import com.fss.openbanking.bean.ConsentTokenDetails;
import com.fss.openbanking.bean.DirectDebitDetails;
import com.fss.openbanking.bean.InstitutionDetails;
import com.fss.openbanking.bean.StandingOrderDetails;
import com.fss.openbanking.bean.TppAccountsRequest;
import com.fss.openbanking.bean.TppBalanceResponse;
import com.fss.openbanking.bean.TppConsentForm;
import com.fss.openbanking.bean.TransactionData;
import com.fss.openbanking.bean.UpdateDTDetails;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.bean.ViewConsent;

public interface DaoRepository  extends ApplicationContextAware {
	
	List<UserDetails> fetchUserDetails();

	List<InstitutionDetails> fetchTppInstDetails();

	List<ConsentDetails> fetchInstFeatureDetailsByBankId(String bankId);

	InstitutionDetails fetchInstDetailsByBankId(String bankId);

	List<ConsentTokenDetails> fetchConsentDetailsByUser(String mobileNumber);

	List<ConsentDetails> fetchConsentDetailsByBank(ConsentTokenDetails consentDetail);

	String getUuIdusingMobileNumber(String mobileNumber);

	boolean saveConsentTemp(ConsentResponse consentResponse, String token);

	ConsentTokenDetails fetchConsentBankId(String bankId,String mobileNumber);

	List<TppBalanceResponse> fetchBalanceDetailsByBankId(TppAccountsRequest tppAccountsRequest, String userId);

	List<AccountIdentifications> fetchAccountIdentificationsDetails(AccountDetails accountDetail);

	List<TransactionData> fetchTransactionDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest);

	List<DirectDebitDetails> fetchDirectDebitsDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest);

	List<StandingOrderDetails> fetchStandingOrdersDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest);

	List<AccountNames> fetchAccountNamesDetails(AccountDetails accountDetail);

	List<AccountIdentifications> fetchStandingOrdersIdentificationsDetails(StandingOrderDetails standingOrderDetail);

	String fetchLastUpdateDateTime(String mobileNumber, String bankId, String bankName);

	boolean deleteConsentTemp(TppConsentForm tppConsentForm);

	List<TransactionData> fetchAllTransactionDetails(UserDetails userDetails);

	List<DirectDebitDetails> fetchAllDirectDebitsDetails(UserDetails userDetails);

	List<StandingOrderDetails> fetchAllStandingOrdersDetails(UserDetails userDetails);

	List<AccountDetails> fetchAccountDetailsByBank(String bankId, String mobileNumber);

	boolean tppCreateAccount(AuthenticationBean authenticationBean);
	
	boolean checkAleardyCreatedAccount(AuthenticationBean authenticationBean);

	List<ViewConsent> fetchAllConsentDetails();

	int fetchTransactionStatusDetails(String transactinStatus);

	int fetchTransactionStatusDetailsbyMoths(String month, int year, String transactinStatus);

	int fetchBankConsentDetailsbyUser(InstitutionDetails institutionDetails, UserDetails userDetails);

	int fetchBankConsentDetailsbyBank(InstitutionDetails institutionDetails);

	String fetchUpdateHoursfromInstitutions(String bankId, String bankName);

	List<UpdateDTDetails> fetchUpdatedDetails(String mobileNumber, String bankId);

	UserDetails fetchUserDetailsbyUserId(String userId);

	String fetchLoginAuthenticatorFlagbyUserId(String userId);

	int updateLoginAuthenticatorbyuserid(String userId, String loginAuthenticatorFlag);

}
