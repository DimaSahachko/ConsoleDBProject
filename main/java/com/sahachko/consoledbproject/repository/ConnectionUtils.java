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
				Statement statement = connection.createStatement();
				statement.execute(DDLqueries.CREATE_DATABASE.getSQLQuery());
				statement.execute(DDLqueries.USE_DATABASE.getSQLQuery());
				statement.execute(DDLqueries.CREATE_REGIONS_TABLE.getSQLQuery());
				statement.execute(DDLqueries.CREATE_POSTS_TABLE.getSQLQuery());
				statement.execute(DDLqueries.CREATE_USERS_TABLE.getSQLQuery());
				statement.execute(DDLqueries.CREATE_POSTS_BY_USERS_TABLE.getSQLQuery());
				statement.close();
			} catch (SQLException | IOException exc) {
				exc.printStackTrace();
			}
		}
	}
}

enum DDLqueries {
	CREATE_DATABASE("CREATE DATABASE IF NOT EXISTS consoleProject;"),
	USE_DATABASE("USE consoleProject;"),
	CREATE_REGIONS_TABLE("CREATE TABLE IF NOT EXISTS regions (id BIGINT, name VARCHAR(100) NOT NULL, PRIMARY KEY(id));"),
	CREATE_POSTS_TABLE("CREATE TABLE IF NOT EXISTS posts (id BIGINT, content VARCHAR(300) NOT NULL, created TIMESTAMP NOT NULL, updated TIMESTAMP NULL, PRIMARY KEY(id));"),
	CREATE_USERS_TABLE("CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY, firstName VARCHAR(40) NOT NULL, lastName VARCHAR(40) NOT NULL, "
			+ "region BIGINT NOT NULL, role VARCHAR(30) NOT NULL, FOREIGN KEY(region) REFERENCES regions(id));"),
	CREATE_POSTS_BY_USERS_TABLE("CREATE TABLE IF NOT EXISTS posts_by_users(user_id BIGINT, post_id BIGINT, PRIMARY KEY(user_id, post_id), "
			+ "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE, FOREIGN KEY(post_id) REFERENCES posts(id) ON DELETE CASCADE);");
	
	DDLqueries(String sql) {
		this.sqlQuery = sql;
	}
	
	private String sqlQuery;
	public String getSQLQuery() {
		return sqlQuery;
	}
}
