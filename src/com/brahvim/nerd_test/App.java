package com.brahvim.nerd_test;

import com.brahvim.nerd.processing_wrapper.Sketch;
import com.brahvim.nerd.processing_wrapper.SketchBuilder;
import com.brahvim.nerd_test.scenes.TestScene1;
import com.brahvim.nerd_test.scenes.TestScene2;

import processing.opengl.PJOGL;

public class App {
    public static Sketch sketchInstance;

    public static void main(String[] p_args) {
        PJOGL.setIcon("data/sunglass_nerd.png");
        App.sketchInstance = new SketchBuilder()
                .setName("The Nerd Project")
                .setFirstScene(TestScene1.class)
                // .setIcon("path/to/icon")
                // .cacheScene(true, TestScene1.class)
                .cacheAllScenes(true,
                        TestScene1.class, TestScene2.class)
                .startFullscreen()
                .canResize()
                .build(p_args);

    }
}
