package com.brahvim.nerd.processing_wrapper.graphics_backends.nerd_graphics_impls;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.svg.PGraphicsSVG;

public class NerdSvgGraphics extends NerdGenericGraphics<PGraphicsSVG> {

    public NerdSvgGraphics(NerdSketch<PGraphicsSVG> p_sketch, PGraphicsSVG p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
