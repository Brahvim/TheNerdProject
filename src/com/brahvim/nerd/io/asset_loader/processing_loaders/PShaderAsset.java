package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.opengl.PShader;

public class PShaderAsset extends NerdAssetLoader<PShader> {

	private String vertPath, fragPath;

	// region Constructors.
	public PShaderAsset(final String p_fragPath) {
		this.fragPath = p_fragPath;
	}

	public PShaderAsset(final String p_vertPath, final String p_fragPath) {
		this.vertPath = p_vertPath;
		this.fragPath = p_fragPath;
	}
	// endregion

	@Override
	protected PShader fetchData(final NerdSketch p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShader shader = this.vertPath == null
				? p_sketch.loadShader(this.fragPath)
				: p_sketch.loadShader(this.vertPath, this.fragPath);

		if (shader == null)
			throw new NerdAssetLoaderException();

		return shader;
	}

	// region Getters.
	@Override
	protected String getAssetName() {
		return super.findNameFromPath(this.vertPath) + ":" + super.findNameFromPath(this.fragPath);
	}

	public String getVertPath() {
		return this.vertPath;
	}

	public String getFragPath() {
		return this.fragPath;
	}
	// endregion

}