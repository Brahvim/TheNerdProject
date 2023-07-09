package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd_demos.App;
import com.brahvim.nerd_demos.scenes.scene3.DemoScene3;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.MouseEvent;

public class DemoScene4 extends NerdScene {

	// region Fields!
	private PImage nerd;
	private float ncx, ncy;
	private PGraphics nerdGraphics;

	private static final float MAG_SCROLL_ACC_MOD = 0.001f,
			MAG_SCROLL_DECAY_ACC = 0.8f,
			MAG_SCROLL_DECAY_VEL = 0.99f;
	private float magScrollAcc, magScrollVel, magScroll = 1;
	// endregion

	@Override
	protected synchronized void preload() {
		ASSETS.addAsset(new OggBufferDataAsset("data/RUBBER DUCK.ogg"));
		// System.out.println("`DemoScene4` asset preload completed!");
	}

	@Override
	protected void setup(final NerdSceneState p_state) {
		// System.out.printf("`DemoScene4::setup()` here, I was called `%d` times!%n",
		// this.SCENE.getTimesLoaded());

		// region OpenAL Test.
		// ..so the effects and filters wrk perfectly, but I just didn't want them in
		// this example. Feel free to uncomment!~

		// AlAuxiliaryEffectSlot slot = new AlAuxiliaryEffectSlot(App.AL);
		// AlEcho effect = new AlEcho(App.AL);
		// slot.setEffect(effect);
		// effect.setEchoDelay(0.01f);
		// effect.setEchoDamping(0.8f);
		// effect.setEchoFeedback(0.001f);

		// AlLowpassFilter filter = new AlLowpassFilter(App.AL);
		// filter.setLowpassGain(1);
		// filter.setLowpassGainHf(0.1f);

		final AlSource rubberDuck = new AlSource(App.openAl, ASSETS.get("RUBBER DUCK").getData());
		// this.rubberDuck.attachDirectFilter(filter);
		rubberDuck.setGain(0.1f);
		// this.rubberDuck.setEffectSlot(slot);
		// endregion

		// Loaded this scene for the first time? Do this!:
		if (App.FIRST_SCENE_CLASS == DemoScene4.class && this.SCENE.getTimesLoaded() == 1) {
			WINDOW.fullscreen = false;
			WINDOW.setSize(1600, 900);
			WINDOW.centerWindow();
		} else { // Do not play `this.rubberDuck` if this is the first start!
			App.openAl.setListenerVelocity(0, 0, 0);
			App.openAl.setListenerPosition(0, 0, 500);
			App.openAl.setListenerOrientation(0, 1, 0);

			rubberDuck.setPosition(
					5 * (INPUT.mouseX - WINDOW.cx), 0,
					5 * (INPUT.mouseY - WINDOW.cy));

			if (!rubberDuck.isPlaying())
				rubberDuck.play();
		}

		this.nerd = WINDOW.getIconImage();
		this.nerdGraphics = SKETCH.createGraphics(this.nerd.width, this.nerd.height);

		GRAPHICS.noStroke();
		GRAPHICS.getCurrentCamera().getPos().z = 500;
		GRAPHICS.textureWrap(PConstants.REPEAT);

		this.ncx = this.nerd.width * 0.5f;
		this.ncy = this.nerd.height * 0.5f;
	}

	@Override
	protected void draw() {
		GRAPHICS.clear();
		GRAPHICS.translate(-WINDOW.cx, -WINDOW.cy);

		this.magScrollVel += this.magScrollAcc *= DemoScene4.MAG_SCROLL_DECAY_ACC;
		this.magScroll += this.magScrollVel *= DemoScene4.MAG_SCROLL_DECAY_VEL;
		CAMERA.getPos().z += this.magScrollVel;

		// region Draw the nerds!!!
		GRAPHICS.beginShape();

		this.nerdGraphics.beginDraw();
		this.nerdGraphics.imageMode(PConstants.CENTER);
		this.nerdGraphics.translate(this.ncx, this.ncy);
		this.nerdGraphics.rotateZ(this.nerdRotTime() * 0.01f);
		this.nerdGraphics.image(this.nerd, 0, 0,
				this.nerd.width * this.magScroll,
				this.nerd.height * this.magScroll);
		this.nerdGraphics.endDraw();

		GRAPHICS.texture(this.nerdGraphics);

		// For just infinite tiles (no scrolling!):

		// GRAPHICS.vertex(0, 0, 0, 0);
		// GRAPHICS.vertex(WINDOW.width, 0, WINDOW.width, 0);
		// GRAPHICS.vertex(WINDOW.width, WINDOW.height, WINDOW.width, WINDOW.height);
		// GRAPHICS.vertex(0, WINDOW.height, 0, WINDOW.height);

		GRAPHICS.vertex(0, 0, this.nerdRotTime(), this.nerdRotTime());
		GRAPHICS.vertex(WINDOW.width, 0, this.nerdRotTime() + WINDOW.width,
				this.nerdRotTime());
		GRAPHICS.vertex(WINDOW.width, WINDOW.height,
				this.nerdRotTime() + WINDOW.width, this.nerdRotTime() + WINDOW.height);
		GRAPHICS.vertex(0, WINDOW.height, this.nerdRotTime(), this.nerdRotTime() +
				WINDOW.height);

		GRAPHICS.endShape();
		// endregion

		GRAPHICS.in2d(() -> {
			GRAPHICS.translate(GRAPHICS.getMouseInWorld());
			GRAPHICS.circle(0, 0, 200);
		});

	}

	private float nerdRotTime() {
		return this.SCENE.getMillisSinceStart() * 0.1f;
	}

	// region Events.
	@Override
	public void mouseClicked() {
		switch (INPUT.mouseButton) {
			case PConstants.LEFT -> MANAGER.restartScene();
			case PConstants.RIGHT -> MANAGER.startScene(DemoScene3.class);
		}
	}

	@Override
	public void mouseWheel(final MouseEvent p_mouseEvent) {
		this.magScrollAcc += p_mouseEvent.getCount() * DemoScene4.MAG_SCROLL_ACC_MOD;
	}

	@Override
	public void exit() {
		System.out.println("Nerd exited!");
	}
	// endregion

}
