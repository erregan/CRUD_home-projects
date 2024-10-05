package projects2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects2.exception.DbException2;

public class DbConnection2 {
	private static final String SCHEMA = "projects2";
	private static final String USER = "projects2";
	private static final String PASSWORD = "projects2";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;

	public static Connection getConnection() {
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER,
				PASSWORD);
		System.out.println("Connecting with url=" + url);

		try {
			Connection conn = DriverManager.getConnection(url);
			System.out.println("Successfully obtained connection!");
			return conn;
		} catch (SQLException e) {
			System.out.println("Error getting connection.");
			throw new DbException2(e);
		}
	}
}
