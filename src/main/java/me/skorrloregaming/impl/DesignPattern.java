/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl;

import org.bukkit.Material;

public class DesignPattern {
	private Material middleCenter;
	private Material innerCircle;
	private Material outerBlocks;
	private boolean completePattern;

	public DesignPattern(Material middleCenter, Material innerCircle, Material outerBlocks, boolean completePattern) {
		this.middleCenter = middleCenter;
		this.innerCircle = innerCircle;
		this.outerBlocks = outerBlocks;
		this.completePattern = completePattern;
	}

	public Material getMiddleCenter() {
		return this.middleCenter;
	}

	public Material getInnerCircle() {
		return this.innerCircle;
	}

	public Material getOuterBlocks() {
		return this.outerBlocks;
	}

	public boolean isCompletePattern() {
		return this.completePattern;
	}

	public void setCompletePattern(boolean completePattern) {
		this.completePattern = completePattern;
	}
}

