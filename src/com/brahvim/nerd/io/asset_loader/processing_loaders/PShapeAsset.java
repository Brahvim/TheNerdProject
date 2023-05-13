package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

import processing.core.PShape;

public class PShapeAsset extends NerdAssetLoader<PShape> {

	@Override
	public PShape fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShape shape = p_sketch.loadShape(p_path);

		if (shape == null)
			throw new NerdAssetLoaderException();

		return shape;
	}

}