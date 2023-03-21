package com.brahvim.nerd.papplet_wrapper;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;

import processing.core.PConstants;

// Hmmm... "`SketchSettings`" instead..?
/* package */ class SketchKey extends NerdKey {
    public Class<? extends NerdScene> firstScene;
    public SceneManagerSettings sceneManagerSettings;
    public int width = 400, height = 400, antiAliasing;
    public AlContext.AlContextSettings alContextSettings;
    public HashSet<Class<? extends NerdScene>> scenesToPreload;
    public Sketch.CallbackOrder preCallOrder, drawCallOrder, postCallOrder;
    public String name, iconPath, renderer = PConstants.P3D, stringTablePath;

    // region Listeners.
    public LinkedHashSet<Consumer<Sketch>> exitListeners, setupListeners,
            settingsListeners, disposalListeners, sketchConstructedListeners;

    // Initializing the listeners:
    {
        this.exitListeners = new LinkedHashSet<>();
        this.setupListeners = new LinkedHashSet<>();
        this.settingsListeners = new LinkedHashSet<>();
        this.disposalListeners = new LinkedHashSet<>();
        this.sketchConstructedListeners = new LinkedHashSet<>();
    }

    public LinkedHashSet<Consumer<Sketch>> preListeners, postListeners,
            drawListeners, preDrawListeners, postDrawListeners;

    // Intializing these listeners as well haha:
    {
        this.preListeners = new LinkedHashSet<>();
        this.postListeners = new LinkedHashSet<>();
        this.drawListeners = new LinkedHashSet<>();
        this.preDrawListeners = new LinkedHashSet<>();
        this.postDrawListeners = new LinkedHashSet<>();
    }
    // endregion

    public boolean useOpenAl, dontCloseOnEscape, startedFullscreen, canResize,
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
