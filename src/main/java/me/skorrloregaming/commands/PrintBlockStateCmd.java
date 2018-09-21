package me.skorrloregaming.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PrintBlockStateCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		player.sendMessage(player.getEyeLocation().getBlock().getState().getData().toString());
		player.sendMessage(player.getEyeLocation().getBlock().getState().getData().toString());
		return true;
	}

}
