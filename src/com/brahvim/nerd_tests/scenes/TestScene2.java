package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.math.SineWave;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;

public class TestScene2 extends NerdScene {
    private SineWave boxHorizWave, boxVertWave;

    @Override
    protected void setup(SceneState p_state) {
        SKETCH.currentCamera.resetCamParams();
        SKETCH.currentCamera.resetSettings();

        this.boxHorizWave = new SineWave(SKETCH, 100 / 60_000.0f);
        this.boxHorizWave.start();

        this.boxVertWave = new SineWave(SKETCH, 200 / 60_000.0f);
        this.boxVertWave.start();
    }

    @Override
    protected void draw() {
        SKETCH.background(0x006699);
        // SKETCH.in2d(() -> SKETCH.alphaBg(0, 102, 153, 0));

        // if (SKETCH.frameCount % 150 == 0)
        // System.out.println(SKETCH.frameRate);

        SKETCH.translate(SKETCH.mouse.x, SKETCH.mouse.y);
        SKETCH.translate(SKETCH.cx, SKETCH.cy);
        SKETCH.text("Scene `2`!", 0, 0);

        System.out.println(SKETCH.modelX(0, 0, 0));

        // region Translation.
        // SKETCH.currentCamera.center.z = this.boxHorizWave.get() * SKETCH.qx -
        // SKETCH.qx / 1.5f;

        // this.camHorizWave.absoluteValue = false;
        // SKETCH.currentCamera.center.y = this.camHorizWave.get() * SKETCH.qy -
        // SKETCH.qy / 2;
        // SKETCH.currentCamera.center.y *= 0.5f;
        // SKETCH.currentCamera.center.y += SKETCH.qy * 0.5f;
        // this.camHorizWave.absoluteValue = true;

        // SKETCH.currentCamera.pos.y = this.boxVertWave.get() * SKETCH.qy;
        // endregion

        // SKETCH.translate(
        // this.boxHorizWave.get() * SKETCH.qx,
        // this.boxVertWave.get() * SKETCH.qy, 0);
        SKETCH.box(45);
    }

    @Override
    public void mouseClicked() {
        MANAGER.startScene(TestScene1.class);
    }

}
