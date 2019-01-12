package me.skorrloregaming.factions.shop.events.item;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.LaShoppe;
import me.skorrloregaming.factions.shop.events.item.CreateItemPriceEventHandler;
import org.bukkit.Bukkit;
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
		final Material fMaterial = material;
		Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

			@Override
			public void run() {
				try {
					new AnvilGUI(event.getPlayer(), new CreateItemPriceEventHandler(shoppe, fMaterial))
							.setInputName("Enter price")
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
