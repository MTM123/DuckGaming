package me.skorrloregaming.runnable;

import me.skorrloregaming.*;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.util.*;

public class GCandAutoDemotion implements Runnable {

	public void processPlayerData() {
		String[] keys = Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false).toArray(new String[0]);
		for (String id : keys) {
			String configPath = "config." + id;
			OfflinePlayer player = CraftGo.Player.getOfflinePlayer(UUID.fromString(id));
			if (!player.hasPlayedBefore() && !player.isOnline()) {
				if (Server.getPlugin().getConfig().contains(configPath + ".username")) {
					String username = Server.getPlugin().getConfig().getString(configPath + ".username");
					OfflinePlayer player2 = CraftGo.Player.getOfflinePlayer(username);
					if (!player2.hasPlayedBefore() && !player.isOnline()) {
						Server.getPlugin().getConfig().set(configPath, null);
						Server.getPlugin().getConfig().set("config." + player2.getUniqueId(), null);
						Logger.info("Cleared old records (1) of " + username + ".");
						for (String domain : $.validStorageMinigames)
							SolidStorage.clearPlayerSave(player, domain);
						for (String domain : $.validStorageMinigames)
							SolidStorage.clearPlayerSave(player2, domain);
					} else {
						process(player2.getUniqueId().toString());
					}
				} else {
					Server.getPlugin().getConfig().set(configPath, null);
					Logger.info("Cleared old records (1) of " + id + ".");
					for (String domain : $.validStorageMinigames)
						SolidStorage.clearPlayerSave(player, domain);
				}
			} else {
				if (!Server.getPlugin().getConfig().contains(configPath + ".username")) {
					Server.getPlugin().getConfig().set(configPath, null);
					Logger.info("Cleared old records (1) of " + player.getName() + ".");
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
				if (!Server.getPlugin().getConfig().contains("config." + id)) {
					new File("plugins/uSkyBlock/players", names.get(i)).delete();
					Logger.info("Cleared skyblock records (1) of " + id + ".");
				}
			}
		} else {
		}
	}

	public void process(String id) {
		String configPath = "config." + id;
		OfflinePlayer player = CraftGo.Player.getOfflinePlayer(UUID.fromString(id));
		if (player.hasPlayedBefore() || player.isOnline()) {
			if (Server.getPlugin().getConfig().contains(configPath + ".username")) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
				if (dayOfYear - 7 < 0)
					return;
				String rank = Link$.getRank(player.getUniqueId());
				int rankId = Link$.getRankId(player.getUniqueId());
				if (rankId > -1 && rankId < 3) {
					long totalPlaytime = $.getLinkServer().getPlaytimeManager().getStoredPlayerPlaytime(player);
					long[] range = $.getLinkServer().getPlaytimeManager().getRangeOfStoredPlayerPlaytime(player, dayOfYear - 6, dayOfYear + 1);
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
								Logger.info("Auto-promotion of " + player.getName() + " to Manager");
								$.getLinkServer().getSqlDatabase().set("rank", player.getUniqueId().toString(), "manager");
								if (Link$.isPrefixedRankingEnabled() && player.isOnline()) {
									Link$.flashPlayerDisplayName(player.getPlayer());
								}
							} else {
								Logger.debug("An unexpected error occured during the auto-promotion task of promoting " + player.getName() + " to Manager.");
							}
						}
					} else if (rank.equals("manager") || rank.equals("builder"))
						playtimeRequirementPerWeek /= 2;
					if (totalTimePlayedInSeconds < playtimeRequirementPerWeek) {
						Logger.info("Auto-demotion of " + player.getName() + " to " + Link$.validRanks.get(0));
						$.getLinkServer().getSqlDatabase().set("rank", player.getUniqueId().toString(), Link$.validRanks.get(0));
						if (Link$.isPrefixedRankingEnabled() && player.isOnline()) {
							Link$.flashPlayerDisplayName(player.getPlayer());
						}
					}
				}
			}
		}
	}

	@Override
	public void run() {
		if (!Server.getPlugin().getConfig().contains("config"))
			return;
		Set<String> keys = Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false);
		for (String id : keys) {
			process(id);
		}
		File uuid2nameFile = new File("plugins/uSkyBlock", "uuid2name.yml");
		if (uuid2nameFile.exists()) {
			uuid2nameFile.delete();
			ConfigurationManager uuidConfig = new ConfigurationManager();
			uuidConfig.setup(uuid2nameFile);
			keys = Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false);
			for (String id : keys) {
				if (Server.getPlugin().getConfig().contains("config." + id + ".username")) {
					String username = Server.getPlugin().getConfig().getString("config." + id + ".username");
					uuidConfig.getData().set(id + ".name", username);
					uuidConfig.getData().set(id + ".displayName", username);
					uuidConfig.getData().set(id + ".updated", System.currentTimeMillis());
				}
			}
			uuidConfig.saveData();
			if (Link$.isPluginEnabled("uSkyBlock"))
				Server.getPlugin().getServer().dispatchCommand(Server.getPlugin().getServer().getConsoleSender(), "usb reload");
		} else {
		}
	}
}
