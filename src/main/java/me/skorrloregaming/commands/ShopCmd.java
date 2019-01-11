package me.skorrloregaming.commands;

import me.skorrloregaming.factions.shop.LaShoppeFrame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.TitleSubtitle;
import me.skorrloregaming.runnable.DelayedTeleport;

public class ShopCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (args.length > 0 && args[0].equals("inv")) {
			Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, 1, false);
		} else {
			Location zoneLocation = $.getZoneLocation($.getMinigameDomain(player) + "-shop");
			DelayedTeleport dt = new DelayedTeleport(player, Server.getTeleportationDelay(), zoneLocation, false);
			dt.setFinishTitleSubtitle(new TitleSubtitle("", "You can use /spawner to purchase mob spawners.", true));
			dt.runTaskTimerAsynchronously(Server.getPlugin(), 4, 4);
		}
		return true;
	}

}
