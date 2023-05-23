package com.brahvim.nerd_demos.debug_layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

public class DebugFpsGizmoLayer extends NerdLayer {

    @Override
    protected void draw() {
        GRAPHICS.begin2d();
        GRAPHICS.textSize(42);
        GRAPHICS.centeredText(Float.toString(SKETCH.frameRate));
        GRAPHICS.end2d();
    }

}
