package com.brahvim.nerd.openal;

import com.brahvim.nerd.papplet_wrapper.CustomSketchBuilder;
import com.brahvim.nerd.papplet_wrapper.ext.NerdExt;
import com.brahvim.nerd.rendering.cameras.NerdAbstractCamera;

import processing.core.PVector;

/**
 * A singleton providing a `Sketch` from Nerd, access to OpenAL.
 * Use `getInstance()` to get one!
 */
public class NerdAlExt extends NerdExt {

	private NerdAl alInst;
	private AlContext.AlContextSettings settings;

	private NerdAlExt(final CustomSketchBuilder p_builder) {
		super(p_builder);
	}

	private NerdAlExt(final CustomSketchBuilder p_builder,
			final AlContext.AlContextSettings p_settings) {
		super(p_builder);
		this.settings = p_settings;
	}

	public static NerdAlExt create(final CustomSketchBuilder p_builder) {
		return new NerdAlExt(p_builder);
	}

	public static NerdAlExt create(final CustomSketchBuilder p_builder,
			final AlContext.AlContextSettings p_settings) {
		return new NerdAlExt(p_builder, p_settings);
	}

	@Override
	protected void addLibrary() {
		super.BUILDER.addSketchConstructionListener((s) -> {
			// Create our `NerdAl` instance:
			this.alInst = new NerdAl(this.settings);

			// When the scene is changed, delete unnecessary OpenAL data:
			s.getSceneManager().addSceneChangedListener((a, p, c) -> {
				if (p != null)
					this.alInst.scenelyDisposal();
				this.alInst.unitSize = NerdAl.UNIT_SIZE_3D_PARK_SCENE;
			});

		});

		// I wanted to declare this lambda as an anon class instead, but I wanted to
		// watch this trick where I have a variable from outside the lambda work there.
		// ...It does!:
		final PVector lastCameraPos = new PVector();

		super.BUILDER.addDrawListener((s) -> {
			// Process everything, every frame!:
			this.alInst.framelyCallback();

			final NerdAbstractCamera camera = s.getCamera();

			this.alInst.setListenerOrientation(camera.up);
			this.alInst.setListenerVelocity(PVector.sub(camera.pos, lastCameraPos));
			// PVector.div((PVector.sub(camera.pos, lastCameraPos)), this.unitSize));
			this.alInst.setListenerPosition(PVector.div(camera.pos, this.alInst.unitSize));

			lastCameraPos.set(camera.pos);
		});

		// When the sketch is exiting, delete all OpenAL native data:
		super.BUILDER.addSketchDisposalListener((s) -> this.alInst.completeDisposal());
	}

	@Override
	public NerdExt getLibraryObject() {
		return null;
	}

}
