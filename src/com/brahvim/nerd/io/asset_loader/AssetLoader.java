package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// Keeping this outside saves typing!:
public abstract class AssetLoader<AssetT> {
    // `private static final YourAssetType LOADER = new YourAssetType();`

    protected AssetLoader() {
    }

    // Hey there! Also include this method in your class!:
    public static AssetLoader<?> getLoader() {
        throw new UnsupportedOperationException();
        // return null; // `return YourAssetType.LOADER;`
    }

    /**
     * @throws AssetLoaderFailedException when a failure occurs.
     * @throws IllegalArgumentException   if the options passed to the loader
     *                                    weren't meant for it.
     */
    public abstract AssetT fetchData(
            final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
