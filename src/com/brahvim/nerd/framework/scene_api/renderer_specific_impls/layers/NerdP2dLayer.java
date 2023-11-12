package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP2dGraphics;

import processing.opengl.PGraphics2D;

public class NerdP2dLayer extends NerdLayer<PGraphics2D> {

    protected NerdP2dGraphics graphics = (NerdP2dGraphics) super.GRAPHICS; // NOSONAR

}
