/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl;

import java.util.UUID;

import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Server;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class CustomNpc {
	public ArmorStand entity;

	public CustomNpc(ArmorStand entity) {
		this.entity = entity;
	}

	public static CustomNpc spawn(Location location, String name) {
		location.setX((double) location.getBlockX() + 0.5);
		location.setY((double) location.getBlockY() + 0.5);
		location.setZ((double) location.getBlockZ() + 0.5);
		ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
		entity.setAI(false);
		entity.setCanPickupItems(false);
		entity.setCollidable(false);
		entity.setInvulnerable(true);
		entity.setArms(true);
		if (name != null) {
			entity.setCustomName(name);
			entity.setCustomNameVisible(true);
		}
		return new CustomNpc(entity);
	}

	public static String getNpcData(Entity entity) {
		if (!Server.npcConfig.getData().contains("npc." + entity.getUniqueId().toString())) {
			return null;
		}
		String value = Server.npcConfig.getData().getString("npc." + entity.getUniqueId().toString());
		if (value.equals("unspecified")) {
			return null;
		}
		return String.valueOf(value);
	}

	public boolean register(String npcData) {
		if (Server.npcConfig.getData().contains("npc." + this.entity.getUniqueId().toString())) {
			return false;
		}
		String hash = "unspecified";
		if (npcData != null) {
			hash = npcData;
		}
		Server.npcConfig.getData().set("npc." + this.entity.getUniqueId().toString(),  hash);
		Server.npcConfig.saveData();
		return true;
	}

	public boolean unregister() {
		if (!Server.npcConfig.getData().contains("npc." + this.entity.getUniqueId().toString())) {
			return false;
		}
		Server.npcConfig.getData().set("npc." + this.entity.getUniqueId().toString(),  null);
		Server.npcConfig.saveData();
		return true;
	}
}

