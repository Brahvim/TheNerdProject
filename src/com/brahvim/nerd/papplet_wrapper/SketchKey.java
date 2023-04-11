package com.brahvim.nerd.papplet_wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

import com.brahvim.nerd.openal.AlContext;
import com.brahvim.nerd.scene_api.NerdScene;
import com.brahvim.nerd.scene_api.SceneManager;
import com.brahvim.nerd.scene_api.SceneManager.SceneManagerSettings;

import processing.core.PConstants;

// Hmmm... "`SketchSettings`" instead..?
/* package */ class SketchKey {
    public Class<? extends NerdScene> firstScene;
    public SceneManagerSettings sceneManagerSettings;
    public int width = 400, height = 400, antiAliasing;
    public AlContext.AlContextSettings alContextSettings;
    public HashMap<String, Object> nerdExtensions = new HashMap<>();
    public Sketch.CallbackOrder preCallOrder, drawCallOrder, postCallOrder;
    public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
    public HashSet<Class<? extends NerdScene>> scenesToPreload = new HashSet<>(0);

    // region Listeners.
    public ArrayList<Consumer<Sketch>> sketchConstructedListeners,
            settingsListeners, setupListeners, exitListeners, disposalListeners;

    // Initializing the listeners:
    {
        this.exitListeners = new ArrayList<>();
        this.setupListeners = new ArrayList<>();
        this.settingsListeners = new ArrayList<>();
        this.disposalListeners = new ArrayList<>();
        this.sceneChangeListeners = new ArrayList<>();
        this.sketchConstructedListeners = new ArrayList<>();
    }

    public ArrayList<SceneManager.SceneChangeListener> sceneChangeListeners = new ArrayList<>();

    public ArrayList<Consumer<Sketch>> preListeners, postListeners,
            drawListeners, preDrawListeners, postDrawListeners;

    // Intializing these listeners as well haha:
    {
        this.preListeners = new ArrayList<>();
        this.postListeners = new ArrayList<>();
        this.drawListeners = new ArrayList<>();
        this.preDrawListeners = new ArrayList<>();
        this.postDrawListeners = new ArrayList<>();
    }
    // endregion

    public boolean dontCloseOnEscape, startedFullscreen, canResize,
            cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;
}
