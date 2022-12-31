package com.brahvim.nerd.scenes.test_scene;

import java.rmi.server.Skeleton;

import com.brahvim.nerd.api.SineWave;
import com.brahvim.nerd.scene_api.Scene;
import com.brahvim.nerd.scene_api.SceneManager;

public class TestScene2 extends Scene {
    private SineWave camHorizWave;

    public TestScene2(SceneManager.SceneInitializer p_sceneInitializer) {
        super(p_sceneInitializer);
    }

    @Override
    protected void setup() {
        SKETCH.background(0x00669);
        SKETCH.currentCamera.reset();

        this.camHorizWave = new SineWave(SKETCH, 250 / 60_000.0f);
        this.camHorizWave.start();
    }

    @Override
    protected void draw() {
        SKETCH.in2d(() -> SKETCH.alphaBg(0, 102, 153, 100));
        SKETCH.translate(SKETCH.cx, SKETCH.cy);

        if (SKETCH.frameCount % 150 == 0)
            System.out.println(SKETCH.frameRate);

        SKETCH.text("Scene `2`!", 0, 0);
        SKETCH.currentCamera.center.z = this.camHorizWave.get() * SKETCH.qx - SKETCH.qx / 1.5f;

        this.camHorizWave.absoluteValue = false;
        SKETCH.currentCamera.center.y = this.camHorizWave.get() * SKETCH.qy - SKETCH.qy / 2;
        SKETCH.currentCamera.center.y *= 0.5f;
        SKETCH.currentCamera.center.y += SKETCH.qy * 0.5f;
        this.camHorizWave.absoluteValue = true;

        SKETCH.box(45);
    }

    @Override
    public void mouseClicked() {
        MANAGER.setScene(TestScene1.class);
    }
}
