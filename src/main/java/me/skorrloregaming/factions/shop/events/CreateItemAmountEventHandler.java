package me.skorrloregaming.factions.shop.events;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.LaShoppe;
import org.bukkit.Material;

public class CreateItemAmountEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private LaShoppe shoppe;

	private Material material;

	private int price;

	public CreateItemAmountEventHandler(LaShoppe shoppe, Material material, int price) {
		this.shoppe = shoppe;
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
		shoppe.createItem(material, price, amount);
	}
}
