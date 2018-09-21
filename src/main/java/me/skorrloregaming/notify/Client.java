package me.skorrloregaming.notify;

import java.net.Socket;

public class Client {
	public Socket socket;
	private String username, password;

	public Client(Socket socket, String username, String password) {
		this.socket = socket;
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
