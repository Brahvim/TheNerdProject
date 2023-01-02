package com.brahvim.nerd.math;

// Thanks to `easings.net`!

public class Easings {
    public static float cubic(float x) {
        return 1.0f - (float) Math.pow(1 - x, 3);
    }

    public static float exponential(float x) {
        return x == 0 ? 0 : (float) Math.pow(2, 10 * x - 10);
    }

    public static float leminiscate(float x, float theta) {
        return (float) Math.sqrt(x * x * Math.cos(2 * theta));
    }

    public static float exponentialSine(float x, float theta) {
        return (float) Math.pow(x, Math.sin(theta));
    }

}
