package com.brahvim.nerd.processing_wrapper.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.pdf.PGraphicsPDF;

public class NerdPdfGraphics extends NerdGenericGraphics<PGraphicsPDF> {

    public NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final PGraphicsPDF p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);

        super.GRAPHICS.nextPage();
    }

}
