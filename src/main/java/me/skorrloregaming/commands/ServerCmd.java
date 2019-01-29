package me.skorrloregaming.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (args.length == 0) {
			player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Syntax " + ChatColor.GOLD + "/" + label + " <name>");
			player.performCommand("servers");
			return true;
		}
		if ("kitpvp".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.KITPVP)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getKitpvp().contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterKitpvp(player, false, false);
		} else if ("factions".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.FACTIONS)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getFactions().contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterFactions(player, false, false);
		} else if ("survival".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getSurvival().contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterSurvival(player, false, false);
		} else if ("skyfight".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.SKYFIGHT)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getSkyfight().containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterSkyfight(player, false, false);
		} else if ("creative".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.CREATIVE)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getCreative().contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterCreative(player, false, false);
		} else if ("skyblock".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.SKYBLOCK)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			if (Server.getSkyblock().contains(player.getUniqueId())) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
				return true;
			}
			Server.getInstance().enterSkyblock(player, false, false);
		} else if ("prison".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.PRISON)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("prison");
			player.sendPluginMessage(Server.getPlugin(), "BungeeCord", out.toByteArray());
		} else if ("dated".startsWith(args[0].toLowerCase())) {
			if (!$.isMinigameEnabled(ServerMinigame.DATED)) {
				player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
				return true;
			}
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("dated");
			player.sendPluginMessage(Server.getPlugin(), "BungeeCord", out.toByteArray());
		} else {
			player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
			player.performCommand("servers");
		}
		return true;
	}

}
