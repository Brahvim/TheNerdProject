package com.brahvim.nerd.math;

public class MillisTimer {
    private boolean active;
    private long startTime, endTime;

    public MillisTimer() {
        this.restart();
    }

    public void restart() {
        this.active = true;
        this.startTime = System.currentTimeMillis();
    }

    public void stop() {
        this.active = false;
        this.endTime = System.currentTimeMillis();
    }

    // region Duration getters.
    public long get() {
        return this.active ? System.currentTimeMillis() - this.startTime : this.endTime - this.startTime;
    }

    public int getInt() {
        return (int) this.get();
    }

    public float getFloat() {
        return (float) this.get();
    }
    // endregion

}
