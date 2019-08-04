package me.skorrloregaming.commands;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Date;

import me.skorrloregaming.*;

public class SpoofVoteCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <count> [minigame?] [service]");
			return true;
		} else {
			OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			if (!targetPlayer.isOnline() && !targetPlayer.hasPlayedBefore()) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			} else {
				int count = Integer.parseInt(args[1]);
				String minigame = "";
				String serviceName = Server.class.getName();
				boolean doJackpots = true;
				if (args.length >= 3) {
					minigame = args[2].toLowerCase();
					if (args.length >= 4) {
						serviceName = args[3];
					} else {
						serviceName = "Spoofed:" + minigame;
					}
				}
				for (int i = 0; i < count; i++) {
					Vote vote = new Vote(serviceName, targetPlayer.getName(), "0.0.0.0", new Date().toString());
					VotifierEvent event = new VotifierEvent(vote);
					Bukkit.getPluginManager().callEvent(event);
				}
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + count + " votes have been spoofed.");
				return true;
			}
		}
	}
}

