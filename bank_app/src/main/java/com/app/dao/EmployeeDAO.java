package com.app.dao;

import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Employee;

public interface EmployeeDAO {
	
	public Employee getEmployeeByCredentials(Credentials credentials) throws BusinessException;

}
