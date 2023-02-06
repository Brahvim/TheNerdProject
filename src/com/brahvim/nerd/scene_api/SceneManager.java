package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.asset_loader.AssetManKey;
import com.brahvim.nerd.io.asset_loader.AssetManager;
import com.brahvim.nerd.papplet_wrapper.Sketch;

public class SceneManager {

    // region Inner classes.
    // region ...here lies `SceneKey`.
    // Why was it rejected? Read this...:
    /*
     * ...because making a new constructor in every subclass is a way to pass the
     * key object around!
     * 
     * ...of course, it's extra code that you have to write out, and..
     * 
     * I learnt that `protected` fields can also be accessed by any class in the
     * same package as the declaring class. If somebody were to construct a
     * `NerdScene` instance (for example, by using a `static` method in their
     * subclass, which could access the constructor because it's `protected`),
     * they won't be able to initialize important fields like `STATE` and `ASSETS`
     * anyway. Sad.
     */

    // ...here. Here it actually lies:
    /*
     * public static class SceneKey extends NerdKey {
     * 
     * private final SceneManager MANAGER;
     * public final Class<? extends NerdScene> INTENDED_USER_CLASS;
     * 
     * private SceneKey(SceneManager p_sceneManager,
     * Class<? extends NerdScene> p_sceneThatWillUseThis) {
     * this.MANAGER = p_sceneManager;
     * this.INTENDED_USER_CLASS = p_sceneThatWillUseThis;
     * }
     * 
     * public SceneManager getSceneManager() {
     * return this.MANAGER;
     * }
     * 
     * @Override
     * public boolean isFor(Class<?> p_class) {
     * // Putting `p_class` in the argument eliminates the need for a `null` check.
     * return this.INTENDED_USER_CLASS.equals(p_class);
     * }
     * 
     * /*
     * public Class<? extends Scene> getSceneClass() {
     * return this.sceneClass;
     * }
     */
    // endregion

    /**
     * Stores scene data while a scene is not active.
     */
    private static class SceneCache {

        // region Fields.
        private final Constructor<? extends NerdScene> CONSTRUCTOR;
        private final SceneState STATE;
        private int timesLoaded = 0;

        private NerdScene cachedReference; // A `SceneManager` should delete this when the scene exits.
        // endregion

        // region Constructor[s].
        private SceneCache(Constructor<? extends NerdScene> p_constructor, NerdScene p_cachedReference) {
            this.CONSTRUCTOR = p_constructor;
            this.cachedReference = p_cachedReference;
            this.STATE = this.cachedReference.STATE;
        }
        // endregion

        // region Cache queries.
        public boolean cacheIsNull() {
            return this.cachedReference == null;
        }

        public void /* nullifyCache() { */ deleteCache() {
            // If this was (hopefully) the only reference to the scene object, it gets GCed!
            this.cachedReference = null;
            System.gc();
        }
        // endregion

    }

    public static class SceneManagerSettings {

        private class OnSceneSwitch {

            /**
             * If set to {@code -1}, will call {@link Sketch#clear()} and not
             * {@link Sketch#background()}. <b>This is the default behavior!</b>
             */
            public int clearColor = -1;

            /**
             * Clears the screen according to
             * {@link SceneManager.SceneManagerSettings.OnSceneSwitch#clearColor}.<br>
             * <br>
             * {@code false} by default.
             */
            public boolean doClear = false;

            /**
             * Resets {@link Sketch#currentCamera} if {@code true}.
             * {@code true} by default!
             */
            public boolean completelyResetCam = true;

            private OnSceneSwitch() {
            }

        }

        public final OnSceneSwitch ON_SCENE_SWITCH = new OnSceneSwitch();

    }
    // endregion

    public final AssetManager PERSISTENT_ASSETS;

