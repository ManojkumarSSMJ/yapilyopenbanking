package com.fss.openbanking.bean;

import java.util.List;

public class TppAccountsResponse {

	private String responseFlag;

	private String responseMessage;

	private List<List<AccountDetails>> accountDetailsListofList;

	private List<InstitutionDetails> institutionDetails;
	
	public String getResponseFlag() {
		return responseFlag;
	}

	public void setResponseFlag(String responseFlag) {
		this.responseFlag = responseFlag;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<InstitutionDetails> getInstitutionDetails() {
		return institutionDetails;
	}

	public void setInstitutionDetails(List<InstitutionDetails> institutionDetails) {
		this.institutionDetails = institutionDetails;
	}

	public List<List<AccountDetails>> getAccountDetailsListofList() {
		return accountDetailsListofList;
	}

	public void setAccountDetailsListofList(List<List<AccountDetails>> accountDetailsListofList) {
		this.accountDetailsListofList = accountDetailsListofList;
	}


}
