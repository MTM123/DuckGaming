package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
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
				Server.getBukkitTasks().add(new BukkitRunnable() {
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
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
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
				Server.getBukkitTasks().add(new BukkitRunnable() {
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
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
			} else if (args[0].equalsIgnoreCase("donator")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -2) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Donator" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.Factions.getDonatorKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Factions.getDonatorKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitDonator) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Factions.getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Factions.getDonatorKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.Factions.getDonatorKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Donator");
							}
							if (Server.Factions.getDonatorKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Factions.getDonatorKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Donator");
			} else if (args[0].equalsIgnoreCase("redstone")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -3) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Redstone" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.Factions.getRedstoneKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Factions.getRedstoneKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitRedstone) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Factions.getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Factions.getRedstoneKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.Factions.getRedstoneKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Redstone");
							}
							if (Server.Factions.getRedstoneKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Factions.getRedstoneKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Redstone");
			} else if (args[0].equalsIgnoreCase("obsidian")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -4) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Obsidian" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.Factions.getObsidianKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Factions.getObsidianKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitObsidian) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Factions.getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Factions.getObsidianKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.Factions.getObsidianKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Obsidian");
							}
							if (Server.Factions.getObsidianKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Factions.getObsidianKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Obsidian");
			} else if (args[0].equalsIgnoreCase("bedrock")) {
				if (Link$.getDonorRankId(player.getUniqueId()) > -5) {
					player.sendMessage(tag + ChatColor.GRAY + "You must have rank " + ChatColor.RED + "Bedrock" + ChatColor.GRAY + " to use this kit.");
					return true;
				}
				if (Server.Factions.getBedrockKitCooldown().containsKey(player.getUniqueId())) {
					player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + Server.Factions.getBedrockKitCooldown().get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
					return true;
				}
				for (ItemStack item : $.Factions.kitBedrock) {
					player.getInventory().addItem(item);
				}
				player.updateInventory();
				final Player fPlayer = player;
				final Plugin fPlugin = Server.getPlugin();
				if (!Server.Factions.getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
					Server.Factions.getBedrockKitCooldown().put(fPlayer.getUniqueId(), 60 * 15);
				}
				Server.getBukkitTasks().add(new BukkitRunnable() {
					int time = 60 * 15;

					public void run() {
						time--;
						Server.Factions.getBedrockKitCooldown().put(fPlayer.getUniqueId(), time);
						if (time <= 0) {
							Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
							if (!(checkPlayer == null)) {
								checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Bedrock");
							}
							if (Server.Factions.getBedrockKitCooldown().containsKey(fPlayer.getUniqueId())) {
								Server.Factions.getBedrockKitCooldown().remove(fPlayer.getUniqueId());
							}
							cancel();
						}
					}
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
				player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Bedrock");
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
						Server.getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
							@Override
							public void run() {
								Server.getDelayedTasks().remove(player.getUniqueId());
							}
						}, 20L));
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
					Server.getBukkitTasks().add(new BukkitRunnable() {
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
					}.runTaskTimer(Server.getPlugin(), 20L, 20L));
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
				Server.getBukkitTasks().add(new BukkitRunnable() {
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
				}.runTaskTimer(Server.getPlugin(), 20L, 20L));
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
