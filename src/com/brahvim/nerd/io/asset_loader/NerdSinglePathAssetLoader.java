package com.brahvim.nerd.io.asset_loader;

import processing.core.PGraphics;

public abstract class NerdSinglePathAssetLoader<SketchPGraphicsT extends PGraphics, AssetT>
		extends NerdAssetLoader<SketchPGraphicsT, AssetT> {

	protected NerdSinglePathAssetLoader(final String p_path) {
		this.path = p_path;
	}

	// Not `final` for users.
	protected String path;

	@Override
	protected String getAssetName() {
		return super.findNameFromPath(this.path);
	}

	public String getPath() {
		return this.path;
	}

}
