package com.brahvim.nerd.io.asset_loader;

import java.util.HashSet;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;

public class AssetManager {
    // region Class `AssetKey`.
    public class AssetKey extends NerdKey {
        public final Sketch SKETCH;

        private AssetKey(Sketch p_sketch) {
            this.SKETCH = p_sketch;
        }

        @Override
        public boolean isFor(Class<?> p_class) {
            // Putting `p_class` in the argument eliminates the need for a `null` check.
            return NerdAsset.class.equals(p_class);
        }
    }
    // endregion

    private final AssetManKey ASSET_MAN_KEY;
    private final AssetManager.AssetKey ASSET_KEY;
    private final HashSet<NerdAsset> ASSETS = new HashSet<>(0); // Start with LITERAL `0`!
    // Do we even *need* assets in any scene from the very beginning?

    // region Constructors.
    @SuppressWarnings("unused")
    private AssetManager() {
        this.ASSET_KEY = null;
        this.ASSET_MAN_KEY = null;
    }

    // public NerdAssetManager(Sketch p_sketch) {
    // this.ASSET_KEY = new AssetKey(p_sketch);
    // }

    public AssetManager(AssetManKey p_key) {
        this.verifyKey(p_key);

        this.ASSET_MAN_KEY = p_key;
        this.ASSET_KEY = new AssetKey(p_key.SKETCH);
    }
    // endregion

    private boolean verifyKey(AssetManKey p_key) {
        if (this.ASSET_MAN_KEY != null)
            return p_key == this.ASSET_MAN_KEY;

        if (p_key == null) {
            throw new IllegalArgumentException("""
                    \"Keys cannot be `null`!\" - a `NerdAssetManager` instance.""");
        } else if (p_key.isUsed()) {
            throw new IllegalArgumentException("""
                    \"That is a used key!\" - a `NerdAssetManager` instance.""");
        } else if (!p_key.isFor(this.getClass()))
            throw new IllegalArgumentException("""
                    That key is not for a `NerdAssetManager`!""");

        p_key.use();
        return true;
    }

    /*
     * private NerdAssetManager add(NerdAsset p_asset) {
     * this.ASSETS.add(p_asset);
     * return this;
     * }
     * 
     * private NerdAssetManager add(NerdAsset... p_assets) {
     * for (NerdAsset a : p_assets)
     * this.ASSETS.add(a);
     * 
     * return this;
     * }
     */

    // region Asset operations.
    public NerdAsset makeAsset(AssetType p_type, String p_path) {
        if (p_type == null || p_path == null)
            throw new IllegalArgumentException("`NerdAsset`s need data!");

        return new NerdAsset(this.ASSET_KEY, p_type, p_path);
    }

    public AssetManager add(AssetType p_type, String p_path) {
        this.ASSETS.add(this.makeAsset(p_type, p_path));
        return this;
    }

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
    // endregion

    // region Load state...
    /**
     * Has every asset completed loading?
     */
    public boolean wasComplete() {
        for (NerdAsset a : this.ASSETS)
            if (!a.wasLoaded())
                return false;
        return true;
    }

    public boolean hasCompleted() {
        for (NerdAsset a : this.ASSETS)
            if (!a.hasLoaded())
                return false;
        return true;
    }

    /**
     * Wait till all assets are done loading!
     */
    public void ensureCompletion() {
        while (this.hasCompleted())
            ;
    }

    public void updatePreviousLoadState(AssetManKey p_key) {
        this.verifyKey(p_key);

        for (NerdAsset a : this.ASSETS) {
            a.updatePreviousLoadState(this.ASSET_KEY);
        }
    }
    // endregion
}
