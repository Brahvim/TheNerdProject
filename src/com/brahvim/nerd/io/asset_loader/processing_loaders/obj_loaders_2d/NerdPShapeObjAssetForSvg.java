package com.brahvim.nerd.io.asset_loader.processing_loaders.obj_loaders_2d;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.io.asset_loader.processing_loaders.NerdProcessingAsset;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PShape;
import processing.svg.PGraphicsSVG;

// This doesn't work at all!
// `PShapeOBJ` *surprisingly* doesn't offer much information anyway.

public class NerdPShapeObjAssetForSvg extends NerdSinglePathAssetLoader<PGraphicsSVG, PShape>
		implements NerdProcessingAsset {

	public NerdPShapeObjAssetForSvg(final String p_path) {
		super(p_path);
	}

	@Override
	protected PShape fetchData(final NerdSketch<PGraphicsSVG> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		final PShape shape = p_sketch.loadShape(super.path);

		if (shape == null)
			throw new NerdAssetLoaderException(this);

		return shape;
	}

}