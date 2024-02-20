package com.brahvim.nerd.processing_wrapper.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dGraphics extends NerdFramebufferBackedGraphics<PGraphicsJava2D> {

    // region Utilitarian constructors.
    protected NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdJava2dGraphics(
            final NerdSketch<PGraphicsJava2D> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdJava2dGraphics(final NerdSketch<PGraphicsJava2D> p_sketch, final PGraphicsJava2D p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
