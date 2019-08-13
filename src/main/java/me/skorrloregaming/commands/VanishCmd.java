package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.VanishedInfo;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanishCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (Server.getInstance().getVanishedPlayers().containsKey(player.getUniqueId())) {
            VanishedInfo iVanish = Server.getInstance().getVanishedPlayers().get(player.getUniqueId());
            player.getInventory().setContents(iVanish.getContents());
            player.setGameMode(iVanish.getGameMode());
            Server.getInstance().getVanishedPlayers().remove(player.getUniqueId());
            for (Player p : Server.getInstance().getPlugin().getServer().getOnlinePlayers()) {
                p.showPlayer(Server.getInstance().getPlugin(), player);
            }
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are no longer vanished.");
            return true;
        }
        if (player.isOp() || Link$.getRankId(player) > 1) {
            if (!Server.getInstance().getModeratingPlayers().containsKey(player.getUniqueId())) {
                player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are not currently moderating the server.");
                return true;
            }
            Server.getInstance().getVanishedPlayers().put(player.getUniqueId(), new VanishedInfo(player.getInventory().getContents(), player.getGameMode()));
            for (Player p : Server.getInstance().getPlugin().getServer().getOnlinePlayers()) {
                p.hidePlayer(Server.getInstance().getPlugin(), player);
            }
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 72000, 0));
            player.setGameMode(GameMode.SPECTATOR);
            $.clearPlayer(player);
            player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now vanished.");
        } else {
            Link$.playLackPermissionMessage(player);
            return true;
        }
        return true;
    }

}
