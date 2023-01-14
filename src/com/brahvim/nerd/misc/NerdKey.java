package com.brahvim.nerd.misc;

public abstract class NerdKey {
    protected boolean used;

    public final void use() {
        this.used = true;
    }

    public final boolean isUsed() {
        return this.used;
    }

    public boolean isFor(Class<?> p_class) {
        return false;
    }

}
