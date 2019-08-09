package me.skorrloregaming;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.Service;
import me.skorrloregaming.impl.ServicePriority;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.text.DecimalFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import me.skorrloregaming.*;

public class VoteManager {

	private final int WEBSITE_COUNT = 9;
	private final double MODIFIER = 1.2;

	public long getMidnight(long timestampOfVote) {
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(timestampOfVote);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		date.add(Calendar.DAY_OF_MONTH, 1);
		return date.getTimeInMillis();
	}

	public long getMidnightGreenwich(long timestampOfVote) {
		LocalTime midnight = LocalTime.MIDNIGHT;
		ZoneId zoneId = ZoneId.of("Etc/GMT");
		LocalDate today = Instant.ofEpochMilli(timestampOfVote).atZone(zoneId).toLocalDateTime().toLocalDate();
		LocalDateTime tomorrowMidnight = LocalDateTime.of(today, midnight).plusDays(1);
		return tomorrowMidnight.atZone(zoneId).toEpochSecond() * 1000;
	}

	public int getMonthlyVotes(String username, int year, int monthId) {
		if (ServerGet.get().getMonthlyVoteConfig().getData().contains("config." + username + "." + year + "." + (monthId + 1)))
			return ServerGet.get().getMonthlyVoteConfig().getData().getInt("config." + username + "." + year + "." + (monthId + 1));
		return 0;
	}

	public void addMonthlyVotes(String username, int year, int monthId, int amount) {
		int currentVotes = getMonthlyVotes(username, year, monthId);
		ServerGet.get().getMonthlyVoteConfig().getData().set("config." + username + "." + year + "." + (monthId + 1), currentVotes + amount);
		ServerGet.get().getMonthlyVoteConfig().saveData();
	}

	public long getLastVoteForService(String username, String service) {
		if (ServerGet.get().getMonthlyVoteConfig().getData().contains("config." + username + "." + service))
			return ServerGet.get().getMonthlyVoteConfig().getData().getLong("config." + username + "." + service);
		return 0;
	}

	public void setLastVoteForService(String username, long timestamp, String service) {
		ServerGet.get().getMonthlyVoteConfig().getData().set("config." + username + "." + service, timestamp);
		ServerGet.get().getMonthlyVoteConfig().saveData();
	}

	public long getTimeDifference(String username, String service, long arg0, boolean isEpoch) {
		if (isEpoch) {
			if (arg0 <= 0)
				return 0;
			long reverseDiff = arg0 - System.currentTimeMillis();
			return reverseDiff / 1000;
		} else {
			long currentTime = System.currentTimeMillis();
			long timestamp = getLastVoteForService(username, service);
			if (timestamp == 0)
				return 0;
			long diff = currentTime - timestamp;
			long reverseDiff = arg0 - diff;
			return reverseDiff / 1000;
		}
	}

	public String getFriendlyTimeDifference(String username, String service, long arg0, boolean isEpoch) {
		long diff = getTimeDifference(username, service, arg0, isEpoch);
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

	public long getMaximumTimeDiffForAllServices(String username, long arg0, boolean isEpoch) {
		long lastDiff = 0;
		for (Service service : Service.values()) {
			long diff = getTimeDifference(username, service.getName(), arg0, isEpoch);
			if (diff < 0)
				diff = 0;
			if (diff > lastDiff)
				lastDiff = diff;
		}
		return lastDiff;
	}

	public VoteManager() {
		ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskTimer(ServerGet.get().getPlugin(), () -> {
			for (String id : ServerGet.get().getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
				String username = ServerGet.get().getPlugin().getConfig().getString("config." + id + ".username");
				if (username == null || username.equals("null"))
					continue;
				for (String key : ServerGet.get().getDiscordVerifyConfig().getData().getConfigurationSection("verified").getKeys(false)) {
					String value = ServerGet.get().getDiscordVerifyConfig().getData().getString("verified." + key);
					if (value.equals(id)) {
						if (getMaximumTimeDiffForAllServices(username, ServicePriority.delay24hour.getDelay(), false) <= 0) {
							if (!hasPlayerBeenPingedToday(UUID.fromString(id))) {
								updatePlayerPingedDate(UUID.fromString(id), new Date());
								boolean subscribed = Boolean.parseBoolean(LinkServerGet.get().getPlugin().getConfig().getString("config." + id + ".subscribed", "true"));
								if (subscribed) {
									Logger.info("It looks like " + username + " can vote, they WILL be notified.");
									for (Member member : ServerGet.get().getDiscordBot().getGuild().getMembers()) {
										String discordUsername = member.getUser().getName();
										if (member.getNickname() != null)
											discordUsername = member.getNickname();
										if (discordUsername.equals(username)) {
											member.getUser().openPrivateChannel().queue((channel) ->
											{
												MessageBuilder messageBuilder = new MessageBuilder();
												messageBuilder.append("Greetings ");
												messageBuilder.append(member);
												messageBuilder.append(",\n");
												messageBuilder.append("It looks like you can vote and collect your daily jackpot now!\n");
												messageBuilder.append("You can vote and view your times at https://vote.skorrloregaming.com.\n");
												messageBuilder.append("If you would like to unsubscribe, feel free to type `/unsubscribe` in-game.");
												channel.sendMessage(messageBuilder.build()).queue();
											});
										}
									}
								} else {
									Logger.info("It looks like " + username + " can vote, they WILL NOT be notified.");
								}
							}
						}
					}
				}
			}
		}, 1200L, 1200L));
	}

	public void updatePlayerPingedDate(UUID id, Date date) {
		ServerGet.get().getPlugin().getConfig().set("config." + id.toString() + ".lastVotePing", date.getTime());
	}

	public boolean hasPlayerBeenPingedToday(UUID id) {
		long lastPing = ServerGet.get().getPlugin().getConfig().getLong("config." + id.toString() + ".lastVotePing", -1);
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
				ServerGet.get().getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT
				);
				LinkServerGet.get().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
			} else {
				String message = "► " + ChatColor.GREEN + player.getName() + ChatColor.RESET + " has just voted for the server.";
				Bukkit.broadcastMessage(message);
				ServerGet.get().getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT
				);
				LinkServerGet.get().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
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
				ServerGet.get().setLastVoteTime(System.currentTimeMillis());
			}
		} else {
			Logger.info(this.toString() + " has denied the vote since \"" + username + "\" is undefined.", true);
		}
	}
}
