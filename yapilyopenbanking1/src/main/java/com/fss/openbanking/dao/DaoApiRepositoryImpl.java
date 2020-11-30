/**
 * 
 */
package com.fss.openbanking.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fss.openbanking.bean.AccountBalances;
import com.fss.openbanking.bean.AccountDetails;
import com.fss.openbanking.bean.AccountIdentifications;
import com.fss.openbanking.bean.AccountNames;
import com.fss.openbanking.bean.AuthenticationBean;
import com.fss.openbanking.bean.ConsentTokenDetails;
import com.fss.openbanking.bean.Countries;
import com.fss.openbanking.bean.CreateResponseData;
import com.fss.openbanking.bean.CreditLines;
import com.fss.openbanking.bean.DirectDebitData;
import com.fss.openbanking.bean.InstitutionData;
import com.fss.openbanking.bean.LoggingDetails;
import com.fss.openbanking.bean.StandingOrderData;
import com.fss.openbanking.bean.TransactionData;

@Repository("DaoApiRepository")
public class DaoApiRepositoryImpl implements DaoApiRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoApiRepositoryImpl.class);
	
	private ApplicationContext applicationContext;
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public boolean deleteBankAccountDetails(String bankId, String mobileNumber) {
		boolean insertRows = false;
		LOGGER.info("Delete Bank Account Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql1 = messageSource.getMessage("delete.account.details.by.bank", null, null);
			  String sql2 = messageSource.getMessage("delete.account.name.details.by.bank", null, null);
			  String sql3 = messageSource.getMessage("delete.account.identification.details.by.bank", null, null);
			  String sql4 = messageSource.getMessage("delete.account.balances.details.by.bank", null, null);
			  String sql5 = messageSource.getMessage("delete.account.balances.credit.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql1); 
			  LOGGER.info("Query {}"+ sql2);
			  LOGGER.info("Query {}"+ sql3);
			  LOGGER.info("Query {}"+ sql4);
			  LOGGER.info("Query {}"+ sql5);
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", mobileNumber);
			  namedParameters.put("bankId", bankId);
			  int count = namedParameterJdbcTemplate.update(sql1, namedParameters);
			  LOGGER.info("Count :::: "+ count);
			  count = namedParameterJdbcTemplate.update(sql2, namedParameters);
			  LOGGER.info("Count :::: "+ count);
			  count = namedParameterJdbcTemplate.update(sql3, namedParameters);
			  LOGGER.info("Count :::: "+ count);
			  count = namedParameterJdbcTemplate.update(sql4, namedParameters);
			  LOGGER.info("Count :::: "+ count);
			  count = namedParameterJdbcTemplate.update(sql5, namedParameters);
			  LOGGER.info("Count :::: "+ count);
			  insertRows=true; 
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	@Override
	public boolean saveAccountDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList) {
		boolean insertRows = false;
		LOGGER.info("Save Account Details from DB");
		try {
			  LOGGER.info("try block");
			  int count = 0;
			  String sql = messageSource.getMessage("insert.account.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  for(AccountDetails accountData : accountsDataList)
			  {
				  Map<String, Object> namedParameters = new HashMap<String, Object>();
				  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
				  namedParameters.put("bankId", consentTokenDetails.getBankId());
				  namedParameters.put("accId", accountData.getId());
				  namedParameters.put("accType", accountData.getType());
				  namedParameters.put("accCrncy", accountData.getCurrency());
				  namedParameters.put("accUsgType", accountData.getUsageType());
				  namedParameters.put("accSType", accountData.getAccountType());
				  namedParameters.put("accNckName",accountData.getNickname());
				  LOGGER.info("Values :::: "+ namedParameters);
				  count += namedParameterJdbcTemplate.update(sql, namedParameters);
			  }
			  LOGGER.info("Count :::: "+ count); 
			  if(count > 0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	

	@Override
	public boolean saveAccountNameDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList) {
		boolean insertRows = false;
		LOGGER.info("Save Account Name Details from DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  String sql = messageSource.getMessage("insert.account.name.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  for(AccountDetails accountData : accountsDataList)
			  {
				  for(AccountNames accountNames : accountData.getAccountNames())
					{
					  Map<String, Object> namedParameters = new HashMap<String, Object>();
					  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
					  namedParameters.put("bankId", consentTokenDetails.getBankId());
					  namedParameters.put("accId", accountData.getId());
					  namedParameters.put("accName", accountNames.getName());
					  LOGGER.info("Values :::: "+ namedParameters);
					  count += namedParameterJdbcTemplate.update(sql, namedParameters);
					 }
					  LOGGER.info("Count :::: "+ count); 
					  if(count > 0)
					  { 
						  insertRows=true; 
					  }
			 } 
		    }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}
	
	@Override
	public boolean saveAccountIdentificationsDetails(ConsentTokenDetails consentTokenDetails, List<AccountDetails> accountsDataList) {
		boolean insertRows = false;
		LOGGER.info("Save Account Identification Details from DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  String sql = messageSource.getMessage("insert.account.identification.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  for(AccountDetails accountData : accountsDataList)
			  {
				  for(AccountIdentifications accountIdentifications : accountData.getAccountIdentifications())
					{
					  Map<String, Object> namedParameters = new HashMap<String, Object>();
					  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
					  namedParameters.put("bankId", consentTokenDetails.getBankId());
					  namedParameters.put("accId", accountData.getId());
					  namedParameters.put("accIdfnType", accountIdentifications.getType());
					  namedParameters.put("accIdfnValue", accountIdentifications.getIdentification());
					  LOGGER.info("Values :::: "+ namedParameters);
					  count += namedParameterJdbcTemplate.update(sql, namedParameters);
					 }
					  LOGGER.info("Count :::: "+ count); 
					  if(count > 0)
					  { 
						  insertRows=true; 
					  }
			  }

			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	@Override
	public boolean saveAccountBalanceDetails(ConsentTokenDetails consentTokenDetails,AccountDetails accountData) {
		boolean insertRows = false;
		LOGGER.info("Save Balance Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("insert.account.balances.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  for(AccountBalances accountBalances : accountData.getAccountBalances())
				{
				  Map<String, Object> namedParameters = new HashMap<String, Object>();
				  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
				  namedParameters.put("bankId", consentTokenDetails.getBankId());
				  namedParameters.put("accId", accountData.getId());
				  namedParameters.put("blceType", accountBalances.getType());
				  namedParameters.put("blceDt", accountBalances.getDateTime());
				  namedParameters.put("balance", accountBalances.getBalanceAmount().getAmount());
				  namedParameters.put("currency", accountBalances.getBalanceAmount().getCurrency());
				  namedParameters.put("crdtIncl", accountBalances.getCreditLineIncluded());
				  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
				  LOGGER.info("Count :::: "+ count); 
				  if(count>0)
					  insertRows=true; 
				  count = 0;
				  if("true".equals(accountBalances.getCreditLineIncluded()))
				  {
					  sql = messageSource.getMessage("insert.account.balances.credit.details.by.bank", null, null);
					  LOGGER.info("Query {}"+ sql);
					  for(CreditLines creditLines : accountBalances.getCreditLines())
					  {
						  namedParameters = new HashMap<String, Object>();
						  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
						  namedParameters.put("bankId", consentTokenDetails.getBankId());
						  namedParameters.put("accId", accountData.getId());
						  namedParameters.put("blceType", accountBalances.getType());
						  namedParameters.put("creditType", creditLines.getType());
						  namedParameters.put("creditAmount", creditLines.getCreditLineAmount().getAmount());
						  namedParameters.put("creditCurrency", creditLines.getCreditLineAmount().getCurrency());
						  LOGGER.info("Values :::: "+ namedParameters);
						  count += namedParameterJdbcTemplate.update(sql, namedParameters);
						 }
						  LOGGER.info("Count :::: "+ count); 
						  if(count > 0)
						  { 
							  insertRows=true; 
						  }
				  }
				}
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}

	@Override
	public boolean saveTransactionDetails(List<TransactionData> transactionDataList, AccountDetails accountData, ConsentTokenDetails consentTokenDetails) {
		boolean insertRows = false;
		LOGGER.info("Save Transaction Details from DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  Map<String, Object> namedParameters = null;
			  String sql = messageSource.getMessage("delete.account.transaction.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> paramMap = new HashMap<String, Object>();
			  paramMap.put("mobileNumber", consentTokenDetails.getMobileNumber());
			  paramMap.put("bankId", consentTokenDetails.getBankId());
			  paramMap.put("accId", accountData.getId());
			  count = namedParameterJdbcTemplate.update(sql, paramMap);
			  LOGGER.info("Count :::: "+ count); 
			  sql = messageSource.getMessage("insert.account.transaction.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  count = 0;
			  for(TransactionData transactionData : transactionDataList)
			  {
				  
				  namedParameters = new HashMap<String, Object>();
				  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
				  namedParameters.put("bankId", consentTokenDetails.getBankId());
				  namedParameters.put("accId", accountData.getId());
				  namedParameters.put("transactionId", transactionData.getId() !=null ? transactionData.getId() : transactionData.getEnrichment().getTransactionHash().getHash());
				  namedParameters.put("reference", transactionData.getReference());
				  namedParameters.put("txnDate", transactionData.getDate());
				  namedParameters.put("bokkingDt", transactionData.getBookingDateTime());
				  namedParameters.put("txnSts", transactionData.getStatus());
				  namedParameters.put("txnAmt", transactionData.getTransactionAmount().getAmount());
				  namedParameters.put("txnCrncy", transactionData.getTransactionAmount().getCurrency());
				  namedParameters.put("txnDesc", transactionData.getDescription());
				  namedParameters.put("txnCode", transactionData.getProprietaryBankTransactionCode().getCode());
				  namedParameters.put("txnIssuer", transactionData.getProprietaryBankTransactionCode().getIssuer());
				  namedParameters.put("blceType", transactionData.getBalance() !=null ? transactionData.getBalance().getType() : null);
				  namedParameters.put("blceAmnt", transactionData.getBalance() !=null ? transactionData.getBalance().getBalanceAmount().getAmount() : null);
				  namedParameters.put("blceCurcy", transactionData.getBalance() !=null ? transactionData.getBalance().getBalanceAmount().getCurrency() : null);
				  LOGGER.info("Values :::: "+ namedParameters);
				  count += namedParameterJdbcTemplate.update(sql, namedParameters);
				 }
				  LOGGER.info("Count :::: "+ count); 
				  if(count > 0)
				  { 
					  insertRows=true; 
				  }
			  
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}


	@Override
	public boolean saveDirectDebitDetails(List<DirectDebitData> directDebitDataList, AccountDetails accountData,ConsentTokenDetails consentTokenDetails) {
		boolean insertRows = false;
		LOGGER.info("Save DirectDebits Details from DB");
		try {
			  LOGGER.info("try block");
			  int count = 0;
			  Map<String, Object> namedParameters = null;
			  String sql = messageSource.getMessage("delete.account.directdebit.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> paramMap = new HashMap<String, Object>();
			  paramMap.put("mobileNumber", consentTokenDetails.getMobileNumber());
			  paramMap.put("bankId", consentTokenDetails.getBankId());
			  paramMap.put("accId", accountData.getId());
			  count = namedParameterJdbcTemplate.update(sql, paramMap);
			  LOGGER.info("Count :::: "+ count); 
			  sql = messageSource.getMessage("insert.account.directdebit.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  count = 0;
			  for(DirectDebitData directDebitData : directDebitDataList)
			  {
				  namedParameters = new HashMap<String, Object>();
				  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
				  namedParameters.put("bankId", consentTokenDetails.getBankId());
				  namedParameters.put("accId", accountData.getId());
				  namedParameters.put("directDebitId", directDebitData.getId());
				  namedParameters.put("directDebitStatus", directDebitData.getStatusDetails().getStatus());
				  namedParameters.put("payeeName", directDebitData.getPayeeDetails().getName());
				  namedParameters.put("reference", directDebitData.getReference());
				  namedParameters.put("prevPayAmount", directDebitData.getPreviousPaymentAmount().getAmount());
				  namedParameters.put("prevPayCurrency", directDebitData.getPreviousPaymentAmount().getCurrency());
				  namedParameters.put("prevPayDT", directDebitData.getPreviousPaymentDateTime());
				  count += namedParameterJdbcTemplate.update(sql, namedParameters);
				 }
				  LOGGER.info("Count :::: "+ count); 
				  if(count > 0)
				  { 
					  insertRows=true; 
				  }
			  
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}


	@Override
	public boolean saveStandingOrdersDetails(List<StandingOrderData> standingOrderDataList, AccountDetails accountData, ConsentTokenDetails consentTokenDetails) {
		boolean insertRows = false;
		LOGGER.info("Save StandingOrders Details from DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  Map<String, Object> namedParameters = null;
			  String sql = messageSource.getMessage("delete.account.standingorder.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> paramMap = new HashMap<String, Object>();
			  paramMap.put("mobileNumber", consentTokenDetails.getMobileNumber());
			  paramMap.put("bankId", consentTokenDetails.getBankId());
			  paramMap.put("accId", accountData.getId());
			  count = namedParameterJdbcTemplate.update(sql, paramMap);
			  LOGGER.info("Count :::: "+ count); 
			  sql = messageSource.getMessage("insert.account.standingorder.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  count = 0;
			  for(StandingOrderData standingOrderData : standingOrderDataList)
			  {
				  namedParameters = new HashMap<String, Object>();
				  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
				  namedParameters.put("bankId", consentTokenDetails.getBankId());
				  namedParameters.put("accId", accountData.getId());
				  namedParameters.put("standingOrderId", standingOrderData.getId());
				  namedParameters.put("standingOrderStatus", standingOrderData.getStatusDetails().getStatus());
				  namedParameters.put("payeeName", standingOrderData.getPayeeDetails().getName());
				  namedParameters.put("reference", standingOrderData.getReference());
				  namedParameters.put("firstPayAmount", standingOrderData.getFirstPaymentAmount().getAmount());
				  namedParameters.put("firstPayCurrency", standingOrderData.getFirstPaymentAmount().getCurrency());
				  namedParameters.put("firstPayDT", standingOrderData.getFirstPaymentDateTime());
				  namedParameters.put("nextPayAmount", standingOrderData.getNextPaymentAmount() != null ? standingOrderData.getNextPaymentAmount().getAmount() : null);
				  namedParameters.put("nextPayCurrency", standingOrderData.getNextPaymentAmount() != null ? standingOrderData.getNextPaymentAmount().getCurrency() : null);
				  namedParameters.put("nextPayDT", standingOrderData.getNextPaymentDateTime());
				  namedParameters.put("finalPayAmount", standingOrderData.getFinalPaymentAmount() != null ? standingOrderData.getFinalPaymentAmount().getAmount() : null);
				  namedParameters.put("finalPayCurrency", standingOrderData.getFinalPaymentAmount() != null ? standingOrderData.getFinalPaymentAmount().getCurrency() : null);
				  namedParameters.put("finalPayDT", standingOrderData.getNextPaymentDateTime());
				  namedParameters.put("freqType", standingOrderData.getFrequency().getFrequencyType());
				  namedParameters.put("freqIntrnWeek", standingOrderData.getFrequency().getIntervalWeek());
				  namedParameters.put("freqExectDay", standingOrderData.getFrequency().getExecutionDay());
				  LOGGER.info("Values :::: "+ namedParameters);
				  count += namedParameterJdbcTemplate.update(sql, namedParameters);
				 }
				  LOGGER.info("Count :::: "+ count); 
				  if(count > 0)
				  { 
					  insertRows=true; 
				  }
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}


	@Override
	public boolean saveStandingOrdersIdentificationDetails(List<StandingOrderData> standingOrderDataList, AccountDetails accountData,ConsentTokenDetails consentTokenDetails) {
		boolean insertRows = false;
		LOGGER.info("Select StandingOrders Identification Details from DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  Map<String, Object> namedParameters = null;
			  String sql = messageSource.getMessage("delete.account.standingorder.identification.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> paramMap = new HashMap<String, Object>();
			  paramMap.put("mobileNumber", consentTokenDetails.getMobileNumber());
			  paramMap.put("bankId", consentTokenDetails.getBankId());
			  paramMap.put("accId", accountData.getId());
			  count = namedParameterJdbcTemplate.update(sql, paramMap);
			  LOGGER.info("Count :::: "+ count);
			  sql = messageSource.getMessage("insert.standingorder.identification.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql);
			  count = 0;
			  for(StandingOrderData standingOrderData : standingOrderDataList)
			  {
				  for(AccountIdentifications accountIdentifications : standingOrderData.getPayeeDetails().getAccountIdentifications())
					{
					  namedParameters = new HashMap<String, Object>();
					  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
					  namedParameters.put("bankId", consentTokenDetails.getBankId());
					  namedParameters.put("accId", accountData.getId());
					  namedParameters.put("soId", standingOrderData.getId());
					  namedParameters.put("soIdfnType", accountIdentifications.getType());
					  namedParameters.put("soIdfnValue", accountIdentifications.getIdentification());
					  LOGGER.info("Values :::: "+ namedParameters);
					  count += namedParameterJdbcTemplate.update(sql, namedParameters);
					 }
					  LOGGER.info("Count :::: "+ count); 
					  if(count > 0)
					  { 
						  insertRows=true; 
					  }
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}

	@Override
	public boolean saveLastUpdatedDateTime(ConsentTokenDetails consentTokenDetails) {
		boolean insertRows = false;
		LOGGER.info("Update LastUpdate Details into DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("insert.last.update.details", null, null);
			  
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", consentTokenDetails.getMobileNumber());
			  namedParameters.put("bankId", consentTokenDetails.getBankId());
			  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}

	@Override
	public boolean saveInstitutionDetails(InstitutionData instData) {
		boolean insertRows = false;
		LOGGER.info("Insert Institution Details into DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.institution.details.as.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", instData.getId());
			  namedParameters.put("bankName", instData.getName());
			  int count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  if(count == 0)
			  { 
				  insertRows = updateInstitutionDetails(instData, "1");
			  }
			  else
			  {
				  insertRows = updateInstitutionDetails(instData, "2");
			  }
			  
			  if(insertRows)
			  {
			  	  deleteInstitutionFtrDetails(instData);
				  insertRows = saveInstitutionFtrDetails(instData);
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	public boolean updateInstitutionDetails(InstitutionData instData, String flag) {
		boolean insertRows = false;
		LOGGER.info("Update Institution Details into DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = null;
			  String country = "";
			  
			  if("1".equals(flag))
				  sql = messageSource.getMessage("insert.institution.details.as.bank", null, null);
			  else
				  sql = messageSource.getMessage("update.institution.details.as.bank", null, null);
			  
			  for(Countries countries : instData.getCountries())
				  country += countries.getDisplayName() + ",";
			  
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", instData.getId());
			  namedParameters.put("bankName", instData.getName());
			  namedParameters.put("bankFName", instData.getFullName());
			  namedParameters.put("bankCnry", country.substring(0,country.length()-1));
			  namedParameters.put("bankLogo", instData.getLogo());
			  namedParameters.put("bankIcon", instData.getIcon());
			  namedParameters.put("updateHours", "4");
			  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	public boolean saveInstitutionFtrDetails(InstitutionData instData) {
		boolean insertRows = false;
		LOGGER.info("Insert Institution feature Details into DB");
		try {
			  LOGGER.info("try block"); 
			  int count = 0;
			  String sql = messageSource.getMessage("insert.institution.feature.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  for(String features : instData.getFeatures())
			  {
				  Map<String, Object> namedParameters = new HashMap<String, Object>();
				  namedParameters.put("bankId", instData.getId());
				  namedParameters.put("ftrId", features);
				  namedParameters.put("ftrName", features.replaceAll("_", " "));
				  count += namedParameterJdbcTemplate.update(sql, namedParameters);
			  }
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	private boolean deleteInstitutionFtrDetails(InstitutionData instData) {
		boolean insertRows = false;
		LOGGER.info("delete Institution feature Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("delete.institution.feature.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", instData.getId());
			  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}

	@Override
	public boolean saveCreateUserDetails(AuthenticationBean authenticationBean, CreateResponseData createResponseData) {
		boolean insertRows = false;
		LOGGER.info("Select User Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.user.yapily.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", createResponseData.getApplicationUserId());
			  namedParameters.put("uuid", createResponseData.getUuid());
			  int count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  if(count == 0)
			  { 
				  insertRows = updateCreateUserDetails(authenticationBean, createResponseData, "1");
			  }
			  else
			  {
				  insertRows = updateCreateUserDetails(authenticationBean, createResponseData, "2");
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}
	
	public boolean updateCreateUserDetails(AuthenticationBean authenticationBean, CreateResponseData createResponseData, String flag) {
		boolean insertRows = false;
		LOGGER.info("Update User Details into DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = null;
			  
			  if("1".equals(flag))
				  sql = messageSource.getMessage("insert.user.yapily.details", null, null);
			  else
				  sql = messageSource.getMessage("update.user.yapily.details", null, null);
			  
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", createResponseData.getApplicationUserId());
			  namedParameters.put("uuid", createResponseData.getUuid());
			  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;

	}

	@Override
	public String fetchTransactionId() {
		String transactionId = null;
		LOGGER.info("Select User Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.transaction.id.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  int count = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  if(count > 0)
			  { 
				  sql = messageSource.getMessage("fetch.transaction.id.details", null, null);
				  
				  transactionId = namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
			transactionId = null;
		  }
		return transactionId;

	}

	@Override
	public boolean saveFetchingDetails(LoggingDetails loggingDetails) {
		boolean insertRows = false;
		LOGGER.info("insert Txn Details");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("insert.transaction.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("transactionId", loggingDetails.getTransactionId());
			  namedParameters.put("transactionDesc", loggingDetails.getTransactionDesc());
			  namedParameters.put("transactionRrn", loggingDetails.getTransactionRrn());
			  namedParameters.put("reqInitiatedDt", loggingDetails.getReqInitiatedDt());
			  namedParameters.put("respArrivalDt", loggingDetails.getRespArrivalDt());
			  namedParameters.put("reqProcessingDt", loggingDetails.getReqProcessingDt());
			  namedParameters.put("respDataSize", loggingDetails.getRespDataSize());
			  namedParameters.put("bankId", loggingDetails.getBankId());
			  namedParameters.put("respCode", loggingDetails.getRespCode());
			  namedParameters.put("respDesc", loggingDetails.getRespDesc());
			  namedParameters.put("transactionStatus", loggingDetails.getTransactionStatus());
			  int count = namedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  sql = messageSource.getMessage("insert.transaction.api.metrics.details", null, null);
				  LOGGER.info("Query {}"+ sql);
				  count = namedParameterJdbcTemplate.update(sql, namedParameters);
				  LOGGER.info("Count :::: "+ count); 
				  if(count>0)
					  insertRows=true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		  }
		return insertRows;

	}

}
