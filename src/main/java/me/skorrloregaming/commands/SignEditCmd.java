package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.SignInfo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SignEditCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (!player.isOp()) {
            Link$.playLackPermissionMessage(player);
            return true;
        }
        String tag = $.getMinigameTag(player);
        if (args.length < 2) {
            player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <line> <text>");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i] + " ");
            }
            String text = sb.toString().trim();
            if (Server.getInstance().getSignEditParam().containsKey(player)) {
                Server.getInstance().getSignEditParam().put(player, new SignInfo(Integer.parseInt(args[0]), text));
                player.sendMessage(tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Sign edit parameters " + ChatColor.ITALIC + "updated in memory" + ChatColor.RESET + ChatColor.GRAY + ".");
                player.sendMessage(tag + ChatColor.GRAY + "Right click on the sign that you would like to edit.");
            } else {
                Server.getInstance().getSignEditParam().put(player, new SignInfo(Integer.parseInt(args[0]), text));
                player.sendMessage(tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Sign edit parameters " + ChatColor.ITALIC + "saved to memory" + ChatColor.RESET + ChatColor.GRAY + ".");
                player.sendMessage(tag + ChatColor.GRAY + "Right click on the sign that you would like to edit.");
            }
        }
        return true;
    }

}
