package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.io.asset_loader.NerdSinglePathAssetLoader;
import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PGraphics;

public class NerdFileInputStreamAsset<SketchPGraphicsT extends PGraphics>
		extends NerdSinglePathAssetLoader<SketchPGraphicsT, FileInputStream> {

	public NerdFileInputStreamAsset(final String p_path) {
		super(p_path);
	}

	@Override
	protected FileInputStream fetchData(final NerdSketch<SketchPGraphicsT> p_sketch)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return new FileInputStream(new File(super.path));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException(this);
		}
	}

}