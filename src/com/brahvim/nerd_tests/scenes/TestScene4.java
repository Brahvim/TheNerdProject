package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneKey;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class TestScene4 extends NerdScene {
    // region Fields!
    private PImage nerd;
    private PGraphics nerdGraphics;
    private float ncx, ncy;

    private final float MAG_SCROLL_ACC_MOD = 0.001f,
            MAG_SCROLL_DECAY_ACC = 0.8f,
            MAG_SCROLL_DECAY_VEL = 0.99f;
    private float magScrollAcc, magScrollVel, magScroll = 1;
    // endregion

    public TestScene4(SceneKey p_sceneKey) {
        super(p_sceneKey);
    }

    @Override
    protected void preload() {
        ASSETS.add(AssetType.PIMAGE, SKETCH.ICON_PATH, () -> {
        });
    }

    @Override
    protected void setup() {
        SKETCH.fullscreen = false;
        SKETCH.getSurface().setSize(1600, 900);
        SKETCH.centerWindow();

        // ASSETS.ensureCompletion();

        this.nerd = this.ASSETS.get("sunglass_nerd").getData();
        this.nerdGraphics = SKETCH.createGraphics(this.nerd.width, this.nerd.height);

        SKETCH.noStroke();
        SKETCH.textureWrap(PConstants.REPEAT);
        SKETCH.currentCamera.pos.z = 500;

        this.ncx = this.nerd.width * 0.5f;
        this.ncy = this.nerd.height * 0.5f;
    }

    private float time() {
        return MANAGER.sinceSceneStarted() * 0.1f;
    }

    @Override
    protected void draw() {
        SKETCH.background(0);

        this.magScrollVel += (this.magScrollAcc *= this.MAG_SCROLL_DECAY_ACC);
        this.magScroll += (this.magScrollVel *= this.MAG_SCROLL_DECAY_VEL);
        SKETCH.currentCamera.pos.z += this.magScrollVel;

        // region Draw the nerds!!!
        SKETCH.beginShape();

        this.nerdGraphics.beginDraw();
        this.nerdGraphics.imageMode(PConstants.CENTER);
        this.nerdGraphics.translate(this.ncx, this.ncy);
        this.nerdGraphics.rotateZ(this.time() * 0.01f);
        this.nerdGraphics.image(this.nerd, 0, 0,
                this.nerd.width * this.magScroll,
                this.nerd.height * this.magScroll);
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
        // endregion

    }

    @Override
    protected void onSceneExit() {
        SKETCH.setSize(SKETCH.INIT_WIDTH, SKETCH.INIT_HEIGHT);
    }

    @Override
    public void mouseWheel(MouseEvent p_mouseEvent) {
        this.magScrollAcc += p_mouseEvent.getCount() * this.MAG_SCROLL_ACC_MOD;
    }

}
