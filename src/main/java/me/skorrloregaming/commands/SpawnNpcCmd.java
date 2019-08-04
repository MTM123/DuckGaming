package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.impl.CustomNpc;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.skorrloregaming.*;

public class SpawnNpcCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		if (args.length == 0) {
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <npcData> <npcName>");
			return true;
		} else {
			String name = null;
			if (args.length > 1) {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				name = sb.toString();
			}
			CustomNpc npc = CustomNpc.spawn(player.getLocation(), name);
			ItemStack skull = Link$.createMaterial(Material.PLAYER_HEAD);
			npc.entity.setHelmet(skull);
			npc.entity.setChestplate(Link$.createMaterial(Material.LEATHER_CHESTPLATE));
			npc.entity.setLeggings(Link$.createMaterial(Material.LEATHER_LEGGINGS));
			npc.entity.setBoots(Link$.createMaterial(Material.LEATHER_BOOTS));
			npc.entity.setItemInHand(Link$.createMaterial(Material.BLAZE_ROD));
			npc.register(args[0]);
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Spawned ArmorStand (Entity ID: " + ChatColor.RED + npc.entity.getEntityId() + ChatColor.GRAY + ")");
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Registered Custom NPC (Entity ID: " + ChatColor.RED + npc.entity.getEntityId() + ChatColor.GRAY + ")");
		}
		return true;
	}

}
