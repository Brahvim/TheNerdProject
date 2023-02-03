package com.brahvim.nerd.papplet_wrapper;

import processing.core.PApplet;

public final class NerdSketchBuilder extends CustomSketchBuilder<Sketch> {

    public Sketch build(String[] p_javaMainArgs) {
        Sketch constructedSketch = new Sketch(super.SKETCH_KEY);
        String[] args = new String[] { constructedSketch.getClass().getName() };

        if (p_javaMainArgs == null || p_javaMainArgs.length == 0)
            PApplet.runSketch(args, constructedSketch);
        else
            PApplet.runSketch(PApplet.concat(p_javaMainArgs, args), constructedSketch);

        return constructedSketch;
    }

}
