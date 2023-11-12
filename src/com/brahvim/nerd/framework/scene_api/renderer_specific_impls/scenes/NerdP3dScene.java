package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdP3dGraphics;

import processing.opengl.PGraphics3D;

public class NerdP3dScene extends NerdScene<PGraphics3D> {

    protected NerdP3dGraphics graphics = (NerdP3dGraphics) super.graphics; // NOSONAR

}
