package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;
import processing.core.PShape;

public class NerdPShapeSvgAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, PShape> implements NerdProcessingAsset {

	public NerdPShapeSvgAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected PShape fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShape shape = p_sketch.loadShape(super.path);

		if (shape == null)
			throw new NerdAssetLoaderException(this);

		return shape;
	}

}