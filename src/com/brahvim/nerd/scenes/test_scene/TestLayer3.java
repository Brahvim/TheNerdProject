package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.scene_api.Layer;
import com.brahvim.nerd.scene_api.Scene.LayerInitializer;

public class TestLayer3 extends Layer {
    public TestLayer3(LayerInitializer p_initializer) {
        super(p_initializer);
    }

    @Override
    protected void draw() {
        if (SKETCH.frameCount % 60 == 0)
            System.out.println(SKETCH.frameRate);
        SKETCH.circle(super.SKETCH.mouseX, super.SKETCH.mouseY, 20);
    }

}
