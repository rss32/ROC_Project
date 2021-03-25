package com.app.dao;

import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Customer;

public interface CustomerDAO {
	public Customer getCustomerByCredentials(Credentials cr) throws BusinessException;

	public Customer getCustomerById(int id) throws BusinessException;

	public int createCustomerRecord(Customer newCustomer) throws BusinessException;

	public int updateCustomerRecord(Customer updatedCustomer) throws BusinessException;
}
