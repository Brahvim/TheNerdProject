package com.brahvim.nerd.math.timing;

public class MillisTimer {
    private boolean active;
    private long startTime, endTime;

    public MillisTimer() {
        this.restart();
    }

    // region State manipulation!
    public void stop() {
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
        return (float) this.get();
    }
    // endregion

}
