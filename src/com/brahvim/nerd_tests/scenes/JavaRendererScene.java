package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_exceptions.AlException;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class JavaRendererScene extends NerdScene {

    private AlSource rubberDuck;

    @Override
    protected synchronized void preload() {
        this.rubberDuck = SKETCH.OPENAL.sourceFromOgg("data/RUBBER DUCK.ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        SKETCH.getSurface().setSize(640, 480);
    }

    @Override
    protected void draw() {
        SKETCH.circle(SKETCH.mouseX, SKETCH.mouseY, 50);
    }

    @Override
    public void mouseClicked() {
        if (!this.rubberDuck.isPlaying()) {
            try {
                SKETCH.OPENAL.checkAlErrors();
                this.rubberDuck.play();
            } catch (AlException e) {
                e.printStackTrace();
            }

            try {
                SKETCH.OPENAL.checkAlErrors();
            } catch (AlException e) {
                System.out.println("Second error:");
                e.printStackTrace();
            }
        }

    }

}
