package me.skorrloregaming.impl;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

public class VanishedInfo {
	private ItemStack[] contents;
	private GameMode gameMode;

	public VanishedInfo(ItemStack[] contents, GameMode gameMode) {
		this.setContents(contents);
		this.setGameMode(gameMode);
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public ItemStack[] getContents() {
		return contents;
	}

	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
}
