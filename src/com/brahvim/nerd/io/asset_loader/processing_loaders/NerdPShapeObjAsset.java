package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PShape;
import processing.opengl.PGraphics3D;

public class NerdPShapeObjAsset extends NerdSinglePathAssetLoader<PGraphics3D, PShape> implements NerdProcessingAsset {

	public NerdPShapeObjAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected PShape fetchData(final NerdSketch<PGraphics3D> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShape shape = p_sketch.loadShape(super.path);

		if (shape == null)
			throw new NerdAssetLoaderException(this);

		return shape;
	}

}