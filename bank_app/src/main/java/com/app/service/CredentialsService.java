
package com.app.service;

import com.app.exception.BusinessException;
import com.app.model.Credentials;

/*Credentials service manages the credentials table in the database.
 * it is only used for accessing directly the usernames and passwords
 * and not accessing user info.
 */
public interface CredentialsService {

	public boolean validateCredentialsInfo(Credentials c) throws BusinessException;

}