    // region `private` fields.
    /**
     * This {@code HashMap} contains cached data about each {@code NerdScene} class
     * any {@code NerdSceneManager} instance has cached or ran.<br>
     * <br>
     * 
     * Actual "caching" of a {@code NerdScene} is when its corresponding
     * {@code SceneCache}'s {@code cachedReference} is not {@code null}.<br>
     * <br>
     * 
     * The initial capacity here (`2`) is to aid performance, since, the JIT
     * does no optimization till the first scene switch. All scene switches after
     * that the initial should be fast enough!
     */
    private final HashMap<Class<? extends NerdScene>, SceneCache> SCENE_CLASS_TO_CACHE = new HashMap<>(2);
    private final SceneManager.SceneManagerSettings SETTINGS;
    private final AssetManKey PERSISTENT_ASSET_MAN_KEY;
    private final Sketch SKETCH;

    // Notes on some strange (useless? Useful?!) idea.
    /*
     * Keep track of what `Sketch`es a manager exists for.
     * `private final static HashSet<? extends Sketch> SKETCHES = new HashSet<>(1);
     * "Don't let anybody build another!" <-- ...idea I've stopped.
     */

    // region Sketch Event Listeners.
    @SuppressWarnings("unused")
    private Sketch.SketchMouseListener mouseListener;

    @SuppressWarnings("unused")
    private Sketch.SketchTouchListener touchListener;

    @SuppressWarnings("unused")
    private Sketch.SketchWindowListener windowListener;

    @SuppressWarnings("unused")
    private Sketch.SketchKeyboardListener keyboardListener;
    // endregion

    private Class<? extends NerdScene> currSceneClass, prevSceneClass;
    private NerdScene currScene;
    // endregion

    // region Construction.
    public SceneManager(Sketch p_sketch) {
        this(p_sketch, new SceneManager.SceneManagerSettings());
    }

    public SceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        this.SKETCH = p_sketch;
        this.SETTINGS = p_settings;
        this.PERSISTENT_ASSET_MAN_KEY = new AssetManKey(p_sketch);
        this.PERSISTENT_ASSETS = new AssetManager(this.PERSISTENT_ASSET_MAN_KEY);

