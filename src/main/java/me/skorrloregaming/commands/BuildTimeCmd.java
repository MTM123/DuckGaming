package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class BuildTimeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        sender.sendMessage(Link$.italicGray + "Server last updated as of " + df.format($.getLastCompilationTime()));
        return true;
    }

}
