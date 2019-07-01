package me.skorrloregaming.hooks;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Calendar;

public class Votifier_Listener implements Listener {

	private final int WEBSITE_COUNT = 9;
	private final double MODIFIER = 1.2;

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

	public void register() {
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		onVote(event, true);
	}

	public void onVote(VotifierEvent event, boolean doJackpots) {
		Vote vote = event.getVote();
		String username = vote.getUsername();
		OfflinePlayer player = CraftGo.Player.getOfflinePlayer(username);
		Logger.info("Votifier has received a vote for player " + username + ".", true);
		boolean spoofed = false;
		ServerMinigame minigame = null;
		if (vote.getServiceName().startsWith("Spoofed:")) {
			spoofed = true;
			minigame = ServerMinigame.valueOf(vote.getServiceName().split("Spoofed:")[1].toString().toUpperCase());
		}
		if (player.hasPlayedBefore() || player.isOnline()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			addMonthlyVotes(vote.getUsername(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
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
			int ceil2 = (int) Math.ceil(balance2 / 25);
			int amountEarned2 = (int) (50 + (ceil2 * 10));
			double balance1 = EconManager.retrieveCash(player.getUniqueId(), "factions");
			int ceil1 = (int) Math.ceil(balance1 / 30);
			int amountEarned1 = (int) (500 + (ceil1 * 4.125));
			double balance3 = EconManager.retrieveCash(player.getUniqueId(), "skyblock");
			int ceil3 = (int) Math.ceil(balance3 / 30);
			int amountEarned3 = (int) (500 + (ceil3 * 5));
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
					break;
				case -4:
					amountEarned1 *= 4;
					amountEarned2 *= 4;
					amountEarned3 *= 4;
					break;
				case -3:
					amountEarned1 *= 3;
					amountEarned2 *= 3;
					amountEarned3 *= 3;
					break;
				case -2:
					amountEarned1 *= 2;
					amountEarned2 *= 2;
					amountEarned3 *= 2;
					break;
			}
			if (!spoofed || (spoofed && minigame == ServerMinigame.KITPVP))
				EconManager.depositCash(player.getUniqueId(), amountEarned2, "kitpvp");
			if (!spoofed || (spoofed && minigame == ServerMinigame.FACTIONS))
				EconManager.depositCash(player.getUniqueId(), amountEarned1, "factions");
			if (!spoofed || (spoofed && minigame == ServerMinigame.SKYBLOCK))
				EconManager.depositCash(player.getUniqueId(), amountEarned3, "skyblock");
			if (player.isOnline()) {
				player.getPlayer().playSound(player.getPlayer().getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
				if (!spoofed || (spoofed && minigame == ServerMinigame.KITPVP))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + amountEarned2 + " in Kitpvp.");
				if (!spoofed || (spoofed && minigame == ServerMinigame.FACTIONS))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + amountEarned1 + " in Factions.");
				if (!spoofed || (spoofed && minigame == ServerMinigame.SKYBLOCK))
					player.getPlayer().sendMessage(ChatColor.GREEN + "Jackpot." + ChatColor.RESET + " You just voted and earned $" + amountEarned3 + " in Skyblock.");
				$.playFirework(player.getPlayer().getLocation());
				Server.setLastVoteTime(System.currentTimeMillis());
			}
		} else {
			Logger.info(this.toString() + " has denied the vote since \"" + username + "\" is undefined.", true);
		}
	}
}