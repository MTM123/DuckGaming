package me.skorrloregaming.commands;

import java.util.Calendar;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class PlaytimeCmd implements CommandExecutor {

	public void execute(CommandSender sender, OfflinePlayer tp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (tp.isOnline())
			Server.getPlaytimeManager().updatePlaytime(tp.getPlayer());
		if ((!tp.isOnline() && tp.hasPlayedBefore()) || tp.isOnline()) {
			String playtime = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(tp));
			String playtimeToday = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(tp, calendar.get(Calendar.DAY_OF_YEAR)));
			Calendar currentTime = Calendar.getInstance();
			currentTime.setTimeInMillis(System.currentTimeMillis());
			final int exactDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);
			long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(tp, exactDayOfYear - 6, exactDayOfYear + 1);
			long totalTimePlayedInSeconds = 0L;
			for (int i = 0; i < range7Day.length; i++)
				totalTimePlayedInSeconds += range7Day[i];
			String day7total = $.formatTime((int) totalTimePlayedInSeconds);
			String path = "config." + tp.getUniqueId().toString();
			long joined = Long.parseLong(Server.getPlugin().getConfig().getString(path + ".joined.value", System.currentTimeMillis() + ""));
			boolean isJoinedAccurate = Boolean.parseBoolean(Server.getPlugin().getConfig().getString(path + "joined.inaccurate", "true"));
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + tp.getName());
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime today: " + ChatColor.RED + playtimeToday);
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Playtime during the past 7 days: " + ChatColor.RED + day7total);
			if (isJoinedAccurate) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Joined: " + ChatColor.RED + new Date(joined));
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Joined: " + ChatColor.RED + "No accurate date available");
			}
		} else {
			sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				execute(sender, (OfflinePlayer) sender);
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			}
		} else {
			execute(sender, CraftGo.Player.getOfflinePlayer(args[0]));
		}
		return true;
	}

}
