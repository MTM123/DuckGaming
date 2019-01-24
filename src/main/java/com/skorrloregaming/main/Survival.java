package com.skorrloregaming.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Survival
		implements Listener {
	public static void saa(Player player) {
		Player p = player;
		ItemStack currentItem = p.getInventory().getHelmet();
		ItemStack AIR = new ItemStack(Material.AIR);
		if (currentItem == new ItemStack(Material.AIR) || currentItem == null || currentItem != new ItemStack(Material.BEDROCK)) {
			return;
		}
		p.getInventory().setHelmet(AIR);
		p.getInventory().setHelmet(AIR);
		p.getInventory().setHelmet(AIR);
		p.getInventory().setHelmet(AIR);
	}

	public static void listAllKits(Player data) {
		data.sendMessage((Object) ChatColor.GRAY + "Current Kits:");
		data.sendMessage((Object) ChatColor.GRAY + "/kit default");
		data.sendMessage((Object) ChatColor.GRAY + "/kit vip");
		data.sendMessage((Object) ChatColor.GRAY + "/kit vip+");
		data.sendMessage((Object) ChatColor.GRAY + "/kit mvp");
		data.sendMessage((Object) ChatColor.GRAY + "/kit mvp+");
	}
}

