package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;

public class NerdAssetManager {
    public class AssetKey extends NerdKey {
        public final Sketch SKETCH;

        private AssetKey(Sketch p_sketch) {
            this.SKETCH = p_sketch;
        }

        public boolean isFor(Class<?> p_class) {
            // Putting `p_class` in the argument eliminates the need for a `null` check.
            return NerdAsset.class.equals(p_class);
        }
    }

    private final NerdAssetManager.AssetKey ASSET_KEY;
    private final HashSet<NerdAsset> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
    // Do we even *need* assets in any scene from the very beginning?

    public NerdAssetManager(Sketch p_sketch) {
        this.ASSET_KEY = new AssetKey(p_sketch);
    }

    // region `add()` overloads.
    private NerdAssetManager add(NerdAsset p_asset) {
        this.ASSETS.add(p_asset);
        return this;
    }

    private NerdAssetManager add(NerdAsset... p_assets) {
        for (NerdAsset a : p_assets)
            this.ASSETS.add(a);

        return this;
    }

    public NerdAssetManager add(NerdAssetType p_type, String p_path) {
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        this.ASSETS.add(new NerdAsset(this.ASSET_KEY, p_type, p_path));
        return this;
    }
    // endregion

    public NerdAsset get(String p_fileName) {
        for (NerdAsset a : this.ASSETS)
            if (a.NAME.equals(p_fileName))
                return a;
        return null;
    }

    // region `remove()` overloads.
    public void remove(NerdAsset... p_assets) {
        for (NerdAsset a : p_assets) {
            this.ASSETS.remove(a);
        }
    }

    public void remove(NerdAsset p_asset) {
        this.ASSETS.remove(p_asset);
    }
    // endregion

    public void clear() {
        this.ASSETS.clear();
    }

    public boolean hasCompleted() {
        for (NerdAsset a : this.ASSETS)
            if (!a.hasLoaded())
                return false;
        return true;
    }

    public void ensureCompletion() {
        while (this.hasCompleted())
            ;
    }

}
