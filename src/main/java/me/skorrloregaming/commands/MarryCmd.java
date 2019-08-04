package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import me.skorrloregaming.impl.MarriageGender;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import me.skorrloregaming.*;

public class MarryCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (args.length == 0) {
			player.performCommand("marry help");
		} else if (args.length > 0 && !(Bukkit.getPlayer(args[0]) == null)) {
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			boolean acceptMarry = false;
			for (Map.Entry<UUID, UUID> id : ServerGet.get().getMarriageRequests().entrySet()) {
				if (id.getKey().equals(targetPlayer.getUniqueId())) {
					if (id.getValue().equals(player.getUniqueId())) {
						ServerGet.get().getMarriageRequests().remove(id.getKey());
						acceptMarry = true;
					}
				}
				if (id.getValue().equals(targetPlayer.getUniqueId())) {
					ServerGet.get().getMarriageRequests().remove(id.getKey());
				}
			}
			if (ServerGet.get().getMarriageRequests().containsKey(player.getUniqueId()))
				ServerGet.get().getMarriageRequests().remove(player.getUniqueId());
			if (acceptMarry) {
				player.performCommand("marry divorce");
				int marriageId = $.Marriage.setPlayerMarriedPlayer(player, targetPlayer);
				$.Marriage.setPlayerMarriedPlayer(targetPlayer, player, marriageId);
				player.sendMessage(Link$.Legacy.tag + "Marriage request accepted.");
				targetPlayer.sendMessage(Link$.Legacy.tag + "You are now married to " + ChatColor.YELLOW + targetPlayer.getName());
				targetPlayer.sendMessage(Link$.Legacy.tag + ChatColor.YELLOW + player.getName() + ChatColor.RESET + " has accepted your proposal.");
				targetPlayer.sendMessage(Link$.Legacy.tag + "You are now married to " + ChatColor.YELLOW + player.getName());
			} else {
				ServerGet.get().getMarriageRequests().put(player.getUniqueId(), targetPlayer.getUniqueId());
				player.sendMessage(Link$.Legacy.tag + "Marriage proposal sent to " + ChatColor.YELLOW + targetPlayer.getName() + ChatColor.RESET + ".");
				targetPlayer.sendMessage(Link$.Legacy.tag + "Marriage proposal received from " + ChatColor.YELLOW + player.getName() + ChatColor.RESET + ".");
				targetPlayer.sendMessage(Link$.Legacy.tag + "To accept, type " + ChatColor.YELLOW + "/marry " + player.getName() + ChatColor.RESET + " in chat.");
			}
		} else {
			if (args[0].equalsIgnoreCase("help")) {
				player.sendMessage(ChatColor.RED + "------------ " + ChatColor.BOLD + "Marriage Commands " + ChatColor.RESET + ChatColor.RED + "------------");
				player.sendMessage(ChatColor.RED + "/marry gender <gender> " + ChatColor.RESET + "- " + ChatColor.GRAY + "Set your perferred gender.");
				player.sendMessage(ChatColor.RED + "/marry <player> " + ChatColor.RESET + "- " + ChatColor.GRAY + "Request marry target player.");
				player.sendMessage(ChatColor.RED + "/marry filter <on/off> " + ChatColor.RESET + "- " + ChatColor.GRAY + "Turn on or off swear filter.");
				player.sendMessage(ChatColor.RED + "/marry pvp <on/off> " + ChatColor.RESET + "- " + ChatColor.GRAY + "Turn on or off married pvp.");
				player.sendMessage(ChatColor.RED + "/marry sethome " + ChatColor.RESET + "- " + ChatColor.GRAY + "Assign the marriage home.");
				player.sendMessage(ChatColor.RED + "/marry home " + ChatColor.RESET + "- " + ChatColor.GRAY + "Teleport to the marriage home.");
				player.sendMessage(ChatColor.RED + "/marry chat " + ChatColor.RESET + "- " + ChatColor.GRAY + "Enter or leave marriage chat.");
				player.sendMessage(ChatColor.RED + "/marry info " + ChatColor.RESET + "- " + ChatColor.GRAY + "Information about the marriage.");
				player.sendMessage(ChatColor.RED + "/marry divorce " + ChatColor.RESET + "- " + ChatColor.GRAY + "Divorce your current partner.");
			} else if (args[0].equalsIgnoreCase("gender") && args.length > 1) {
				int marriageId = $.Marriage.getPlayerMarriageId(player);
				if (marriageId > 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You cannot change gender while married.");
					return true;
				}
				String gender = args[1].toLowerCase();
				if (gender.equals("male") || gender.equals("boy") || gender.equals("man")) {
					$.Marriage.setPlayerGender(player, MarriageGender.MALE);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now considered " + ChatColor.RED + "Male" + ChatColor.GRAY + ".");
				} else if (gender.equals("female") || gender.equals("girl") || gender.equals("woman")) {
					$.Marriage.setPlayerGender(player, MarriageGender.FEMALE);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now considered " + ChatColor.RED + "Female" + ChatColor.GRAY + ".");
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please specify either " + ChatColor.RED + "Male" + ChatColor.GRAY + " or " + ChatColor.RED + "Female" + ChatColor.GRAY + ".");
				}
			} else if (args[0].equalsIgnoreCase("filter") && args.length > 1) {
				String value = args[1].toLowerCase();
				if (value.equals("enable") || value.equals("on")) {
					$.Marriage.setPlayerSwearFilter(player, true);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You can no longer bypass the filter.");
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "This filter setting applies only to marry chat.");
				} else if (value.equals("disable") || value.equals("off")) {
					$.Marriage.setPlayerSwearFilter(player, false);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You can now bypass the filter.");
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Just please remember the age rating of this game.");
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "This filter setting applies only to marry chat.");
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please specify either " + ChatColor.RED + "on" + ChatColor.GRAY + " or " + ChatColor.RED + "off" + ChatColor.GRAY + ".");
				}
			} else if (args[0].equalsIgnoreCase("pvp") && args.length > 1) {
				String value = args[1].toLowerCase();
				if (value.equals("enable") || value.equals("on")) {
					$.Marriage.setPlayerMarriedPvp(player, true);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You can now attack your partner.");
				} else if (value.equals("disable") || value.equals("off")) {
					$.Marriage.setPlayerMarriedPvp(player, true);
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You can no longer attack your partner.");
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please specify either " + ChatColor.RED + "on" + ChatColor.GRAY + " or " + ChatColor.RED + "off" + ChatColor.GRAY + ".");
				}
			} else if (args[0].equalsIgnoreCase("sethome")) {
				int marriageId = $.Marriage.getPlayerMarriageId(player);
				if (marriageId == 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are currently not married to anyone.");
					return true;
				}
				ServerMinigame minigame = $.getMinigameFromWorld(player.getWorld());
				if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.SKYFIGHT || minigame == ServerMinigame.UNKNOWN) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "You cannot set a home in this minigame.");
					return true;
				}
				String base = "homes." + marriageId;
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".world", player.getWorld().getName());
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".x", player.getLocation().getX());
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".y", player.getLocation().getY());
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".z", player.getLocation().getZ());
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".yaw", (int) player.getLocation().getYaw());
				ServerGet.get().getMarriageHomesConfig().getData().set(base + ".pitch", (int) player.getLocation().getPitch());
				ServerGet.get().getMarriageHomesConfig().saveData();
				player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have set your home on this server.");
			} else if (args[0].equalsIgnoreCase("home")) {
				int marriageId = $.Marriage.getPlayerMarriageId(player);
				if (marriageId == 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are currently not married to anyone.");
					return true;
				}
				String base = "homes." + marriageId;
				World world = ServerGet.get().getPlugin().getServer().getWorld(ServerGet.get().getMarriageHomesConfig().getData().getString(base + ".world"));
				if ($.getMinigameFromWorld(world) == $.getMinigameFromWorld(player.getWorld())) {
					double x = ServerGet.get().getMarriageHomesConfig().getData().getDouble(base + ".x");
					double y = ServerGet.get().getMarriageHomesConfig().getData().getDouble(base + ".y");
					double z = ServerGet.get().getMarriageHomesConfig().getData().getDouble(base + ".z");
					float yaw = (float) ServerGet.get().getMarriageHomesConfig().getData().getDouble(base + ".yaw");
					float pitch = (float) ServerGet.get().getMarriageHomesConfig().getData().getDouble(base + ".pitch");
					Location homeLocation = new Location(world, x, y, z, yaw, pitch);
					DelayedTeleport dt = new DelayedTeleport(player, ServerGet.get().getTeleportationDelay(), homeLocation, false);
					ServerGet.get().getBukkitTasks().add(dt.runTaskTimer(ServerGet.get().getPlugin(), 4, 4));
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The definied home is in another minigame.");
				}
			} else if (args[0].equalsIgnoreCase("chat")) {
				if (ServerGet.get().getMarriageChatPlayers().contains(player.getUniqueId())) {
					ServerGet.get().getMarriageChatPlayers().remove(player.getUniqueId());
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have left the marriage chat.");
				} else {
					ServerGet.get().getMarriageChatPlayers().add(player.getUniqueId());
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have entered the marriage chat.");
				}
			} else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("list")) {
				int marriageId = $.Marriage.getPlayerMarriageId(player);
				if (marriageId == 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are currently not married to anyone.");
					return true;
				}
				OfflinePlayer marriagePartner = $.Marriage.getMarriedOfflinePlayer(player);
				player.sendMessage(Link$.Legacy.tag + "/ Marriage for player " + ChatColor.YELLOW + player.getName());
				player.sendMessage(Link$.Legacy.tag + "Marriage Id: " + ChatColor.YELLOW + marriageId);
				player.sendMessage(Link$.Legacy.tag + "Marriage Partner: " + ChatColor.YELLOW + marriagePartner.getName());
				player.sendMessage(Link$.Legacy.tag + "Partner Gender: " + ChatColor.YELLOW + WordUtils.capitalize($.Marriage.getPlayerGender(marriagePartner).toString().toLowerCase()));
				player.sendMessage(Link$.Legacy.tag + "Your Gender: " + ChatColor.YELLOW + WordUtils.capitalize($.Marriage.getPlayerGender(player).toString().toLowerCase()));
			} else if (args[0].equalsIgnoreCase("divorce")) {
				int marriageId = $.Marriage.getPlayerMarriageId(player);
				if (marriageId == 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are currently not married to anyone.");
					return true;
				}
				OfflinePlayer marriedPlayer = $.Marriage.getMarriedOfflinePlayer(player);
				if (marriedPlayer.isOnline()) {
					marriedPlayer.getPlayer().sendMessage(Link$.Legacy.tag + ChatColor.YELLOW + player.getName() + ChatColor.RESET + " has broke up with you.");
				}
				$.Marriage.setPlayerMarriedPlayer(marriedPlayer, null);
				$.Marriage.setPlayerMarriedPlayer(player, null);
				ServerGet.get().getMarriageHomesConfig().getData().set("homes." + marriageId, null);
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have broke up with your partner.");
			} else {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "That marriage command could not be found.");
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " help");
			}
		}
		return true;
	}

}
