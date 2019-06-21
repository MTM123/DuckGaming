package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		Server.getDiscordVerifyConfig().reloadData();
		Server.getChatItemConfig().reloadData();
		Server.getBanConfig().reloadData();
		Server.getWarpConfig().reloadData();
		Server.getSignConfig().reloadData();
		Server.getFactionsConfig().reloadData();
		Server.getShoppeConfig().reloadData();
		Server.getMarriageHomesConfig().reloadData();
		Server.getNpcConfig().reloadData();
		Server.getSpawnerConfig().reloadData();
		LinkServer.getInstance().getUUIDCache().reloadData();
		LinkServer.getInstance().getGeolocationCache().reloadData();
		Server.getSessionManager().sessionConfig.reloadData();
		Server.getMonthlyVoteConfig().reloadData();
		if (!(Server.getInstance().chatitem == null))
			ChatItem.reload(null);
		Server.getInstance().garbageCollector.run();
		Server.getInstance().reload();
		sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Server configuration successfully reloaded.");
		return true;
	}

}
