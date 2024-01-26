package com.brahvim.nerd.math.timing;

public class NerdNanosTimer implements NerdTimer {

	private boolean active;
	private long startTime, endTime;

	public NerdNanosTimer() {
		this.restart();
	}

	// region State manipulation!
	public void close() {
		this.active = false;
		this.endTime = System.nanoTime();
	}

	public void restart() {
		this.active = true;
		this.startTime = System.nanoTime();
	}
	// endregion

	// region Duration getters.
	public long get() {
		return this.active ? System.nanoTime() - this.startTime : this.endTime - this.startTime;
	}

	public int getInt() {
		return (int) this.get();
	}

	public float getFloat() {
		return this.get();
	}
	// endregion

}
