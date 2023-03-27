package com.brahvim.nerd_tests.scenes;

import com.brahvim.nerd.io.asset_loader.processing_loaders.PFontAsset;
import com.brahvim.nerd.math.easings.built_in_easings.SineEase;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd.openal.al_ext_efx.AlAuxiliaryEffectSlot;
import com.brahvim.nerd.openal.al_ext_efx.al_effects.AlDistortion;
import com.brahvim.nerd.openal.al_ext_efx.al_filter.AlBandpassFilter;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneState;
import com.brahvim.nerd_tests.layers.BackgroundLayer;
import com.brahvim.nerd_tests.layers.BoxAnimationLayer;
import com.brahvim.nerd_tests.layers.RevolvingParticlesLayer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class TestScene1 extends NerdScene {

    private PFont font;
    private SineEase ease;
    private AlSource sceneOneAnnounce;

    @Override
    protected synchronized void preload() {
        MANAGER.PERSISTENT_ASSETS.add(PFontAsset.getLoader(), "data/Arial-Black-48.vlw");
        ASSETS.add(OggBufferDataAsset.getLoader(), "data/SceneOne.ogg");
    }

    @Override
    protected void setup(SceneState p_state) {
        // SKETCH.fullscreen = true;
        if (SCENE.getTimesLoaded() == 0)
            SKETCH.centerWindow();

        this.font = MANAGER.PERSISTENT_ASSETS.get("Arial-Black-48").getData();
        this.ease = new SineEase(SKETCH, 0.00075f).endWhenAngleIncrementsBy(90).start();

        this.sceneOneAnnounce = new AlSource(SKETCH.AL, ASSETS.get("SceneOne").getData());
        this.sceneOneAnnounce.attachDirectFilter(new AlBandpassFilter(SKETCH.AL)
                .setBandpassGainHf(0.01f)
                .setBandpassGainLf(0.18f));
        this.sceneOneAnnounce.setEffectSlot(
                new AlAuxiliaryEffectSlot(SKETCH.AL,
                        new AlDistortion(SKETCH.AL).setDistortionGain(1)));
        this.sceneOneAnnounce.setGain(0.25f);

        SCENE.addLayers(
                // Yes, these are started in order:
                BackgroundLayer.class,
                BoxAnimationLayer.class,
                RevolvingParticlesLayer.class);

        this.sceneOneAnnounce.play();
    }

    @Override
    protected void draw() {
        if (this.ease.active) {
            SKETCH.textFont(this.font);
            float easeVal = this.ease.get();
            SKETCH.colorMode(PConstants.HSB);
            SKETCH.fill(easeVal * 255, 255, 255, 255 * (1 - easeVal));
            SKETCH.text("Scene `1`!", 0, PApplet.sin(SCENE.getMillisSinceStart() * 0.005f) * 25, 50);
            // 0, PApplet.sin(MANAGER.sinceSceneStarted() * 0.0125f) * 25);
        }

        if (this.ease.wasActive() && !this.ease.active)
            this.sceneOneAnnounce.dispose();

        CAMERA.pos.z = PApplet.abs(PApplet.sin(SCENE.getMillisSinceStart() * 0.001f)) * 500;

        /*
         * if (SKETCH.frameCount % 5 == 0) {
         * SKETCH.glWindow.setPosition(0, 0);
         * SKETCH.glWindow.setSize(
         * 250 + (int) PApplet.abs((PApplet.sin(MANAGER.sinceSceneStarted() * 0.001f) *
         * 250)),
         * 250 + (int) PApplet.abs((PApplet.sin(MANAGER.sinceSceneStarted() * 0.001f) *
         * 250)));
         * }
         */

    }

    @Override
    public void mouseClicked() {
        switch (SKETCH.mouseButton) {
            case PConstants.RIGHT -> MANAGER.startScene(TestScene4.class);
        }
    }

}
