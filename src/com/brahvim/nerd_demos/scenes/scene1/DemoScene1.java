package com.brahvim.nerd_demos.scenes.scene1;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.io.asset_loader.processing_loaders.PFontAsset;
import com.brahvim.nerd.math.easings.built_in_easings.NerdSineEase;
import com.brahvim.nerd_demos.scenes.DemoScene4;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class DemoScene1 extends NerdScene {
	private PFont font;
	private NerdSineEase ease;
	// private AlSource sceneOneAnnounce;

	@Override
	protected synchronized void preload() {
		SKETCH.ASSETS.addAsset(new PFontAsset("data/Arial-Black-48.vlw"));
		// ASSETS.addAsset(new OggBufferDataAsset("data/SceneOne.ogg"));
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		// WINDOW.fullscreen = true;
		if (this.SCENE.getTimesLoaded() == 0)
			WINDOW.centerWindow();

		CAMERA.setClearColor(0x006699);

		this.font = SKETCH.ASSETS.get("Arial-Black-48").getData();
		this.ease = new NerdSineEase(SKETCH, 0.00075f).endWhenAngleIncrementsBy(PConstants.HALF_PI).start();

		// this.sceneOneAnnounce = new AlSource(App.openAl,
		// ASSETS.get("SceneOne").getData());
		// this.sceneOneAnnounce.attachDirectFilter(new AlBandpassFilter(App.openAl)
		// .setBandpassGainHf(0.01f)
		// .setBandpassGainLf(0.18f));

		// this.sceneOneAnnounce.setEffectSlot(
		// new AlAuxiliaryEffectSlot(App.openAl,
		// new AlDistortion(App.openAl)
		// .setDistortionGain(1)));

		// this.sceneOneAnnounce.setGain(0.25f);

		SCENE.addLayer(BoxAnimationLayer.class);
		SCENE.addLayer(RevolvingParticlesLayer.class);

		// this.sceneOneAnnounce.play();
	}

	@Override
	protected void draw() {
		if (this.ease.active) {
			SKETCH.textFont(this.font);
			final float easeVal = this.ease.get();
			SKETCH.colorMode(PConstants.HSB);
			SKETCH.fill(easeVal * 255, 255, 255, 255 * (1 - easeVal));
			SKETCH.text("Scene 1", 0, PApplet.sin(this.SCENE.getMillisSinceStart() * 0.005f) * 25, 50);
			// 0, PApplet.sin(MANAGER.sinceSceneStarted() * 0.0125f) * 25);
		}

		// ..could be lazy about this, haha:
		// if (this.ease.wasActive() && !this.ease.active)
		// this.sceneOneAnnounce.dispose();

		CAMERA.getPos().z = PApplet.abs(PApplet.sin(this.SCENE.getMillisSinceStart() * 0.001f)) * 500;

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
		if (SKETCH.mouseButton == PConstants.RIGHT)
			MANAGER.startScene(DemoScene4.class);
	}

}
