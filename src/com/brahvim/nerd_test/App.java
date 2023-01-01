package com.brahvim.nerd_test;

import com.brahvim.nerd.processing_wrapper.SketchBuilder;
import com.brahvim.nerd_test.scenes.TestScene1;

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
