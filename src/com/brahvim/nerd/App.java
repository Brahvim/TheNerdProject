package com.brahvim.nerd;

import com.brahvim.nerd.api.SketchBuilder;

public class App {
    public static void main(String[] p_args) {
        new SketchBuilder()
                .setWidth(400)
                .setHeight(400)
                .startFullscreen()
                .closeOnEscape()
                .build(p_args);
    }
}
