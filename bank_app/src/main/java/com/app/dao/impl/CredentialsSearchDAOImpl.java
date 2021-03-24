package com.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.app.dao.CredentialsSearchDAO;
import com.app.exception.BusinessException;

import com.app.util.dbutil.PostgresConnection;

public class CredentialsSearchDAOImpl implements CredentialsSearchDAO {
	
	@Override
	public boolean isUsernamePresent(String username) throws BusinessException {

		/*
		 * userCount- based on query should be 1 if user found and zero if user not found.
		 */
		int userCount = 0;

		try (Connection conn = PostgresConnection.getConnection()) {

			String sql = "select count(username) from bank_schema.credentials where username = ?";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			preparedStatement.setString(1, username);
			ResultSet results = preparedStatement.executeQuery();

			while (results.next()) {
				userCount = results.getInt("count");
			}
		} catch (ClassNotFoundException | SQLException e) {
			
			throw new BusinessException(
					"E-CR1: Contact system administrator" + "\nReason " + e.getMessage());
		}

		if (userCount >= 1) {
			return true;
		} else {
			return false;
		}
	}
}
