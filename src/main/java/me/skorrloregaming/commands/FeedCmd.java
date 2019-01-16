package me.skorrloregaming.commands;

import com.massivecraft.factions.FPlayers;
import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if ($.getRankId(player) < -1) {
			player.setFoodLevel(20);
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Your food level has been saturated.");
		} else {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a donor rank to feed yourself.");
		}
		return true;
	}

}
