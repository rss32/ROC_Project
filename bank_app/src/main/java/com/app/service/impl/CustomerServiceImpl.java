package com.app.service.impl;

import java.util.Date;

import com.app.dao.CustomerDAO;
import com.app.dao.impl.CustomerDAOImpl;
import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Customer;
import com.app.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

	private CustomerDAO customerDAO = new CustomerDAOImpl();

	@Override
	public Customer getCustomerByCredentials(Credentials credentials) throws BusinessException {

		Customer customer = customerDAO.getCustomerByCredentials(credentials);

		return customer;
	}

	@Override
	public Customer getCustomerById(int id) throws BusinessException {

		return customerDAO.getCustomerById(id);
	}

	/*
	 * We want to make sure that all of the details are valid before entering into the database.
	 */
	@Override
	public boolean validateCustomerInfo(Customer c) throws BusinessException {

		if (!CustInfoValidation.isValidCustomerName(c.getfName())) {
			throw new BusinessException("Entered first name " + c.getfName() + " is not valid.");
		}
		if (!CustInfoValidation.isValidCustomerName(c.getlName())) {
			throw new BusinessException("Entered last name " + c.getlName() + " is not valid.");
		}
		if (!CustInfoValidation.isValidAge(
				(int) ((new Date().getTime() - c.getDob().getTime()) / (31556952000L)))) {

			throw new BusinessException("Entered date " + c.getDob() + " is not valid.");
		}

		if (!CustInfoValidation.isTaxIdValid(c.getSsn())) {
			throw new BusinessException(
					"Entered social security number " + c.getSsn() + " is not valid.");
		}

		if (!CustInfoValidation.isValidContactNumber(c.getContactNum())) {
			throw new BusinessException(
					"Entered contact number " + c.getContactNum() + " is not valid.");
		}

		if (!CustInfoValidation.isValidEmail(c.getEmail())) {
			throw new BusinessException("Entered email " + c.getEmail() + " is not valid.");
		}

		if (!CustInfoValidation.isValidZip(c.getZip())) {
			throw new BusinessException("Entered zip " + c.getZip() + " is not valid.");
		}

		if (!CustInfoValidation.isValidStreetName(c.getStreetName())) {
			throw new BusinessException("Entered street " + c.getStreetName() + " is not valid.");
		}

		if (!CustInfoValidation.isValidCity(c.getCity())) {
			throw new BusinessException("Entered city " + c.getCity() + " is not valid.");
		}

		/*
		 * return true if everything checks out. The method will fail if one of the details is
		 * invalid
		 */
		return true;
	}

	@Override
	public boolean validateCustomerId(int id) throws BusinessException {

		if (!CustInfoValidation.isValidCustomerId(id)) {
			throw new BusinessException("Entered customer id number " + id + " is not valid.");
		}
		/*
		 * return true if everything checks out. The method will fail if one of the details is
		 * invalid
		 */
		return true;
	}

	@Override
	public boolean createCustomer(Customer c) throws BusinessException {
		int updated = customerDAO.createCustomerRecord(c);
		if (updated >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateCustomer(Customer c) throws BusinessException {
		int updated = customerDAO.updateCustomerRecord(c);
		if (updated >= 1) {
			return true;
		} else {
			return false;
		}
	}

}
