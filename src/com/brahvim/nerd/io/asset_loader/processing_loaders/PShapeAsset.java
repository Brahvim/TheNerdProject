package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PShape;

public class PShapeAsset extends NerdSinglePathAssetLoader<PShape> {

	public PShapeAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected PShape fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShape shape = p_sketch.loadShape(super.path);

		if (shape == null)
			throw new NerdAssetLoaderException();

		return shape;
	}

}