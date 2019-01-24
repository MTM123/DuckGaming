package com.skorrloregaming.main;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MaterialManager {
	public static ItemStack Create(Material material, String name, int Amount, int data) {
		ItemStack item = new ItemStack(material, Amount, (short) data);
		if (name == null) {
			return item;
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack addEnchant(ItemStack origItem, Enchantment enchant, int value, boolean append) {
		ItemStack item = origItem;
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addEnchant(enchant, value, append);
		item.setItemMeta(itemMeta);
		return item;
	}
}

