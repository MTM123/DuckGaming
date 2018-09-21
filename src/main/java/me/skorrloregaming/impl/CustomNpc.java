package me.skorrloregaming.impl;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.skorrloregaming.Server;

public class CustomNpc {
	public ArmorStand entity;

	public CustomNpc(ArmorStand entity) {
		this.entity = entity;
	}

	public static CustomNpc spawn(Location location, String name) {
		location.setX(location.getBlockX() + 0.5);
		location.setY(location.getBlockY() + 0.5);
		location.setZ(location.getBlockZ() + 0.5);
		ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		entity.setAI(false);
		entity.setCanPickupItems(false);
		entity.setCollidable(false);
		entity.setInvulnerable(true);
		entity.setArms(true);
		if (!(name == null)) {
			entity.setCustomName(name);
			entity.setCustomNameVisible(true);
		}
		return new CustomNpc(entity);
	}

	public static String getNpcData(Entity entity) {
		if (!Server.getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
			return null;
		String value = Server.getNpcConfig().getData().getString("npc." + entity.getUniqueId().toString());
		if (value.equals("unspecified"))
			return null;
		return String.valueOf(value);
	}

	public boolean register(String npcData) {
		if (Server.getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
			return false;
		String hash = "unspecified";
		if (!(npcData == null))
			hash = npcData;
		Server.getNpcConfig().getData().set("npc." + entity.getUniqueId().toString(), hash);
		Server.getNpcConfig().saveData();
		return true;
	}

	public boolean unregister() {
		if (!Server.getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
			return false;
		Server.getNpcConfig().getData().set("npc." + entity.getUniqueId().toString(), null);
		Server.getNpcConfig().saveData();
		return true;
	}
}
