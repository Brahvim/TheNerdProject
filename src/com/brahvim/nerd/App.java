package com.brahvim.nerd;

import com.brahvim.nerd.api.Sketch;

public class App {
    public static void main(String[] p_args) {
        Sketch.SketchBuilder builder = new Sketch.SketchBuilder();
        Sketch sketch = builder.build(p_args);
    }
}
