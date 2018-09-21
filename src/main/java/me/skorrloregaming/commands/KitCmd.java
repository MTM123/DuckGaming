package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class KitCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		String tag = $.getMinigameTag(player);
		if (Server.getSurvival().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (Server.Survival.getRecruitKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Survival.getRecruitKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Survival.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Survival.getRecruitKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Survival.getRecruitKitCooldown().put(fPlayer.getUniqueId(), 60 * 5);
				}
				new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.Survival.getRecruitKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (Server.Survival.getRecruitKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Survival.getRecruitKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L);
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (Server.getFactions().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (Server.Factions.getRecruitKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Factions.getRecruitKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Factions.getRecruitKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Factions.getRecruitKitCooldown().put(fPlayer.getUniqueId(), 60 * 5);
				}
				new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.Factions.getRecruitKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (Server.Factions.getRecruitKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Factions.getRecruitKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L);
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (Server.getKitpvp().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("starter")) {
				int upgradeCount = $.Kitpvp.getPreferredUpgrade(player);
				if (Server.Kitpvp.getStarterKitCooldown().containsKey(player.getUniqueId()) && upgradeCount > 0) {
					if (!Server.getDelayedTasks().contains(player.getUniqueId())) {
						Server.getDelayedTasks().add(player.getUniqueId());
						Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
							@Override
							public void run() {
								Server.getDelayedTasks().remove(player.getUniqueId());
							}
						}, 20L);
						player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Kitpvp.getStarterKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
						player.sendMessage(tag + ChatColor.GRAY + "To use the default kit instead, use this kit again.");
						return true;
					} else
						upgradeCount = 0;
				}
				for (ItemStack item : $.Kitpvp.getUpgradeClass(player, upgradeCount, false)) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				if (upgradeCount > 0) {
					final Player fPlayer = player;
					final Plugin fPlugin = Server.getPlugin();
					if (!Server.Kitpvp.getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
						Server.Kitpvp.getStarterKitCooldown().put(fPlayer.getUniqueId(), 60 * 1);
					}
					new BukkitRunnable() {
						int time = 60 * 1;

						public void run() {
							time--;
							Server.Kitpvp.getStarterKitCooldown().put(fPlayer.getUniqueId(), time);
							if (time <= 0) {
								Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
								if (!(checkPlayer == null)) {
									checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Starter");
								}
								if (Server.Kitpvp.getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
									Server.Kitpvp.getStarterKitCooldown().remove(fPlayer.getUniqueId());
								}
								cancel();
							}
						}
					}.runTaskTimer(Server.getPlugin(), 20L, 20L);
				}
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Starter #" + (upgradeCount + 1));
			} else if (args[0].equalsIgnoreCase("potions")) {
				if (Server.Kitpvp.getPotionsKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Kitpvp.getPotionsKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Kitpvp.kitPotions) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Kitpvp.getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Kitpvp.getPotionsKitCooldown().put(fPlayer.getUniqueId(), 60 * 5);
				}
				new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.Kitpvp.getPotionsKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Potions");
							}
							if (Server.Kitpvp.getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Kitpvp.getPotionsKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L);
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Potions");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		return true;
	}

}
