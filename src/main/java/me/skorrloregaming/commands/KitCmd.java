package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.skorrloregaming.*;

public class KitCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getKitpvp().contains(player.getUniqueId()) && !ServerGet.get().getFactions().contains(player.getUniqueId()) && !ServerGet.get().getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		String tag = $.getMinigameTag(player);
		if (ServerGet.get().getSurvival().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Survival.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).put(fPlayer.getUniqueId(), 60 * 5);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getRecruitKitCooldown(ServerMinigame.SURVIVAL).remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (ServerGet.get().getFactions().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).put(fPlayer.getUniqueId(), 60 * 5);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getRecruitKitCooldown(ServerMinigame.FACTIONS).remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else if (args[0].equalsIgnoreCase("donator")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -2) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Donator" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (ServerGet.get().getDonatorKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getDonatorKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitDonator) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getDonatorKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						ServerGet.get().getDonatorKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Donator");
							}
							if (ServerGet.get().getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getDonatorKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Donator");
			} else if (args[0].equalsIgnoreCase("redstone")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -3) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Redstone" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (ServerGet.get().getRedstoneKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getRedstoneKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRedstone) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getRedstoneKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						ServerGet.get().getRedstoneKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Redstone");
							}
							if (ServerGet.get().getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getRedstoneKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Redstone");
			} else if (args[0].equalsIgnoreCase("obsidian")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -4) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Obsidian" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (ServerGet.get().getObsidianKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getObsidianKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitObsidian) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getObsidianKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						ServerGet.get().getObsidianKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Obsidian");
							}
							if (ServerGet.get().getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getObsidianKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Obsidian");
			} else if (args[0].equalsIgnoreCase("bedrock")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -5) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Bedrock" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (ServerGet.get().getBedrockKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getBedrockKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitBedrock) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getBedrockKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						ServerGet.get().getBedrockKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Bedrock");
							}
							if (ServerGet.get().getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getBedrockKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Bedrock");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (ServerGet.get().getKitpvp().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("starter")) {
				int upgradeCount = $.Kitpvp.getPreferredUpgrade(player);
				if (ServerGet.get().getStarterKitCooldown().containsKey(player.getUniqueId()) && upgradeCount > 0) {
					if (!ServerGet.get().getDelayedTasks().contains(player.getUniqueId())) {
						ServerGet.get().getDelayedTasks().add(player.getUniqueId());
						ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
							@Override
							public void run() {
								ServerGet.get().getDelayedTasks().remove(player.getUniqueId());
							}
						}, 20L));
						player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getStarterKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
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
					final Plugin fPlugin = ServerGet.get().getPlugin();
					if (!ServerGet.get().getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
						ServerGet.get().getStarterKitCooldown().put(fPlayer.getUniqueId(), 60 * 1);
					}
					ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
						int time = 60 * 1;

						public void run() {
							time--;
							ServerGet.get().getStarterKitCooldown().put(fPlayer.getUniqueId(), time);
							if (time <= 0) {
								Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
								if (!(checkPlayer == null)) {
									checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Starter");
								}
								if (ServerGet.get().getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
									ServerGet.get().getStarterKitCooldown().remove(fPlayer.getUniqueId());
								}
								cancel();
							}
						}
					}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
				}
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Starter #" + (upgradeCount + 1));
			} else if (args[0].equalsIgnoreCase("potions")) {
				if (ServerGet.get().getPotionsKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + ServerGet.get().getPotionsKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Kitpvp.kitPotions) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = ServerGet.get().getPlugin();
				if (!ServerGet.get().getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
					ServerGet.get().getPotionsKitCooldown().put(fPlayer.getUniqueId(), 60 * 5);
				}
				ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						ServerGet.get().getPotionsKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Potions");
							}
							if (ServerGet.get().getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
								ServerGet.get().getPotionsKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
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
