package com.brahvim.nerd.papplet_wrapper;

import java.util.HashSet;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.scene_api.NerdScene;

import processing.core.PConstants;

// Hmmm... "`SketchSettings`" instead..?
/* package */ class SketchKey extends NerdKey {
    public int width = 400, height = 400;
    public Class<? extends NerdScene> firstScene;
    public HashSet<Class<? extends NerdScene>> scenesToPreload;
    public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
    public Sketch.CallbackOrder preCallOrder, drawCallOrder, postCallOrder;

    public boolean useOpenal = true, dontCloseOnEscape, startedFullscreen, canResize,
            cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;

    public Sketch.SketchInsideListener exitListener, setupListener, disposalListener,
            sketchconstructedListener;

    // region Stuff that isn't a field.
    public SketchKey() {
        this.scenesToPreload = new HashSet<>(0);
    }

    @Override
    public boolean isFor(Class<?> p_class) {
        // Putting `p_class` in the argument eliminates the need for a `null` check.
        return Sketch.class.isAssignableFrom(p_class);
    }
    // endregion

}
