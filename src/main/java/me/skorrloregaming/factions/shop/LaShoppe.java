package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.events.LaShoppeEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class LaShoppe {

	private LaShoppeEventHandler eventHandler;

	public LaShoppe() {
		eventHandler = new LaShoppeEventHandler(this);
		Bukkit.getServer().getPluginManager().registerEvents(eventHandler, Server.getPlugin());
	}

	public Inventory createInventory(Player player) {
		try {
			new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
					player.sendMessage("You have entered: " + event.getName());
				}
			})
					.setInputName("Please enter a valid material name")
					.open();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return Bukkit.createInventory(null, 9);
	}

}
