package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;

public class FileInputStreamAsset extends AssetLoader<FileInputStream> {

	@Override
	public FileInputStream fetchData(final NerdSketch p_sketch, final String p_path)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return new FileInputStream(new File(p_path));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
		}
	}

}