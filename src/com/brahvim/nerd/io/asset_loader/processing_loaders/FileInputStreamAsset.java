package com.brahvim.nerd.io.asset_loader.processing_loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.brahvim.nerd.io.asset_loader.AssetLoaderFailedException;
import com.brahvim.nerd.io.asset_loader.AssetType;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class FileInputStreamAsset extends AssetType<FileInputStream> {

	// region SINGLETON STUFF.
	private static FileInputStreamAsset LOADER = new FileInputStreamAsset();

	public static AssetType<?> getLoader() {
		return FileInputStreamAsset.LOADER;
	}
	// endregion

	@Override
	public FileInputStream fetchData(Sketch p_sketch, String p_path, Object... p_options)
			throws AssetLoaderFailedException, IllegalArgumentException {
		try {
			return new FileInputStream(new File(p_path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AssetLoaderFailedException();
		}
	}

}