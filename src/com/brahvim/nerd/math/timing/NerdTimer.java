package com.brahvim.nerd.math.timing;

public interface NerdTimer extends AutoCloseable {

    // State manipulation:
    public void restart();

    // Duration getters:
    public long get();

    public int getInt();

    public float getFloat();

}
