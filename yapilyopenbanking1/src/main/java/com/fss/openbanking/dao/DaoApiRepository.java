/**
 * 
 */
package com.fss.openbanking.dao;

import java.util.List;

import org.springframework.context.ApplicationContextAware;

import com.fss.openbanking.bean.AccountDetails;
import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ConsentTokenDetails;
import com.fss.openbanking.bean.CreateResponseData;
import com.fss.openbanking.bean.DirectDebitData;
import com.fss.openbanking.bean.InstitutionData;
import com.fss.openbanking.bean.LoggingDetails;
import com.fss.openbanking.bean.StandingOrderData;
import com.fss.openbanking.bean.TransactionData;

public interface DaoApiRepository  extends ApplicationContextAware {
	
	boolean saveAccountDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList);

	boolean saveAccountNameDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList);

	boolean saveAccountIdentificationsDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList);
	
	boolean saveAccountBalanceDetails(ConsentTokenDetails consentTokenDetails, AccountDetails accountData);

	boolean saveTransactionDetails(List<TransactionData> transactionDataList, AccountDetails accountData, ConsentTokenDetails consentTokenDetails);

	boolean saveDirectDebitDetails(List<DirectDebitData> directDebitDataList, AccountDetails accountData,ConsentTokenDetails consentTokenDetails);

	boolean saveStandingOrdersDetails(List<StandingOrderData> standingOrderDataList, AccountDetails accountData, ConsentTokenDetails consentTokenDetails);

	boolean saveStandingOrdersIdentificationDetails(List<StandingOrderData> standingOrderDataList, AccountDetails accountData,ConsentTokenDetails consentTokenDetails);

	boolean saveLastUpdatedDateTime(ConsentTokenDetails consentTokenDetails);

	boolean saveInstitutionDetails(InstitutionData institutionData);

	boolean saveCreateUserDetails(AuthenticationBean authenticationBean, CreateResponseData createResponseData);

	String fetchTransactionId();

	boolean saveFetchingDetails(LoggingDetails loggingDetails);

	boolean deleteBankAccountDetails(String bankId, String mobileNumber);

}
