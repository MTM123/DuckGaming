package me.skorrloregaming.commands;

import java.util.Calendar;

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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				Server.getPlaytimeManager().updatePlaytime(player);
				String playtime = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(player));
				String playtimeToday = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(player, calendar.get(Calendar.DAY_OF_YEAR)));
				Calendar currentTime = Calendar.getInstance();
				currentTime.setTimeInMillis(System.currentTimeMillis());
				final int exactDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);
				long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, exactDayOfYear - 6, exactDayOfYear + 1);
				long totalTimePlayedInSeconds = 0L;
				for (int i = 0; i < range7Day.length; i++)
					totalTimePlayedInSeconds += range7Day[i];
				String day7total = $.formatTime((int) totalTimePlayedInSeconds);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime today: " + ChatColor.RED + playtimeToday);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Playtime during the past 7 days: " + ChatColor.RED + day7total);
				return true;
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
				return true;
			}
		} else {
			OfflinePlayer tp = CraftGo.Player.getOfflinePlayer(args[0]);
			if (!tp.isOnline() && tp.hasPlayedBefore()) {
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
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + tp.getName());
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime today: " + ChatColor.RED + playtimeToday);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Playtime during the past 7 days: " + ChatColor.RED + day7total);
			} else if (tp.isOnline()) {
				Server.getPlaytimeManager().updatePlaytime(tp.getPlayer());
				String playtime = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(tp.getPlayer()));
				String playtimeToday = $.formatTime((int) Server.getPlaytimeManager().getStoredPlayerPlaytime(tp.getPlayer(), calendar.get(Calendar.DAY_OF_YEAR)));
				Calendar currentTime = Calendar.getInstance();
				currentTime.setTimeInMillis(System.currentTimeMillis());
				final int exactDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);
				long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(tp, exactDayOfYear - 6, exactDayOfYear + 1);
				long totalTimePlayedInSeconds = 0L;
				for (int i = 0; i < range7Day.length; i++)
					totalTimePlayedInSeconds += range7Day[i];
				String day7total = $.formatTime((int) totalTimePlayedInSeconds);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + tp.getName());
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Approximate playtime today: " + ChatColor.RED + playtimeToday);
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Playtime during the past 7 days: " + ChatColor.RED + day7total);
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			return true;
		}
	}

}
