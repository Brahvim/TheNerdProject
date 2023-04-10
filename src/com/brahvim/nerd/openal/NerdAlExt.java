package com.brahvim.nerd.openal;

import java.util.function.Supplier;

import com.brahvim.nerd.papplet_wrapper.CustomSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.NerdExt;
import com.brahvim.nerd.rendering.cameras.NerdAbstractCamera;

import processing.core.PVector;

public class NerdAlExt extends NerdExt {

	private AlContext.AlContextSettings settings;

	// region Constructors.
	public NerdAlExt(final AlContext.AlContextSettings p_settings) {
		this.settings = p_settings;
	}

	public NerdAlExt(final Supplier<AlContext.AlContextSettings> p_settingsBuilder) {
		if (p_settingsBuilder != null)
			this.settings = p_settingsBuilder.get();
		// `get()` resulting in `null`, won't matter! `AlContext` handles that!~
	}
	// endregion

	@Override
	public Object init(final CustomSketchBuilder p_builder) {
		final NerdAl toRet = new NerdAl(this.settings);

		// When the scene is changed, delete unnecessary OpenAL data:
		p_builder.addSketchConstructionListener((s) -> s
				.getSceneManager().addSceneChangedListener((a, p, c) -> {
					if (p != null)
						toRet.scenelyDisposal();
					toRet.unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;
				}));

		// I wanted to declare this lambda as an anon class instead, but I wanted to
		// watch this trick where I have a variable from outside the lambda work there.
		// ...It does!:
		final PVector lastCameraPos = new PVector();

		p_builder.addDrawListener((s) -> {
			// Process everything, every frame!:
			toRet.framelyCallback();

			final NerdAbstractCamera camera = s.getCamera();

			toRet.setListenerOrientation(camera.up);
			toRet.setListenerVelocity(PVector.sub(camera.pos, lastCameraPos));
			// PVector.div((PVector.sub(camera.pos, lastCameraPos)), this.unitSize));

			// JIT-style optimization + protection from `0`!:
			if (toRet.unitSize == 1.0f)
				toRet.setListenerPosition(camera.pos);
			if (toRet.unitSize == 0.0f)
				toRet.setListenerPosition(camera.pos);
			else
				toRet.setListenerPosition(PVector.div(camera.pos, toRet.unitSize));

			lastCameraPos.set(camera.pos);
		});

		// When the sketch is exiting, delete all OpenAL native data:
		p_builder.addSketchDisposalListener((s) -> toRet.completeDisposal());

		return toRet;

	}

	@Override
	public String getExtName() {
		return "OpenAL";
	}

}
