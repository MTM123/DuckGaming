package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.skorrloregaming.*;

public class ActivateShopCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		ItemStack item = Link$.createMaterial(Material.STICK, ChatColor.LIGHT_PURPLE + "Activation Wand");
		item = Link$.addLore(item, new String[]{"Activation Wand"});
		player.getInventory().addItem(item);
		return true;
	}

}
