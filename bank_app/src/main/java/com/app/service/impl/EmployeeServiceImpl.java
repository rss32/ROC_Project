package com.app.service.impl;

import com.app.dao.EmployeeDAO;
import com.app.dao.impl.EmployeeDAOImpl;
import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Employee;
import com.app.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeDAO employeeDAO = new EmployeeDAOImpl();

	@Override
	public Employee getEmployeeByCredentials(Credentials credentials) throws BusinessException {
		Employee employee = employeeDAO.getEmployeeByCredentials(credentials);

		return employee;
	}
}
