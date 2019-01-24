package com.skorrloregaming.main;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {
	public static ItemStack Create(ItemStack Item2, String name, Enchantment E1, Integer E1V, Enchantment E2, Integer E2V, Enchantment E3, Integer E3V) {
		ItemStack output = Item2;
		ItemMeta outputMeta = output.getItemMeta();
		outputMeta.setDisplayName(name);
		outputMeta.addEnchant(E1, E1V.intValue(), true);
		outputMeta.addEnchant(E2, E2V.intValue(), true);
		outputMeta.addEnchant(E3, E3V.intValue(), true);
		output.setItemMeta(outputMeta);
		return output;
	}
}

