package com.app.service.impl;

import com.app.dao.CredentialsSearchDAO;
import com.app.dao.impl.CredentialsSearchDAOImpl;
import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.service.CredentialsService;

public class CredentialsServiceImpl implements CredentialsService {

	private CredentialsSearchDAO crDAO = new CredentialsSearchDAOImpl();

	@Override
	public boolean validateCredentialsInfo(Credentials c) throws BusinessException {
		
		 if(crDAO.isUsernamePresent(c.getUsername())) {
			 throw new BusinessException("Username already exists");
		 }
		
		if (!CredentialsValidation.isValidUsername(c.getUsername())) {
			throw new BusinessException("Username is in an invalid format");
		}
		if (!CredentialsValidation.isValidPassword(c.getPassword())) {
			throw new BusinessException("Password is in an invalid format");
		}

		/*
		 * return true if everything checks out. The method will fail if one of the details is
		 * invalid
		 */
		return true;
	}

}
