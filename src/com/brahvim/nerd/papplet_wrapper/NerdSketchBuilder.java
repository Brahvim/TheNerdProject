package com.brahvim.nerd.papplet_wrapper;

public final class NerdSketchBuilder extends NerdCustomSketchBuilder {

    @Override
    protected NerdSketch buildImpl(final String[] p_javaMainArgs) {
        return new NerdSketch(super.SKETCH_KEY);
    }

}
