package me.skorrloregaming.impl;

import me.skorrloregaming.*;

public class SignInfo {
	private int line;
	private String text;

	public SignInfo(int line, String text) {
		this.line = line;
		this.text = text;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
