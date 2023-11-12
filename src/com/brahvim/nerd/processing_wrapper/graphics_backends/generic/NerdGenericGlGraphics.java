package com.brahvim.nerd.processing_wrapper.graphics_backends.generic;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.opengl.PGraphicsOpenGL;

public class NerdGenericGlGraphics<SketchPGraphicsT extends PGraphicsOpenGL>
        extends NerdGenericGraphics<SketchPGraphicsT> {

    public NerdGenericGlGraphics(
            final NerdSketch<SketchPGraphicsT> p_sketch,
            final SketchPGraphicsT p_pGraphicsToWrap) {
        super(p_sketch, p_pGraphicsToWrap);
    }

}
