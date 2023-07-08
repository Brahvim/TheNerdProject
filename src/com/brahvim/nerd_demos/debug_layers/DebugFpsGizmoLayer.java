package com.brahvim.nerd_demos.debug_layers;

import com.brahvim.nerd.framework.scene_api.NerdLayer;

public class DebugFpsGizmoLayer extends NerdLayer {

    @Override
    protected void draw() {
        GRAPHICS.begin2d();
        GRAPHICS.textSize(42);
        GRAPHICS.centeredText(Float.toString(SKETCH.frameRate));

        final String heldKeys = INPUT.getHeldKeysDebugString();
        GRAPHICS.translate(WINDOW.width - SKETCH.textWidth(heldKeys), 0);
        GRAPHICS.centeredText(heldKeys);
        GRAPHICS.end2d();
    }

}
