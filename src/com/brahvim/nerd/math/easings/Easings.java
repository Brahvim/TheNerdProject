package com.brahvim.nerd.math.easings;

// Thanks to `easings.net`!

public class Easings {

    public static float cubic(float x) {
        return 1.0f - (float) Math.pow(1 - x, 3);
    }

    public static float quadratic(float x) {
        return x * x;
    }

    public static float exponential(float x) {
        return x == 0 ? 0 : (float) Math.pow(2, 10 * x);
    }

    // Mine! https://www.desmos.com/calculator/88fimxy2ox:
    public static float exponentialSine(float x, float theta) {
        return (float) Math.pow(x, Math.sin(theta));
    }

    // Mine! https://www.desmos.com/calculator/hgs8iom7sk:
    public static float exponentialSine(float x) {
        return (float) Math.pow(x, Math.sin(x));
    }

}
