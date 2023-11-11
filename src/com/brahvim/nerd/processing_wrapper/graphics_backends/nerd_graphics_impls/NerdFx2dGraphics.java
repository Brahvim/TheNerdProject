package com.brahvim.nerd.processing_wrapper.graphics_backends.nerd_graphics_impls;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dGraphics extends NerdGenericGraphics<PGraphicsFX2D> {

    public NerdFx2dGraphics(final NerdSketch<PGraphicsFX2D> p_sketch, final PGraphicsFX2D p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
