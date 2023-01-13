package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;
import com.brahvim.nerd_test.AnimatedCube;

public class TestScene3 extends Scene {
    AnimatedCube cube;

    public TestScene3(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void setup() {
        this.cube = new AnimatedCube(SKETCH);

    }

    @Override
    protected void draw() {
        // SKETCH.background(0x006699);
        this.cube.pos.set(SKETCH.mouseX, SKETCH.mouseY);
        this.cube.draw();
    }

    @Override
    public void mousePressed() {
        if (this.cube.isVisible())
            this.cube.plopOut();
        else
            this.cube.plopIn();
    }

}
