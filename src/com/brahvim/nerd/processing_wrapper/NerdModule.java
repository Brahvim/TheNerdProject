package com.brahvim.nerd.processing_wrapper;

public class NerdModule {

    private NerdModule() {
        throw new IllegalAccessError("Please instantiate `"
                + this.getClass().getSimpleName()
                + "`es the way they're supposed to be! Sorry...");
    }

}
