package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LaShoppeEnchant {

	private Enchantment enchantment;

	private int price;

	private int tier;

	private int index;

	public LaShoppeEnchant(Enchantment enchantment, int price, int tier, int index) {
		this.enchantment = enchantment;
		this.price = price;
		this.tier = tier;
		this.index = index;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}

	public int getPrice() {
		return price;
	}

	public int getTier() {
		return tier;
	}

	public int getIndex() {
		return index;
	}

	public ItemStack toItemStack() {
		ItemStack itemStack = $.createMaterial(Material.ENCHANTED_BOOK, $.formatEnchantment(enchantment.getName(), tier));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "Index: " + index);
		lore.add(ChatColor.RESET + "Price: $" + price);
		lore.add(ChatColor.RESET + "Tier: " + tier);
		lore.add("");
		lore.add(ChatColor.RESET + "Use RIGHT click to BUY this enchant");
		return $.addLore(itemStack, lore.toArray(new String[0]));
	}

}
