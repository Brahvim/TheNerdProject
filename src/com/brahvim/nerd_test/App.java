package com.brahvim.nerd_test;

import com.brahvim.nerd.processing_wrapper.SketchBuilder;
import com.brahvim.nerd_test.scenes.TestScene1;

import processing.opengl.PJOGL;

public class App {
    public static void main(String[] p_args) {
        PJOGL.setIcon("data/sunglass_nerd.png");
        new SketchBuilder()
                .setName("The Nerd Project")
                .setFirstScene(TestScene1.class)
                // .setIcon("path/to/icon")
                // TODO: Implement `cacheScene()` and `cacheScenes()` as shown:
                // `cacheScene(boolean p_isDeletable, Class<? extends Scene> p_sceneClass)`,
                // `.cacheScenes(Class<? extends Scene>... p_sceneClasses)`.
                .startFullscreen()
                .canResize()
                .build(p_args);
    }
}
