package me.skorrloregaming.lockette;

import me.skorrloregaming.$;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.Set;

public class LockettePlayerListener implements Listener {
	private static Lockette plugin;

	public LockettePlayerListener(Lockette instance) {
		plugin = instance;
	}

	protected void registerEvents() {
		PluginManager pm = Server.getInstance().getPlugin().getServer().getPluginManager();
		pm.registerEvents(this, Server.getInstance().getPlugin());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (!$.validLocketteMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase()))
			return;
		String[] command = event.getMessage().split(" ", 3);
		if (command.length < 1)
			return;
		if (!(command[0].equalsIgnoreCase("/lockette") || command[0].equalsIgnoreCase("/lock")))
			return;
		event.setCancelled(true);
		if (command.length == 2) {
			if (command[1].equalsIgnoreCase("reload")) {
				if (!plugin.hasPermission(player.getWorld(), player, "lockette.admin.reload"))
					return;
				plugin.loadProperties(true);
				plugin.localizedMessage(player, Lockette.broadcastReloadTarget, "msg-admin-reload");
				return;
			}
			if (command[1].equalsIgnoreCase("version")) {
				player.sendMessage(ChatColor.RED + "Lockette version " + Server.getInstance().getPlugin().getDescription().getVersion() + " loaded.");
				return;
			}
			if (command[1].equalsIgnoreCase("fix")) {
				if (fixDoor(player)) {
					plugin.localizedMessage(player, null, "msg-error-fix");
				}
				return;
			}
		} else if (command.length == 3) {
			if (command[1].equalsIgnoreCase("debug")) {
				Lockette.DEBUG = Boolean.parseBoolean(command[2]);
				player.sendMessage(ChatColor.RED + "[" + Lockette.pluginName + "] DEBUG mode is set to " + Lockette.DEBUG);
				return;
			}
		}
		if ((command.length == 2) || (command.length == 3)) {
			if (command[1].equals("1") || command[1].equals("2") || command[1].equals("3") || command[1].equals("4")) {
				Block block = plugin.playerList.get(player.getName());
				if (block == null) {
					plugin.localizedMessage(player, null, "msg-error-edit");
					return;
				} else if (!$.isWallSign(block.getType())) {
					plugin.localizedMessage(player, null, "msg-error-edit");
					return;
				}
				Sign sign = (Sign) block.getState();
				Sign owner = sign;
				String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
				boolean privateSign;
				if (text.equals("[private]") || text.equalsIgnoreCase(Lockette.altPrivate))
					privateSign = true;
				else if (text.equals("[more users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) {
					privateSign = false;
					Block checkBlock = Lockette.getSignAttachedBlock(block);
					if (checkBlock == null) {
						plugin.localizedMessage(player, null, "msg-error-edit");
						return;
					}
					Block signBlock = Lockette.findBlockOwner(checkBlock);
					if (signBlock == null) {
						plugin.localizedMessage(player, null, "msg-error-edit");
						return;
					}
					owner = (Sign) signBlock.getState();
				} else {
					plugin.localizedMessage(player, null, "msg-error-edit");
					return;
				}
				if (Lockette.isOwner(owner, player) || Lockette.debugMode) {
					int line = Integer.parseInt(command[1]) - 1;
					if (!Lockette.debugMode) {
						if (line <= 0)
							return;
						if (line <= 1) {
							if (privateSign && (command.length < 3 || command[2].isEmpty() || !(command[2].replaceAll("&([0-9A-Fa-f])", "").equalsIgnoreCase(player.getName())))) {
								return;
							}
						}
					}
					if (command.length == 3) {
						String id = (!Lockette.colorTags) ? command[2].replaceAll("&([0-9A-Fa-f])", "") : command[2];
						if (Lockette.DEBUG) {
							Logger.debug("[" + Lockette.pluginName + "] striped name = " + command[2].replaceAll("&([0-9A-Fa-f])", ""));
							Logger.debug("[" + Lockette.pluginName + "] player name = " + player.getName());
						}
						Lockette.setLine(sign, line, id);
					} else
						Lockette.setLine(sign, line, "");
					sign.update();
					plugin.localizedMessage(player, null, "msg-owner-edit");
					return;
				} else {
					plugin.localizedMessage(player, null, "msg-error-edit");
					return;
				}
			}
		}
		plugin.localizedMessage(player, null, "msg-help-command1");
		plugin.localizedMessage(player, null, "msg-help-command2");
		plugin.localizedMessage(player, null, "msg-help-command3");
		plugin.localizedMessage(player, null, "msg-help-command4");
		plugin.localizedMessage(player, null, "msg-help-command5");
		plugin.localizedMessage(player, null, "msg-help-command6");
		plugin.localizedMessage(player, null, "msg-help-command7");
		plugin.localizedMessage(player, null, "msg-help-command8");
		plugin.localizedMessage(player, null, "msg-help-command9");
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!$.validLocketteMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase()))
			return;
		if (!event.hasBlock())
			return;
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		Material type = block.getType();
		BlockFace face = event.getBlockFace();
		ItemStack item;
		if (action == Action.RIGHT_CLICK_BLOCK) {
			if ((Lockette.protectTrapDoors) && (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors))) {
				if (interactDoor(block, player))
					return;
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				return;
			}
			if ((Lockette.protectDoors) && (BlockUtil.isInList(type, BlockUtil.materialListDoors))) {
				if (interactDoor(block, player))
					return;
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				return;
			}
			if ($.isWallSign(type)) {
				interactSign(block, player);
				return;
			}
			if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors) || Lockette.isInList(type, Lockette.customBlockList)) {
				if (Lockette.directPlacement)
					if (event.hasItem())
						if ((face != BlockFace.UP) && (face != BlockFace.DOWN)) {
							item = event.getItem();
							if ($.isPostSign(item.getType())) {
								Block checkBlock = block.getRelative(face);
								type = checkBlock.getType();
								if (type == Material.AIR) {
									boolean place = false;
									if (Lockette.isProtected(block)) {
										if (Lockette.isOwner(block, player))
											place = true;
									} else
										place = true;
									if (place) {
										event.setUseItemInHand(Result.ALLOW);
										event.setUseInteractedBlock(Result.ALLOW);
										return;
									}
								}
							}
						}
				if (interactContainer(block, player))
					return;
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				return;
			}
			if (type == Material.DIRT)
				if (event.hasItem()) {
					item = event.getItem();
					type = item.getType();
					if ((type == Material.DIAMOND_HOE) || (type == Material.GOLDEN_HOE) || (type == Material.IRON_HOE) || (type == Material.STONE_HOE) || (type == Material.WOODEN_HOE)) {
						Block checkBlock = block.getRelative(BlockFace.UP);
						type = checkBlock.getType();
						if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
							event.setUseInteractedBlock(Result.DENY);
							return;
						}
						if (hasAttachedTrapDoor(block)) {
							event.setUseInteractedBlock(Result.DENY);
							return;
						}
					}
				}
		} else if (action == Action.LEFT_CLICK_BLOCK) {
			if (Lockette.protectTrapDoors)
				if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
					if (interactDoor(block, player))
						return;
					event.setUseInteractedBlock(Result.DENY);
					event.setUseItemInHand(Result.DENY);
					return;
				}
			if (Lockette.protectDoors)
				if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
					if (interactDoor(block, player))
						return;
					event.setUseInteractedBlock(Result.DENY);
					event.setUseItemInHand(Result.DENY);
					return;
				}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.playerList.remove(player.getName());
	}

	private static boolean interactDoor(Block block, Player player) {
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return true;
		boolean wooden = BlockUtil.isInList(block.getType(), BlockUtil.materialListWoodenDoors);
		boolean trap = false;
		if (Lockette.protectTrapDoors)
			if (BlockUtil.isInList(block.getType(), BlockUtil.materialListTrapDoors)) {
				trap = true;
			}
		boolean allow = false;
		if (canInteract(block, signBlock, player, true))
			allow = true;
		if (allow) {
			List<Block> list = Lockette.toggleDoors(block, Lockette.getSignAttachedBlock(signBlock), wooden, trap);
			int delta = Lockette.getSignOption(signBlock, "timer", Lockette.altTimer, Lockette.defaultDoorTimer);
			plugin.doorCloser.add(list, delta != 0, delta);
			return (true);
		}
		if (block.equals(plugin.playerList.get(player.getName())))
			return (false);
		plugin.playerList.put(player.getName(), block);
		plugin.localizedMessage(player, null, "msg-user-denied-door");
		return (false);
	}

	private static void interactSign(Block block, Player player) {
		Sign sign = (Sign) block.getState();
		String text = ChatColor.stripColor(sign.getLine(0)).toLowerCase();
		if (text.equals("[private]") || text.equalsIgnoreCase(Lockette.altPrivate)) {
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] private sign!");
			}
		} else if (text.equals("[more users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) {
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] more users sign!");
			}
			Block checkBlock = Lockette.getSignAttachedBlock(block);
			if (checkBlock == null)
				return;
			Block signBlock = Lockette.findBlockOwner(checkBlock);
			if (signBlock == null)
				return;
			sign = (Sign) signBlock.getState();
		} else
			return;
		if (Lockette.isOwner(sign, player) || Lockette.debugMode) {
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] you're the owner!!");
			}
			if (!block.equals(plugin.playerList.get(player.getName()))) {
				plugin.playerList.put(player.getName(), block);
				plugin.localizedMessage(player, null, "msg-help-select");
			}
		} else {
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] notify!!");
			}
			if (!block.equals(plugin.playerList.get(player.getName()))) {
				plugin.playerList.put(player.getName(), block);
				plugin.localizedMessage(player, null, "msg-user-touch-owned", sign.getLine(1));
			}
		}
	}

	private static boolean interactContainer(Block block, Player player) {
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return (true);
		if (canInteract(block, signBlock, player, false))
			return (true);
		if (block.equals(plugin.playerList.get(player.getName())))
			return (false);
		plugin.playerList.put(player.getName(), block);
		plugin.localizedMessage(player, null, "msg-user-denied");
		return (false);
	}

	private static boolean canInteract(Block block, Block signBlock, Player player, boolean isDoor) {
		Sign sign = (Sign) signBlock.getState();
		if (Lockette.isUser(block, player, true))
			return true;
		boolean snoop = false;
		if (isDoor) {
			if (Lockette.adminBypass) {
				if (plugin.hasPermission(block.getWorld(), player, "lockette.admin.bypass"))
					snoop = true;
				if (snoop) {
					Logger.debug("[" + Lockette.pluginName + "] (Admin) " + player.getName() + " has bypassed a door owned by " + sign.getLine(1));
					plugin.localizedMessage(player, null, "msg-admin-bypass", sign.getLine(1));
					return (true);
				}
			}
		} else if (Lockette.adminSnoop) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.admin.snoop"))
				snoop = true;
			if (snoop) {
				Logger.debug("[" + Lockette.pluginName + "] (Admin) " + player.getName() + " has snooped around in a container owned by " + sign.getLine(1) + "!");
				plugin.localizedMessage(player, Lockette.broadcastSnoopTarget, "msg-admin-snoop", sign.getLine(1));
				return (true);
			}
		}
		return (false);
	}

	private static boolean fixDoor(Player player) {
		Block block = player.getTargetBlock((Set<Material>) null, 10);
		Material type = block.getType();
		boolean doCheck = false;
		if (Lockette.protectTrapDoors) {
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors))
				doCheck = true;
		}
		if (Lockette.protectDoors) {
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors))
				doCheck = true;
		}
		if (!doCheck)
			return (true);
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return (true);
		if (Lockette.isOwner(block, player)) {
			Lockette.toggleSingleDoor(block);
			return (false);
		}
		return (true);
	}

	public static boolean hasAttachedTrapDoor(Block block) {
		Block checkBlock;
		Material type = Material.AIR;
		int face;
		checkBlock = block.getRelative(BlockFace.NORTH);
		type = checkBlock.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
			face = checkBlock.getData() & 0x3;
			if (face == 2)
				return (true);
		}
		checkBlock = block.getRelative(BlockFace.EAST);
		type = checkBlock.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
			face = checkBlock.getData() & 0x3;
			if (face == 0)
				return (true);
		}
		checkBlock = block.getRelative(BlockFace.SOUTH);
		type = checkBlock.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
			face = checkBlock.getData() & 0x3;
			if (face == 3)
				return (true);
		}
		checkBlock = block.getRelative(BlockFace.WEST);
		type = checkBlock.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
			face = checkBlock.getData() & 0x3;
			if (face == 1)
				return (true);
		}
		return (false);
	}
}
