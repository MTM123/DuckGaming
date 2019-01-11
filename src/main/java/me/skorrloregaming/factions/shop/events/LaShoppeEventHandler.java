package me.skorrloregaming.factions.shop.events;

import me.skorrloregaming.factions.shop.LaShoppe;
import me.skorrloregaming.factions.shop.LaShoppeFrame;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LaShoppeEventHandler implements Listener {

	private LaShoppe shoppe;

	public LaShoppeEventHandler(LaShoppe shoppe) {
		this.shoppe = shoppe;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		ItemStack item = event.getCurrentItem();
		if (item == null)
			return;
		boolean removeMode = false;
		if (inventory.getItem(0) != null) {
			if (inventory.getItem(0).getType() == Material.ROSE_RED) {
				if (inventory.getItem(0).getItemMeta().hasEnchants()) {
					if (inventory.getItem(0).getItemMeta().getDisplayName().equals("Remove items from shop")) {
						removeMode = true;
					}
				}
			}
		}
		if (inventory.getName().startsWith("La Shoppe")) {
			event.setCancelled(true);
			String pageString = inventory.getName().substring(inventory.getName().indexOf("page ") + 5);
			int page = Integer.parseInt(pageString);
			if (item.getType() == Material.ROSE_RED) {
				if (item.getItemMeta().getDisplayName().equals("Remove items from shop")) {
					shoppe.createInventory(player, LaShoppeFrame.HOME, page, !removeMode);
					return;
				}
			} else if (item.getType() == Material.CACTUS_GREEN) {
				if (item.getItemMeta().getDisplayName().equals("Add new shop item")) {
					shoppe.createInventory(player, LaShoppeFrame.CREATE_ITEM, page, removeMode);
				}
			} else if (item.getType() == Material.EMERALD) {
				if (item.getItemMeta().getDisplayName().equals("View previous page")) {
					if (page == 1) {
						player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
						return;
					}
					shoppe.createInventory(player, LaShoppeFrame.HOME, page - 1, removeMode);
				} else if (item.getItemMeta().getDisplayName().equals("View following page")) {
					shoppe.createInventory(player, LaShoppeFrame.HOME, page + 1, removeMode);
				}
			}
			if (item.getItemMeta().hasLore()) {
				String priceIndexString = item.getItemMeta().getLore().get(0);
				String priceString = priceIndexString.substring(priceIndexString.indexOf("Price: $") + 6);
				int price = Integer.parseInt(priceString);
				String amountIndexString = item.getItemMeta().getLore().get(1);
				String amountString = amountIndexString.substring(amountIndexString.indexOf("Amount: ") + 6, amountIndexString.length() - 1);
				int amount = Integer.parseInt(amountString);
				player.sendMessage("Material: " + item.getType().toString());
				player.sendMessage("Price: $" + price);
				player.sendMessage("Amount: " + amount + "x");
			}
		}
	}

}
