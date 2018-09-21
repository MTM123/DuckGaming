package me.skorrloregaming.commands;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.VotifierSession.ProtocolVersion;
import me.skorrloregaming.impl.Vote;

public class SpoofVoteCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			$.playLackPermissionMessage(sender);
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <count> [minigame] [doJackpots]");
			return true;
		} else {
			OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			if (!targetPlayer.isOnline() && !targetPlayer.hasPlayedBefore()) {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			} else {
				int count = Integer.parseInt(args[1]);
				String minigame = "";
				String serviceName = Server.class.getName();
				boolean doJackpots = true;
				if (args.length >= 3) {
					minigame = args[2].toLowerCase();
					serviceName = "Spoofed:" + minigame;
				}
				if (args.length >= 4) {
					doJackpots = Boolean.parseBoolean(args[3].toLowerCase());
				}
				for (int i = 0; i < count; i++) {
					Server.getVoteListener().onVote(new Vote(serviceName, targetPlayer.getName(), "0.0.0.0", new Date().toString(), System.currentTimeMillis()), ProtocolVersion.TWO, doJackpots);
				}
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + count + " votes have been spoofed.");
				return true;
			}
		}
	}

}
