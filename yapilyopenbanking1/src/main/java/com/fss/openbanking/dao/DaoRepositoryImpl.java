/**
 * 
 */
package com.fss.openbanking.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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

@Repository("daoRepository")
public class DaoRepositoryImpl implements DaoRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoRepositoryImpl.class);
	
	private ApplicationContext applicationContext;
	
	private NamedParameterJdbcTemplate bankNamedParameterJdbcTemplate;
	
	@Autowired
	private MessageSource messageSource;	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public UserDetails fetchUserDetailsbyUserId(String userId) {
		UserDetails userDetails = new UserDetails();
		try {
			final String sql = messageSource.getMessage("fetch.openbanking.user.details.by.userid", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("userId", userId);
			userDetails = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(UserDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
		}
		return userDetails;
	}
	
	@Override
	public List<UserDetails> fetchUserDetails() {
		List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.openbanking.user.details", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			userDetailsList = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(UserDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return userDetailsList;
	}

	@Override
	public List<InstitutionDetails> fetchTppInstDetails() {
		List<InstitutionDetails> instDetails = new ArrayList<InstitutionDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.inst.details", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			instDetails = bankNamedParameterJdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(InstitutionDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return instDetails;
	}


	@Override
	public List<ConsentDetails> fetchInstFeatureDetailsByBankId(String bankId) {
		List<ConsentDetails> consentDetails = new ArrayList<ConsentDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.inst.feature.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", bankId);
			consentDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(ConsentDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return consentDetails;
	}


	@Override
	public boolean deleteConsentTemp(TppConsentForm tppConsentForm) {
		boolean result = false;
		try {
			final String sql = messageSource.getMessage("delete.consent.details.by.bank", null, null);
			final String sql1 = messageSource.getMessage("delete.consent.feature.details.by.bank", null, null);
			LOGGER.info("Query 1 ::{}", sql);
			LOGGER.info("Query 2 ::{}", sql1);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", tppConsentForm.getMobileNumber());
			paramMap.put("institutionId", tppConsentForm.getBankId());
			paramMap.put("consentId", tppConsentForm.getConsentId());
			paramMap.put("uuid", tppConsentForm.getUuId());

			int count = bankNamedParameterJdbcTemplate.update(sql, paramMap);
			LOGGER.info("Count 1 ::{}", count);
			if(count > 0) {
				count = bankNamedParameterJdbcTemplate.update(sql1, paramMap);
				LOGGER.info("Count 2 ::{}", count);
				if(count > 0)
					result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	@Override
	public InstitutionDetails fetchInstDetailsByBankId(String bankId) {
		InstitutionDetails instDetails = null;
		try {
			final String sql = messageSource.getMessage("fetch.bank.details.by.bank.id", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", bankId);
			instDetails = bankNamedParameterJdbcTemplate.queryForObject(sql,paramMap,BeanPropertyRowMapper.newInstance(InstitutionDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return instDetails;
	}


	@Override
	public List<ConsentTokenDetails> fetchConsentDetailsByUser(String mobileNumber) {
		List<ConsentTokenDetails> consentTokenDetails = new ArrayList<>();
		try {
			final String sql = messageSource.getMessage("consent.fetch.details.by.user.id", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", mobileNumber);
			consentTokenDetails = bankNamedParameterJdbcTemplate.query(sql,paramMap,BeanPropertyRowMapper.newInstance(ConsentTokenDetails.class));
		
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return consentTokenDetails;
	}


	@Override
	public List<ConsentDetails> fetchConsentDetailsByBank(ConsentTokenDetails consentDetail) {
		List<ConsentDetails> consentDetails = new ArrayList<ConsentDetails>();
		try {
			final String sql = messageSource.getMessage("consent.fetch.consent.by.uuid", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("uuId", consentDetail.getUuId());
			paramMap.put("consentId", consentDetail.getConsentId());
			consentDetails = bankNamedParameterJdbcTemplate.query(sql,paramMap,BeanPropertyRowMapper.newInstance(ConsentDetails.class));
		    
		} catch(Exception e) {
			LOGGER.error("catch block");
			LOGGER.error("Failed!", e);
		}
		return consentDetails;
	}


	@Override
	public String getUuIdusingMobileNumber(String mobileNumber){
		String uuid = null;
		LOGGER.info("Get User Details from DB");
		try {
			
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("fetch.user.details.by.user.id", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", mobileNumber);
			  uuid = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
			  
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
			uuid = null;
		}
		return uuid;

	}


	@Override
	public boolean saveConsentTemp(ConsentResponse consentResponse, String token) {
		boolean insertRows = false;
		LOGGER.info("Select Consent Details into DB");
		try {
			  LOGGER.info("try block"); 
			  String qry = messageSource.getMessage("insert.consent.details.by.bank", null, null);	
			  LOGGER.info("Query {}"+ qry); 
			  LOGGER.info("named Parameter Jdbc Template");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", consentResponse.getApplicationUserId());
			  namedParameters.put("institutionId", consentResponse.getInstitutionId());
			  namedParameters.put("uuid",consentResponse.getUserUuid());
			  namedParameters.put("consentId",consentResponse.getId());
			  namedParameters.put("token",token);
			  namedParameters.put("status","Y");
			  namedParameters.put("expiryAt",consentResponse.getExpiresAt().split("T")[0] + " " +consentResponse.getExpiresAt().split("T")[1].substring(0,8));
			  int count = bankNamedParameterJdbcTemplate.update(qry, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				 insertRows=true; 
			  }
			  
			  if(insertRows)
			  {
				  deleteConsentFeatureDetails(consentResponse);
				  insertRows = saveConsentFeatureDetails(consentResponse, consentResponse.getFeatureScope(), token);
			  }
				  
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	
	private boolean saveConsentFeatureDetails(ConsentResponse consentResponse, String[] featureScope, String token) {
		boolean insertRows = false;
		LOGGER.info("Select Consent Feature Details into DB");
		try {
			  LOGGER.info("try block");
			  int count = 0;
			  String qry = messageSource.getMessage("insert.consent.feature.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ qry); 
			  LOGGER.info("named Parameter Jdbc Template");
			  for(String feautureId : featureScope)
			  {
				  Map<String, Object> namedParameters = new HashMap<String, Object>();
				  namedParameters.put("featureId", feautureId);
				  namedParameters.put("consentId", consentResponse.getId());
				  namedParameters.put("uuid",consentResponse.getUserUuid());
				  namedParameters.put("token",token);
				  namedParameters.put("status","Y");
				  count += bankNamedParameterJdbcTemplate.update(qry, namedParameters);
			  }
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
				 insertRows=true;
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}
	
	private boolean deleteConsentFeatureDetails(ConsentResponse consentResponse) {
		boolean insertRows = false;
		LOGGER.info("Delete Consent Feature Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String qry = messageSource.getMessage("delete.consent.feature.details.by.bank", null, null);			  
			  
			  LOGGER.info("Query {}"+ qry); 
			  LOGGER.info("named Parameter Jdbc Template");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("consentId", consentResponse.getId());
			  namedParameters.put("uuid",consentResponse.getUserUuid());
			  int count = bankNamedParameterJdbcTemplate.update(qry, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
				 insertRows=true;
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}


	@Override
	public ConsentTokenDetails fetchConsentBankId(String bankId, String mobileNumber) {
		ConsentTokenDetails consentTokenDetails = null;
		try {
			String sql = messageSource.getMessage("select.tpp.user.consent.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", bankId);
			paramMap.put("mobileNumber", mobileNumber);
			int count = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, Integer.class);
			if(count > 0)
			{
				sql = messageSource.getMessage("fetch.tpp.user.consent.details.by.bank", null, null);
				consentTokenDetails = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, BeanPropertyRowMapper.newInstance(ConsentTokenDetails.class));
			}
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return consentTokenDetails;
	}

	@Override
	public List<TppBalanceResponse> fetchBalanceDetailsByBankId(TppAccountsRequest tppAccountsRequest, String userId) {
		List<TppBalanceResponse> tppBalanceResponse = null;
		try {
			final String sql = messageSource.getMessage("fetch.tpp.balance.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", tppAccountsRequest.getBankId());
			paramMap.put("accountId", tppAccountsRequest.getAccountId());
			paramMap.put("mobileNumber", tppAccountsRequest.getMobileNumber());
			tppBalanceResponse = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(TppBalanceResponse.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return tppBalanceResponse;
	}

	@Override
	public List<AccountIdentifications> fetchAccountIdentificationsDetails(AccountDetails accountDetail) {
		 List<AccountIdentifications> accountIdentifications = new ArrayList<AccountIdentifications>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.account.identification.details", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", accountDetail.getBankId());
			paramMap.put("mobileNumber", accountDetail.getMobileNumber());
			paramMap.put("accountId", accountDetail.getId());
			accountIdentifications = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(AccountIdentifications.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return accountIdentifications;
	}

	@Override
	public List<AccountNames> fetchAccountNamesDetails(AccountDetails accountDetail) {
		 List<AccountNames> accountNames = new ArrayList<AccountNames>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.account.names.details", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", accountDetail.getBankId());
			paramMap.put("mobileNumber", accountDetail.getMobileNumber());
			paramMap.put("accountId", accountDetail.getId());
			accountNames = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(AccountNames.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return accountNames;
	}
	

	@Override
	public List<TransactionData> fetchTransactionDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest) {
		 List<TransactionData> transactionData = new ArrayList<TransactionData>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.transaction.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", tppAccountsRequest.getBankId());
			paramMap.put("accountId", tppAccountsRequest.getAccountId());
			paramMap.put("mobileNumber", tppAccountsRequest.getMobileNumber());
			transactionData = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(TransactionData.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return transactionData;
	}
	
	@Override
	public List<TransactionData> fetchAllTransactionDetails(UserDetails userDetails) {
		 List<TransactionData> transactionData = new ArrayList<TransactionData>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.transaction.details.all", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", userDetails.getMobileNumber());
			transactionData = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(TransactionData.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return transactionData;
	}

	@Override
	public List<DirectDebitDetails> fetchDirectDebitsDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest) {
		 List<DirectDebitDetails> directDebitDetails = new ArrayList<>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.directdebits.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", tppAccountsRequest.getBankId());
			paramMap.put("accountId", tppAccountsRequest.getAccountId());
			paramMap.put("mobileNumber", tppAccountsRequest.getMobileNumber());
			directDebitDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DirectDebitDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return directDebitDetails;
	}

	@Override
	public List<DirectDebitDetails> fetchAllDirectDebitsDetails(UserDetails userDetails) {
		 List<DirectDebitDetails> directDebitDetails = new ArrayList<DirectDebitDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.directdebits.details.all", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", userDetails.getMobileNumber());
			directDebitDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(DirectDebitDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return directDebitDetails;
	}
	
	@Override
	public List<StandingOrderDetails> fetchStandingOrdersDetails(UserDetails userDetails, TppAccountsRequest tppAccountsRequest) {
		 List<StandingOrderDetails> standingOrderDetails = new ArrayList<StandingOrderDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.standingorders.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", tppAccountsRequest.getBankId());
			paramMap.put("accountId", tppAccountsRequest.getAccountId());
			paramMap.put("mobileNumber", tppAccountsRequest.getMobileNumber());
			standingOrderDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(StandingOrderDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return standingOrderDetails;
	}

	@Override
	public List<StandingOrderDetails> fetchAllStandingOrdersDetails(UserDetails userDetails) {
		 List<StandingOrderDetails> standingOrderDetails = new ArrayList<>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.standingorders.details.all", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", userDetails.getMobileNumber());
			standingOrderDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(StandingOrderDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return standingOrderDetails;
	}
	
	@Override
	public List<AccountIdentifications> fetchStandingOrdersIdentificationsDetails(StandingOrderDetails standingOrderDetail) {
		 List<AccountIdentifications> accountIdentifications = new ArrayList<>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.standingorders.identification.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", standingOrderDetail.getBankId());
			paramMap.put("mobileNumber", standingOrderDetail.getMobileNumber());
			paramMap.put("accountId", standingOrderDetail.getAccountId());
			paramMap.put("soId", standingOrderDetail.getStandingOrderId());
			accountIdentifications = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(AccountIdentifications.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return accountIdentifications;
	}


	@Override
	public String fetchLastUpdateDateTime(String mobileNumber, String bankId, String bankName) {
		 String LastDt = null;
		try {
		    String sql = messageSource.getMessage("select.last.update.datetime", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", mobileNumber);
			paramMap.put("bankId", bankId);
			int count = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, Integer.class);		  
			LOGGER.info("Count :::: "+ count); 
		    if(count>0)
		    { 
		    	sql = messageSource.getMessage("fetch.last.update.datetime", null, null);
		    	LastDt = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, String.class);
			}
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
			LastDt = null;
		}
		return LastDt;
	}


	@Override
	public List<AccountDetails> fetchAccountDetailsByBank(String bankId, String mobileNumber) {
		 List<AccountDetails> AccountDetails = new ArrayList<AccountDetails>();
		try {
			final String sql = messageSource.getMessage("fetch.tpp.account.details.by.bank", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("bankId", bankId);
			paramMap.put("mobileNumber", mobileNumber);
			AccountDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(AccountDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return AccountDetails;
	}


	@Override
	public String fetchUpdateHoursfromInstitutions(String bankId, String bankName) {
		String updHrs = null;
		LOGGER.info("Get Institution Update Hours Details from DB");
		try {
			
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("fetch.tpp.inst.update.hours.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  LOGGER.info("named Parameter Jdbc Template");
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", bankId);
			  namedParameters.put("bankName", bankName);
			  updHrs = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
			  
			 }
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
			updHrs = null;
		}
		return updHrs;

	}


	@Override
	public boolean tppCreateAccount(AuthenticationBean authenticationBean) {
		boolean insertRows = false;
		LOGGER.info("Select Account Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("insert.user.account.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("role", "2");
			  namedParameters.put("customerName", authenticationBean.getCustomerName());
			  namedParameters.put("mobileNumber", authenticationBean.getMobileNumber());
			  namedParameters.put("userId", authenticationBean.getUserId());
			  namedParameters.put("password", authenticationBean.getEncodedPassword());
			  namedParameters.put("activeStatus", "Y");
			  namedParameters.put("passwordStatus", "Y");
			  namedParameters.put("loginAuthFlag", "yes");
			  int count = bankNamedParameterJdbcTemplate.update(sql, namedParameters);
			  LOGGER.info("Count :::: "+ count); 
			  if(count>0)
			  { 
				  insertRows = true; 
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return insertRows;
	}


	@Override
	public boolean checkAleardyCreatedAccount(AuthenticationBean authenticationBean) {
		boolean result = false;
		LOGGER.info("Select Account Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.user.account.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("mobileNumber", authenticationBean.getMobileNumber());
			  namedParameters.put("userId", authenticationBean.getUserId());
			  int count = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  LOGGER.info("Count :::: "+ count); 
			  if(count == 0)
			  { 
				  result = true;
			  }
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return result;
	}
	
	@Override
	public List<UpdateDTDetails> fetchUpdatedDetails(String mobileNumber, String bankId) {
		 List<UpdateDTDetails> updateDTDetails = new ArrayList<UpdateDTDetails>();
		try {
			final String sql = messageSource.getMessage("select.update.details.by.mobilenumber", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("mobileNumber", mobileNumber);
			paramMap.put("bankId", bankId);
			updateDTDetails = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(UpdateDTDetails.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return updateDTDetails;
	}


	@Override
	public List<ViewConsent> fetchAllConsentDetails() {
		List<ViewConsent> viewConsent = new ArrayList<ViewConsent>();
		try {
			final String sql = messageSource.getMessage("fetch.all.consent.details", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			viewConsent = bankNamedParameterJdbcTemplate.query(sql, paramMap, BeanPropertyRowMapper.newInstance(ViewConsent.class));
		} catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return viewConsent;
	}


	@Override
	public int fetchTransactionStatusDetails(String transactinStatus) {
		int count = 0;
		LOGGER.info("Select Txn Status Details from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.txn.status.details", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("transactinStatus", transactinStatus);
			  count = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  LOGGER.info("Count :::: "+ count); 
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return count;
	}


	@Override
	public int fetchTransactionStatusDetailsbyMoths(String month, int year, String transactinStatus) {
		int count = 0;
		LOGGER.info("Select Txn Status Details by Month from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.txn.status.details.by.month", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("month", month);
			  namedParameters.put("year", year);
			  namedParameters.put("transactinStatus", transactinStatus);
			  count = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  LOGGER.info("Count :::: "+ count); 
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return count;
	}


	@Override
	public int fetchBankConsentDetailsbyUser(InstitutionDetails institutionDetails, UserDetails userDetails) {
		int count = 0;
		LOGGER.info("Select Bank Consent Details by User from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.bank.consent.details.by.user", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", institutionDetails.getBankId());
			  namedParameters.put("mobileNumber", userDetails.getMobileNumber());
			  count = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  LOGGER.info("Count :::: "+ count); 
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return count;
	}


	@Override
	public int fetchBankConsentDetailsbyBank(InstitutionDetails institutionDetails) {
		int count = 0;
		LOGGER.info("Select Bank Consent Details by Bank from DB");
		try {
			  LOGGER.info("try block"); 
			  String sql = messageSource.getMessage("select.bank.consent.details.by.bank", null, null);
			  LOGGER.info("Query {}"+ sql); 
			  bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			  Map<String, Object> namedParameters = new HashMap<String, Object>();
			  namedParameters.put("bankId", institutionDetails.getBankId());
			  count = bankNamedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
			  LOGGER.info("Count :::: "+ count); 
			 } 
			catch (Exception e) {
			LOGGER.info("catch block");
			LOGGER.error("The Exception message is::{}" + e);
		}
		return count;
	}

	@Override
	public String fetchLoginAuthenticatorFlagbyUserId(String userId) {
		String loginAuthenticatorFlag = null;
		try {
			final String sql = messageSource.getMessage("fetch.login.authentication.flag.by.userid", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("userId", userId);
			loginAuthenticatorFlag = bankNamedParameterJdbcTemplate.queryForObject(sql, paramMap, String.class);
		
		} catch(Exception e) {
			LOGGER.error("catch block");
		}
		return loginAuthenticatorFlag;
	}

	@Override
	public int updateLoginAuthenticatorbyuserid(String userId, String loginAuthenticatorFlag) {
		int count = 0;
		try {
			final String sql = messageSource.getMessage("update.login.authentication.flag.by.userid", null, null);
			LOGGER.info("Query::{}", sql);
			bankNamedParameterJdbcTemplate = (NamedParameterJdbcTemplate) applicationContext.getBean("namedParameterJdbcTemplate");
			Map<String, Object> paramMap = new ConcurrentHashMap<String, Object>();
			paramMap.put("userId", userId);
			paramMap.put("loginAuthenticatorFlag", loginAuthenticatorFlag);
			count = bankNamedParameterJdbcTemplate.update(sql, paramMap);
			
		} catch(Exception e) {
			LOGGER.error("catch block");
		}
		return count;
	}

}
