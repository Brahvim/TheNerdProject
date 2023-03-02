package com.brahvim.nerd.misc;

// I keep forgetting and realizing that you can use try-catch statements and throws declarations together.
// And yes, overloads (for default values of this!)
// Might find a use for this someday?!

// `@Deprecated`-
// ACTUALLY-ACTUALLY,
// WAIT, WAIT!
// Overloading with just a different *throws-decl* is not a thing!
// ....long-live the `OnCatch`!

public interface OnCatch {
    public final static OnCatch DEFAULT = new OnCatch() {
        public void onCatch(Exception p_except) {
            p_except.printStackTrace();
        }
    };

    public default void onCatch(Exception p_except) {
        p_except.printStackTrace();
    }
}
