package com.brahvim.nerd.processing_wrapper.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdAbstractGraphics;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

public class Nerd2dGenericGraphics<SketchPGraphicsT extends PGraphics> extends NerdAbstractGraphics<SketchPGraphicsT> {

    // region Constructors.
    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch) {
        super(p_sketch);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final float p_size) {
        super(p_sketch, p_size);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final float p_width,
            final float p_height) {
        super(p_sketch, p_width, p_height);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final int p_size) {
        super(p_sketch, p_size);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width,
            final int p_height) {
        super(p_sketch, p_width, p_height);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width, final int p_height,
            final String p_renderer) {
        super(p_sketch, p_width, p_height, p_renderer);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width, final int p_height,
            final String p_renderer,
            final String p_path) {
        super(p_sketch, p_width, p_height, p_renderer, p_path);
    }

    protected Nerd2dGenericGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch,
            final SketchPGraphicsT p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }
    // endregion

}
