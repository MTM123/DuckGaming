package me.skorrloregaming.notify;

import me.skorrloregaming.Logger;

public class ClientConnectEvent {
	public ClientConnectEvent(Client client) {
		Logger.info(client.getUsername() + " has connected to the notify worker.");
	}
}
