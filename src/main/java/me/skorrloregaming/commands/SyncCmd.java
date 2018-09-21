package me.skorrloregaming.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class SyncCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> [-f]");
			return true;
		}
		OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
		if ((args.length > 1 && (args[1].equalsIgnoreCase("-f") || args[1].equalsIgnoreCase("-force") || args[1].equalsIgnoreCase("-c") || args[1].equalsIgnoreCase("-code"))) || targetPlayer.hasPlayedBefore() || targetPlayer.isOnline()) {
			String path = "sync." + args[0];
			int codeInputIndex = 3;
			if (args.length > 1 && (args[1].equalsIgnoreCase("-c") || args[1].equalsIgnoreCase("-code")))
				codeInputIndex--;
			if (Server.getPlugin().getConfig().contains(path) && args.length > codeInputIndex && (args[codeInputIndex - 1].equalsIgnoreCase("-c") || args[codeInputIndex - 1].equalsIgnoreCase("-code"))) {
				if (args[codeInputIndex].toString().equals((Server.getPlugin().getConfig().getInt(path + ".id") + "").toString())) {
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified account is now synced with this account. Your username and uuid will be the shared between both accounts, which means that you cannot login with both accounts at the same time.");
					Server.getPlugin().getConfig().set(path + ".est", true);
					if (targetPlayer.isOnline()) {
						targetPlayer.getPlayer().kickPlayer(player.getName() + " has synced with your account, please rejoin for the changes to take effect.");
					}
				} else {
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified sync code is invalid for the specified sync target, make sure you entered the sync code correctly.");
				}
			} else {
				Server.getPlugin().getConfig().set(path + ".name", player.getName());
				Server.getPlugin().getConfig().set(path + ".uuid", player.getUniqueId().toString());
				Server.getPlugin().getConfig().set(path + ".est", false);
				int id = Integer.parseInt((Math.abs(UUID.randomUUID().hashCode() * 10_000) + "").substring(0, 5));
				Server.getPlugin().getConfig().set(path + ".id", id);
				if (targetPlayer.isOnline()) {
					Server.getPlugin().getConfig().set(path + ".suppress", true);
					targetPlayer.getPlayer().sendMessage($.Legacy.tag + ChatColor.RED + player.getName() + ChatColor.GRAY + " would like you to sync your account with his/her account. If you accept, your username will be changed to match his/her username. This means that you two will not be able to play at the same time. If you want to accept this sync request, have " + player.getName() + " type the following command: /sync " + targetPlayer.getName() + " -f -c " + id + ". To decline this request, you can simply ignore this request.");
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Almost done. " + ChatColor.GRAY + "You will need to type a command that was just given to the target player . This is a security measure to prevent unauthorized access to accounts.");
				} else {
					Server.getPlugin().getConfig().set(path + ".suppress", false);
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Warning. " + ChatColor.GRAY + "The target player is not online at this time. If you are syncing with a pocket edition client, the client must be actively connected to the server in order to sync your account with the client.");
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Almost done. " + ChatColor.GRAY + "You will need to type a command that will be given to the target player the next time he/she connects to the server. This is a security measure to prevent unauthorized access to accounts.");
				}
			}
		} else {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player was not found on record. Usernames are case-sensitive, so make sure you typed it correctly. This is a safe-guard to help people sync their accounts correctly. If you typed the username correctly, you can force the account to sync by typing " + ChatColor.RED + "/" + label + " " + args[0] + " -f" + ChatColor.GRAY + ".");
		}
		return true;
	}

}
