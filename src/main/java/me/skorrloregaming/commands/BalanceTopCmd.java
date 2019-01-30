package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Server;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;

public class BalanceTopCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		int minimumValue = 0;
		int maximumValue = 8;
		int pageNumber = 1;
		if (args.length > 0) {
			try {
				int value = Integer.parseInt(args[0]);
				minimumValue = 8 * value - 8;
				maximumValue = 8 * value;
				pageNumber = value;
			} catch (Exception ig) {
				minimumValue = 0;
				maximumValue = 8;
				pageNumber = 1;
			}
		}
		String subDomain = $.getMinigameDomain(player);
		String minigameTag = $.getMinigameTag(player);
		HashMap<Double, UUID> array1 = new HashMap<>();
		for (String uuid : Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
			if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
				double money = EconManager.retrieveCash(UUID.fromString(uuid), subDomain);
				if (money > 0) {
					UUID id = UUID.fromString(uuid);
					while (array1.containsKey(money)) {
						money = money + 0.00001;
					}
					array1.put(money, id);
				}
			}
		}
		double[] valueArray = $.sortNumericallyReverse(ArrayUtils.toPrimitive(array1.keySet().toArray(new Double[0])));
		int serverTotal = 0;
		for (double value : valueArray) {
			serverTotal = (int) Math.floor(serverTotal + value);
		}
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		sender.sendMessage(minigameTag + ChatColor.GRAY + "Server total: " + ChatColor.RED + "$" + formatter.format(serverTotal));
		for (int i = minimumValue; i < maximumValue; i++) {
			double value = 0;
			if (i < valueArray.length)
				value = valueArray[i];
			OfflinePlayer op = null;
			if (array1.containsKey(value))
				op = CraftGo.Player.getOfflinePlayer(array1.get(value));
			if (!(op == null) && value > 0) {
				value = Math.floor(value);
				sender.sendMessage(minigameTag + ChatColor.ITALIC + "(" + (i + 1) + ") " + ChatColor.RESET + "" + ChatColor.RED + op.getName() + ChatColor.GRAY + ": " + ChatColor.RED + "$" + formatter.format(value));
			}
		}
		sender.sendMessage(minigameTag + ChatColor.GRAY + "Type " + ChatColor.RED + "/" + label + " " + (pageNumber + 1) + ChatColor.GRAY + " to read the next page.");
		return true;
	}

}
