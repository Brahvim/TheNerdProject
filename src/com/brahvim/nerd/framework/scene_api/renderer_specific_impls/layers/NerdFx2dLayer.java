package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdFx2dGraphics;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dLayer extends NerdLayer<PGraphicsFX2D> {

    protected NerdFx2dGraphics graphics = (NerdFx2dGraphics) super.GRAPHICS; // NOSONAR

}
