package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.InventoryType;
import me.skorrloregaming.impl.ServerMinigame;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TransportCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Link$.getDonorRankId(player) > -3) {
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need Redstone rank to use transport.");
			player.performCommand("store");
			return true;
		}
		int invSize = 45;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 54;
		if (args.length == 0) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <minigame>");
			return true;
		}
		String minigame = args[0].toLowerCase();
		if (!$.validTransportMinigames.contains(minigame) || !$.validTransportMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase())) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You cannot transport to/from " + WordUtils.capitalize(minigame) + ".");
			return true;
		}
		if ($.getCurrentMinigame(player).toString().toLowerCase().equals(minigame)) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry. " + ChatColor.GRAY + "Using this as a trash can is not the idea.");
			return true;
		}
		ItemStack[] contents = SolidStorage.restoreInventoryToArray(player, minigame);
		if (contents == null) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "There is nothing stored for that minigame.");
			return true;
		}
		Inventory transportInventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.TRANSPORT, minigame), invSize, "Inventory of " + player.getName() + " from " + WordUtils.capitalize(minigame));
		for (int i = 0; i < contents.length; i++)
			transportInventory.setItem(i, contents[i]);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		player.openInventory(transportInventory);
		return true;
	}

}
