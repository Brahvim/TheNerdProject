package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

public class NerdPShaderAsset extends NerdAssetLoader<PGraphicsOpenGL, PShader> implements NerdProcessingAsset {

	private final String vertPath, fragPath;

	// region Constructors.
	public NerdPShaderAsset(final String p_fragPath) {
		this(null, p_fragPath);
	}

	public NerdPShaderAsset(final String p_vertPath, final String p_fragPath) {
		this.vertPath = p_vertPath;
		this.fragPath = p_fragPath;
	}
	// endregion

	@Override // Apparently `P2D` supports shaders too! :O
	protected PShader fetchData(final NerdSketch<PGraphicsOpenGL> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShader shader = this.vertPath == null
				? p_sketch.loadShader(this.fragPath)
				: p_sketch.loadShader(this.vertPath, this.fragPath);

		if (shader == null)
			throw new NerdAssetLoaderException(this);

		return shader;
	}

	// region Getters.
	@Override
	protected String getAssetName() {
		if (this.vertPath == null)
			return super.findNameFromPath(this.fragPath);
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