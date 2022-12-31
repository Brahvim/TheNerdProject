package com.brahvim.nerd;

import com.brahvim.nerd.api.SketchBuilder;
import com.brahvim.nerd.scenes.test_scene.TestScene1;

public class App {
    public static void main(String[] p_args) {
        new SketchBuilder()
                .setFirstScene(TestScene1.class)
                .startFullscreen()
                .closeOnEscape()
                .setWidth(400)
                .setHeight(400)
                .canResize()
                .build(p_args);
    }
}
