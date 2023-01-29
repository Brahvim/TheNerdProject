package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.scene_api.NerdScene;

public class TestScene5 extends NerdScene {

    @Override
    protected void setup() {
        System.out.println("Welcome to `TestScene5`!");
    }

    @Override
    protected void draw() {
        SKETCH.text(SCENE.getClass().getSimpleName(), SKETCH.cx, SKETCH.cy);
    }

    @Override
    public void mouseClicked() {
        MANAGER.startScene(TestScene4.class);
    }

}
