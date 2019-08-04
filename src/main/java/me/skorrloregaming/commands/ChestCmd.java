package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.skorrloregaming.*;

public class ChestCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getKitpvp().contains(player.getUniqueId()) && !ServerGet.get().getFactions().contains(player.getUniqueId()) && !ServerGet.get().getSkyblock().contains(player.getUniqueId()) && !ServerGet.get().getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String subDomain = $.getMinigameDomain(player);
		if (ServerGet.get().getSaveOtherInventory().containsKey(player))
			ServerGet.get().getSaveOtherInventory().remove(player);
		int chestNumber = 1;
		if (args.length > 0) {
			try {
				chestNumber = Math.abs(Integer.parseInt(args[0]));
			} catch (Exception ig) {
			}
		}
		if (chestNumber < 1) {
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified vault does not exist.");
			return true;
		}
		if (args.length < 2 || (!(Bukkit.getPlayer(args[1]) == null) && Bukkit.getPlayer(args[1]).getName().equals(player.getName()))) {
			if (chestNumber > 1) {
				int rankId = Link$.getDonorRankId(player);
				switch (chestNumber) {
					case 2:
						if (!(rankId < -1)) {
							player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							return true;
						}
						break;
					case 3:
						if (!(rankId < -2)) {
							if (rankId < -1) {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
							return true;
						}
						break;
					case 4:
						if (!(rankId < -3)) {
							if (rankId < -1) {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
							return true;
						}
						break;
					case 5:
						if (!(rankId < -4)) {
							if (rankId < -1) {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a higher donor rank to use this.");
							} else {
								player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry, you need a donor rank to use this vault.");
							}
							return true;
						}
						break;
					default:
						player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified vault does not exist.");
						return true;
				}
			}
			Inventory inv = SolidStorage.restorePersonalChest(player, subDomain, true, chestNumber);
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
			player.openInventory(inv);
		} else {
			Player tp = Bukkit.getPlayer(args[1]);
			if (tp == null) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				boolean hasControl = false;
				if (player.isOp())
					hasControl = true;
				String tpSubDomain = $.getMinigameDomain(player);
				Inventory inv = SolidStorage.restorePersonalChest(tp, tpSubDomain, hasControl, chestNumber);
				if (hasControl) {
					ServerGet.get().getSavePersonalChest().put(player, tp);
				}
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				player.openInventory(inv);
			}
		}
		return true;
	}

}
