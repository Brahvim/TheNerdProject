package com.brahvim.nerd.math.timing;

/**
 * A "very fast" timer for measuring in milliseconds.
 * You may not be able to use it across threads!
 *
 * Oh! And it cannot be instantiated.
 */

public final class FastestMillisTimer {
    private static long startTime;

    private FastestMillisTimer() {
    }

    public static synchronized void start() {
        FastestMillisTimer.startTime = System.currentTimeMillis();
    }

    public static synchronized long stop() {
        return System.currentTimeMillis() - FastestMillisTimer.startTime;
    }
}
