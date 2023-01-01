package com.brahvim.nerd;

import com.brahvim.nerd.api.SketchBuilder;
import com.brahvim.nerd.scenes.test_scene.TestScene1;

import processing.opengl.PJOGL;

public class App {
    public static void main(String[] p_args) {
        PJOGL.setIcon("data/sunglass_nerd.png");
        new SketchBuilder()
                .setFirstScene(TestScene1.class)
                .startFullscreen()
                .canResize()
                .build(p_args);
    }
}
