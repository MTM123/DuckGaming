package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

import me.skorrloregaming.*;

public class VerifyCmd implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		int code = new Random(UUID.randomUUID().hashCode()).nextInt(900) + 100;
		ServerGet.get().getDiscordVerifyPlayers().put(code, player.getUniqueId());
		player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Type " + ChatColor.RED + "?verify " + code + ChatColor.GRAY + " in the verify channel in our discord server to link your account. Doing this allows you to talk to people on the minecraft server from discord.");
		return true;
	}
}
