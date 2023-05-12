package com.brahvim.nerd.papplet_wrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.ecs.NerdEcsSystem;
import com.brahvim.nerd.framework.scenes.NerdScene;
import com.brahvim.nerd.framework.scenes.NerdSceneManager;
import com.brahvim.nerd.framework.scenes.NerdSceneManager.SceneManagerSettings;
import com.brahvim.nerd.openal.AlContext;

import processing.core.PConstants;

/* `package` */ class NerdSketchBuilderSettings {

    public Class<? extends NerdScene> firstScene;
    public NerdEcsSystem<?>[] ecsSystemOrder = null;
    public SceneManagerSettings sceneManagerSettings;
    public int width = 400, height = 400, antiAliasing;
    public AlContext.AlContextSettings alContextSettings;
    public HashMap<String, Object> nerdExtensions = new HashMap<>();
    public String name, iconPath, renderer = PConstants.P3D, stringTablePath;
    public HashSet<Class<? extends NerdScene>> scenesToPreload = new HashSet<>(0);
    public NerdSceneManager.SceneManagerSettings.CallbackOrder preCallOrder, drawCallOrder, postCallOrder;

    // region Listeners.
    public LinkedHashSet<Consumer<NerdSketch>> sketchConstructedListeners,
            settingsListeners, setupListeners, exitListeners, disposalListeners;

    // Initializing the listeners:
    {
        this.exitListeners = new LinkedHashSet<>();
        this.setupListeners = new LinkedHashSet<>();
        this.settingsListeners = new LinkedHashSet<>();
        this.disposalListeners = new LinkedHashSet<>();
        this.sceneChangeListeners = new LinkedHashSet<>();
        this.sketchConstructedListeners = new LinkedHashSet<>();
    }

    public LinkedHashSet<NerdSceneManager.SceneChangeListener> sceneChangeListeners = new LinkedHashSet<>();

    public LinkedHashSet<Consumer<NerdSketch>> preListeners, postListeners,
            drawListeners, preDrawListeners, postDrawListeners;

    // Intializing these listeners as well, haha:
    {
        this.preListeners = new LinkedHashSet<>();
        this.postListeners = new LinkedHashSet<>();
        this.drawListeners = new LinkedHashSet<>();
        this.preDrawListeners = new LinkedHashSet<>();
        this.postDrawListeners = new LinkedHashSet<>();
    }
    // endregion

    public boolean dontCloseOnEscape, startedFullscreen, canResize,
            cannotFullscreen, cannotAltEnterFullscreen, cannotF11Fullscreen;

}
