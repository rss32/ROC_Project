package com.app.dao;

import com.app.exception.BusinessException;

/*Credentials DAO accesses only the credentials table in the database.
 * it is only used for accessing directly the usernames and passwords
 * and not accessing user info.
 */
public interface CredentialsSearchDAO {	
	
	public boolean isUsernamePresent(String username) throws BusinessException;
}
