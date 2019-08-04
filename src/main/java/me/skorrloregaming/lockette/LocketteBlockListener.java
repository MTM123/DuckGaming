package me.skorrloregaming.lockette;

import me.skorrloregaming.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.TrapDoor;
import org.bukkit.plugin.PluginManager;

import java.util.List;

import me.skorrloregaming.*;

public class LocketteBlockListener implements Listener {
	private static Lockette plugin;

	public LocketteBlockListener(Lockette instance) {
		plugin = instance;
	}

	protected void registerEvents() {
		PluginManager pm = ServerGet.get().getPlugin().getServer().getPluginManager();
		pm.registerEvents(this, ServerGet.get().getPlugin());
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!$.validLocketteMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase()))
			return;
		Block block = event.getBlock();
		Material type = block.getType();
		if (event.isCancelled())
			if (!BlockUtil.isInList(type, BlockUtil.materialListJustDoors))
				return;
		if ($.isWallSign(type)) {
			if (block.getData() == 0) {
				block.getState().setData(new MaterialData(block.getType(), (byte) 5));
			}
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0));
			if (text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate)) {
				if (Lockette.isOwner(sign, player)) {
					Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " has released a container.");
					Lockette.removeUUIDMetadata(sign);
					plugin.localizedMessage(player, null, "msg-owner-release");
					return;
				}
				if (Lockette.adminBreak) {
					boolean snoop = false;
					if (plugin.hasPermission(block.getWorld(), player, "lockette.admin.break"))
						snoop = true;
					if (snoop) {
						Logger.debug("[" + Lockette.pluginName + "] (Admin) " + player.getName() + " has broken open a container owned by " + sign.getLine(1) + "!");
						Lockette.removeUUIDMetadata(sign);
						plugin.localizedMessage(player, Lockette.broadcastBreakTarget, "msg-admin-release", sign.getLine(1));
						return;
					}
				}
				event.setCancelled(true);
				sign.update();
				plugin.localizedMessage(player, null, "msg-user-release-owned", sign.getLine(1));
			} else if (text.equalsIgnoreCase("[More Users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) {
				Block checkBlock = Lockette.getSignAttachedBlock(block);
				if (checkBlock == null)
					return;
				Block signBlock = Lockette.findBlockOwner(checkBlock);
				if (signBlock == null)
					return;
				Sign sign2 = (Sign) signBlock.getState();
				if (Lockette.isOwner(sign2, player)) {
					Lockette.removeUUIDMetadata(sign);
					plugin.localizedMessage(player, null, "msg-owner-remove");
					return;
				}
				event.setCancelled(true);
				sign.update();
				plugin.localizedMessage(player, null, "msg-user-remove-owned", sign2.getLine(1));
			}
		} else {
			Block signBlock = Lockette.findBlockOwner(block);
			if (signBlock == null)
				return;
			Sign sign = (Sign) signBlock.getState();
			if (Lockette.isOwner(sign, player)) {
				signBlock = Lockette.findBlockOwnerBreak(block);
				if (signBlock != null) {
					sign = (Sign) signBlock.getState();
					Lockette.removeUUIDMetadata(sign);
					Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " has released a container.");
				}
				return;
			}
			event.setCancelled(true);
			plugin.localizedMessage(player, null, "msg-user-break-owned", sign.getLine(1));
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		Block block = event.getBlock();
		if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(block.getWorld()).toString().toLowerCase()))
			return;
		Block checkBlock;
		List<Block> blockList = event.getBlocks();
		int x, count = blockList.size();
		for (x = 0; x < count; ++x) {
			checkBlock = blockList.get(x);
			if (Lockette.isProtected(checkBlock)) {
				event.setCancelled(true);
				return;
			}
		}
		checkBlock = block.getRelative(Lockette.getPistonFacing(block), event.getBlocks().size() + 1);
		if (Lockette.isProtected(checkBlock)) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		if (!(event.isSticky()))
			return;
		Block block = event.getBlock();
		if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(block.getWorld()).toString().toLowerCase()))
			return;
		Block checkBlock = block.getRelative(Lockette.getPistonFacing(block), 2);
		Material type = checkBlock.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors))
			return;
		if (BlockUtil.isInList(type, BlockUtil.materialListJustDoors))
			return;
		if (Lockette.isProtected(checkBlock))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (!$.validLocketteMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase()))
			return;
		Block block = event.getBlockPlaced();
		Material type = block.getType();
		Block against = event.getBlockAgainst();
		Block checkBlock;
		Block signBlock;
		if ($.isWallSign(against.getType())) {
			Sign sign = (Sign) against.getState();
			String text = ChatColor.stripColor(sign.getLine(0));
			if (text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate) || text.equalsIgnoreCase("[More Psers]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) {
				event.setCancelled(true);
				return;
			}
		}
		if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
			if (canBuildDoor(block, against, player))
				return;
			event.setCancelled(true);
			plugin.localizedMessage(player, null, "msg-user-conflict-door");
			return;
		}
		if (Lockette.directPlacement) {
			if ($.isWallSign(type)) {
				checkBlock = Lockette.getSignAttachedBlock(block);
				if (checkBlock == null)
					return;
				type = checkBlock.getType();
				if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors) || BlockUtil.isInList(type, BlockUtil.materialListDoors) || Lockette.isInList(type, Lockette.customBlockList)) {
					Sign sign = (Sign) block.getState();
					if (Lockette.isProtected(checkBlock)) {
						if (Lockette.isOwner(checkBlock, player)) {
							sign.setLine(0, Lockette.altMoreUsers);
							sign.setLine(1, Lockette.altEveryone);
							sign.setLine(2, "");
							sign.setLine(3, "");
							sign.update(true);
							plugin.localizedMessage(player, null, "msg-owner-adduser");
						} else
							event.setCancelled(true);
						return;
					} else {
						if (!checkPermissions(player, block, checkBlock)) {
							event.setCancelled(true);
							plugin.localizedMessage(player, null, "msg-error-permission");
							return;
						}
						sign.setLine(0, Lockette.altPrivate);
						Lockette.setLine(sign, 1, player.getName());
						sign.setLine(2, "");
						sign.setLine(3, "");
						sign.update(true);
						Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " has protected a block or door.");
						plugin.localizedMessage(player, null, "msg-owner-claim");
					}
				}
				return;
			}
		}
		if (BlockUtil.isInList(type, BlockUtil.materialListChests)) {
			signBlock = Lockette.findBlockOwner(block);
			if (signBlock != null) {
				Sign sign = (Sign) signBlock.getState();
				if (Lockette.isOwner(sign, player))
					return;
				event.setCancelled(true);
				plugin.localizedMessage(player, null, "msg-user-resize-owned", sign.getLine(1));
			} else {
				if (plugin.playerList.get(player.getName()) == null) {
					plugin.playerList.put(player.getName(), block);
					plugin.localizedMessage(player, null, "msg-help-chest");
				}
			}
		}
		if (type == Material.HOPPER) {
			checkBlock = block.getRelative(BlockFace.UP);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors) || Lockette.isInList(type, Lockette.customBlockList)) {
				if (!validateOwner(checkBlock, player)) {
					event.setCancelled(true);
					plugin.localizedMessage(player, null, "msg-user-denied");
					return;
				}
			}
			checkBlock = block.getRelative(BlockFace.DOWN);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors) || Lockette.isInList(type, Lockette.customBlockList)) {
				if (!validateOwner(checkBlock, player)) {
					event.setCancelled(true);
					plugin.localizedMessage(player, null, "msg-user-denied");
					return;
				}
			}
		}
	}

	private boolean checkPermissions(Player player, Block block, Block checkBlock) {
		Material type = checkBlock.getType();
		boolean create = false;
		if (plugin.hasPermission(block.getWorld(), player, "lockette.create.all"))
			create = true;
		else if (BlockUtil.isInList(type, BlockUtil.materialListChests)) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.chest"))
				create = true;
		} else if ((type == Material.FURNACE)) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.furnace"))
				create = true;
		} else if (type == Material.DISPENSER) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.dispenser"))
				create = true;
		} else if (type == Material.DROPPER) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.dropper"))
				create = true;
		} else if (type == Material.BREWING_STAND) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.brewingstand"))
				create = true;
		} else if (Lockette.isInList(type, Lockette.customBlockList)) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.custom"))
				create = true;
		} else if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
			if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.door"))
				create = true;
		}
		return create;
	}

	private boolean validateOwner(Block block, Player player) {
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return true;
		Sign sign = (Sign) signBlock.getState();
		if (Lockette.isOwner(sign, player))
			return true;
		return false;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		Block block = event.getBlock();
		if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(block.getWorld()).toString().toLowerCase()))
			return;
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
		if (doCheck) {
			Block signBlock = Lockette.findBlockOwner(block);
			if (signBlock == null)
				return;
			Sign sign = (Sign) signBlock.getState();
			String line;
			for (int y = 1; y <= 3; ++y)
				if (!sign.getLine(y).isEmpty()) {
					line = ChatColor.stripColor(sign.getLine(y));
					if (line.equalsIgnoreCase("[Everyone]") || line.equalsIgnoreCase(Lockette.altEveryone))
						return;
				}
			List<Block> list = Lockette.findBlockUsers(block, signBlock);
			for (Block blk : list) {
				sign = (Sign) blk.getState();
				for (int y = 1; y <= 3; ++y)
					if (!sign.getLine(y).isEmpty()) {
						line = ChatColor.stripColor(sign.getLine(y));
						if (line.equalsIgnoreCase("[Everyone]") || line.equalsIgnoreCase(Lockette.altEveryone))
							return;
					}
			}
			event.setNewCurrent(event.getOldCurrent());
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if (!$.validLocketteMinigames.contains($.getCurrentMinigame(player).toString().toLowerCase()))
			return;
		Block block = event.getBlock();
		Material blockType = block.getType();
		boolean typeWallSign = ($.isWallSign(blockType));
		boolean typeSignPost = ($.isPostSign(blockType));
		if (typeWallSign) {
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0));
			if ((text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate)) && isEmptyChange(event)) {
				event.setCancelled(true);
				return;
			}
		} else if (typeSignPost) {
		} else {
			event.setCancelled(true);
			return;
		}
		String text = ChatColor.stripColor(event.getLine(0));
		if (text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate)) {
			boolean doChests = true, doFurnaces = true, doDispensers = true, doDroppers = true;
			boolean doBrewingStands = true, doCustoms = true;
			boolean doTrapDoors = true, doDoors = true;
			boolean create = false;
			doChests = false;
			doFurnaces = false;
			doDispensers = false;
			doDroppers = false;
			doBrewingStands = false;
			doCustoms = false;
			doTrapDoors = false;
			doDoors = false;
			if (plugin.hasPermission(block.getWorld(), player, "lockette.create.all")) {
				create = true;
				doChests = true;
				doFurnaces = true;
				doDispensers = true;
				doDroppers = true;
				doBrewingStands = true;
				doCustoms = true;
				doTrapDoors = true;
				doDoors = true;
			} else {
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.chest")) {
					create = true;
					doChests = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.furnace")) {
					create = true;
					doFurnaces = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.dispenser")) {
					create = true;
					doDispensers = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.dropper")) {
					create = true;
					doDroppers = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.brewingstand")) {
					create = true;
					doBrewingStands = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.custom")) {
					create = true;
					doCustoms = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.trapdoor")) {
					create = true;
					doTrapDoors = true;
				}
				if (plugin.hasPermission(block.getWorld(), player, "lockette.user.create.door")) {
					create = true;
					doDoors = true;
				}
			}
			if (!create) {
				event.setLine(0, "[?]");
				plugin.localizedMessage(player, null, "msg-error-permission");
				return;
			}
			int x;
			Block checkBlock[] = new Block[4];
			BlockFace face = null;
			int type = 0;
			boolean conflict = false;
			boolean deny = false;
			boolean zonedeny = false;
			if (Lockette.protectTrapDoors)
				if (typeWallSign) {
					checkBlock[3] = Lockette.getSignAttachedBlock(block);
					if (checkBlock[3] != null)
						if (!BlockUtil.isInList(checkBlock[3].getType(), BlockUtil.materialListBad)) {
							checkBlock[0] = checkBlock[3].getRelative(BlockFace.NORTH);
							checkBlock[1] = checkBlock[3].getRelative(BlockFace.EAST);
							checkBlock[2] = checkBlock[3].getRelative(BlockFace.SOUTH);
							checkBlock[3] = checkBlock[3].getRelative(BlockFace.WEST);
							for (x = 0; x < 4; ++x) {
								if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListTrapDoors)) {
									if (Lockette.findBlockOwner(checkBlock[x], block, true) == null) {
										if (!doTrapDoors)
											deny = true;
										else {
											TrapDoor trapDoor = (TrapDoor) block.getState().getData();
											face = trapDoor.getFacing();
											type = 4;
											break;
										}
									}
								}
							}
						}
				}
			if (Lockette.protectDoors)
				if (typeWallSign) {
					checkBlock[0] = Lockette.getSignAttachedBlock(block);
					if (Lockette.DEBUG) {
						Logger.debug("[" + Lockette.pluginName + "] checkBlock[0] : " + checkBlock[0]);
					}
					if (checkBlock[0] != null)
						if (!BlockUtil.isInList(checkBlock[0].getType(), BlockUtil.materialListBad)) {
							checkBlock[1] = checkBlock[0].getRelative(BlockFace.UP);
							checkBlock[2] = checkBlock[0].getRelative(BlockFace.DOWN);
							if (BlockUtil.isInList(checkBlock[1].getType(), BlockUtil.materialListDoors)) {
								if (Lockette.findBlockOwner(checkBlock[1], block, true) == null) {
									if (BlockUtil.isInList(checkBlock[2].getType(), BlockUtil.materialListDoors)) {
										if (Lockette.findBlockOwner(checkBlock[2], block, true) == null) {
											if (!doDoors)
												deny = true;
											else {
												Door door = (Door) block.getState().getData();
												face = door.getFacing();
												type = 5;
											}
										} else
											conflict = true;
									} else {
										if (!doDoors)
											deny = true;
										else {
											Door door = (Door) block.getState().getData();
											face = door.getFacing();
											type = 5;
										}
									}
								} else
									conflict = true;
							} else if (BlockUtil.isInList(checkBlock[2].getType(), BlockUtil.materialListDoors)) {
								if (Lockette.findBlockOwner(checkBlock[2], block, true) == null) {
									if (!doDoors)
										deny = true;
									else {
										Door Door = (Door) block.getState().getData();
										face = Door.getFacing();
										type = 5;
									}
								} else
									conflict = true;
							}
						}
				}
			if (conflict == true) {
				face = null;
				type = 0;
			}
			if (face == null) {
				int lastType;
				checkBlock[0] = block.getRelative(BlockFace.NORTH);
				checkBlock[1] = block.getRelative(BlockFace.EAST);
				checkBlock[2] = block.getRelative(BlockFace.SOUTH);
				checkBlock[3] = block.getRelative(BlockFace.WEST);
				for (x = 0; x < 4; ++x) {
					if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListChests)) {
						if (!doChests) {
							deny = true;
							continue;
						}
						lastType = 1;
					} else if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListFurnaces)) {
						if (!doFurnaces) {
							deny = true;
							continue;
						}
						lastType = 2;
					} else if (checkBlock[x].getType() == Material.DISPENSER) {
						if (!doDispensers) {
							deny = true;
							continue;
						}
						lastType = 3;
					} else if (checkBlock[x].getType() == Material.DROPPER) {
						if (!doDroppers) {
							deny = true;
							continue;
						}
						lastType = 8;
					} else if (checkBlock[x].getType() == Material.BREWING_STAND) {
						if (!doBrewingStands) {
							deny = true;
							continue;
						}
						lastType = 6;
					} else if (Lockette.isInList(checkBlock[x].getType(), Lockette.customBlockList)) {
						if (!doCustoms) {
							deny = true;
							continue;
						}
						lastType = 7;
					} else if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListTrapDoors)) {
						if (!Lockette.protectTrapDoors)
							continue;
						if (!doTrapDoors) {
							deny = true;
							continue;
						}
						lastType = 4;
					} else if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListDoors)) {
						if (!Lockette.protectDoors)
							continue;
						if (!doDoors) {
							deny = true;
							continue;
						}
						lastType = 5;
					} else
						continue;
					if (Lockette.findBlockOwner(checkBlock[x], block, true) == null) {
						face = Link$.getBlockFaceFromId(BlockUtil.faceList[x]);
						type = lastType;
						break;
					} else {
						if (Lockette.protectTrapDoors) {
							if (doTrapDoors) {
								if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListTrapDoors)) {
									conflict = true;
								}
							}
						}
						if (Lockette.protectDoors) {
							if (doDoors) {
								if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListDoors)) {
									conflict = true;
								}
							}
						}
					}
				}
			}
			if (face == null) {
				event.setLine(0, "[?]");
				if (conflict)
					plugin.localizedMessage(player, null, "msg-error-claim-conflict");
				else if (zonedeny)
					plugin.localizedMessage(player, null, "msg-error-zone", "null");
				else if (deny)
					plugin.localizedMessage(player, null, "msg-error-permission");
				else
					plugin.localizedMessage(player, null, "msg-error-claim");
				return;
			}
			boolean anyone = true;
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] creating new Lockette sign");
				Logger.debug("[" + Lockette.pluginName + "] 1st line = " + event.getLine(1));
				Logger.debug("[" + Lockette.pluginName + "] type = " + type);
			}
			if (event.getLine(1).isEmpty())
				anyone = false;
			event.setCancelled(false);
			if (anyone) {
				if (type == 1) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.chest"))
						anyone = false;
				} else if (type == 2) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.furnace"))
						anyone = false;
				} else if (type == 3) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.dispenser"))
						anyone = false;
				} else if (type == 8) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.dropper"))
						anyone = false;
				} else if (type == 6) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.brewingstand"))
						anyone = false;
				} else if (type == 7) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.custom"))
						anyone = false;
				} else if (type == 4) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.trapdoor"))
						anyone = false;
				} else if (type == 5) {
					if (!plugin.hasPermission(block.getWorld(), player, "lockette.admin.create.door"))
						anyone = false;
				} else
					anyone = false;
			}
			if (!anyone) {
				Sign sign = (Sign) block.getState();
				if (Lockette.DEBUG) {
					Logger.debug("[" + Lockette.pluginName + "] Setting player's name : " + player.getName());
				}
				Lockette.setLine(sign, 1, player.getName());
				event.setLine(1, player.getName());
				sign.update(true);
			} else {
				Sign sign = (Sign) block.getState();
				if (Lockette.DEBUG) {
					Logger.debug("[" + Lockette.pluginName + "] Setting other's name : " + event.getLine(1));
				}
				Lockette.setLine(sign, 1, event.getLine(1));
				event.setLine(1, event.getLine(1));
			}
			if (!typeWallSign) {
				block.setType(Material.OAK_WALL_SIGN);
				Sign sign = (Sign) block.getState();
				Directional signData = (Directional) sign.getBlockData();
				signData.setFacing(face);
				sign.setBlockData(signData);
				sign.setLine(0, event.getLine(0));
				Lockette.setLine(sign, 1, event.getLine(1));
				Lockette.setLine(sign, 2, event.getLine(2));
				Lockette.setLine(sign, 3, event.getLine(3));
				sign.update(true);
			} else {
				Sign sign = (Sign) block.getState();
				Directional signData = (Directional) sign.getBlockData();
				signData.setFacing(face);
				sign.setBlockData(signData);
			}
			if (anyone) {
				Logger.debug("[" + Lockette.pluginName + "] (Admin) " + player.getName() + " has claimed a container for " + event.getLine(1) + ".");
				if (!plugin.playerOnline(event.getLine(1)))
					plugin.localizedMessage(player, null, "msg-admin-claim-error", event.getLine(1));
				else
					plugin.localizedMessage(player, null, "msg-admin-claim", event.getLine(1));
			} else {
				Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " has claimed a container.");
				plugin.localizedMessage(player, null, "msg-owner-claim");
			}
		} else if (text.equalsIgnoreCase("[More Users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) {
			int x;
			Block checkBlock[] = new Block[4];
			Block signBlock = null;
			Sign sign = null;
			BlockFace face = null;
			if (Lockette.protectDoors || Lockette.protectTrapDoors) {
				if (typeWallSign) {
					checkBlock[0] = Lockette.getSignAttachedBlock(block);
					if (checkBlock[0] != null) {
						if (!BlockUtil.isInList(checkBlock[0].getType(), BlockUtil.materialListBad)) {
							signBlock = Lockette.findBlockOwner(checkBlock[0]);
							if (signBlock != null) {
								sign = (Sign) signBlock.getState();
								if (Lockette.isOwner(sign, player)) {
									org.bukkit.material.Sign signData = (org.bukkit.material.Sign) sign.getData();
									face = signData.getFacing();
								}
							}
						}
					}
				}
			}
			if (face == null) {
				checkBlock[0] = block.getRelative(BlockFace.NORTH);
				checkBlock[1] = block.getRelative(BlockFace.EAST);
				checkBlock[2] = block.getRelative(BlockFace.SOUTH);
				checkBlock[3] = block.getRelative(BlockFace.WEST);
				for (x = 0; x < 4; ++x) {
					if (!BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialList))
						continue;
					if (!Lockette.protectTrapDoors) {
						if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListTrapDoors))
							continue;
					}
					if (!Lockette.protectDoors) {
						if (BlockUtil.isInList(checkBlock[x].getType(), BlockUtil.materialListDoors))
							continue;
					}
					signBlock = Lockette.findBlockOwner(checkBlock[x]);
					if (signBlock != null) {
						sign = (Sign) signBlock.getState();
						if (Lockette.isOwner(sign, player)) {
							face = Link$.getBlockFaceFromId(BlockUtil.faceList[x]);
							break;
						}
					}
				}
			}
			if (face == null) {
				event.setLine(0, "[?]");
				if (sign != null) {
					plugin.localizedMessage(player, null, "msg-error-adduser-owned", sign.getLine(1));
				} else {
					plugin.localizedMessage(player, null, "msg-error-adduser");
				}
				return;
			}
			event.setCancelled(false);
			if (!typeWallSign) {
				block.setType(Material.OAK_WALL_SIGN);
				sign = (Sign) block.getState();
				Directional signData = (Directional) sign.getBlockData();
				signData.setFacing(face);
				sign.setBlockData(signData);
				sign.setLine(0, event.getLine(0));
				Lockette.setLine(sign, 1, event.getLine(1));
				Lockette.setLine(sign, 2, event.getLine(2));
				Lockette.setLine(sign, 3, event.getLine(3));
				sign.update(true);
			} else {
				Sign sign2 = (Sign) block.getState();
				Directional signData = (Directional) sign2.getBlockData();
				signData.setFacing(face);
				sign2.setBlockData(signData);
			}
			if (Lockette.colorTags) {
				event.setLine(0, ChatColor.translateAlternateColorCodes('&', event.getLine(0)));
				event.setLine(1, ChatColor.translateAlternateColorCodes('&', event.getLine(1)));
				event.setLine(2, ChatColor.translateAlternateColorCodes('&', event.getLine(2)));
				event.setLine(3, ChatColor.translateAlternateColorCodes('&', event.getLine(3)));
			}
			plugin.localizedMessage(player, null, "msg-owner-adduser");
		}
	}

	private static boolean canBuildDoor(Block block, Block against, Player player) {
		Block checkBlock;
		if (!Lockette.isOwner(against, player))
			return (false);
		if (Lockette.protectTrapDoors)
			if (BlockUtil.isInList(block.getType(), BlockUtil.materialListTrapDoors)) {
				return (true);
			}
		if (!Lockette.isOwner(against.getRelative(BlockFace.UP, 3), player))
			return (false);
		checkBlock = block.getRelative(BlockFace.NORTH);
		if (checkBlock.getType() == block.getType()) {
			if (!Lockette.isOwner(checkBlock, player))
				return (false);
		}
		checkBlock = block.getRelative(BlockFace.EAST);
		if (checkBlock.getType() == block.getType()) {
			if (!Lockette.isOwner(checkBlock, player))
				return (false);
		}
		checkBlock = block.getRelative(BlockFace.SOUTH);
		if (checkBlock.getType() == block.getType()) {
			if (!Lockette.isOwner(checkBlock, player))
				return (false);
		}
		checkBlock = block.getRelative(BlockFace.WEST);
		if (checkBlock.getType() == block.getType()) {
			if (!Lockette.isOwner(checkBlock, player))
				return (false);
		}
		return (true);
	}

	static boolean isEmptyChange(SignChangeEvent signe) {
		for (int i = 0; i < 4; i++) {
			String str = ChatColor.stripColor(signe.getLine(i));
			if (!str.isEmpty())
				return false;
		}
		return true;
	}
}
