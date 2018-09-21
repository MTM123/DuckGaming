package me.skorrloregaming.notify;

import java.util.Arrays;

import me.skorrloregaming.Server;

public class NotificationWorker_Listen implements Runnable {
	public Client client = null;

	public NotificationWorker_Listen(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] messageBytes = new byte[2048];
				int read = client.socket.getInputStream().read(messageBytes);
				if (read < 1)
					break;
				byte[] formattedBytes = Arrays.copyOf(messageBytes, read);
				Server.getNotifyWorker().handle(client, formattedBytes);
			}
		} catch (Exception ex) {
			Server.getNotifyWorker().kickPlayer(client, null);
		}
	}
}
