package me.skorrloregaming.scoreboard.boards;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.Switches;
import me.skorrloregaming.impl.Switches.SwitchIntString;
import me.skorrloregaming.scoreboard.DisposableScoreboard;

public class Kitpvp_LeaderboardScoreboard implements DisposableScoreboard {
	Comparator<Switches.SwitchIntString> myComparator = new Comparator<Switches.SwitchIntString>() {
		public int compare(Switches.SwitchIntString o1, Switches.SwitchIntString o2) {
			return Integer.compare(o2.getArg0(), o1.getArg0());
		}
	};

	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		Set<String> keys = Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false);
		List<SwitchIntString> playerMoney = new ArrayList<>();
		for (String key : keys) {
			if (Server.getPlugin().getConfig().contains("config." + key + ".kitpvp.kills")) {
				OfflinePlayer player2 = Bukkit.getOfflinePlayer(UUID.fromString(key));
				if (player2.hasPlayedBefore() || player2.isOnline()) {
					int kills = Integer.parseInt(Server.getPlugin().getConfig().getString("config." + key + ".kitpvp.kills"));
					playerMoney.add(new SwitchIntString(kills, player2.getUniqueId().toString()));
				}
			}
		}
		playerMoney.sort(myComparator);
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Leaderboard", 6);
		for (int i = 0; i < 5; i++) {
			UUID uuid = UUID.fromString(playerMoney.get(i).getArg1());
			String username = Bukkit.getOfflinePlayer(uuid).getName();
			list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " " + username + ": " + ChatColor.RESET + formatter.format(playerMoney.get(i).getArg0()), 5 - i);
		}
		$.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
	}
}
