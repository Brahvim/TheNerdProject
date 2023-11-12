package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP2dGraphics;

import processing.opengl.PGraphics2D;

public class NerdP2dScene extends NerdScene<PGraphics2D> {

    protected NerdP2dGraphics graphics = (NerdP2dGraphics) super.graphics; // NOSONAR

}
