package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.events.CreateItemTypeEventHandler;
import me.skorrloregaming.factions.shop.events.LaShoppeEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class LaShoppe {

	private LaShoppeEventHandler eventHandler;

	public LaShoppe() {
		eventHandler = new LaShoppeEventHandler(this);
		Bukkit.getServer().getPluginManager().registerEvents(eventHandler, Server.getPlugin());
	}

	public Inventory createInventory(Player player, LaShoppeFrame frame, int page) {
		switch (frame) {
			case HOME:
				int startIndex = 0;
				Inventory inventory = null;
				if (player.isOp()) {
					inventory = Bukkit.createInventory(null, 36, ChatColor.RESET + "La Shoppe, page " + page);
					inventory.setItem(0, $.createMaterial(Material.ROSE_RED, "Remove items from shop"));
					inventory.setItem(8, $.createMaterial(Material.CACTUS_GREEN, "Add new shop item"));
					startIndex = 9;
				} else {
					inventory = Bukkit.createInventory(null, 27, ChatColor.RESET + "La Shoppe, page " + page);
				}
				ItemStack viewPrevious = $.createMaterial(Material.EMERALD, "View previous page");
				ItemStack viewFollowing = $.createMaterial(Material.EMERALD, "View following page");
				inventory.setItem(startIndex + 18, viewPrevious);
				inventory.setItem(startIndex + 26, viewFollowing);
				break;
			case CREATE_ITEM:
				try {
					new AnvilGUI(player, new CreateItemTypeEventHandler())
							.setInputName("Enter item type")
							.open();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
		return Bukkit.createInventory(null, 9);
	}

}
