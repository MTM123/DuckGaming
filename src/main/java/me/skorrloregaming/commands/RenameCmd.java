package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.skorrloregaming.*;

public class RenameCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (player.isOp()) {
			if (args.length < 1) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				String message = sb.toString();
				ItemStack itm = player.getInventory().getItemInMainHand();
				if (itm == null || itm.getType() == Material.AIR) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please hold the item you would like to rename.");
					return true;
				}
				ItemMeta meta = itm.getItemMeta();
				meta.setDisplayName(message);
				itm.setItemMeta(meta);
				player.getInventory().setItemInMainHand(itm);
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The display name of the held item has been modified.");
			}
		} else {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		return true;
	}

}
