package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetLoaderOptions;
import com.brahvim.nerd.io.asset_loader.AssetLoader;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class FileInputStreamAsset extends AssetLoader<FileInputStream> {

	// region SINGLETON STUFF.
	private static FileInputStreamAsset LOADER = new FileInputStreamAsset();

	public static AssetLoader<?> getLoader() {
		return FileInputStreamAsset.LOADER;
	}
	// endregion

	@Override
	public FileInputStream fetchData(final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return new FileInputStream(new File(p_path));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
		}
	}

}