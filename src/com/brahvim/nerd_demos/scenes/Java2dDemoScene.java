package com.brahvim.nerd_demos.scenes;

import com.brahvim.nerd.framework.scene_api.NerdScene;
import com.brahvim.nerd.framework.scene_api.NerdSceneState;
import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.al_asset_loaders.OggBufferDataAsset;
import com.brahvim.nerd_demos.App;

import processing.core.PApplet;

public class Java2dDemoScene extends NerdScene {

	private AlSource rubberDuckSource;

	@Override
	protected synchronized void preload() {
		ASSETS.add(new OggBufferDataAsset(), "data/RUBBER DUCK.ogg");
	}

	@Override
	protected void setup(NerdSceneState p_state) {
		App.OPENAL.unitSize = 1;
		this.rubberDuckSource = new AlSource(App.OPENAL, ASSETS.get("RUBBER DUCK").getData());
	}

	@Override
	protected void draw() {
		App.OPENAL.setListenerPosition(WINDOW.cx, WINDOW.cy, 0);
	}

	@Override
	public void mouseClicked() {
		this.rubberDuckSource.setPosition(
				PApplet.map(INPUT.mouseX, 0, WINDOW.width, WINDOW.qx, WINDOW.q3x),
				PApplet.map(INPUT.mouseY, 0, WINDOW.height, WINDOW.qy, WINDOW.q3y), 0);
		System.out.println(this.rubberDuckSource.getPosition());
		this.rubberDuckSource.play();
	}

}
