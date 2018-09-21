package me.skorrloregaming.commands;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class PlaytimeReportCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			return true;
		} else {
			OfflinePlayer player = CraftGo.Player.getOfflinePlayer(args[0]);
			if (player.isOnline() || player.hasPlayedBefore()) {
				if (args.length == 0) {
					sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <month> <day>");
				}
				if (player.isOnline()) {
					Server.getPlaytimeManager().updatePlaytime(player.getPlayer());
				}
				int arg1 = -1;
				int arg2 = -1;
				try {
					if (args.length >= 2)
						arg1 = Integer.parseInt(args[1]);
				} catch (Exception ex) {
					sender.sendMessage("Invalid syntax for month argument, ignoring..");
				}
				try {
					if (args.length >= 3)
						arg2 = Integer.parseInt(args[2]);
				} catch (Exception ex) {
					sender.sendMessage("Invalid syntax for day argument, ignoring..");
				}
				if (arg1 == -1 && arg2 == -1) {
					if (sender instanceof Player) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(System.currentTimeMillis());
						Player p = (Player) sender;
						Server.getPlaytimeManager().openComplexInventory(p, player, calendar.get(Calendar.MONTH));
					} else {
						sender.sendMessage("Ordering will commence in about half a second..");
						Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
							@Override
							public void run() {
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(System.currentTimeMillis());
								long[] range = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, 0, 365);
								for (int i = 0; i < range.length; i++) {
									Calendar loopCalendar = Calendar.getInstance();
									loopCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
									long timePlayedInSeconds = range[i];
									loopCalendar.set(Calendar.DAY_OF_YEAR, i);
									loopCalendar.add(Calendar.SECOND, (int) timePlayedInSeconds);
									if (timePlayedInSeconds == 0)
										continue;
									sender.sendMessage(player.getName() + " played for " + $.formatTime((int) timePlayedInSeconds) + " on " + $.basicFormatCalendarTime(loopCalendar));
								}
								int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
								long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, dayOfYear - 6, dayOfYear + 1);
								long totalTimePlayedInSeconds = 0L;
								for (int i = 0; i < range7Day.length; i++)
									totalTimePlayedInSeconds += range7Day[i];
								sender.sendMessage(player.getName() + " played for " + $.formatTime((int) totalTimePlayedInSeconds) + " during the past 7 days.");
								sender.sendMessage("Ordering completed, stats should be shown above.");
							}
						}, 10L);
					}
				} else if (!(arg1 == -1) && !(arg2 == -1)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					Calendar gc = Calendar.getInstance();
					gc.set(Calendar.DAY_OF_MONTH, arg2);
					gc.set(Calendar.MONTH, arg1 - 1);
					gc.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
					long timePlayedInSeconds = Server.getPlaytimeManager().getStoredPlayerPlaytime(player, gc.get(Calendar.DAY_OF_YEAR));
					sender.sendMessage(player.getName() + " played for " + $.formatTime((int) timePlayedInSeconds) + " on " + $.basicFormatCalendarTime(gc));
					int dayOfYear = gc.get(Calendar.DAY_OF_YEAR);
					long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, dayOfYear - 6, dayOfYear + 1);
					long totalTimePlayedInSeconds = 0L;
					for (int i = 0; i < range7Day.length; i++)
						totalTimePlayedInSeconds += range7Day[i];
					sender.sendMessage(player.getName() + " played for " + $.formatTime((int) totalTimePlayedInSeconds) + " during the past 7 days.");
					sender.sendMessage("Ordering completed, stats should be shown above.");
				} else if (!(arg1 == -1)) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					Calendar gc = Calendar.getInstance();
					gc.set(Calendar.DAY_OF_MONTH, 0);
					gc.set(Calendar.MONTH, arg1 - 1);
					gc.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
					sender.sendMessage("Ordering entries for " + $.formatMonthId(arg1 - 1) + " " + calendar.get(Calendar.YEAR) + ".");
					sender.sendMessage("Ordering will commence in about half a second..");
					final int dayOfYear = gc.get(Calendar.DAY_OF_YEAR);
					final int exactDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
					Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

						@Override
						public void run() {
							long[] range = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, dayOfYear, dayOfYear + gc.getActualMaximum(Calendar.DAY_OF_MONTH));
							for (int i = 0; i < range.length; i++) {
								Calendar loopCalendar = Calendar.getInstance();
								loopCalendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
								long timePlayedInSeconds = range[i];
								loopCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear + i + 1);
								loopCalendar.add(Calendar.SECOND, (int) timePlayedInSeconds);
								if (timePlayedInSeconds == 0)
									continue;
								sender.sendMessage(player.getName() + " played for " + $.formatTime((int) timePlayedInSeconds) + " on " + $.basicFormatCalendarTime(loopCalendar));
							}
							long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, exactDayOfYear - 6, exactDayOfYear + 1);
							long totalTimePlayedInSeconds = 0L;
							for (int i = 0; i < range7Day.length; i++)
								totalTimePlayedInSeconds += range7Day[i];
							sender.sendMessage(player.getName() + " played for " + $.formatTime((int) totalTimePlayedInSeconds) + " during the past 7 days.");
							sender.sendMessage("Ordering completed, stats should be shown above.");
						}
					}, 10L);
				} else {
					sender.sendMessage("Invalid operation, please follow the syntax accordingly.");
				}
				return true;
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
		}
	}

}
