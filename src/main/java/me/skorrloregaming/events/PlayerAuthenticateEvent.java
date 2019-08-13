package me.skorrloregaming.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.skorrloregaming.*;

public class PlayerAuthenticateEvent extends Event {

	private Player player;
	private String joinMessage;

	private static final HandlerList handlers = new HandlerList();

	public PlayerAuthenticateEvent(Player player, String joinMessage) {
		this.player = player;
		this.joinMessage = joinMessage;
	}

	public Player getPlayer() {
		return player;
	}

	public String getJoinMessage() {
		return joinMessage;
	}

	public void setJoinMessage(String message) {
		this.joinMessage = message;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}