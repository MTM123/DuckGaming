package me.skorrloregaming.notify;

import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationWorker_AuthLoop implements Runnable {
	public Socket socket;
	public int stage = 0;

	public NotificationWorker_AuthLoop(Socket socket) {
		this.socket = socket;
	}

	public boolean stop = false;

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (stop)
				break;
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			switch (stage) {
			case 0:
				try {
					socket.getOutputStream().write((dateFormat.format(date) + " [INFO] " + "Please type the username you'd like to use.").getBytes());
					socket.getOutputStream().flush();
				} catch (IOException e) {
				}
				break;
			case 1:
				try {
					socket.getOutputStream().write((dateFormat.format(date) + " [INFO] " + "Please type the authentication password for the account.").getBytes());
					socket.getOutputStream().flush();
				} catch (IOException e) {
				}
				break;
			default:
				break;
			}
		}
		if (!stop) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}
}
