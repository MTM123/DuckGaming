package me.skorrloregaming.factions.shop.events;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.factions.shop.LaShoppe;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

public class CreateItemPriceEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private LaShoppe shoppe;

	private Material material;

	public CreateItemPriceEventHandler(LaShoppe shoppe, Material material) {
		this.shoppe = shoppe;
		this.material = material;
	}

	@Override
	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		String priceString = event.getName().replace("$", "");
		int price = 0;
		try {
			price = Integer.parseInt(priceString);
		} catch (Exception ex) {
			event.getPlayer().sendMessage("Sorry, that's not a valid item price.");
			return;
		}
		try {
			new AnvilGUI(event.getPlayer(), new CreateItemAmountEventHandler(shoppe, material, price))
					.setInputName("Enter item amount")
					.open();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
}
