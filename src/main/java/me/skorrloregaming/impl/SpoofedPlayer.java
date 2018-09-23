package me.skorrloregaming.impl;

import me.skorrloregaming.CraftGo;
import org.bukkit.Bukkit;

import java.util.UUID;

public class SpoofedPlayer {

	private String playerName;
	private ServerMinigame minigame;

	private UUID offlineId;

	public SpoofedPlayer(String playerName, ServerMinigame minigame) {
		try {
			this.playerName = playerName;
			this.minigame = minigame;
			this.offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public ServerMinigame getMinigame() {
		return minigame;
	}

	public UUID getId() {
		if (Bukkit.getOnlineMode())
			return UUID.fromString(CraftGo.Player.getUUID(playerName, true));
		return offlineId;
	}

	public UUID getOnlineId() {
		return UUID.fromString(CraftGo.Player.getUUID(playerName, true));
	}

	public UUID getOfflineId() {
		return offlineId;
	}

}
