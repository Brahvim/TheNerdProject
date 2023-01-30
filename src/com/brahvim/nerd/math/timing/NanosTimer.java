package com.brahvim.nerd.math.timing;

public class NanosTimer {
    private boolean active;
    private long startTime, endTime;

    public NanosTimer() {
        this.restart();
    }

    // region State manipulation!
    public void stop() {
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
        return (float) this.get();
    }
    // endregion

}
