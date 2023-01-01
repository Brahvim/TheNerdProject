package com.brahvim.nerd_test.layers;

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
        for (int i = 0; i < 20; i++) {
            float angle = PConstants.TAU / i;
            SKETCH.circle(
                    ((i & 1) == 0 ? 1 : -1) * SKETCH.cx
                            + 200 * angle
                                    * PApplet.cos(
                                            PApplet.sin(SKETCH.millis() * 0.001f) * SKETCH.millis() * 0.001f * angle),
                    ((i & 1) == 0 ? 1 : -1) * SKETCH.cy
                            + 200 * angle
                                    * PApplet.sin(
                                            PApplet.cos(SKETCH.millis() * 0.001f) * SKETCH.millis() * 0.001f * angle),
                    20);
        }
    }

}
