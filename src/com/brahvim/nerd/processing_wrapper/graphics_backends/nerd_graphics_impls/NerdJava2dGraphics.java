package com.brahvim.nerd.processing_wrapper.graphics_backends.nerd_graphics_impls;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dGraphics extends NerdGenericGraphics<PGraphicsJava2D> {

    public NerdJava2dGraphics(NerdSketch<PGraphicsJava2D> p_sketch, PGraphicsJava2D p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
