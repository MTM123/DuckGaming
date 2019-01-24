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

	public void testLogic() {
		System.out.println("Hit testLogic() of SQLDatabase");
		createTable("test");
		System.out.println("Passed section 1 of testLogic()");
		if (tableExists("test"))
			System.out.println("Passed section 2 of testLogic()");
		set("test", "message", "Hello World");
		System.out.println("Passed section 3 of testLogic()");
		String message = getString("test", "message");
		System.out.println("Passed section 4 of testLogic()");
		if (message.equals("Hello World"))
			System.out.println("Passed section 5 of testLogic()");
		dropTable("test");
		System.out.println("Passed section 6 of testLogic()");
		set("test2", "message2", "Hello there World, it is I.");
		System.out.println("Passed section 7 of testLogic()");
		if (tableExists("test2"))
			System.out.println("Passed section 8 of testLogic()");
		String message2 = getString("test2", "message2");
		System.out.println("Passed section 9 of testLogic()");
		if (message2.equals("Hello there World, it is I."))
			System.out.println("Passed section 10 of testLogic()");
		dropTable("test2");
		System.out.println("Passed section 11 of testLogic()");
	}

	public int createTable(String table) {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS " + table + "(sql_key VARCHAR(255), sql_value VARCHAR(255))";
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int dropTable(String table) {
		try {
			String sql = "DROP TABLE " + table;
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean tableExists(String table) {
		try {
			String sql = "SELECT 1 FROM " + table + " LIMIT 1";
			connection.prepareStatement(sql).executeQuery();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int set(String table, String key, String value) {
		return set(table, key, value, true);
	}

	private int set(String table, String key, String value, boolean loopback) {
		try {
			String sql = "INSERT INTO " + table + "(sql_key, sql_value) VALUES('" + key + "', '" + value + "')";
			return connection.prepareStatement(sql).executeUpdate();
		} catch (Exception e) {
			if (loopback) {
				if (createTable(table) == -1) {
					e.printStackTrace();
					return -1;
				}
				return set(table, key, value, false);
			}
			e.printStackTrace();
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
				if (createTable(table) == -1) {
					e.printStackTrace();
					return null;
				}
				return getString(table, key, false);
			}
			e.printStackTrace();
			return null;
		}
	}

	public boolean close() {
		if (connection == null)
			return false;
		try {
			connection.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
