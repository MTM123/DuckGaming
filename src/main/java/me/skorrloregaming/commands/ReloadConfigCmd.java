package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.*;

public class ReloadConfigCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		ServerGet.get().getDiscordVerifyConfig().reloadData();
		ServerGet.get().getChatItemConfig().reloadData();
		ServerGet.get().getBanConfig().reloadData();
		ServerGet.get().getWarpConfig().reloadData();
		ServerGet.get().getSignConfig().reloadData();
		ServerGet.get().getFactionsConfig().reloadData();
		ServerGet.get().getShoppeConfig().reloadData();
		ServerGet.get().getMarriageHomesConfig().reloadData();
		ServerGet.get().getNpcConfig().reloadData();
		ServerGet.get().getSpawnerConfig().reloadData();
		new LinkServerGet().get().getInstance().getUUIDCache().reloadData();
		new LinkServerGet().get().getInstance().getGeolocationCache().reloadData();
		ServerGet.get().getSessionManager().sessionConfig.reloadData();
		ServerGet.get().getMonthlyVoteConfig().reloadData();
		if (!(ServerGet.get().getInstance().chatitem == null))
			ChatItem.reload(null);
		ServerGet.get().getInstance().garbageCollector.run();
		ServerGet.get().getInstance().reload();
		sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Server configuration successfully reloaded.");
		return true;
	}

}
