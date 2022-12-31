package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.scene_api.Layer;
import com.brahvim.nerd.scene_api.Scene.LayerInitializer;

import processing.core.PApplet;
import processing.core.PConstants;

public class MouseEllipseLayer extends Layer {
    public MouseEllipseLayer(LayerInitializer p_initializer) {
        super(p_initializer);
    }

    @Override
    protected void draw() {
        if (SKETCH.frameCount % 60 == 0)
            System.out.println(SKETCH.frameRate);

        // SKETCH.circle(super.SKETCH.mouseX, super.SKETCH.mouseY, 20);

        SKETCH.noStroke();
        for (int i = 0; i < 50; i++) {
            float angle = PConstants.TAU / i;
            SKETCH.circle(
                    SKETCH.cx + 200 * angle * PApplet.cos(SKETCH.millis() * 0.001f * angle),
                    SKETCH.cy + 200 * angle * PApplet.sin(SKETCH.millis() * 0.001f * angle), 20);
        }
    }

}
