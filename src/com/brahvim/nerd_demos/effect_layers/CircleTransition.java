package com.brahvim.nerd_demos.effect_layers;

import com.brahvim.nerd.api.scene_api.NerdLayer;

public class CircleTransition extends NerdLayer {

    @Override
    protected void draw() {
        SKETCH.begin2d();
        SKETCH.fill(0);

        SKETCH.end2d();
    }

}
