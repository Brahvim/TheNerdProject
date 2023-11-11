package com.brahvim.nerd.processing_wrapper.graphics_backends.nerd_graphics_impls;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.graphics_backends.generic.NerdGenericGraphics;

import processing.pdf.PGraphicsPDF;

public class NerdPdfGraphics extends NerdGenericGraphics<PGraphicsPDF> {

    public NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final PGraphicsPDF p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);

        super.GRAPHICS.nextPage();
    }

}
