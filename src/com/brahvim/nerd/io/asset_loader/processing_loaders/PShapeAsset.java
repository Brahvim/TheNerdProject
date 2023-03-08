package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PShape;

public class PShapeAsset extends AssetType<PShape> {

	// region SINGLETON STUFF.
	private static PShapeAsset LOADER = new PShapeAsset();

	public static AssetType<PShape> getLoader() {
		return PShapeAsset.LOADER;
	}
	// endregion

	@Override
	public PShape fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		PShape shape = p_sketch.loadShape(p_path);

		if (shape == null)
			throw new AssetLoaderFailedException();

		return shape;
	}

}