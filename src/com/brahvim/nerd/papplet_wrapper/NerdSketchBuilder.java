package com.brahvim.nerd.papplet_wrapper;

public final class NerdSketchBuilder extends CustomSketchBuilder {

    @Override
    protected Sketch buildImpl(String[] p_javaMainArgs) {
        return new Sketch(super.SKETCH_KEY);
    }

}
