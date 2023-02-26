package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// Keeping this outside saves typing!:
public abstract class AssetType<T> {
    // `private final static YourAssetType LOADER = new YourAssetType();`

    protected AssetType() {
    }

    // Hey there! Also include this method in your class!:
    public static AssetType<?> getLoader() {
        return null; // `return YourAssetType.LOADER;`
    }

    public abstract T fetchData(Sketch p_sketch, String p_path, Object... p_options)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
