package com.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import com.app.dao.CustomerDAO;
import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Customer;
import com.app.util.dbutil.PostgresConnection;

public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public Customer getCustomerByCredentials(Credentials cr) throws BusinessException {

		Customer customer = null;
		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select c.customerid, c.firstname, c.lastname, c.dob, "
					+ "c.email, c.ssn, c.phone, c.streetname, c.city, c.zip "
					+ "from bank_schema.customer c join bank_schema.credentials cr "
					+ "on c.credentialsid = cr.credentialsid where "
					+ "cr.username = ? and cr.password = ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, cr.getUsername());
			preparedStatement.setString(2, cr.getPassword());

			ResultSet results = preparedStatement.executeQuery();

			if (results.next()) {
				customer = new Customer();

				customer.setCredentials(cr);
				customer.setId(results.getInt("customerid"));
				customer.setfName(results.getString("firstname"));
				customer.setlName(results.getString("lastname"));

				customer.setDob(results.getDate("dob"));
				customer.setEmail(results.getString("email"));
				customer.setSsn(results.getString("ssn"));
				customer.setContactNum(results.getString("phone"));
				customer.setStreetName(results.getString("streetname"));
				customer.setCity(results.getString("city"));
				customer.setZip(results.getString("zip"));
			}
			if (customer == null) {
				throw new BusinessException("Account not found");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-C01 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}

		return customer;
	}

	@Override
	public Customer getCustomerById(int id) throws BusinessException {
		Customer customer = null;
		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select firstname, lastname, dob, "
					+ "email, ssn, phone, streetname, city, zip "
					+ "from bank_schema.customer  where " + "customerid = ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);

			ResultSet results = preparedStatement.executeQuery();

			if (results.next()) {
				customer = new Customer();

				customer.setId(id);
				customer.setfName(results.getString("firstname"));
				customer.setlName(results.getString("lastname"));

				customer.setDob(results.getDate("dob"));
				customer.setEmail(results.getString("email"));
				customer.setSsn(results.getString("ssn"));
				customer.setContactNum(results.getString("phone"));
				customer.setStreetName(results.getString("streetname"));
				customer.setCity(results.getString("city"));
				customer.setZip(results.getString("zip"));
			}
			if (customer == null) {
				throw new BusinessException("Account not found");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException(
					"E-C02 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return customer;
	}

	@Override
	public int createCustomerRecord(Customer newCustomer) throws BusinessException {

		int update = 0;// stores the value of a updated query
		Connection conn = null;
		try {
			conn = PostgresConnection.getConnection();
			conn.setAutoCommit(false);

			// updating the 'credentials' table first.
			String sql1 = "insert into bank_schema.credentials(username, password) "
					+ "values(?, ?) returning credentialsid";
			PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
			preparedStatement1.setString(1, newCustomer.getCredentials().getUsername());
			preparedStatement1.setString(2, newCustomer.getCredentials().getPassword());
			ResultSet results1 = preparedStatement1.executeQuery();
			results1.next();

			// getting the auto generated id
			int credentialsid = results1.getInt("credentialsid");

			// updating the 'customer' table second.
			String sql2 = "insert into bank_schema.customer (firstname, lastname,"
					+ "dob,ssn,email,phone,streetname,city,zip, credentialsid) "
					+ "values(?,?,?,?, ?,?,?,?,? ,?) returning customerid";
			PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
			preparedStatement2.setString(1, newCustomer.getfName());
			preparedStatement2.setString(2, newCustomer.getlName());
			preparedStatement2.setDate(3, new java.sql.Date(newCustomer.getDob().getTime()));
			preparedStatement2.setString(4, newCustomer.getSsn());
			preparedStatement2.setString(5, newCustomer.getEmail());
			preparedStatement2.setString(6, newCustomer.getContactNum());
			preparedStatement2.setString(7, newCustomer.getStreetName());
			preparedStatement2.setString(8, newCustomer.getCity());
			preparedStatement2.setString(9, newCustomer.getZip());
			preparedStatement2.setInt(10, credentialsid);
			ResultSet results2 = preparedStatement2.executeQuery();
			results2.next();
			int customerid = results2.getInt("customerid");

			// updating the 'account' table third.
			String sql3 = "insert into bank_schema.account (customerid, accounttypeid, statusid, balance, "
					+ "opendate) values(?,"
					+ "(select accounttypeid from bank_schema.accounttype where type = ?), "
					+ "(select statusid from bank_schema.accountstatus where status =?),"
					+ "?,?) returning accountid";
			PreparedStatement preparedStatement3 = conn.prepareStatement(sql3);
			preparedStatement3.setInt(1, customerid);
			preparedStatement3.setString(2, newCustomer.getBankAccounts().get(0).getAcctType());
			preparedStatement3.setString(3, newCustomer.getBankAccounts().get(0).getStatus());
			preparedStatement3.setFloat(4, newCustomer.getBankAccounts().get(0).getBalance());
			preparedStatement3.setDate(5, new java.sql.Date(
					newCustomer.getBankAccounts().get(0).getOpeningDate().getTime()));
			ResultSet results3 = preparedStatement3.executeQuery();
			results3.next();
			int accountid = results3.getInt("accountid");

			String sql4 = "insert into bank_schema.transactions(accountid, "
					+ "transactiontypeid, destinationaccount,"
					+ " transactiondate, amount) values(?,1,?,?,?)";
			PreparedStatement preparedStatement4 = conn.prepareStatement(sql4);
			preparedStatement4.setInt(1, accountid);
			preparedStatement4.setInt(2, accountid);
			preparedStatement4.setTimestamp(3, new Timestamp(new Date().getTime()));
			preparedStatement4.setFloat(4, newCustomer.getBankAccounts().get(0).getBalance());
			update = preparedStatement4.executeUpdate();

			conn.commit();
			conn.close();
			if (credentialsid == 0 || customerid == 0 || accountid == 0 || update == 0) {
				throw new BusinessException("Error in inserting new customer.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {

				throw new BusinessException(
						"E-C03 : Internal Error- Please contact System Administrator" + "\nReason: "
								+ e1.getMessage());
			}
			throw new BusinessException(
					"E-C03 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return update;
	}

	@Override
	public int updateCustomerRecord(Customer updatedCustomer) throws BusinessException {

		int update = 0;
		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "update bank_schema.customer "
					+ "set firstname=?, lastname =?, email=?, phone=?, streetname=?"
					+ ", city =?, zip=? where customerid = ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			preparedStatement.setString(1, updatedCustomer.getfName());
			preparedStatement.setString(2, updatedCustomer.getlName());
			preparedStatement.setString(3, updatedCustomer.getEmail());
			preparedStatement.setString(4, updatedCustomer.getContactNum());
			preparedStatement.setString(5, updatedCustomer.getStreetName());
			preparedStatement.setString(6, updatedCustomer.getCity());
			preparedStatement.setString(7, updatedCustomer.getZip());
			preparedStatement.setInt(8, updatedCustomer.getId());

			update = preparedStatement.executeUpdate();

		} catch (ClassNotFoundException | SQLException e) {
			throw new BusinessException(
					"E-C04 : Internal Error- Please contact System Administrator" + "\nReason: "
							+ e.getMessage());
		}
		return update;
	}
}
