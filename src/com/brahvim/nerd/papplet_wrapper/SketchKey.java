package com.brahvim.nerd.papplet_wrapper;

import java.util.HashSet;
import java.util.function.Consumer;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.openal.AlContext.AlContextSettings;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;

import processing.core.PConstants;

// Hmmm... "`SketchSettings`" instead..?
/* package */ class SketchKey extends NerdKey {
    public AlContextSettings alContextSettings;
    public Class<? extends NerdScene> firstScene;
    public SceneManagerSettings sceneManagerSettings;
    public int width = 400, height = 400, antiAliasing;
    public HashSet<Class<? extends NerdScene>> scenesToPreload;
    public Sketch.CallbackOrder preCallOrder, drawCallOrder, postCallOrder;
    public String name, iconPath, renderer = PConstants.P3D, stringTablePath;

    public Consumer<Sketch> exitListener, setupListener, disposalListener, sketchConstructedListener;

    public boolean useOpenal, dontCloseOnEscape, startedFullscreen, canResize,
            cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;

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
