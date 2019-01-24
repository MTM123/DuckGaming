package com.sg.qs.QuantumSurvival;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class QuantumSurvival
		extends JavaPlugin
		implements Listener {
	private ArrayList<Player> jumpers = new ArrayList();
	int kills;
	List<String> fly = new ArrayList<String>();
	List<String> player = new ArrayList<String>();
	ArrayList<Player> cooldown = new ArrayList();
	ArrayList<Player> cooldown2 = new ArrayList();
	ArrayList<Player> cooldown3 = new ArrayList();
	ArrayList<Player> godmode = new ArrayList();
	ArrayList<Player> vanished = new ArrayList();
	ArrayList<Player> muted = new ArrayList();

	public void ExampleListener(QuantumSurvival plugin) {
		plugin.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
	}

	public void sleep() {
		try {
			Thread.sleep(100L);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		String motd = this.getConfig().getString("MOTD.system");
		motd = motd.replaceAll("&", "\u00a7");
		e.setMotd(motd);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.LAVA) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.FIRE) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.STARVATION) {
				e.setDamage(0.0);
			}
			if (e.getCause() == EntityDamageEvent.DamageCause.THORNS) {
				e.setDamage(0.0);
			}
			if (this.godmode.contains(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(ChatColor.YELLOW + "(" + ChatColor.DARK_RED + "-" + ChatColor.YELLOW + ") " + ChatColor.AQUA + e.getPlayer().getName());
		Player p = e.getPlayer();
		String icon = ChatColor.BLUE + "[" + ChatColor.GREEN + "Q-S" + ChatColor.BLUE + "] ";
		this.getConfig().addDefault("Players." + p.getName() + ".timeplayed", System.currentTimeMillis());
		this.getConfig().createSection("PlayerData." + p.getName());
		this.getConfig().set("PlayerData." + p.getName() + ".X", p.getLocation().getBlockX());
		this.getConfig().set("PlayerData." + p.getName() + ".Y", p.getLocation().getBlockY());
		this.getConfig().set("PlayerData." + p.getName() + ".Z", p.getLocation().getBlockZ());
		this.getConfig().set("PlayerData." + p.getName() + ".Banned", p.isBanned());
		this.getConfig().set("PlayerData." + p.getName() + ".Flying", p.isFlying());
		this.getConfig().set("PlayerData." + p.getName() + ".Opped", p.isOp());
		this.getConfig().set("PlayerData." + p.getName() + ".Whitelisted", p.isWhitelisted());
		this.getConfig().set("PlayerData." + p.getName() + ".Fireticks", p.getFireTicks());
		this.getConfig().set("PlayerData." + p.getName() + ".DisplayName", p.getDisplayName());
		this.getConfig().set("PlayerData." + p.getName() + ".CustomName", p.getCustomName());
		this.getConfig().set("PlayerData." + p.getName() + ".MinecraftName", p.getName());
		++this.kills;
		this.getConfig().set("PlayerData." + p.getName() + ".Player-Kills", this.kills);
		int data = this.getConfig().getInt("PlayerData." + p.getName() + ".Player-Kills");
		this.saveConfig();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(ChatColor.YELLOW + "(" + ChatColor.DARK_GREEN + "+" + ChatColor.YELLOW + ") " + ChatColor.AQUA + e.getPlayer().getName());
		Player p = e.getPlayer();
		String icon = ChatColor.BLUE + "[" + ChatColor.GREEN + "Q-S" + ChatColor.BLUE + "] ";
		String nickname = this.getConfig().getString("nicknames." + p.getName());
		p.setDisplayName(nickname);
		this.getConfig().addDefault("Players." + p.getName() + ".timeplayed", System.currentTimeMillis());
		this.getConfig().createSection("PlayerData." + p.getName());
		this.getConfig().set("PlayerData." + p.getName() + ".X", p.getLocation().getBlockX());
		this.getConfig().set("PlayerData." + p.getName() + ".Y", p.getLocation().getBlockY());
		this.getConfig().set("PlayerData." + p.getName() + ".Z", p.getLocation().getBlockZ());
		this.getConfig().set("PlayerData." + p.getName() + ".Banned", p.isBanned());
		this.getConfig().set("PlayerData." + p.getName() + ".Flying", p.isFlying());
		this.getConfig().set("PlayerData." + p.getName() + ".Opped", p.isOp());
		this.getConfig().set("PlayerData." + p.getName() + ".Whitelisted", p.isWhitelisted());
		this.getConfig().set("PlayerData." + p.getName() + ".Fireticks", p.getFireTicks());
		this.getConfig().set("PlayerData." + p.getName() + ".DisplayName", p.getDisplayName());
		this.getConfig().set("PlayerData." + p.getName() + ".CustomName", p.getCustomName());
		this.getConfig().set("PlayerData." + p.getName() + ".MinecraftName", p.getName());
		++this.kills;
		this.getConfig().set("PlayerData." + p.getName() + ".Player-Kills", this.kills);
		int data = this.getConfig().getInt("PlayerData." + p.getName() + ".Player-Kills");
		this.saveConfig();
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String icon = ChatColor.BLUE + "[" + ChatColor.GREEN + "Q-S" + ChatColor.BLUE + "] ";
		Player p = e.getPlayer();
		String[] arrstring = e.getMessage().split(" ");
		int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			String badword = arrstring[n2];
			if (this.getConfig().getStringList("Bad-Words").contains(badword)) {
				e.setCancelled(true);
				p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Do not swear in chat!");
			}
			++n2;
		}
		if (this.muted.contains(p)) {
			e.setCancelled(true);
			p.sendMessage(String.valueOf(icon) + ChatColor.RED + "You cannot send messages while muted!");
		}
	}

	public void onEnable() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents((Listener) this, (Plugin) this);
		PluginDescriptionFile pdfFile = this.getDescription();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getLogger().info("0----------------------------------------0");
		this.getLogger().info("| Enabled " + pdfFile.getName());
		this.getLogger().info("| Enabled KitPvP v1.1.1");
		this.getLogger().info("| Enabled ConditionalOnlineJava v1.9.2");
		this.getLogger().info("0----------------------------------------0");
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.getLogger().info("0----------------------------------------0");
		this.getLogger().info("| Disabled " + pdfFile.getName());
		this.getLogger().info("| Disabled KitPvP v1.1.1");
		this.getLogger().info("| Disabled ConditionalOnlineJava v1.9.2");
		this.getLogger().info("0----------------------------------------0");
	}

	@EventHandler
	public void DOM(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		Player k = e.getEntity().getKiller();
		String icon = ChatColor.BLUE + "[" + ChatColor.GREEN + "Q-S" + ChatColor.BLUE + "] ";
		e.setDeathMessage(String.valueOf(icon) + ChatColor.GOLD + p.getName() + ChatColor.DARK_AQUA + " Was killed by " + ChatColor.GOLD + k.getName());
		k.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been healed for killing " + ChatColor.GOLD + k.getName());
		k.setHealth(20);
		k.setFoodLevel(20);
		k.setFireTicks(0);
		PlayerInventory ki = k.getInventory();
		ki.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 4)});
		Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + k.getName() + ChatColor.DARK_AQUA + " Has been healed for killing " + ChatColor.GOLD + p.getName()));
		this.getConfig().addDefault("Players." + p.getName() + ".timeplayed", System.currentTimeMillis());
		this.getConfig().createSection("PlayerData." + p.getName());
		this.getConfig().set("PlayerData." + k.getName() + ".X", p.getLocation().getBlockX());
		this.getConfig().set("PlayerData." + k.getName() + ".Y", p.getLocation().getBlockY());
		this.getConfig().set("PlayerData." + k.getName() + ".Z", p.getLocation().getBlockZ());
		this.getConfig().set("PlayerData." + k.getName() + ".Banned", p.isBanned());
		this.getConfig().set("PlayerData." + k.getName() + ".Flying", p.isFlying());
		this.getConfig().set("PlayerData." + k.getName() + ".Opped", p.isOp());
		this.getConfig().set("PlayerData." + k.getName() + ".Whitelisted", p.isWhitelisted());
		this.getConfig().set("PlayerData." + k.getName() + ".Fireticks", p.getFireTicks());
		this.getConfig().set("PlayerData." + k.getName() + ".DisplayName", p.getDisplayName());
		this.getConfig().set("PlayerData." + k.getName() + ".CustomName", p.getCustomName());
		this.getConfig().set("PlayerData." + k.getName() + ".MinecraftName", p.getName());
		++this.kills;
		this.getConfig().set("PlayerData." + k.getName() + ".Player-Kills", this.kills);
		int data = this.getConfig().getInt("PlayerData." + k.getName() + ".Player-Kills");
		this.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		String icon = ChatColor.BLUE + "[" + ChatColor.GREEN + "Q-S" + ChatColor.BLUE + "] ";
		if (!(sender instanceof Player)) {
			sender.sendMessage(String.valueOf(icon) + ChatColor.RED + "Only ingame players can use commands in this plugin!");
			return true;
		}
		final Player p = (Player) sender;
		this.getConfig().addDefault("Players." + p.getName() + ".timeplayed", System.currentTimeMillis());
		this.getConfig().createSection("PlayerData." + p.getName());
		this.getConfig().set("PlayerData." + p.getName() + ".X", p.getLocation().getBlockX());
		this.getConfig().set("PlayerData." + p.getName() + ".Y", p.getLocation().getBlockY());
		this.getConfig().set("PlayerData." + p.getName() + ".Z", p.getLocation().getBlockZ());
		this.getConfig().set("PlayerData." + p.getName() + ".Banned", p.isBanned());
		this.getConfig().set("PlayerData." + p.getName() + ".Flying", p.isFlying());
		this.getConfig().set("PlayerData." + p.getName() + ".Opped", p.isOp());
		this.getConfig().set("PlayerData." + p.getName() + ".Whitelisted", p.isWhitelisted());
		this.getConfig().set("PlayerData." + p.getName() + ".Fireticks", p.getFireTicks());
		this.getConfig().set("PlayerData." + p.getName() + ".DisplayName", p.getDisplayName());
		this.getConfig().set("PlayerData." + p.getName() + ".CustomName", p.getCustomName());
		this.getConfig().set("PlayerData." + p.getName() + ".MinecraftName", p.getName());
		++this.kills;
		this.getConfig().set("PlayerData." + p.getName() + ".Player-Kills", this.kills);
		int data = this.getConfig().getInt("PlayerData." + p.getName() + ".Player-Kills");
		this.saveConfig();
		if (commandLabel.equalsIgnoreCase("qop")) {
			if (p.hasPermission("quantumsurvival.qop")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qop <PlayerName>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				targetPlayer.setOp(true);
				Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + targetPlayer.getName() + ChatColor.DARK_AQUA + " Has been opped by " + ChatColor.GOLD + p.getName()));
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qgive")) {
			if (!p.hasPermission("quantumsurvival.qgive")) return false;
			if (args.length == 0) {
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qgive <Player-Name> <ItemID> <Amount>");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qgive <Player-Name> <ItemID> <Amount>");
				return true;
			}
			if (args.length == 2) {
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qgive <Player-Name> <ItemID> <Amount>");
				return true;
			}
			if (args.length == 3) {
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				PlayerInventory playerInv = targetPlayer.getInventory();
				if (args[2] == null) {
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "You must specify a valid item ID!");
					return true;
				}
				ItemStack item = new ItemStack(Material.getMaterial((int) Integer.parseInt(args[1])), Integer.parseInt(args[2]));
				playerInv.addItem(new ItemStack[]{item});
				targetPlayer.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been given " + ChatColor.GREEN + "<Amount: " + args[2] + ">--<ItemID: " + args[1] + ">" + ChatColor.DARK_AQUA + " By " + ChatColor.GOLD + p.getName() + ChatColor.DARK_AQUA + "!");
				p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have given " + ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " <Amount: " + args[2] + ">--<ItemID: " + args[1] + ">" + ChatColor.DARK_AQUA + "!");
				return true;
			}
			p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qgive <Player-Name> <ItemID> <Amount>");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qenchant")) {
			if (p.hasPermission("quantumsurvival.qenchant")) {
				PlayerInventory playerInv = p.getInventory();
				ItemStack inHandItem = p.getItemInHand();
				if (args.length == 0) {
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qenchant <Enchantment> <Level>");
					return true;
				}
				if (args[0].equalsIgnoreCase("sharpness")) {
					if (args.length != 2) return false;
					if (inHandItem.getType() == Material.DIAMOND_SWORD) {
						if (args[1].contentEquals("1")) {
							inHandItem.addEnchantment(Enchantment.DAMAGE_ALL, 1);
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a sharpness enchant of LVL: " + args[1]);
							return true;
						}
						if (args[1].contentEquals("2")) {
							inHandItem.addEnchantment(Enchantment.DAMAGE_ALL, 2);
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a sharpness enchant of LVL: " + args[1]);
							return true;
						}
						if (args[1].contentEquals("3")) {
							inHandItem.addEnchantment(Enchantment.DAMAGE_ALL, 3);
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a sharpness enchant of LVL: " + args[1]);
							return true;
						}
						if (args[1].contentEquals("4")) {
							inHandItem.addEnchantment(Enchantment.DAMAGE_ALL, 4);
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a sharpness enchant of LVL: " + args[1]);
							return true;
						}
						if (args[1].contentEquals("5")) {
							inHandItem.addEnchantment(Enchantment.DAMAGE_ALL, 5);
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a sharpness enchant of LVL: " + args[1]);
							return true;
						}
						p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
						return true;
					}
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "You must be holding the proper tool for this enchant! (Diamond-Sword)");
					return true;
				}
				if (args[0].equalsIgnoreCase("efficiency")) {
					if (args.length == 2) {
						if (inHandItem.getType() == Material.DIAMOND_PICKAXE) {
							if (args[1].contentEquals("1")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 1);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("2")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 2);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("3")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 3);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("4")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 4);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("5")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 5);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
							return true;
						}
						if (inHandItem.getType() == Material.DIAMOND_AXE) {
							if (args[1].contentEquals("1")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 1);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("2")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 2);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("3")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 3);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("4")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 4);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("5")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 5);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
							return true;
						}
						if (inHandItem.getType() == Material.DIAMOND_SPADE) {
							if (args[1].contentEquals("1")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 1);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("2")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 2);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("3")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 3);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("4")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 4);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							if (args[1].contentEquals("5")) {
								inHandItem.addEnchantment(Enchantment.DIG_SPEED, 5);
								p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a efficiency enchant of LVL: " + args[1]);
								return true;
							}
							p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
							return true;
						}
						p.sendMessage(String.valueOf(icon) + ChatColor.RED + "You must be holding the proper tool for this enchant! (Diamond-Tool)");
						return true;
					}
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qenchant <Enchantment> <Level>");
					return true;
				}
				if (!args[0].equalsIgnoreCase("unbreaking") || args.length != 2) return false;
				if (inHandItem.getType() == Material.DIAMOND_PICKAXE) {
					if (args[1].contentEquals("1")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 1);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("2")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 2);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("3")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 3);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
					return true;
				}
				if (inHandItem.getType() == Material.DIAMOND_AXE) {
					if (args[1].contentEquals("1")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 1);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("2")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 2);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("3")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 3);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
					return true;
				}
				if (inHandItem.getType() == Material.DIAMOND_SPADE) {
					if (args[1].contentEquals("1")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 1);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("2")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 2);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					if (args[1].contentEquals("3")) {
						inHandItem.addEnchantment(Enchantment.DURABILITY, 3);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have added a unbreaking enchant of LVL: " + args[1]);
						return true;
					}
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Invalid number for this enchant!");
					return true;
				}
				p.sendMessage(String.valueOf(icon) + ChatColor.RED + "You must be holding the proper tool for this enchant! (Diamond-Tool)");
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qhat")) {
			if (p.hasPermission("quantumsurvival.qhat")) {
				PlayerInventory playerInv = p.getInventory();
				ItemStack inHandItem = p.getItemInHand();
				if (inHandItem == null) {
					p.sendMessage(String.valueOf(icon) + ChatColor.RED + "Hold an item to get a hat!");
					return true;
				}
				playerInv.setHelmet(inHandItem);
				playerInv.removeItem(new ItemStack[]{inHandItem});
				p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You now have a hat!");
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qmute")) {
			if (p.hasPermission("quantumsurvival.qmute")) {
				if (args.length == 1) {
					Player targetPlayer = Bukkit.getPlayer(args[0]);
					if (this.muted.contains(targetPlayer)) {
						targetPlayer.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been unmuted!");
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have unmuted " + targetPlayer.getName());
						this.muted.remove(targetPlayer);
						return true;
					}
					targetPlayer.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been muted!");
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have muted " + targetPlayer.getName());
					this.muted.add(targetPlayer);
					return true;
				}
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qmute <PlayerName>");
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qgod")) {
			if (p.hasPermission("quantumsurvival.qgod")) {
				if (this.godmode.contains(p)) {
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You are no longer in god mode!");
					this.godmode.remove(p);
					return true;
				}
				p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You are now in god mode!");
				this.godmode.add(p);
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qsetmotd")) {
			if (p.hasPermission("quantumsurvival.qsetmotd")) {
				if (args.length == 0) {
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "Please specify a message!");
					return true;
				}
				StringBuilder str = new StringBuilder();
				int i = 0;
				while (i < args.length) {
					str.append(String.valueOf(args[i]) + " ");
					++i;
				}
				String motd = str.toString();
				this.getConfig().set("MOTD.system", motd);
				this.saveConfig();
				String system = this.getConfig().getString("MOTD.system");
				system = system.replaceAll("&", "\u00a7");
				sender.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "MOTD: " + ChatColor.GOLD + system);
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qvanish")) {
			if (p.hasPermission("quantumsurvival.qvanish")) {
				if (this.vanished.contains(p)) {
					Player[] arrplayer = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
					int system = arrplayer.length;
					int motd = 0;
					while (motd < system) {
						Player pl = arrplayer[motd];
						pl.showPlayer(p);
						++motd;
					}
					this.vanished.remove(p);
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been unvanished!");
					return true;
				}
				Player[] arrplayer = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);
				int system = arrplayer.length;
				int motd = 0;
				while (motd < system) {
					Player pl = arrplayer[motd];
					pl.hidePlayer(p);
					++motd;
				}
				this.vanished.add(p);
				p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been vanished!");
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qnick")) {
			if (p.hasPermission("quantumsurvival.qnick")) {
				if (args.length == 1) {
					p.setDisplayName(ChatColor.WHITE + "~" + ChatColor.AQUA + args[0] + ChatColor.WHITE + "~");
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "Your nickname is now: " + p.getDisplayName());
					this.getConfig().set("nicknames." + p.getName(), p.getDisplayName());
					this.saveConfig();
					return true;
				}
				if (args.length == 2) {
					Player targetPlayer = Bukkit.getPlayer(args[1]);
					targetPlayer.setDisplayName(ChatColor.WHITE + "~" + ChatColor.AQUA + args[0] + ChatColor.WHITE + "~");
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "That players' nickname is now: " + p.getDisplayName());
					targetPlayer.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "Your nickname is now: " + targetPlayer.getDisplayName());
					this.getConfig().set("nicknames." + p.getName(), p.getDisplayName());
					this.saveConfig();
					return true;
				}
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qnick <Nickname> <Player>");
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qban")) {
			if (p.hasPermission("quantumsurvival.qban")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qban <PlayerName>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				targetPlayer.kickPlayer(String.valueOf(icon) + ChatColor.RED + "You have been banned from this server!");
				targetPlayer.setBanned(true);
				Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + targetPlayer.getName() + ChatColor.DARK_AQUA + " Has been banned by " + ChatColor.GOLD + p.getName()));
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qkick")) {
			if (p.hasPermission("quantumsurvival.qkick")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qkick <PlayerName>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				targetPlayer.kickPlayer(String.valueOf(icon) + ChatColor.RED + "You have been kicked from this server!");
				Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + targetPlayer.getName() + ChatColor.DARK_AQUA + " Has been kicked by " + ChatColor.GOLD + p.getName()));
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qdeop")) {
			if (p.hasPermission("quantumsurvival.qdeop")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qdeop <PlayerName>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				targetPlayer.setOp(false);
				Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + targetPlayer.getName() + ChatColor.DARK_AQUA + " Has been deopped by " + ChatColor.GOLD + p.getName()));
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qheal")) {
			if (p.hasPermission("quantumsurvival.qheal")) {
				if (this.cooldown.contains(p)) {
					p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You must wait 10s before using (/qheal) again!");
					return true;
				}
				p.setHealth(20);
				p.setFoodLevel(20);
				p.setFireTicks(0);
				p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been healed!");
				this.cooldown.add(p);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {

					@Override
					public void run() {
						QuantumSurvival.this.cooldown.remove(p);
					}
				}, 200L);
				return true;
			}
			p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (commandLabel.equalsIgnoreCase("qsinfo")) {
			p.sendMessage(ChatColor.GOLD + "View Distance: " + ChatColor.DARK_AQUA + Bukkit.getViewDistance());
			p.sendMessage(ChatColor.GOLD + "Server MOTD: " + ChatColor.DARK_AQUA + Bukkit.getMotd());
			p.sendMessage(ChatColor.GOLD + "Server IP: " + ChatColor.DARK_AQUA + Bukkit.getIp() + Bukkit.getPort());
			return false;
		} else {
			if (commandLabel.equalsIgnoreCase("qsetspawn")) {
				if (p.hasPermission("quantumsurvival.setspawn")) {
					p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
					p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "You have set the QuantumSurvival spawn!");
					return true;
				}
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (commandLabel.equalsIgnoreCase("qspawn")) {
				p.teleport(p.getWorld().getSpawnLocation());
				p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "You have now entered the QuantumSurvival spawn!");
				return true;
			}
			if (commandLabel.equalsIgnoreCase("qfakeleave")) {
				if (p.hasPermission("quantumsurvival.qfakeleave")) {
					if (args.length == 0) {
						p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qfakeleave <Player-Name>");
					}
					OfflinePlayer targetPlayer = p.getServer().getOfflinePlayer(args[0]);
					Bukkit.broadcastMessage((ChatColor.YELLOW + "(" + ChatColor.DARK_RED + "-" + ChatColor.YELLOW + ")" + ChatColor.AQUA + " " + targetPlayer.getName()));
					return false;
				} else {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				}
				return false;
			} else {
				if (commandLabel.equalsIgnoreCase("qfakejoin")) {
					if (p.hasPermission("quantumsurvival.qfakejoin")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qfakejoin <Player-Name>");
							return true;
						}
						OfflinePlayer targetPlayer = p.getServer().getOfflinePlayer(args[0]);
						Bukkit.broadcastMessage((ChatColor.YELLOW + "(" + ChatColor.DARK_GREEN + "+" + ChatColor.YELLOW + ")" + ChatColor.AQUA + " " + targetPlayer.getName()));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qfly")) {
					if (!p.hasPermission("quantumsurvival.qfly")) return false;
					if (!this.fly.contains(p.getName())) {
						this.fly.add(p.getName());
						p.setAllowFlight(true);
						p.setFlying(true);
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "You now have flight enabled");
						return true;
					}
					this.fly.remove(p.getName());
					p.setAllowFlight(false);
					p.setFlying(false);
					p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "You now have flight disabled");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qfeed")) {
					if (p.hasPermission("quantumsurvival.qfeed")) {
						if (this.cooldown3.contains(p)) {
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You must wait 10s before using (/qfeed) again!");
							return true;
						}
						p.setFoodLevel(20);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been fed!");
						this.cooldown3.add(p);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {

							@Override
							public void run() {
								QuantumSurvival.this.cooldown3.remove(p);
							}
						}, 200L);
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qxp")) {
					if (p.hasPermission("quantumsurvival.qxp")) {
						if (this.cooldown2.contains(p)) {
							p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You must wait 40s before using (/qxp) again!");
							return true;
						}
						p.setLevel(100);
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You now know everything to be known for now..");
						this.cooldown2.add(p);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {

							@Override
							public void run() {
								QuantumSurvival.this.cooldown2.remove(p);
							}
						}, 800L);
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qkill")) {
					if (p.hasPermission("quantumsurvival.qkill")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qkill <Player-Name>");
							return true;
						}
						Player p2 = p.getServer().getPlayer(args[0]);
						p2.setHealth(0);
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "You killed " + p2.getName());
						Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + p.getName() + ChatColor.DARK_AQUA + " Did /qkill on " + p2.getName()));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qsuicide")) {
					if (p.hasPermission("quantumsurvival.qsuicide")) {
						p.setHealth(0);
						Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + p.getName() + ChatColor.DARK_AQUA + " No longer wanted to live.."));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qbroadcast")) {
					if (p.hasPermission("quantumsurvival.qbroadcast")) {
						if (args.length < 1) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qbroadcast <Message>");
							return true;
						}
						String cast = "";
						String[] arrstring = args;
						int n = arrstring.length;
						int system = 0;
						while (system < n) {
							String str = arrstring[system];
							cast = String.valueOf(cast) + str + " ";
							++system;
						}
						Bukkit.broadcastMessage((ChatColor.GOLD + "[" + ChatColor.RED + "Broadcast" + ChatColor.GOLD + "] " + ChatColor.AQUA + ChatColor.translateAlternateColorCodes((char) '&', cast)));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qclear")) {
					if (p.hasPermission("quantumsurvival.qclear")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qclear <Player-Name>");
							return true;
						}
						Player p2 = p.getServer().getPlayer(args[0]);
						p2.getInventory().clear();
						p2.sendMessage(String.valueOf(icon) + ChatColor.GOLD + " Your inventory has been cleared by " + p.getName());
						p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "You have cleared " + ChatColor.GOLD + p2.getName() + "'s " + ChatColor.DARK_AQUA + "Inventory");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qreload")) {
					if (p.hasPermission("quantumsurvival.qreload")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /qreload <Plugin-Name>");
							return true;
						}
						if (!args[0].equalsIgnoreCase("QuantumSurvival")) return true;
						if (args.length == 1) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /qreload QuantumSurvival <Both/Enable/Disable>");
							return true;
						}
						if (args[1].equalsIgnoreCase("Both")) {
							p.getServer().getPluginManager().disablePlugin((Plugin) this);
							p.getServer().getPluginManager().enablePlugin((Plugin) this);
							Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GRAY + "Reloaded Plugin: " + ChatColor.GOLD + "Q-S"));
							return true;
						}
						if (args[1].equalsIgnoreCase("Enable")) {
							p.getServer().getPluginManager().enablePlugin((Plugin) this);
							Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GRAY + "Enabled Plugin: " + ChatColor.GOLD + "Q-S"));
							return true;
						}
						if (args[1].equalsIgnoreCase("Disable")) {
							if (p.hasPermission("quantumsurvival.qreload.disable")) {
								p.getServer().getPluginManager().disablePlugin((Plugin) this);
								Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GRAY + "Enabled Plugin: " + ChatColor.GOLD + "Q-S"));
								return true;
							}
							p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
							return true;
						}
						p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /qreload QuantumSurvival <Both/Enable/Disable>");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qgm")) {
					if (p.hasPermission("quantumsurvival.qgm")) {
						if (args.length == 0) {
							if (p.hasPermission("quantumsurvival.qgm.others")) {
								p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/gm <0/1/2> <Player-Name>");
								return true;
							}
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/gm <0/1/2>");
							return true;
						}
						if (args[0].equalsIgnoreCase("0")) {
							Player p2 = p.getServer().getPlayer(args[1]);
							if (p.hasPermission("quantumsurvival.qgm.others")) {
								if (args.length == 1) {
									p.setGameMode(GameMode.SURVIVAL);
									p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Survival");
									return true;
								}
								p2.setGameMode(GameMode.SURVIVAL);
								p2.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Survival");
								p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Changed Gamemode For Player: " + ChatColor.GOLD + p2.getName());
								return true;
							}
							p.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Survival");
							return true;
						}
						if (args[0].equalsIgnoreCase("1")) {
							Player p2 = p.getServer().getPlayer(args[1]);
							if (p.hasPermission("quantumsurvival.qgm.others")) {
								if (args.length == 1) {
									p.setGameMode(GameMode.CREATIVE);
									p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Survival");
									return true;
								}
								p2.setGameMode(GameMode.CREATIVE);
								p2.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Creative");
								p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Changed Gamemode For Player: " + ChatColor.GOLD + p2.getName());
								return true;
							}
							p.setGameMode(GameMode.CREATIVE);
							p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Creative");
							return true;
						}
						if (!args[0].equalsIgnoreCase("2")) return true;
						Player p2 = p.getServer().getPlayer(args[1]);
						if (p.hasPermission("quantumsurvival.qgm.others")) {
							if (args.length == 1) {
								p.setGameMode(GameMode.ADVENTURE);
								p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Survival");
								return true;
							}
							p2.setGameMode(GameMode.ADVENTURE);
							p2.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Adventure");
							p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Changed Gamemode For Player: " + ChatColor.GOLD + p2.getName());
							return true;
						}
						p.setGameMode(GameMode.ADVENTURE);
						p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "Gamemode-Changed: " + ChatColor.GOLD + "Adventure");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qsmite")) {
					if (p.hasPermission("quantumsurvival.qsmite")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qsmite <Player-Name>");
							return true;
						}
						World world = p.getWorld();
						Player targetPlayer = p.getServer().getPlayer(args[0]);
						Location targetPlayerLocation = targetPlayer.getLocation();
						world.strikeLightning(targetPlayerLocation);
						targetPlayer.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have been smited, behave next time!");
						p.sendMessage(String.valueOf(icon) + ChatColor.DARK_AQUA + "You have smited " + targetPlayer.getName());
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qpinfo")) {
					if (p.hasPermission("quantumsurvival.qpinfo")) {
						if (args.length == 0) {
							p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ": " + ChatColor.GREEN + "/qpinfo <Player-Name>");
							return true;
						}
						if (args.length != 1) return true;
						Player tPlayer = p.getServer().getPlayer(args[0]);
						p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Username: " + ChatColor.AQUA + tPlayer.getName());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "IP: " + ChatColor.AQUA + tPlayer.getAddress());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Gamemode: " + ChatColor.AQUA + tPlayer.getGameMode());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "XP: " + ChatColor.AQUA + tPlayer.getExp());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Flying: " + ChatColor.AQUA + tPlayer.isFlying());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Fire: " + ChatColor.AQUA + tPlayer.getFireTicks());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Opped: " + ChatColor.AQUA + tPlayer.isOp());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Whitelisted: " + ChatColor.AQUA + tPlayer.isWhitelisted());
						p.sendMessage(String.valueOf(icon) + ChatColor.GREEN + "Banned: " + ChatColor.AQUA + tPlayer.isBanned());
						p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qrepair")) {
					if (p.hasPermission("quantumsurvival.qrepair")) {
						p.getItemInHand().setDurability((short) 0);
						p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "Repaired Item In Hand!");
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (commandLabel.equalsIgnoreCase("qclearchat")) {
					if (p.hasPermission("quantumsurvival.qclearchat")) {
						double timer = 0.0;
						while (timer < 100.0) {
							timer += 1.0;
							Bukkit.broadcastMessage("");
						}
						timer = 0.0;
						Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + "-----------------------------------------------------------------------------"));
						Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GREEN + " Your chat was cleared by " + ChatColor.RED + p.getName()));
						Bukkit.broadcastMessage((String.valueOf(icon) + ChatColor.GOLD + "-----------------------------------------------------------------------------"));
						return true;
					}
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (!commandLabel.equalsIgnoreCase("qhelp")) return false;
				if (args.length == 0) {
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[1]" + ChatColor.DARK_AQUA + " /qheal");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[2]" + ChatColor.DARK_AQUA + " /qfeed");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[3]" + ChatColor.DARK_AQUA + " /qxp");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[4]" + ChatColor.DARK_AQUA + " /qclear");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[5]" + ChatColor.DARK_AQUA + " /qkill");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[6]" + ChatColor.DARK_AQUA + " /qclearchat");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[7]" + ChatColor.DARK_AQUA + " /qrepair");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[8]" + ChatColor.DARK_AQUA + " /qpinfo");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[9]" + ChatColor.DARK_AQUA + " /qbroadcast");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[10]" + ChatColor.DARK_AQUA + " /qsuicide");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[11]" + ChatColor.DARK_AQUA + " /qsmite");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[12]" + ChatColor.DARK_AQUA + " /qhelp");
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					return true;
				}
				if (args[0].equalsIgnoreCase("1")) {
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[1]" + ChatColor.DARK_AQUA + " /qheal");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[2]" + ChatColor.DARK_AQUA + " /qfeed");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[3]" + ChatColor.DARK_AQUA + " /qxp");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[4]" + ChatColor.DARK_AQUA + " /qclear");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[5]" + ChatColor.DARK_AQUA + " /qkill");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[6]" + ChatColor.DARK_AQUA + " /qclearchat");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[7]" + ChatColor.DARK_AQUA + " /qrepair");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[8]" + ChatColor.DARK_AQUA + " /qpinfo");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[9]" + ChatColor.DARK_AQUA + " /qbroadcast");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[10]" + ChatColor.DARK_AQUA + " /qsuicide");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[11]" + ChatColor.DARK_AQUA + " /qsmite");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[12]" + ChatColor.DARK_AQUA + " /qhelp");
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					return true;
				}
				if (args[0].equalsIgnoreCase("2")) {
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[13]" + ChatColor.DARK_AQUA + " /qfly");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[14]" + ChatColor.DARK_AQUA + " /qfakejoin");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[15]" + ChatColor.DARK_AQUA + " /qfakeleave");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[16]" + ChatColor.DARK_AQUA + " /qsetspawn");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[17]" + ChatColor.DARK_AQUA + " /qspawn");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[18]" + ChatColor.DARK_AQUA + " /qreload");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[19]" + ChatColor.DARK_AQUA + " /qop");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[20]" + ChatColor.DARK_AQUA + " /qdeop");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[21]" + ChatColor.DARK_AQUA + " /qban");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[22]" + ChatColor.DARK_AQUA + " /qkick");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[23]" + ChatColor.DARK_AQUA + " /qsetmotd");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[24]" + ChatColor.DARK_AQUA + " /qnick");
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					return true;
				}
				if (args[0].equalsIgnoreCase("3")) {
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[25]" + ChatColor.DARK_AQUA + " /qvanish");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[26]" + ChatColor.DARK_AQUA + " /qgod");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[27]" + ChatColor.DARK_AQUA + " /qmute");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[28]" + ChatColor.DARK_AQUA + " /qhat");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[28]" + ChatColor.DARK_AQUA + " /qenchant");
					p.sendMessage(String.valueOf(icon) + ChatColor.GOLD + "[28]" + ChatColor.DARK_AQUA + " /qgive");
					p.sendMessage(String.valueOf(icon) + ChatColor.GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
					return true;
				}
				p.sendMessage(ChatColor.AQUA + "Usage" + ChatColor.GRAY + ":" + ChatColor.GREEN + " /qhelp <Page-Number>");
				return true;
			}
		}
	}

}

