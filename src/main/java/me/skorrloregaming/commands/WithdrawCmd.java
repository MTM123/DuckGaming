package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.InventoryType;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class WithdrawCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String subDomain = $.getMinigameDomain(player);
		ServerMinigame minigame = $.getCurrentMinigame(player);
		String tag = $.getMinigameTag(player);
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Material material = Material.getMaterial(Server.getPlugin().getConfig().getString("settings.economy.base." + minigame.toString()));
		String materialName = Link$.formatMaterial(material);
		int amount = 1;
		if (args.length > 0) {
			try {
				amount = Integer.parseInt(args[0]);
			} catch (Exception ex) {
				player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please specify a whole number to withdraw.");
			}
		}
		double cash = EconManager.retrieveCash(player, subDomain);
		if (amount >= cash) {
			int totalWithdraw = 0;
			do {
				ItemStack purchaseItem = new ItemStack(material, amount % 64);
				EconManager.withdrawCash(player, amount % 64, subDomain);
				player.getInventory().addItem(purchaseItem);
				player.updateInventory();
				totalWithdraw += amount % 64;
				amount -= amount % 64;
				if (player.getInventory().firstEmpty() == -1) {
					player.sendMessage(tag + ChatColor.RED + "Inventory full. " + ChatColor.GRAY + "Empty some slots then try again.");
					return true;
				}
			} while (amount > 64 || amount > 0);
			player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + materialName + " x" + totalWithdraw + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(totalWithdraw));
			return true;
		}
		player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
		return true;
	}

}
