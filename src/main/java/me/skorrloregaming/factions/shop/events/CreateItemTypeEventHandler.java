package me.skorrloregaming.factions.shop.events;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.factions.shop.LaShoppe;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

public class CreateItemTypeEventHandler implements AnvilGUI.AnvilClickEventHandler {

	private LaShoppe shoppe;

	public CreateItemTypeEventHandler(LaShoppe shoppe) {
		this.shoppe = shoppe;
	}

	@Override
	public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
		String materialName = event.getName().toUpperCase().replace(" ", "_");
		Material material = null;
		try {
			material = Material.getMaterial(materialName);
		} catch (Exception ex) {
			try {
				material = Material.getMaterial(materialName, true);
			} catch (Exception ex2) {
				event.getPlayer().sendMessage("Sorry, that's not a valid material name.");
				return;
			}
		}
		try {
			new AnvilGUI(event.getPlayer(), new CreateItemPriceEventHandler(shoppe, material))
					.setInputName("Enter item price")
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