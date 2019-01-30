package me.skorrloregaming.commands;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (player.isOp() || Server.getCreative().contains(player.getUniqueId())) {
			if (args.length < 1) {
				if (player.isOp()) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " [player] <skullOwner>");
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <skullOwner>");
				}
				return true;
			} else {
				ItemStack skull = Link$.createMaterial(Material.PLAYER_HEAD);
				SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
				skull.setDurability((short) 3);
				int argValue = 0;
				if (player.isOp() && args.length > 1) {
					argValue = 1;
				}
				OfflinePlayer skullOwner = CraftGo.Player.getOfflinePlayer(args[argValue]);
				if (skullOwner.hasPlayedBefore() || skullOwner.isOnline()) {
					skullMeta.setDisplayName(skullOwner.getName() + "'s Skull");
					skullMeta.setOwningPlayer(skullOwner);
				}
				skull.setItemMeta(skullMeta);
				if (argValue == 1) {
					Player targetPlayer = Bukkit.getPlayer(args[0]);
					if (targetPlayer == null) {
						player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					}
					targetPlayer.getInventory().addItem(skull);
				} else {
					player.getInventory().addItem(skull);
				}
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have been given " + ChatColor.RED + Link$.formatMaterial(skull.getType()) + " x1");
				return true;
			}
		} else {
			Link$.playLackPermissionMessage(player);
			return true;
		}
	}

}
