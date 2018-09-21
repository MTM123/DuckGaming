package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.SolidStorage;

public class ChestCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String subDomain = $.getMinigameDomain(player);
		if (Server.getSaveOtherInventory().containsKey(player))
			Server.getSaveOtherInventory().remove(player);
		int chestNumber = -1;
		if (args.length > 0) {
			if (Bukkit.getPlayer(args[0]) == null) {
				try {
					chestNumber = Math.abs(Integer.parseInt(args[0]));
				} catch (Exception ig) {
				}
			}
		}
		if (args.length == 0 || chestNumber > -1 || (!(Bukkit.getPlayer(args[0]) == null) && Bukkit.getPlayer(args[0]).getName().equals(player.getName()))) {
			Inventory inv = SolidStorage.restorePersonalChest(player, subDomain, true);
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
			player.openInventory(inv);
		} else {
			Player tp = Bukkit.getPlayer(args[0]);
			if (tp == null) {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				boolean hasControl = false;
				if (player.isOp())
					hasControl = true;
				String tpSubDomain = $.getMinigameDomain(player);
				Inventory inv = SolidStorage.restorePersonalChest(tp, tpSubDomain, hasControl);
				if (hasControl) {
					Server.getSavePersonalChest().put(player, tp);
				}
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				player.openInventory(inv);
			}
		}
		return true;
	}

}
