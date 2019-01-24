package com.skorrloregaming.main;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Storage {
	public static void saveInventory(Player p, String data) throws IOException {
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.armor", (Object) p.getInventory().getArmorContents());
		c.set("inventory.content", (Object) p.getInventory().getContents());
		c.save(new File("plugins/" + File.separator + "PlayerData" + File.separator + "Inventory", String.valueOf(p.getName()) + "_" + data + ".yml"));
	}

	public static void restoreInventory(Player p, String data) throws IOException {
		YamlConfiguration c = YamlConfiguration.loadConfiguration((File) new File("plugins/" + File.separator + "PlayerData" + File.separator + "Inventory", String.valueOf(p.getName()) + "_" + data + ".yml"));
		ItemStack[] content = (ItemStack[]) ((List) c.get("inventory.armor")).toArray(new ItemStack[0]);
		p.getInventory().setArmorContents(content);
		content = (ItemStack[]) ((List) c.get("inventory.content")).toArray(new ItemStack[0]);
		p.getInventory().setContents(content);
	}
}

