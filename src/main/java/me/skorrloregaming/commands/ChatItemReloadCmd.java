package me.skorrloregaming.commands;

import me.skorrloregaming.ChatItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.*;

public class ChatItemReloadCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ChatItem.reload(sender);
		return true;
	}

}
