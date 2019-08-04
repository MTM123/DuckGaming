package me.skorrloregaming.runnable;

import me.skorrloregaming.*;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.*;

import me.skorrloregaming.*;

public class GCandAutoDemotion implements Runnable {

	public void processPlayerData() {
		String[] keys = ServerGet.get().getPlugin().getConfig().getConfigurationSection("config").getKeys(false).toArray(new String[0]);
		for (String id : keys) {
			String configPath = "config." + id;
			OfflinePlayer player = CraftGo.Player.getOfflinePlayer(UUID.fromString(id));
			if (!player.hasPlayedBefore() && !player.isOnline()) {
				if (ServerGet.get().getPlugin().getConfig().contains(configPath + ".username")) {
					String username = ServerGet.get().getPlugin().getConfig().getString(configPath + ".username");
					OfflinePlayer player2 = CraftGo.Player.getOfflinePlayer(username);
					if (!player2.hasPlayedBefore() && !player.isOnline()) {
						ServerGet.get().getPlugin().getConfig().set(configPath, null);
						ServerGet.get().getPlugin().getConfig().set("config." + player2.getUniqueId(), null);
						String rawMessage = "Cleared old records (1) of " + username + ".";
						Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
						LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
						Logger.info(rawMessage);
						for (String domain : $.validStorageMinigames)
							SolidStorage.clearPlayerSave(player, domain);
						for (String domain : $.validStorageMinigames)
							SolidStorage.clearPlayerSave(player2, domain);
					} else {
						process(player2.getUniqueId().toString());
					}
				} else {
					ServerGet.get().getPlugin().getConfig().set(configPath, null);
					String rawMessage = "Cleared old records (1) of " + id + ".";
					Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
					LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
					Logger.info(rawMessage);
					for (String domain : $.validStorageMinigames)
						SolidStorage.clearPlayerSave(player, domain);
				}
			} else {
				if (!ServerGet.get().getPlugin().getConfig().contains(configPath + ".username")) {
					ServerGet.get().getPlugin().getConfig().set(configPath, null);
					String rawMessage = "Cleared old records (1) of " + player.getName() + ".";
					Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
					LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
					Logger.info(rawMessage);
					for (String domain : $.validStorageMinigames)
						SolidStorage.clearPlayerSave(player, domain);
				}
			}
		}
		File f = new File("plugins/uSkyBlock/players");
		if (f.exists()) {
			ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
			for (int i = 0; i < names.size(); i++) {
				String id = names.get(i);
				if (id.contains("."))
					id = id.substring(0, id.indexOf("."));
				if (!ServerGet.get().getPlugin().getConfig().contains("config." + id)) {
					new File("plugins/uSkyBlock/players", names.get(i)).delete();
					String rawMessage = "Cleared skyblock records (1) of " + id + ".";
					Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
					LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
					Logger.info(rawMessage);
				}
			}
		} else {
		}
	}

	public void process(String id) {
		String configPath = "config." + id;
		OfflinePlayer player = CraftGo.Player.getOfflinePlayer(UUID.fromString(id));
		if (player.hasPlayedBefore() || player.isOnline()) {
			if (ServerGet.get().getPlugin().getConfig().contains(configPath + ".username")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
				if (dayOfYear - 7 < 0)
					return;
				String rank = Link$.getRank(player.getUniqueId());
				int rankId = Link$.getRankId(player.getUniqueId());
				if (rankId > -1 && rankId < 3) {
					long totalPlaytime = LinkServerGet.get().getInstance().getPlaytimeManager().getStoredPlayerPlaytime(player);
					long[] range = LinkServerGet.get().getInstance().getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, dayOfYear - 6, dayOfYear + 1);
					long totalTimePlayedInSeconds = 0L;
					for (int i = 0; i < range.length; i++)
						totalTimePlayedInSeconds += range[i];
					int playtimeRequirementPerWeek = 60 * 60 * 4;
					if (rankId > 0)
						playtimeRequirementPerWeek = 60 * 60 * 7;
					int managerPlaytimeRequirement = 60 * 60 * 24 * 4;
					if (rank.equals("admin")) {
						if (totalPlaytime > managerPlaytimeRequirement) {
							if (Link$.validRanks.contains("manager")) {
								String rawMessage = "Auto-promotion of " + player.getName() + " to Manager";
								Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
								LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
								Logger.info(rawMessage);
								LinkServerGet.get().getInstance().getRedisDatabase().set("rank", player.getUniqueId().toString(), "manager");
								if (Link$.isPrefixedRankingEnabled() && player.isOnline()) {
									Link$.flashPlayerDisplayName(player.getPlayer());
								}
								LinkServerGet.get().getInstance().getRedisMessenger().ping(RedisChannel.CHAT, "RANK_UPDATE", player.getName());
							} else {
								Logger.debug("An unexpected error occured during the auto-promotion task of promoting " + player.getName() + " to Manager.");
							}
						}
					} else if (rank.equals("manager") || rank.equals("builder"))
						playtimeRequirementPerWeek /= 2;
					if (totalTimePlayedInSeconds < playtimeRequirementPerWeek) {
						String rawMessage = "Auto-demotion of " + player.getName() + " to " + Link$.validRanks.get(0);
						Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
						LinkServerGet.get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
						Logger.info(rawMessage);
						LinkServerGet.get().getInstance().getRedisDatabase().set("rank", player.getUniqueId().toString(), Link$.validRanks.get(0));
						if (Link$.isPrefixedRankingEnabled() && player.isOnline()) {
							Link$.flashPlayerDisplayName(player.getPlayer());
						}
						LinkServerGet.get().getInstance().getRedisMessenger().ping(RedisChannel.CHAT, "RANK_UPDATE", player.getName());
					}
				}
			}
		}
	}

	@Override
	public void run() {
		if (!ServerGet.get().getPlugin().getConfig().contains("config"))
			return;
		Set<String> keys = ServerGet.get().getPlugin().getConfig().getConfigurationSection("config").getKeys(false);
		for (String id : keys) {
			process(id);
		}
		File uuid2nameFile = new File("plugins/uSkyBlock", "uuid2name.yml");
		if (uuid2nameFile.exists()) {
			uuid2nameFile.delete();
			ConfigurationManager uuidConfig = new ConfigurationManager();
			uuidConfig.setup(uuid2nameFile);
			keys = ServerGet.get().getPlugin().getConfig().getConfigurationSection("config").getKeys(false);
			for (String id : keys) {
				if (ServerGet.get().getPlugin().getConfig().contains("config." + id + ".username")) {
					String username = ServerGet.get().getPlugin().getConfig().getString("config." + id + ".username");
					uuidConfig.getData().set(id + ".name", username);
					uuidConfig.getData().set(id + ".displayName", username);
					uuidConfig.getData().set(id + ".updated", System.currentTimeMillis());
				}
			}
			uuidConfig.saveData();
			if (Link$.isPluginEnabled("uSkyBlock"))
				ServerGet.get().getPlugin().getServer().dispatchCommand(ServerGet.get().getPlugin().getServer().getConsoleSender(), "usb reload");
		} else {
		}
	}
}
