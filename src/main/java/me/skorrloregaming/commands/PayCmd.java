package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class PayCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String subDomain = "";
        String tag = Link$.Legacy.tag;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Server.getInstance().getKitpvp().contains(player.getUniqueId()) && !Server.getInstance().getSurvival().contains(player.getUniqueId()) && !Server.getInstance().getFactions().contains(player.getUniqueId()) && !Server.getInstance().getSkyblock().contains(player.getUniqueId())) {
                player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
                return true;
            }
            subDomain = $.getMinigameDomain(player);
        } else {
            subDomain = args[2].toLowerCase();
        }
        if ((sender instanceof Player && !(args.length == 2)) || (!(sender instanceof Player) && !(args.length == 3))) {
            sender.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <amount>");
            return true;
        }
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
            return true;
        }
        double amount = Double.parseDouble(args[1]);
        DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
        if (sender.isOp()) {
            EconManager.depositCash(targetPlayer, amount, subDomain);
            sender.sendMessage(tag + ChatColor.GRAY + "You have given " + ChatColor.RED + "$" + formatter.format(amount) + ChatColor.GRAY + " to " + ChatColor.RED + targetPlayer.getName());
            targetPlayer.sendMessage(tag + ChatColor.GRAY + "You have been payed " + ChatColor.RED + "$" + formatter.format(amount) + ChatColor.GRAY + ", by " + ChatColor.RED + sender.getName());
            return true;
        } else if (sender instanceof Player) {
            amount = Math.abs(amount);
            Player player = (Player) sender;
            double currentPlayerCash = EconManager.retrieveCash(player, subDomain);
            if (currentPlayerCash >= amount) {
                EconManager.withdrawCash(player, amount, subDomain);
                EconManager.depositCash(targetPlayer, amount, subDomain);
                player.sendMessage(tag + ChatColor.GRAY + "You have given " + ChatColor.RED + "$" + formatter.format(amount) + ChatColor.GRAY + " to " + ChatColor.RED + targetPlayer.getName());
                targetPlayer.sendMessage(tag + ChatColor.GRAY + "You have been payed " + ChatColor.RED + "$" + formatter.format(amount) + ChatColor.GRAY + ", by " + ChatColor.RED + player.getName());
                return true;
            }
            player.sendMessage(tag + ChatColor.GRAY + "You do not have enough money to fulfill the transaction.");
            return true;
        }
        return true;
    }

}
