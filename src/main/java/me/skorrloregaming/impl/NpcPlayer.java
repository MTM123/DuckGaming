package me.skorrloregaming.impl;

import me.skorrloregaming.CraftGo;
import org.bukkit.Bukkit;

import java.util.UUID;

public class NpcPlayer {

	private String name;
	private ServerMinigame minigame;

	private UUID offlineId;

	public NpcPlayer(String name, ServerMinigame minigame) {
		try {
			this.name = name;
			this.minigame = minigame;
			this.offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public ServerMinigame getMinigame() {
		return minigame;
	}

	public UUID getId() {
		if (Bukkit.getOnlineMode())
			return UUID.fromString(CraftGo.Player.getUUID(name, true));
		return offlineId;
	}

	public UUID getOnlineId() {
		return UUID.fromString(CraftGo.Player.getUUID(name, true));
	}

	public UUID getOfflineId() {
		return offlineId;
	}

}
