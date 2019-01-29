package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (player.isOp() || $.getCurrentMinigame(player) == ServerMinigame.CREATIVE) {
			if (args.length < 1) {
				if (player.isOp()) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " [player] <id> <amount>");
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <id> <amount>");
				}
			} else {
				int arg = 0;
				Player targetPlayer = player;
				if (!(Bukkit.getPlayer(args[0]) == null) && player.isOp()) {
					targetPlayer = Bukkit.getPlayer(args[0]);
					if (targetPlayer == null) {
						player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					}
					arg++;
				}
				Material type = null;
				int[] successTimes = new int[2];
				try {
					type = Material.getMaterial(args[arg].split(":")[0].toUpperCase());
					successTimes[0] = 1;
				} catch (Exception ex) {
					successTimes[0] = 0;
				}
				if ((successTimes[0] == 0 && successTimes[1] == 0) || type == null || type == Material.AIR || type == Material.END_PORTAL || type == Material.NETHER_PORTAL) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified material could not be found.");
					return true;
				}
				short data = 0;
				int amount = 1;
				if (args[arg].contains(":"))
					data = (short) Integer.parseInt(args[arg].split(":")[1]);
				if (args.length > 1)
					amount = Integer.parseInt(args[arg + 1]);
				ItemStack itm = new ItemStack(type, amount, data);
				if (type == Material.SPAWNER)
					itm = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), amount);
				targetPlayer.getInventory().addItem(itm);
				targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have been given " + ChatColor.RED + Link$.formatMaterial(type) + ":" + data + " x" + amount);
			}
		} else {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		return true;
	}

}
