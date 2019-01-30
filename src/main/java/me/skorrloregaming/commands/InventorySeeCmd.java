package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventorySeeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (args.length == 0) {
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
		} else {
			Player tp = Bukkit.getPlayer(args[0]);
			if (tp == null) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				String name = ChatColor.BOLD + "Temporary Inventory";
				if (player.isOp()) {
					name = ChatColor.BOLD + "Personal Inventory";
					if (Server.getSavePersonalChest().containsKey(player))
						Server.getSavePersonalChest().remove(player);
					Server.getSaveOtherInventory().put(player, tp);
				}
				Inventory inv = Bukkit.createInventory(null, 54, name);
				inv.setContents(tp.getInventory().getContents());
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				player.openInventory(inv);
			}
		}
		return true;
	}

}
