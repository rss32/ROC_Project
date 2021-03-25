package com.app.service;

import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Customer;

public interface CustomerService {

	public Customer getCustomerByCredentials(Credentials credential) throws BusinessException;

	public Customer getCustomerById(int id) throws BusinessException;

	public boolean validateCustomerInfo(Customer c) throws BusinessException;

	public boolean validateCustomerId(int id) throws BusinessException;

	public boolean createCustomer(Customer c) throws BusinessException;

	public boolean updateCustomer(Customer c) throws BusinessException;

}
