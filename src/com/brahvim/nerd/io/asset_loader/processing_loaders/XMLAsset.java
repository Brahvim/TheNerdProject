package com.brahvim.nerd.io.asset_loader.processing_loaders;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

import processing.data.XML;

public class XMLAsset extends AssetType<XML> {

	// region SINGLETON STUFF.
	private static XMLAsset LOADER = new XMLAsset();

	public static AssetType<XML> getLoader() {
		return XMLAsset.LOADER;
	}
	// endregion

	@Override
	public XML fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		final XML markup = p_sketch.loadXML(p_path);
		if (markup == null)
			throw new AssetLoaderFailedException();
		return markup;
	}

}
