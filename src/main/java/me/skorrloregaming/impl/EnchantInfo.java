package me.skorrloregaming.impl;

import org.bukkit.enchantments.Enchantment;

public class EnchantInfo {
	public Enchantment enchant;
	public int power;

	public EnchantInfo(Enchantment enchant, int power) {
		this.enchant = enchant;
		this.power = power;
	}
}
