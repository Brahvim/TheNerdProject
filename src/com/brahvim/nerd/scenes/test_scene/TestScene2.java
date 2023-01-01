package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.api.SineWave;
import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

public class TestScene2 extends Scene {
    private SineWave boxHorizWave, boxVertWave;

    public TestScene2(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
    }

    @Override
    protected void setup() {
        this.boxHorizWave = new SineWave(SKETCH, 100 / 60_000.0f);
        this.boxHorizWave.start();

        this.boxVertWave = new SineWave(SKETCH, 200 / 60_000.0f);
        this.boxVertWave.start();
    }

    @Override
    protected void draw() {
        SKETCH.in2d(() -> SKETCH.alphaBg(0, 102, 153, 100));
        // SKETCH.background(0x006699);

        if (SKETCH.frameCount % 150 == 0)
            System.out.println(SKETCH.frameRate);

        SKETCH.translate(SKETCH.cx, SKETCH.cy);
        SKETCH.text("Scene `2`!", 0, 0);

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

        SKETCH.translate(
                this.boxHorizWave.get() * SKETCH.qx,
                this.boxVertWave.get() * SKETCH.qy, 0);
        SKETCH.box(45);
    }

    @Override
    public void mouseClicked() {
        MANAGER.startScene(TestScene1.class);
    }
}
