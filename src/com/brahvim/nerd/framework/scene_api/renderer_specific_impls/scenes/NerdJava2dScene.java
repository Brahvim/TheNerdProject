package com.brahvim.nerd.framework.scene_api.renderer_specific_impls.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.processing_wrapper.graphics_backends.NerdJava2dGraphics;

import processing.awt.PGraphicsJava2D;

public abstract class NerdJava2dScene extends NerdScene<PGraphicsJava2D> {

    protected NerdJava2dGraphics graphics;

    @Override
    protected void sceneRendererInit() {
        this.graphics = (NerdJava2dGraphics) super.genericGraphics;
    }

    /* package */ void runSceneInit() {
    }

    protected void sceneInit() {
    }

}
