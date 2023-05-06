package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.papplet_wrapper.NerdSketch;

// Keeping this outside saves typing!:
public abstract class AssetLoader<AssetT> {

    /**
     * @throws AssetLoaderFailedException when a failure occurs.
     * @throws IllegalArgumentException   if the options passed to the loader
     *                                    weren't meant for it.
     */
    public abstract AssetT fetchData(
            final NerdSketch p_sketch, final String p_path)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
