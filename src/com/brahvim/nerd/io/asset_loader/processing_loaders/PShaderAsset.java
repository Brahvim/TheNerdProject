package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.opengl.PShader;

public class PShaderAsset extends AssetType<PShader> {

	// region SINGLETON STUFF.
	private static PShaderAsset LOADER = new PShaderAsset();

	public static AssetType<?> getLoader() {
		return PShaderAsset.LOADER;
	}
	// endregion

	@Override
	public PShader fetchData(Sketch SKETCH, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		PShader shader = SKETCH.loadShader(p_path);

		if (shader == null)
			throw new AssetLoaderFailedException();

		return shader;
	}

}