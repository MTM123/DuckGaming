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
import org.bukkit.inventory.Inventory;

public class WarningsCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			if (sender instanceof Player)
				((Player) sender).performCommand(label + " " + sender.getName());
			return true;
		} else {
			OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0]);
			if (!op.hasPlayedBefore() && !op.isOnline()) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String path = "config." + op.getUniqueId().toString();
			if (!Server.getPlugin().getConfig().contains(path + ".ip")) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String ipAddress = Server.getPlugin().getConfig().getString(path + ".ip");
			String configPath = "warning." + ipAddress + ".count";
			int warnings = 0;
			if (Server.getPlugin().getConfig().contains(configPath)) {
				warnings = Integer.parseInt(Server.getPlugin().getConfig().getString(configPath));
				if (sender instanceof Player) {
					int invSize = 9;
					if (CraftGo.Player.isPocketPlayer((Player) sender))
						invSize = 27;
					Inventory inv = Bukkit.createInventory(null, invSize, ChatColor.BOLD + op.getName() + "'s warnings");
					int add = 0;
					if (CraftGo.Player.isPocketPlayer((Player) sender))
						add = 9;
					for (int i = 1; i <= 5; i++) {
						inv.setItem(i + 1 + add, Link$.createMaterial(Material.SKELETON_SKULL, 1, ChatColor.RESET + "" + ChatColor.UNDERLINE + op.getName() + "'s warning #" + i, (short) 0, new String[0]));
					}
					for (String key : Server.getPlugin().getConfig().getConfigurationSection("warning." + ipAddress).getKeys(false)) {
						if (key.equals("count"))
							continue;
						String[] message = Server.getPlugin().getConfig().getString("warning." + ipAddress + "." + key).split("[\\r?\\n]+");
						for (int i = 0; i < message.length; i++)
							message[i] = ChatColor.RESET + message[i];
						inv.setItem(Integer.valueOf(key) + 1 + add, Link$.createMaterial(Material.ZOMBIE_HEAD, 1, ChatColor.RESET + "" + ChatColor.UNDERLINE + op.getName() + "'s warning #" + key, (short) 0, message));
					}
					((Player) sender).openInventory(inv);
				}
			}
			if (warnings == 1) {
				sender.sendMessage(op.getName() + " has " + warnings + " warning on record.");
			} else {
				sender.sendMessage(op.getName() + " has " + warnings + " warnings on record.");
			}
			if (Server.getBanConfig().getData().contains(ipAddress.replace(".", "x"))) {
				sender.sendMessage("Banned signature found, " + op.getName() + " is banned.");
			} else {
				sender.sendMessage("Banned signature not found, " + op.getName() + " is not banned.");
			}
			return true;
		}
	}

}
