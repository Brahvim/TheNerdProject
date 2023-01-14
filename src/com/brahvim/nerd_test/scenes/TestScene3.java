package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;
import com.brahvim.nerd_test.AnimatedCube;

public class TestScene3 extends NerdScene {
    private AnimatedCube cube;

    public TestScene3(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void setup() {
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
