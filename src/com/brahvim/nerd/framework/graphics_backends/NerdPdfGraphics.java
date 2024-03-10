package com.brahvim.nerd.framework.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdAbstractGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.pdf.PGraphicsPDF;

public class NerdPdfGraphics extends NerdAbstractGraphics<PGraphicsPDF> {

    // region Utilitarian constructors.
    protected NerdPdfGraphics(
            final NerdSketch<PGraphicsPDF> p_sketch,
            final int p_width,
            final int p_height,
            final String p_renderer,
            final String p_path) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer, p_path));
    }

    protected NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdPdfGraphics(
            final NerdSketch<PGraphicsPDF> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdPdfGraphics(final NerdSketch<PGraphicsPDF> p_sketch, final PGraphicsPDF p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
        super.GRAPHICS.nextPage();
    }

}
