package me.skorrloregaming.factions.shop.events.item;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.factions.shop.LaShoppe;
import org.bukkit.Material;

public class CreateItemDataEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private LaShoppe shoppe;

	private Material material;

	private int price;

	private int amount;

	public CreateItemDataEventHandler(LaShoppe shoppe, Material material, int price, int amount) {
		this.shoppe = shoppe;
		this.material = material;
		this.price = price;
		this.amount = amount;
	}

	@Override
	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		String dataString = event.getName();
		final int data;
		try {
			data = Integer.parseInt(dataString);
		} catch (Exception ex) {
			event.getPlayer().sendMessage("Sorry, that's not a valid item data.");
			return;
		}
		event.getPlayer().sendMessage("Material: " + material.toString());
		event.getPlayer().sendMessage("Price: $" + price);
		event.getPlayer().sendMessage("Amount: " + amount + "x");
		event.getPlayer().sendMessage("Data: " + data);
		shoppe.createItem(material, price, amount, data);
	}
}