        this.initSceneListeners();
    }

    private void initSceneListeners() {
        final SceneManager SCENE_MAN = this;

        this.mouseListener = this.SKETCH.new SketchMouseListener() {
            @Override
            public void mousePressed() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mousePressed();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mousePressed();
            }

            @Override
            public void mouseReleased() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseReleased();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseReleased();
            }

            @Override
            public void mouseMoved() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseMoved();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseMoved();
            }

            @Override
            public void mouseClicked() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseClicked();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseClicked();
            }

            @Override
            public void mouseDragged() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseDragged();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseDragged();
            }

            @Override
            public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseWheel(p_mouseEvent);
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseWheel(p_mouseEvent);
            }
        };

        this.touchListener = this.SKETCH.new SketchTouchListener() {
            @Override
            public void touchStarted() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.touchStarted();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchStarted();
            }

            @Override
            public void touchMoved() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.touchMoved();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchMoved();
            }

            @Override
            public void touchEnded() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.touchEnded();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchEnded();
            }
        };

        this.windowListener = this.SKETCH.new SketchWindowListener() {
            @Override
            public void focusLost() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.focusLost();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.focusLost();
            }

            @Override
            public void resized() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.resized();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.resized();
            }

            @Override
            public void focusGained() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.focusGained();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.focusGained();
            }
        };

        this.keyboardListener = this.SKETCH.new SketchKeyboardListener() {
            @Override
            public void keyTyped() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.keyTyped();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyTyped();
            }

            @Override
            public void keyPressed() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.keyPressed();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyPressed();
            }

            @Override
            public void keyReleased() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.keyReleased();
                for (NerdLayer l : SCENE_MAN.currScene.getLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyReleased();
            }
        };
    }
    // endregion

    // region [`public`] Getters.
    public Sketch getSketch() {
        return this.SKETCH;
    }

    public NerdScene getCurrentScene() {
        return this.currScene;
    }

    public SceneManagerSettings getManagerSettings() {
        return this.SETTINGS;
    }

    public Class<? extends NerdScene> getCurrentSceneClass() {
        return this.currSceneClass;
    }

    public Class<? extends NerdScene> getPreviousSceneClass() {
        return this.prevSceneClass;
    }
    // endregion

    // region [`public`] Queries.
    /**
     * Returns a {@link HashSet} of {@link NerdScene} classes including only classes
     * instances of which this {@link SceneManager} has ran.
     *
     * @deprecated Need to check to see if this actually works
     *             (it probably doesn't)!
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public HashSet<Class<? extends NerdScene>> knownScenes() {
        // Here's what's happening here:

        // HashSet<Class<? extends Scene>> toRetCloneOf =
        // (HashSet<Class<? extends Scene>>) this.SCENE_CACHE.keySet();
        // return (HashSet<Class<? extends Scene>>) toRetCloneOf.clone();

        // Cast the `keySet`, clone it, and cast the clone:

        return ((HashSet<Class<? extends NerdScene>>)

        ((HashSet<Class<? extends NerdScene>>) (this.SCENE_CLASS_TO_CACHE.keySet())).clone());
    }

    // Older implementation of `knownScenes()`:
    /*
     * @SuppressWarnings("unchecked")
     * public Class<? extends Scene>[] getSceneClasses() {
     * return (Class<? extends Scene>[]) this.SCENE_CLASSES.toArray();
     * }
     */
    // endregion

    // region App workflow callbacks. Please don't use these outside any `PApplet`!
    public void pre() {
        if (this.currScene != null)
            this.currScene.runPre();
    }

    public void draw() {
        if (this.currScene != null)
            this.currScene.runDraw();
    }

    public void post() {
        if (this.PERSISTENT_ASSETS != null)
            this.PERSISTENT_ASSETS.updatePreviousLoadState(this.PERSISTENT_ASSET_MAN_KEY);

        if (this.currScene != null)
            this.currScene.runPost();
    }

    public void exit() {
        if (this.currScene != null)
            this.currScene.runExit();
    }
    // endregion

    // region `Scene`-operations.
    public int timesGivenSceneWasLoaded(Class<? extends NerdScene> p_sceneClass) {
        return this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).timesLoaded;
    }

    // To those who want vararg versions
    // of these `loadSceneAssets` tasks:
    // "...no!".

    public void loadSceneAssetsAsync(Class<? extends NerdScene> p_sceneClass) {
        if (!this.hasCached(p_sceneClass))
            this.cacheScene(p_sceneClass, true);

        if (this.givenSceneRanPreload(p_sceneClass))
            return;

        Thread thread = new Thread(() -> {
            this.loadSceneAssets(p_sceneClass);
        });

        thread.setName("AssetLoader_" + this.getClass().getSimpleName());
        thread.start();
    }

    public void loadSceneAssets(Class<? extends NerdScene> p_sceneClass) {
        if (!this.hasCached(p_sceneClass))
            this.cacheScene(p_sceneClass, true);

        if (this.givenSceneRanPreload(p_sceneClass))
            return;

        SceneCache cache = this.SCENE_CLASS_TO_CACHE.get(p_sceneClass);

        if (cache != null)
            if (cache.cachedReference.hasCompletedPreload())
                return;

        cache.cachedReference.runPreload();
    }

    // region Starting a scene.
    public void restartScene() {
        this.restartScene(null);
    }

    public void restartScene(SceneState p_setupState) {
        if (this.currSceneClass == null)
            return;

        // SceneCache data = this.SCENE_CLASS_TO_CACHE.get(this.currSceneClass);
        // NerdScene toUse = this.constructAndCacheScene(data.CONSTRUCTOR);
        this.startSceneImpl(this.currSceneClass, p_setupState);
    }

    public void startPreviousScene() {
        this.startPreviousScene(null);
    }

    public void startPreviousScene(SceneState p_setupState) {
        if (this.prevSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CLASS_TO_CACHE.get(this.prevSceneClass);
        NerdScene toUse = this.constructScene(cache.CONSTRUCTOR);

        this.setScene(toUse, p_setupState);
    }

    // "Cache if not cached" / "Start cached" method.
    // Used to experience these (now solved!) problems:
    /*
     * - Asking for deletion permissions when you may not be caching is awkward,
     * - Structure. `cache == null`, `cache.getCache() == null` must result in the
     * same, but can't be grouped together logicaly, for optimization. This can be
     * fixed with the use of an "impl" method, but this class already has too many
     * similarly-named methods!
     * 
     * Another approach would be to call `SceneManager::cacheScene()` then query
     * `SceneManager::SCENE_CACHE`, but that sounds even slower. Even with the JIT!
     */

    /**
     * Starts a {@code NerdScene}, and tells using the return value, whether it was
     * restored from cache or started again.
     */
    public boolean startScene(Class<? extends NerdScene> p_sceneClass) {
        return this.startScene(p_sceneClass, null);
    }

    public boolean startScene(Class<? extends NerdScene> p_sceneClass, SceneState p_setupState) {
        if (p_sceneClass == null)
            throw new NullPointerException("`SceneManager::startScene()` received `null`.");

        if (this.hasCached(p_sceneClass)) {
            this.setScene(this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference, p_setupState);
            return true;
        } else {
            this.startSceneImpl(p_sceneClass, p_setupState);
            return false;
        }

        /*
         * // This is where `HashSets` shine more than `ArrayList`s!:
         * if (this.SCENE_CLASSES.add(p_sceneClass))
         * this.startSceneImpl(p_sceneClass);
         * else
         * throw new IllegalArgumentException("""
         * Use `SceneManager::restartScene()
         * to restart a `NerdScene` while it runs!""");
         */

    }
    // endregion

    // region `private` `NerdScene` operations.
    private boolean givenSceneRanPreload(Class<? extends NerdScene> p_sceneClass) {
        return this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference.hasCompletedPreload();
    }

    private void loadSceneAssets(NerdScene p_scene) {
        if (p_scene == null)
            return;

        if (p_scene.hasCompletedPreload())
            return;

        p_scene.runPreload();
    }

    // region (`private`) Caching operations.
    @SuppressWarnings("unused")
    private void ensureCache(Class<? extends NerdScene> p_sceneClass) {
        if (!this.hasCached(p_sceneClass))
            this.cacheScene(p_sceneClass, false);
    }

    private boolean hasCached(Class<? extends NerdScene> p_sceneClass) {
        // If you haven't been asked to run the scene even once, you didn't cache it!
        // Say you haven't!:
        if (!this.SCENE_CLASS_TO_CACHE.containsKey(p_sceneClass))
            return false;

        // ...so you ran the scene? Great! ...BUT DO YOU HAVE THE SCENE OBJECT?!
        return !this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cacheIsNull();

        // Ugh, -_- this is cheating...:
        // return !this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference != null;
    }

    private void cacheScene(Class<? extends NerdScene> p_sceneClass, boolean p_isDeletable) {
        if (this.SCENE_CLASS_TO_CACHE.containsKey(p_sceneClass))
            return;

        Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

        NerdScene toCache = this.constructScene(sceneConstructor);

        if (toCache == null)
            throw new RuntimeException("The scene could not be constructed.");
    }
    // endregion

    // region `private` construction-and-setup operations!
    private Constructor<? extends NerdScene> getSceneConstructor(Class<? extends NerdScene> p_sceneClass) {
        Constructor<? extends NerdScene> toRet = null;

        try {
            toRet = p_sceneClass.getConstructor();
        } catch (NoSuchMethodException e) {
            System.err.println("""
                    Every subclass of `NerdScene` must be `public` with a `public` \"null-constructor\"
                        (constructor with no arguments), or no overriden constructors at all.""");
            // e.printStackTrace();
        }

        return toRet;
    }

    private NerdScene constructScene(Constructor<? extends NerdScene> p_sceneConstructor) {
        NerdScene toRet = null;

        try {
            toRet = (NerdScene) p_sceneConstructor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // region Initialize it!
        final Class<? extends NerdScene> SCENE_CLASS = p_sceneConstructor.getDeclaringClass();
        final SceneCache SCENE_CACHE = this.SCENE_CLASS_TO_CACHE.get(SCENE_CLASS);
        final AssetManKey RETURNED_SCENE_ASSET_MANAGER_KEY = new AssetManKey(this.SKETCH);

        // Initialize fields as if this was a part of the construction.
        toRet.MANAGER = this;
        toRet.SKETCH = this.SKETCH;
        toRet.CAMERA = this.SKETCH.getCurrentCamera();
        // toRet.setAssetManagerKey(RETURNED_SCENE_ASSET_MANAGER_KEY);
        toRet.ASSETS = new AssetManager(RETURNED_SCENE_ASSET_MANAGER_KEY); // Is this actually a good idea?

        // If this is the first time we're constructing this scene, ensure it has a
        // cache and a saved state!
        if (SCENE_CACHE == null) {
            toRet.STATE = new SceneState();
            this.SCENE_CLASS_TO_CACHE.put(SCENE_CLASS, new SceneCache(p_sceneConstructor, toRet));
        } else {
            toRet.STATE = SCENE_CACHE.STATE;
        }

        if (SCENE_CACHE != null)
            SCENE_CACHE.timesLoaded++;
        // endregion

        return toRet;
    }

    // Yes, this checks for errors.
    private void startSceneImpl(Class<? extends NerdScene> p_sceneClass, SceneState p_state) {
        Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

        NerdScene toStart = this.constructScene(sceneConstructor);

        if (toStart == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.setScene(toStart, p_state);
    }

    // The scene-deleter!!!
    private void setScene(NerdScene p_currentScene, SceneState p_state) {
        // region `this.SETTINGS.onSceneSwitch` tasks.
        if (this.SETTINGS.ON_SCENE_SWITCH.doClear) {
            if (this.SETTINGS.ON_SCENE_SWITCH.clearColor == -1)
                this.SKETCH.clear();
            else
                this.SKETCH.background(this.SETTINGS.ON_SCENE_SWITCH.clearColor);
        }

        if (this.SETTINGS.ON_SCENE_SWITCH.completelyResetCam)
            this.SKETCH.getCurrentCamera().completeReset();
        // endregion

        this.prevSceneClass = this.currSceneClass;
        if (this.prevSceneClass != null) {
            // Exit the scene, and nullify the cache.
            this.currScene.runSceneExited();

            if (!this.hasCached(this.currSceneClass))
                this.currScene.ASSETS.clear();

            SceneCache cache = this.SCENE_CLASS_TO_CACHE.get(this.currSceneClass);
            cache.deleteCache();

            // What `deleteCacheIfCan()` did, I guess (or used to do)!:
            /*
             * // Delete the scene reference if needed:
             * SceneCache oldSceneCache = this.SCENE_CACHE.get(this.previousSceneClass);
             * if (!oldSceneCache.doNotDelete)
             * oldSceneCache.deleteCache();
             * // If this was the only reference to the scene object, the scene gets GCed!
             * System.gc();
             */

        }

        this.currSceneClass = p_currentScene.getClass();
        this.currScene = p_currentScene;
        this.setupCurrentScene(p_state);
    }

    // Set the time, *then* call `SceneManager::runSetup()`.
    private void setupCurrentScene(SceneState p_state) {
        this.loadSceneAssets(this.currScene);

        // Helps in resetting style and transformation info across scenes! YAY!:
        if (this.prevSceneClass != null)
            this.SKETCH.pop();

        this.SKETCH.push();

        this.currScene.runSetup(p_state);
    }
    // endregion
    // endregion
    // endregion

}
