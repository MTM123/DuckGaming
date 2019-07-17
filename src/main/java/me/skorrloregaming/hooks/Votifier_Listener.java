package me.skorrloregaming.hooks;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.Service;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import net.dv8tion.jda.core.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Votifier_Listener implements Listener {

	private final int WEBSITE_COUNT = 9;
	private final double MODIFIER = 1.2;

	private ConcurrentMap<Integer, String> services;

	public int getMonthlyVotes(String username, int year, int monthId) {
		if (Server.getMonthlyVoteConfig().getData().contains("config." + username + "." + year + "." + (monthId + 1)))
			return Server.getMonthlyVoteConfig().getData().getInt("config." + username + "." + year + "." + (monthId + 1));
		return 0;
	}

	public void addMonthlyVotes(String username, int year, int monthId, int amount) {
		int currentVotes = getMonthlyVotes(username, year, monthId);
		Server.getMonthlyVoteConfig().getData().set("config." + username + "." + year + "." + (monthId + 1), currentVotes + amount);
		Server.getMonthlyVoteConfig().saveData();
	}

	public long getLastVoteForService(String username, String service) {
		if (Server.getMonthlyVoteConfig().getData().contains("config." + username + "." + service))
			return Server.getMonthlyVoteConfig().getData().getLong("config." + username + "." + service);
		return 0;
	}

	public void setLastVoteForService(String username, long timestamp, String service) {
		Server.getMonthlyVoteConfig().getData().set("config." + username + "." + service, timestamp);
		Server.getMonthlyVoteConfig().saveData();
	}

	public String getServiceNameFromFriendly(Service service) {
		return services.get(service.ordinal());
	}

	public long getTimeDifference(String username, String service) {
		long timestamp = getLastVoteForService(username, service);
		if (timestamp == 0)
			return 0;
		long currentTime = System.currentTimeMillis();
		long diff = currentTime - timestamp;
		long reverseDiff = (1000 * 60 * 60 * 24) - diff;
		return reverseDiff / 1000;
	}

	public String getFriendlyTimeDifference(String username, String service) {
		long diff = getTimeDifference(username, service);
		boolean flag = false;
		if (diff < 0) {
			flag = true;
			diff = Math.abs(diff);
		}
		String friendly = Link$.formatTime(diff);
		if (flag)
			friendly = "-" + friendly;
		return friendly;
	}

	public long getMaximumTimeDiffForAllServices(String username) {
		long lastDiff = 0;
		for (Service service : Service.values()) {
			long diff = getTimeDifference(username, getServiceNameFromFriendly(service));
			if (diff < 0)
				diff = 0;
			if (diff > lastDiff)
				lastDiff = diff;
		}
		return lastDiff;
	}

	public void register() {
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
		services = new ConcurrentHashMap<>();
		services.put(0, "PlanetMinecraft.com");
		services.put(1, "Minecraft-MP.com");
		services.put(2, "MinecraftServers.org");
		services.put(3, "MinecraftServers.biz");
		services.put(4, "MCSL");
		services.put(5, "Minecraft-Server.net");
		services.put(6, "MinecraftServersList");
		services.put(7, "TopG.org");
		services.put(8, "Trackyserver.com");
		services.put(9, "/Top Minecraft Servers");
		Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), () -> {
			for (String id : Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
				OfflinePlayer player = Bukkit.getOfflinePlayer(id);
				if (player.hasPlayedBefore() || player.isOnline()) {
					long diff;
					if ((diff = getMaximumTimeDiffForAllServices(player.getName())) <= 0) {
						if (diff > -86400000) {
							if (!hasPlayerBeenPingedToday(player)) {
								updatePlayerPingedDate(player, new Date());
								boolean subscribed = Boolean.parseBoolean(LinkServer.getPlugin().getConfig().getString("config." + player.getUniqueId().toString() + ".subscribed", "true"));
								if (subscribed) {
									for (Member member : Server.getDiscordBot().getGuild().getMembers()) {
										if (member.getNickname() != null)
											if (member.getNickname().equals(player.getName())) {
												member.getUser().openPrivateChannel().queue((channel) ->
												{
													channel.sendMessage("It looks like you can vote and collect your daily jackpot now!").queue();
												});
											}
									}
								}
							}
						}
					}
				}

			}
		}, 1200L, 1200L);
	}

	public void updatePlayerPingedDate(OfflinePlayer player, Date date) {
		Server.getPlugin().getConfig().set("config." + player.getUniqueId().toString() + ".lastVotePing", date.getTime());
	}

	public boolean hasPlayerBeenPingedToday(OfflinePlayer player) {
		long lastPing = Server.getPlugin().getConfig().getLong("config." + player.getUniqueId().toString() + ".lastVotePing", -1);
		if (lastPing > -1) {
			Date date = new Date(lastPing);
			Date currentDate = new Date();
			if (date.getYear() == currentDate.getYear())
				if (date.getMonth() == currentDate.getMonth())
					if (date.getDay() == currentDate.getDay())
						return true;
		}
		return false;
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		onVote(event, true);
	}

	public void onVote(VotifierEvent event, boolean doJackpots) {
		Vote vote = event.getVote();
		String username = vote.getUsername();
		OfflinePlayer player = CraftGo.Player.getOfflinePlayer(username);
		if (player.hasPlayedBefore() || player.isOnline())
			username = player.getName();
		Logger.info("Votifier has received a vote for player " + username + " at " + event.getVote().getServiceName() + ".", true);
		boolean spoofed = false;
		ServerMinigame minigame = null;
		if (vote.getServiceName().startsWith("Spoofed:")) {
			spoofed = true;
			minigame = ServerMinigame.valueOf(vote.getServiceName().split("Spoofed:")[1].toString().toUpperCase());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		addMonthlyVotes(vote.getUsername(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		if (!spoofed)
			setLastVoteForService(username, System.currentTimeMillis(), event.getVote().getServiceName());
		if (player.hasPlayedBefore() || player.isOnline()) {
			if (doJackpots) {
				String message = "► " + ChatColor.GREEN + player.getName() + ChatColor.RESET + " has just voted and earned a jackpot.";
				Bukkit.broadcastMessage(message);
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT
				);
				LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
			} else {
				String message = "► " + ChatColor.GREEN + player.getName() + ChatColor.RESET + " has just voted for the server.";
				Bukkit.broadcastMessage(message);
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT
				);
				LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
			}
			double balance2 = EconManager.retrieveCash(player.getUniqueId(), "kitpvp");
			double ceil2 = balance2 / 25;
			double amountEarned2 = 50 + (ceil2 * 10);
			double balance1 = EconManager.retrieveCash(player.getUniqueId(), "factions");
			double ceil1 = balance1 / 30;
			double amountEarned1 = 500 + (ceil1 * 4.125);
			double balance3 = EconManager.retrieveCash(player.getUniqueId(), "skyblock");
			double ceil3 = balance3 / 30;
			double amountEarned3 = 500 + (ceil3 * 5);
			double balance4 = EconManager.retrieveCash(player.getUniqueId(), "survival");
			double ceil4 = balance4 / 100;
			double amountEarned4 = 2.5 + (ceil4 * 0.1);
			amountEarned1 *= MODIFIER;
			amountEarned2 *= MODIFIER;
			amountEarned3 *= MODIFIER;
			amountEarned1 /= WEBSITE_COUNT;
			if (amountEarned1 > 65000)
				amountEarned1 = 65000;
			amountEarned2 /= WEBSITE_COUNT;
			if (amountEarned2 > 1500)
				amountEarned2 = 1500;
			amountEarned3 /= WEBSITE_COUNT;
			if (amountEarned3 > 85000)
				amountEarned3 = 85000;
			amountEarned4 /= WEBSITE_COUNT;
			if (amountEarned4 > 50)
				amountEarned4 = 50;

			if (!doJackpots) {
				amountEarned1 = 0;
				amountEarned2 = 0;
				amountEarned3 = 0;
			}
			switch (Link$.getDonorRankId(player.getUniqueId())) {
				case -5:
					amountEarned1 *= 5;
					amountEarned2 *= 5;
					amountEarned3 *= 5;
					amountEarned4 *= 5;
					break;
				case -4:
					amountEarned1 *= 4;
					amountEarned2 *= 4;
					amountEarned3 *= 4;
					amountEarned4 *= 4;
					break;
				case -3:
					amountEarned1 *= 3;
					amountEarned2 *= 3;
					amountEarned3 *= 3;
					amountEarned4 *= 3;
					break;
				case -2:
					amountEarned1 *= 2;
					amountEarned2 *= 2;
					amountEarned3 *= 2;
					amountEarned4 *= 2;
					break;
			}
			if (!spoofed || (spoofed && minigame == ServerMinigame.KITPVP))
				EconManager.depositCash(player.getUniqueId(), amountEarned2, "kitpvp");
			if (!spoofed || (spoofed && minigame == ServerMinigame.FACTIONS))
				EconManager.depositCash(player.getUniqueId(), amountEarned1, "factions");
			if (!spoofed || (spoofed && minigame == ServerMinigame.SKYBLOCK))
				EconManager.depositCash(player.getUniqueId(), amountEarned3, "skyblock");
			DecimalFormat formatter = new DecimalFormat("###,###,###,###,###.##");
			if (player.isOnline()) {
				player.getPlayer().playSound(player.getPlayer().getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
				if (!spoofed || (spoofed && minigame == ServerMinigame.KITPVP))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + formatter.format(amountEarned2) + " in Kitpvp.");
				if (!spoofed || (spoofed && minigame == ServerMinigame.FACTIONS))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + formatter.format(amountEarned1) + " in Factions.");
				if (!spoofed || (spoofed && minigame == ServerMinigame.SKYBLOCK))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + formatter.format(amountEarned3) + " in Skyblock.");
				if (!spoofed || (spoofed && minigame == ServerMinigame.SURVIVAL))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + formatter.format(amountEarned4) + " in Survival.");
				$.playFirework(player.getPlayer().getLocation());
				Server.setLastVoteTime(System.currentTimeMillis());
			}
		} else {
			Logger.info(this.toString() + " has denied the vote since \"" + username + "\" is undefined.", true);
		}
	}
}