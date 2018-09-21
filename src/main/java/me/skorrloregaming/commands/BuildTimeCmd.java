package me.skorrloregaming.commands;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;

public class BuildTimeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sender.sendMessage($.italicGray + "Server last updated as of " + df.format($.getLastCompilationTime()));
		return true;
	}

}
