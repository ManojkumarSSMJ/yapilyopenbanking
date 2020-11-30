/**
 * 
 */
package com.fss.openbanking.service;

import com.fss.openbanking.bean.InstitutionDetails;
import com.fss.openbanking.bean.TppAccountsResponse;
import com.fss.openbanking.bean.UserDetails;
import com.fss.openbanking.bean.UserResponse;

public interface ApiService {

	UserResponse getInstitutions();

	TppAccountsResponse updateBankDetailsByBank(UserDetails userDetails, InstitutionDetails institutionDetails);

	TppAccountsResponse tppUpdateBankDetails(UserDetails userDetails);

}
