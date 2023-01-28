package com.brahvim.nerd.io.asset_loader;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class AssetManKey extends NerdKey {
    public final Sketch SKETCH;

    public AssetManKey(Sketch p_sketch) {
        // Could be more strict about it and ask for a `NerdSceneManager` instance,
        // but won't. It allows for Nerd's API to be extendable.
        this.SKETCH = p_sketch;
    }

    // @Override
    // public boolean isFor(Class<?> p_class) {
    /*
     * // Putting `p_class` in the argument eliminates the need for a `null` check.
     * return NerdScene.class.isAssignableFrom(p_class)
     * || SceneManager.class.isAssignableFrom(p_class);
     * }
     */

}
