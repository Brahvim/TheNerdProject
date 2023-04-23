package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.data.XML;

public class XMLAsset extends AssetLoader<XML> {

	@Override
	public XML fetchData(final Sketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final XML markup = p_sketch.loadXML(p_path);
		if (markup == null)
			throw new AssetLoaderFailedException();
		return markup;
	}

}
