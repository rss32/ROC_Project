package com.app.util.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {
	private static Connection connection;

	private PostgresConnection() {
	}

	public static Connection getConnection()
			throws ClassNotFoundException, SQLException {

		// if (connection == null) {
		Class.forName("org.postgresql.Driver");
		String url = "jdbc:postgresql://localhost:5432/postgres";
		String username = "postgres";
		String password = "revature";
		connection = DriverManager.getConnection(url, username, password);
		// }
		return connection;

	}
}
