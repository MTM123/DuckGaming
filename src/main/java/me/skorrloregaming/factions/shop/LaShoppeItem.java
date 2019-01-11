package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LaShoppeItem {

	private Material material;

	private int price;

	private int amount;

	public LaShoppeItem(Material material, int price, int amount) {
		this.material = material;
		this.price = price;
		this.amount = amount;
	}

	public Material getMaterial() {
		return material;
	}

	public int getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}

	public ItemStack toItemStack() {
		ItemStack itemStack = $.createMaterial(material);
		List<String> lore = new ArrayList<String>();
		lore.add("Price: $" + price);
		lore.add("Amount: " + amount + "x");
		return $.addLore(itemStack, lore.toArray(new String[0]));
	}

}
