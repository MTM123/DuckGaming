package me.skorrloregaming.mysql;

import java.sql.*;

public class SQLDatabase {

	private Connection connection;

	public SQLDatabase(String hostname, String username, String password) {
		String url = "jdbc:mysql://" + hostname + ":3306/javabase";
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public int createTable(String table) {
		try {
			String sql = "CREATE TABLE " + table + "(sql_key VARCHAR(255), sql_value VARCHAR(255))";
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			return -1;
		}
	}

	public int dropTable(String table) {
		try {
			String sql = "DROP TABLE " + table;
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			return -1;
		}
	}

	public boolean tableExists(String table) {
		try {
			String sql = "SELECT 1 FROM " + table + " LIMIT 1";
			connection.prepareStatement(sql).executeQuery();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int set(String table, String key, String value) {
		return set(table, key, value, true);
	}

	private int set(String table, String key, String value, boolean loopback) {
		try {
			if (contains(table, key)) {
				String sql = "DELETE FROM " + table + " WHERE sql_key = '" + key + "'";
				connection.prepareStatement(sql).executeUpdate();
			}
			String sql = "INSERT INTO " + table + "(sql_key, sql_value) VALUES('" + key + "', '" + value + "')";
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			if (loopback) {
				if (createTable(table) == -1)
					return -1;
				return set(table, key, value, false);
			} else
				return -1;
		}
	}

	public String getString(String table, String key) {
		return getString(table, key, true);
	}

	private String getString(String table, String key, boolean loopback) {
		try {
			String query = "SELECT * FROM " + table;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String currentKey = rs.getString(1);
				String currentValue = rs.getString(2);
				if (currentKey.equals(key))
					return currentValue;
			}
			return null;
		} catch (Exception e) {
			if (loopback) {
				if (createTable(table) == -1)
					return null;
				return getString(table, key, false);
			} else
				return null;
		}
	}

	public boolean contains(String table, String key) {
		if (getString(table, key) == null)
			return false;
		return true;
	}

	public boolean close() {
		if (connection == null)
			return false;
		try {
			connection.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
