package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
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

public class KitCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getInstance().getKitpvp().contains(player.getUniqueId()) && !Server.getInstance().getFactions().contains(player.getUniqueId()) && !Server.getInstance().getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		String tag = $.getMinigameTag(player);
		if (Server.getInstance().getSurvival().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Survival.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).put(fPlayer.getUniqueId(), 60 * 5);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getRecruitKitCooldown(ServerMinigame.SURVIVAL).remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (Server.getInstance().getFactions().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("recruit")) {
				if (Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRecruit) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).put(fPlayer.getUniqueId(), 60 * 5);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
							}
							if (Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getRecruitKitCooldown(ServerMinigame.FACTIONS).remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else if (args[0].equalsIgnoreCase("donator")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -2) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Donator" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.getInstance().getDonatorKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getDonatorKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitDonator) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getDonatorKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.getInstance().getDonatorKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Donator");
							}
							if (Server.getInstance().getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getDonatorKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Donator");
			} else if (args[0].equalsIgnoreCase("redstone")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -3) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Redstone" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.getInstance().getRedstoneKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getRedstoneKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRedstone) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getRedstoneKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.getInstance().getRedstoneKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Redstone");
							}
							if (Server.getInstance().getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getRedstoneKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Redstone");
			} else if (args[0].equalsIgnoreCase("obsidian")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -4) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Obsidian" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.getInstance().getObsidianKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getObsidianKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitObsidian) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getObsidianKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.getInstance().getObsidianKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Obsidian");
							}
							if (Server.getInstance().getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getObsidianKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Obsidian");
			} else if (args[0].equalsIgnoreCase("bedrock")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -5) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Bedrock" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.getInstance().getBedrockKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getBedrockKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitBedrock) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getBedrockKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.getInstance().getBedrockKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Bedrock");
							}
							if (Server.getInstance().getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getBedrockKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Bedrock");
			} else {
				player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
				player.performCommand("kits");
				return true;
			}
			return true;
		}
		if (Server.getInstance().getKitpvp().contains(player.getUniqueId())) {
			if (args.length == 0) {
				player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
				player.performCommand("kits");
				return true;
			}
			if (args[0].equalsIgnoreCase("starter")) {
				int upgradeCount = $.Kitpvp.getPreferredUpgrade(player);
				if (Server.getInstance().getStarterKitCooldown().containsKey(player.getUniqueId()) && upgradeCount > 0) {
					if (!Server.getInstance().getDelayedTasks().contains(player.getUniqueId())) {
						Server.getInstance().getDelayedTasks().add(player.getUniqueId());
						Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), new Runnable() {
							@Override
							public void run() {
								Server.getInstance().getDelayedTasks().remove(player.getUniqueId());
							}
						}, 20L));
						player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getStarterKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
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
					final Plugin fPlugin = Server.getInstance().getPlugin();
					if (!Server.getInstance().getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
						Server.getInstance().getStarterKitCooldown().put(fPlayer.getUniqueId(), 60 * 1);
					}
					Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
						int time = 60 * 1;

						public void run() {
							time--;
							Server.getInstance().getStarterKitCooldown().put(fPlayer.getUniqueId(), time);
							if (time <= 0) {
								Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
								if (!(checkPlayer == null)) {
									checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Starter");
								}
								if (Server.getInstance().getStarterKitCooldown().containsKey(fPlayer.getUniqueId())) {
									Server.getInstance().getStarterKitCooldown().remove(fPlayer.getUniqueId());
								}
								cancel();
							}
						}
					}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
				}
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Starter #" + (upgradeCount + 1));
			} else if (args[0].equalsIgnoreCase("potions")) {
				if (Server.getInstance().getPotionsKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.getInstance().getPotionsKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Kitpvp.kitPotions) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getInstance().getPlugin();
				if (!Server.getInstance().getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.getInstance().getPotionsKitCooldown().put(fPlayer.getUniqueId(), 60 * 5);
				}
				Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 5;

					public void run() {
						time--;
						Server.getInstance().getPotionsKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Potions");
							}
							if (Server.getInstance().getPotionsKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.getInstance().getPotionsKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getInstance().getPlugin(), 20L, 20L));
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
