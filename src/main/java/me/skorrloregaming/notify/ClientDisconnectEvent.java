package me.skorrloregaming.notify;

import me.skorrloregaming.Logger;

public class ClientDisconnectEvent {
	public ClientDisconnectEvent(Client client) {
		Logger.info(client.getUsername() + " lost connection to the notify worker.");
	}
}
