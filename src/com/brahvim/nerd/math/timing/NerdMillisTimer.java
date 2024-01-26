package com.brahvim.nerd.math.timing;

public class NerdMillisTimer implements NerdTimer {

	private boolean active;
	private long startTime, endTime;

	public NerdMillisTimer() {
		this.restart();
	}

	// region State manipulation!
	public void close() {
		this.active = false;
		this.endTime = System.currentTimeMillis();
	}

	public void restart() {
		this.active = true;
		this.startTime = System.currentTimeMillis();
	}
	// endregion

	// region Duration getters.
	public long get() {
		return this.active ? System.currentTimeMillis() - this.startTime : this.endTime - this.startTime;
	}

	public int getInt() {
		return (int) this.get();
	}

	public float getFloat() {
		return this.get();
	}
	// endregion

}
