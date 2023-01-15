package com.brahvim.nerd_test.scenes;

import com.brahvim.nerd.io.asset_loader.NerdAsset;
import com.brahvim.nerd.io.asset_loader.NerdAssetType;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.NerdSceneManager.SceneKey;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class TestScene4 extends NerdScene {
    PImage nerd;
    PGraphics nerdGraphics;
    float ncx, ncy;

    public TestScene4(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void preload() {
        ASSETS.add(NerdAssetType.PIMAGE, SKETCH.ICON_PATH);
        System.out.println("TestScene4.preload()");
    }

    @Override
    protected void setup() {
        SKETCH.fullscreen = false;
        SKETCH.setSize(1600, 900);

        // ASSETS.ensureCompletion();

        this.nerd = SKETCH.loadImage("data/sunglass_nerd.png");
        NerdAsset a = this.ASSETS.get("sunglass_nerd");

        System.out.println("TestScene4.setup() - before image.");
        a.completeLoad();
        System.out.println("TestScene4.setup() - AFTER image.");


        this.nerd = a.getData();
        this.nerdGraphics = SKETCH.createGraphics(this.nerd.width, this.nerd.height);

        SKETCH.noStroke();
        SKETCH.textureWrap(PConstants.REPEAT);

        this.ncx = this.nerd.width * 0.5f;
        this.ncy = this.nerd.height * 0.5f;
    }

    float time() {
        return MANAGER.sinceSceneStarted() * 0.1f;
    }

    @Override
    protected void draw() {
        SKETCH.background(0);

        // SKETCH.image(this.nerd, SKETCH.mouseX, SKETCH.mouseY);

        SKETCH.beginShape();

        this.nerdGraphics.beginDraw();
        this.nerdGraphics.imageMode(PConstants.CENTER);
        this.nerdGraphics.translate(this.ncx, this.ncy);
        this.nerdGraphics.rotateZ(this.time() * 0.01f);
        this.nerdGraphics.image(this.nerd, 0, 0);
        this.nerdGraphics.endDraw();

        SKETCH.texture(this.nerdGraphics);

        // For just infinite tiles (no scrolling!):

        // SKETCH.vertex(0, 0, 0, 0);
        // SKETCH.vertex(SKETCH.width, 0, SKETCH.width, 0);
        // SKETCH.vertex(SKETCH.width, SKETCH.height, SKETCH.width, SKETCH.height);
        // SKETCH.vertex(0, SKETCH.height, 0, SKETCH.height);

        SKETCH.vertex(0, 0, this.time(), this.time());
        SKETCH.vertex(SKETCH.width, 0, this.time() + SKETCH.width, this.time());
        SKETCH.vertex(SKETCH.width, SKETCH.height,
                this.time() + SKETCH.width, this.time() + SKETCH.height);
        SKETCH.vertex(0, SKETCH.height, this.time(), this.time() + SKETCH.height);

        SKETCH.endShape();
    }

    @Override
    protected void onSceneExit() {
        SKETCH.setSize(SKETCH.INIT_WIDTH, SKETCH.INIT_HEIGHT);
    }

}
