package com.app.service;

import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Employee;

public interface EmployeeService {
	
	public Employee getEmployeeByCredentials(Credentials credentials) throws BusinessException;

}
