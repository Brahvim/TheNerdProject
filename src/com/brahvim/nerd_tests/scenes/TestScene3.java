package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.AnimatedCube;

public class TestScene3 extends NerdScene {
    private AnimatedCube cube;

    @Override
    protected void setup(SceneState p_state) {
        this.cube = new AnimatedCube(this);
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
