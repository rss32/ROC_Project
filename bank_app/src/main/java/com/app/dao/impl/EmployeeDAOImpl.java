package com.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.app.dao.EmployeeDAO;
import com.app.exception.BusinessException;
import com.app.model.Credentials;
import com.app.model.Employee;
import com.app.util.dbutil.PostgresConnection;


public class EmployeeDAOImpl implements EmployeeDAO {

	@Override
	public Employee getEmployeeByCredentials(Credentials credentials) throws BusinessException {
		Employee employee = null;
		try (Connection conn = PostgresConnection.getConnection()) {
			
			String sql = "select e.employeeid, e.firstname, e.lastname, "
					+ "e.email, e.phone, pr.type "
					+ "from bank_schema.employee e join bank_schema.credentials cr "
					+ "on e.credentialsid = cr.credentialsid "
					+ "join bank_schema.privilege  pr on e.privilegeid = pr.privilegeid "
					+ "where  cr.username = ? and cr.password = ? ";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, credentials.getUsername());
			preparedStatement.setString(2, credentials.getPassword());

			ResultSet results = preparedStatement.executeQuery();

			if (results.next()) {
				employee = new Employee();

				employee.setCredentials(credentials);
				employee.setId(results.getInt("employeeid"));
				employee.setfName(results.getString("firstname"));
				employee.setlName(results.getString("lastname"));

				employee.setEmail(results.getString("email"));

				employee.setContactNum(results.getString("phone"));
				employee.setPrivilegeLevel(results.getString("type"));	
			}
			
			if (employee == null) {
				throw new BusinessException("User not found");
			}
		} catch (ClassNotFoundException | SQLException e) {

			throw new BusinessException("E-E01 : Internal Error- Please contact System Administrator" +
			"Reason: "+ e.getMessage());
		}

		return employee;
	}
}
