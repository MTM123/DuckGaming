package me.skorrloregaming.factions.shop.events;

import me.skorrloregaming.AnvilGUI;
import org.bukkit.Material;

public class CreateItemAmountEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private Material material;

	private int price;

	public CreateItemAmountEventHandler(Material material, int price) {
		this.material = material;
		this.price = price;
	}

	@Override
	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		String amountString = event.getName().replace("x", "");
		int amount = 0;
		try {
			amount = Integer.parseInt(amountString);
		} catch (Exception ex) {
			event.getPlayer().sendMessage("Sorry, that's not a valid item amount.");
			return;
		}
		event.getPlayer().sendMessage("Material: " + material.toString());
		event.getPlayer().sendMessage("Price: $" + price);
		event.getPlayer().sendMessage("Amount: " + amount + "x");
	}
}
