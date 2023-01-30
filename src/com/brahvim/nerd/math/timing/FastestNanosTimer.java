package com.brahvim.nerd.math.timing;

/**
 * A "very fast" timer for measuring in nanoseconds.
 * You may not be able to use it across threads...
 *
 * Oh! And it cannot be instantiated.
 */

// "Java ain't even all that fast bruh! This idea's stupeehd"
// - You, probably. But, my friend, I will ignore that.
public final class FastestNanosTimer { // `final` means no v-tables, right?!
    private static long startTime;

    private FastestNanosTimer() {
    }

    // ...***I say*** that `synchronized` methods are faster.
    // This stackoverflow post might help clarify:
    // [https://stackoverflow.com/questions/7371089/synchronized-code-performs-faster-than-unsynchronized-one]

    public static synchronized void start() {
        // Today, I learned:
        // Instant.now(); // <-- exists for system times, :O!
        FastestNanosTimer.startTime = System.nanoTime();
    }

    public static synchronized long stop() {
        return System.nanoTime() - FastestNanosTimer.startTime;
    }
}
