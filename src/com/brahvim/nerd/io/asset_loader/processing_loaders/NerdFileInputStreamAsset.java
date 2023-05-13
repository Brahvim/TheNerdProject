package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.asset_loader.NerdAssetLoaderException;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.io.asset_loader.NerdAssetLoader;

public class NerdFileInputStreamAsset extends NerdAssetLoader<FileInputStream> {

	@Override
	public FileInputStream fetchData(final NerdSketch p_sketch, final String p_path)
			throws NerdAssetLoaderException, IllegalArgumentException {
		try {
			return new FileInputStream(new File(p_path));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new NerdAssetLoaderException();
		}
	}

}