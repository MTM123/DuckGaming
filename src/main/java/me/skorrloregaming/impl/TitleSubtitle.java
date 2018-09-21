package me.skorrloregaming.impl;

public class TitleSubtitle {
	private String title, subtitle;
	private int fadeIn = 10;
	private int stay = 40;
	private int fadeOut = 5;
	private boolean forceChatNotify = false;

	public TitleSubtitle(String title, String subtitle) {
		this.setTitle(title);
		this.setSubtitle(subtitle);
	}

	public TitleSubtitle(String title, String subtitle, boolean forceChatNotify) {
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setForceChatNotify(forceChatNotify);
	}

	public TitleSubtitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setFadeIn(fadeIn);
		this.setStay(stay);
		this.setFadeOut(fadeOut);
	}

	public TitleSubtitle(String title, String subtitle, int fadeIn, int stay, int fadeOut, boolean forceChatNotify) {
		this.setTitle(title);
		this.setSubtitle(subtitle);
		this.setFadeIn(fadeIn);
		this.setStay(stay);
		this.setFadeOut(fadeOut);
		this.setForceChatNotify(forceChatNotify);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getFadeIn() {
		return fadeIn;
	}

	public void setFadeIn(int fadeIn) {
		this.fadeIn = fadeIn;
	}

	public int getStay() {
		return stay;
	}

	public void setStay(int stay) {
		this.stay = stay;
	}

	public int getFadeOut() {
		return fadeOut;
	}

	public void setFadeOut(int fadeOut) {
		this.fadeOut = fadeOut;
	}

	public boolean isForceChatNotify() {
		return forceChatNotify;
	}

	public void setForceChatNotify(boolean forceChatNotify) {
		this.forceChatNotify = forceChatNotify;
	}
}
