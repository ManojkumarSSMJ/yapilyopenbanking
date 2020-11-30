/**
 * 
 */
package com.fss.openbanking.service;

import java.util.List;

import com.fss.openbanking.bean.Dashboard;
import com.fss.openbanking.bean.ViewConsent;

public interface AdminService {

	List<ViewConsent> fetchAllConsentDetails();

	List<Dashboard> fetchApiStatisticsList();

	List<Dashboard> fetchMonthWiseApiStatisticsList();

	List<Dashboard> fetchBankWiseUserStatisticsList();

	List<Dashboard> fetchBankWiseConsentStatisticsList();

}
