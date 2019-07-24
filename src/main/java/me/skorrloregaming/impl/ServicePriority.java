package me.skorrloregaming.impl;

public enum ServicePriority {

	delay24hour(1000 * 60 * 60 * 24),
	delay12hour(1000 * 60 * 60 * 12),
	midnight(),
	GMTplus1midnight();

	private long delay = 0L;
	private boolean isEpoch = false;

	public long getDelay() {
		return delay;
	}

	public boolean isEpoch() {
		return isEpoch;
	}

	ServicePriority(long delay) {
		this.delay = delay;
	}

	ServicePriority() {
		this.isEpoch = true;
	}

}
