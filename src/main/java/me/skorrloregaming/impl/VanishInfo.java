/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class VanishInfo {
	private ItemStack[] contents;
	private GameMode gameMode;

	public VanishInfo(ItemStack[] contents, GameMode gameMode) {
		this.setContents(contents);
		this.setGameMode(gameMode);
	}

	public GameMode getGameMode() {
		return this.gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public ItemStack[] getContents() {
		return this.contents;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
}

