package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MotdCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 0) {
                sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <motd>");
                return true;
            } else {
                if (!Server.getInstance().getTempMotd().equals("/unspecified")) {
                    sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must wait before using this command again.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (String arg : args) {
                        sb.append(arg).append(" ");
                    }
                    Server.getInstance().setTempMotd(sb.toString());
                    Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> Server.getInstance().setTempMotd("/unspecified"), 1200L));
                    sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The motd has been temporarily changed.");
                }
            }
        } else {
            Link$.playLackPermissionMessage(sender);
            return true;
        }
        return true;
    }

}
