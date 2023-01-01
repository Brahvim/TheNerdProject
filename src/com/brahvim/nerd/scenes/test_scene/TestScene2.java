package com.brahvim.nerd.scenes.test_scene;

import com.brahvim.nerd.api.SineWave;
import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

public class TestScene2 extends Scene {
    private SineWave camHorizWave, camVertWave;

    public TestScene2(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
    }

    @Override
    protected void setup() {
        this.camHorizWave = new SineWave(SKETCH, 100 / 60_000.0f);
        this.camHorizWave.start();

        this.camVertWave = new SineWave(SKETCH, 200 / 60_000.0f);
        this.camVertWave.start();
    }

    // TODO: Add "before camera" and "after camera" callbacks.

    @Override
    protected void draw() {
        // Won't work because of the camera, :sweat_smile::
        SKETCH.in2d(() -> SKETCH.alphaBg(0, 102, 153, 100));

        // SKETCH.background(0x006699);

        SKETCH.translate(SKETCH.cx, SKETCH.cy);

        if (SKETCH.frameCount % 150 == 0)
            System.out.println(SKETCH.frameRate);

        SKETCH.text("Scene `2`!", 0, 0);
        SKETCH.currentCamera.center.z = this.camHorizWave.get() * SKETCH.qx - SKETCH.qx / 1.5f;

        // this.camHorizWave.absoluteValue = false;
        // SKETCH.currentCamera.center.y = this.camHorizWave.get() * SKETCH.qy -
        // SKETCH.qy / 2;
        // SKETCH.currentCamera.center.y *= 0.5f;
        // SKETCH.currentCamera.center.y += SKETCH.qy * 0.5f;
        // this.camHorizWave.absoluteValue = true;

        SKETCH.currentCamera.pos.y = this.camVertWave.get() * SKETCH.qy;
        SKETCH.box(45);
    }

    @Override
    public void mouseClicked() {
        MANAGER.startScene(TestScene1.class);
    }
}
