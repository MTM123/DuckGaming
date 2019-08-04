package me.skorrloregaming.shop.events.enchant;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.shop.LaShoppe;
import me.skorrloregaming.shop.LaShoppeFrame;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.InvocationTargetException;

import me.skorrloregaming.*;

public class CreateEnchantPriceEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private LaShoppe shoppe;

	private Enchantment enchantment;

	public CreateEnchantPriceEventHandler(LaShoppe shoppe, Enchantment enchantment) {
		this.shoppe = shoppe;
		this.enchantment = enchantment;
	}

	@Override
	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		String priceString = event.getName().replace("$", "");
		final int price;
		try {
			price = Integer.parseInt(priceString);
		} catch (Exception ex) {
			event.getPlayer().sendMessage("Sorry, that's not a valid enchant price.");
			return;
		}
		Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {

			@Override
			public void run() {
				try {
					new AnvilGUI(event.getPlayer(), ServerGet.get().getShoppe().getInventoryName(LaShoppeFrame.CREATE_ENCHANT), new CreateEnchantTierEventHandler(shoppe, enchantment, price))
							.setInputName("Enter tier")
							.open();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		}, 1L);
	}
}
