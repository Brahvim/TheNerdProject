package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdJava2dGraphics;

import processing.awt.PGraphicsJava2D;

public class NerdJava2dLayer extends NerdLayer<PGraphicsJava2D> {

    protected NerdJava2dGraphics graphics = (NerdJava2dGraphics) super.GRAPHICS; // NOSONAR

}
