package me.skorrloregaming.events;

import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerPreMinigameChangeEvent extends Event {

	private Player player;
	private ServerMinigame minigame;

	private static final HandlerList handlers = new HandlerList();

	public PlayerPreMinigameChangeEvent(Player player, ServerMinigame minigame) {
		this.player = player;
		this.minigame = minigame;
	}

	public Player getPlayer() {
		return player;
	}

	public ServerMinigame getMinigame() {
		return minigame;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
