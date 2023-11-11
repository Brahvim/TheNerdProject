package com.brahvim.nerd.processing_wrapper;

import processing.core.PGraphics;

public class NerdRendererReliantModule<SketchPGraphicsT extends PGraphics> extends NerdModule { // NOSONAR

    protected NerdRendererReliantModule(final NerdSketch<?> p_sketch) {
        super(p_sketch);
    }

}
