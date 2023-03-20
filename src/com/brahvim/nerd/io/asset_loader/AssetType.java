package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// Keeping this outside saves typing!:
public abstract class AssetType<AssetT> {
    // `private final static YourAssetType LOADER = new YourAssetType();`

    protected AssetType() {
    }

    // Hey there! Also include this method in your class!:
    public static AssetType<?> getLoader() {
        throw new UnsupportedOperationException();
        // return null; // `return YourAssetType.LOADER;`
    }

    /**
     * @throws AssetLoaderFailedException when a failure occurs.
     * @throws IllegalArgumentException   if the options passed to the loader
     *                                    weren't meant for it.
     */
    public abstract AssetT fetchData(
            Sketch p_sketch, String p_path, AssetLoaderOptions... p_options)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
