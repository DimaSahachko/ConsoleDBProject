package com.sahachko.consoledbproject.repository;
import java.io.*;
import java.sql.*;
import java.util.*;
public class ConnectionUtils {
	private static Connection connection;
	
	public static Statement getStatement() {
		connect();
		Statement statement = null;
		try{
			statement = connection.createStatement();
			return statement;
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return statement;
	}
	
	public static Statement getStatement(int resultSetType, int resultSetConcurrency) {
		connect();
		Statement statement = null;
		try{
			statement = connection.createStatement(resultSetType, resultSetConcurrency);
			return statement;
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return statement;
	}
	
	public static PreparedStatement getPreparedStatement(String sql) {
		connect();
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sql);
			return preparedStatement;
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return preparedStatement;
	}
	
	public static PreparedStatement getPreparedStatement(String sql, int resultSetType, int resultSetConcurrency) {
		connect();
		PreparedStatement preparedStatement = null;
		try{
			preparedStatement = connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
			return preparedStatement;
		} catch(SQLException exc) {
			exc.printStackTrace();
		}
		return preparedStatement;
	}
	
	public static void disconnect() {
		if(connection != null) {
			try{
				connection.close();
			} catch(SQLException exc) {
				exc.printStackTrace();
			}
		}
	}
	
	private static void connect() {
		if(connection == null) {
			String url = null;
			String user = null;
			String password = null;
			Properties properties = new Properties();
			try(FileInputStream fis = new FileInputStream("src\\main\\resources\\application.properties")) {
				properties.load(fis);
				url = properties.getProperty("url");
				user = properties.getProperty("user");
				password = properties.getProperty("password");
				connection = DriverManager.getConnection(url, user, password);
			} catch (SQLException | IOException exc) {
				exc.printStackTrace();
			}
		}
	}
}