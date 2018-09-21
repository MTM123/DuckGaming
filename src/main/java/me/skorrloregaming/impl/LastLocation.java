package me.skorrloregaming.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LastLocation extends Location {
	private final Player player;
	private final boolean insideVehicle;

	public LastLocation(final Player player, Location location) {
		super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.player = player;
		this.insideVehicle = player.isInsideVehicle();
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isInsideVehicle() {
		return insideVehicle;
	}
}
