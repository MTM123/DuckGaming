package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.skorrloregaming.*;

public class SayCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2 || !(args[0].charAt(0) == '/')) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <id> <message>");
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				if (args[0].split("/")[1].equals(".")) {
					if (Link$.isPrefixedRankingEnabled()) {
						String processedMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + ServerGet.get().getLastKnownHubWorld() + ChatColor.GRAY + "] " + ChatColor.WHITE + $.consoleTag + "Server" + ChatColor.WHITE + " " + '\u00BB' + " " + sb.toString();
						Bukkit.broadcastMessage(processedMessage);
						ServerGet.get().getDiscordBot().broadcast(
								"**Console** Server " + '\u00BB' + " " + sb.toString()
								, Channel.SERVER_CHAT
						);
						LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(processedMessage).build());
					} else {
						String processedMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + ServerGet.get().getLastKnownHubWorld() + ChatColor.GRAY + "] " + ChatColor.RED + "Server" + ChatColor.WHITE + " " + '\u00BB' + " " + sb.toString();
						Bukkit.broadcastMessage(processedMessage);
						ServerGet.get().getDiscordBot().broadcast(
								"**Server** " + '\u00BB' + " " + sb.toString()
								, Channel.SERVER_CHAT
						);
						LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(processedMessage).build());
					}
				} else {
					OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0].split("/")[1]);
					Player checkPlayer = Bukkit.getPlayer(args[0].split("/")[1]);
					String world = ServerGet.get().getLastKnownHubWorld();
					String message = sb.toString();
					if (!(checkPlayer == null)) {
						world = checkPlayer.getWorld().getName();
					}
					if (!op.hasPlayedBefore() && !op.isOnline()) {
						sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					} else {
						if (message.startsWith("/") && op.isOnline()) {
							PlayerCommandPreprocessEvent commandEvent = new PlayerCommandPreprocessEvent(op.getPlayer(), message);
							Bukkit.getServer().getPluginManager().callEvent(commandEvent);
							if (!commandEvent.isCancelled())
								op.getPlayer().performCommand(message.substring(1));
						} else {
							String rankName = WordUtils.capitalize(Link$.toRankDisplayName(Link$.getDiscordRank(op.getUniqueId())));
							if (rankName.equals("Youtube"))
								rankName = "YouTube";
							if (Link$.isPrefixedRankingEnabled()) {
								String processedMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.WHITE + Link$.getFlashPlayerDisplayName(op) + ChatColor.WHITE + " " + '\u00BB' + " " + message;
								Bukkit.broadcastMessage(processedMessage);
								ServerGet.get().getDiscordBot().broadcast(
										"**" + rankName + "** " + op.getName() + " " + '\u00BB' + " " + LinkServerGet.get().getInstance().getAntiCheat().processAntiSwear(op, message)
										, Channel.SERVER_CHAT
								);
								LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(processedMessage).build());
							} else {
								String processedMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.WHITE + op.getName() + ChatColor.WHITE + " " + '\u00BB' + " " + message;
								Bukkit.broadcastMessage(processedMessage);
								ServerGet.get().getDiscordBot().broadcast(
										"**" + op.getName() + "** " + '\u00BB' + " " + LinkServerGet.get().getInstance().getAntiCheat().processAntiSwear(op, message)
										, Channel.SERVER_CHAT
								);
								LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(processedMessage).build());
							}
						}
					}
				}
			}
		} else {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
