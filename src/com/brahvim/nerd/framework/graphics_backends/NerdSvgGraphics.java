package com.brahvim.nerd.framework.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdAbstractGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.svg.PGraphicsSVG;

public class NerdSvgGraphics extends NerdAbstractGraphics<PGraphicsSVG> {

    // region Utilitarian constructors.
    protected NerdSvgGraphics(
            final NerdSketch<PGraphicsSVG> p_sketch,
            final int p_width,
            final int p_height,
            final String p_renderer,
            final String p_path) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer, p_path));
    }

    protected NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height, p_renderer));
    }

    protected NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch, final int p_width, final int p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdSvgGraphics(
            final NerdSketch<PGraphicsSVG> p_sketch,
            final float p_width,
            final float p_height) {
        this(p_sketch, p_sketch.createGraphics(p_width, p_height));
    }

    protected NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch, final float p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch, final int p_size) {
        this(p_sketch, p_sketch.createGraphics(p_size));
    }

    protected NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch) {
        this(p_sketch, p_sketch.createGraphics());
    }
    // endregion

    public NerdSvgGraphics(final NerdSketch<PGraphicsSVG> p_sketch, final PGraphicsSVG p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
