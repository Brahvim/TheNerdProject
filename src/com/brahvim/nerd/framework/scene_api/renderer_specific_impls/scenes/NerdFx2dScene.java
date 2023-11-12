package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdFx2dGraphics;

import processing.javafx.PGraphicsFX2D;

public class NerdFx2dScene extends NerdScene<PGraphicsFX2D> {

    protected NerdFx2dGraphics graphics;

    @Override
    protected void sceneRendererInit() {
        this.graphics = (NerdFx2dGraphics) super.genericGraphics;
    }

    /* package */ void runSceneInit() {
    }

    protected void sceneInit() {
    }

}
