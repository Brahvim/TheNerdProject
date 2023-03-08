package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.core.PApplet;
import processing.opengl.PShader;

public class PShaderAsset extends AssetType<PShader> {

	// region SINGLETON STUFF.
	private static PShaderAsset LOADER = new PShaderAsset();

	public static AssetType<?> getLoader() {
		return PShaderAsset.LOADER;
	}
	// endregion

	/**
	 * To load two shaders, separate the paths passed to {@code p_path} with a `\0`.
	 */
	@Override
	public PShader fetchData(Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final int questId = p_path.indexOf('\0');
		PShader shader = null;

		if (questId != -1) {
			String[] paths = PApplet.split(p_path, '\0');
			shader = p_sketch.loadShader(paths[0], paths[1]);
		} else
			shader = p_sketch.loadShader(p_path);

		if (shader == null)
			throw new AssetLoaderFailedException();

		return shader;
	}

}