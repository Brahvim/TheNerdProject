package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.asset_loader.AssetManKey;
import com.brahvim.nerd.io.asset_loader.AssetManager;
import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;

public class SceneManager {
    // region Inner classes.
    public class SceneKey extends NerdKey {
        private final SceneManager MANAGER;
        public final Class<? extends NerdScene> INTENDED_USER_CLASS;

        private SceneKey(SceneManager p_sceneManager,
                Class<? extends NerdScene> p_sceneThatWillUseThis) {
            this.MANAGER = p_sceneManager;
            this.INTENDED_USER_CLASS = p_sceneThatWillUseThis;
        }

        public SceneManager getSceneManager() {
            return this.MANAGER;
        }

        @Override
        public boolean isFor(Class<?> p_class) {
            // Putting `p_class` in the argument eliminates the need for a `null` check.
            return this.INTENDED_USER_CLASS.equals(p_class);
        }

        /*
         * public Class<? extends Scene> getSceneClass() {
         * return this.sceneClass;
         * }
         */

    }

    public class SceneData {
        private final Constructor<? extends NerdScene> CONSTRUCTOR;
        private final Class<? extends NerdScene> SCENE_CLASS;
        private NerdScene cachedReference;
        private boolean isDeletable, hasCompletedPreload;

        // region Constructors.
        private SceneData(Class<? extends NerdScene> p_sceneClass,
                Constructor<? extends NerdScene> p_constructor,
                NerdScene p_cachedReference) {
            this.SCENE_CLASS = p_sceneClass;
            this.CONSTRUCTOR = p_constructor;
            this.cachedReference = p_cachedReference;
        }

        private SceneData(Class<? extends NerdScene> p_sceneClass,
                Constructor<? extends NerdScene> p_constructor,
                NerdScene p_cachedReference, boolean p_isDeletable) {
            this.SCENE_CLASS = p_sceneClass;
            this.CONSTRUCTOR = p_constructor;
            this.isDeletable = p_isDeletable;
            this.cachedReference = p_cachedReference;
        }
        // endregion

        // region Getters.
        // No class other than `SceneManager` is allowed to access an instance of
        // `SceneData` anyway, so... these deem useless!:
        /*
         * public Constructor<? extends NerdScene> getSceneConstructor() {
         * return this.CONSTRUCTOR;
         * }
         * 
         * public boolean isDeletable() {
         * return this.isDeletable;
         * }
         * 
         * public boolean hasCompletedPreload() {
         * return this.hasCompletedPreload;
         * }
         * 
         * public NerdScene getCache(SceneKey p_key) {
         * if (p_key == null) {
         * throw new
         * IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
         * }
         * if (p_key.isUsed()) {
         * throw new
         * IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
         * }
         * if (p_key.INTENDED_USER_CLASS.equals(this.SCENE_CLASS)) {
         * throw new
         * IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
         * }
         * 
         * return this.cachedReference;
         * }
         */
        // endregion

        // region Cache queries.
        public boolean cacheIsNull() {
            return this.cachedReference == null;
        }

        public void deleteCache() {
            // If this was the only reference to the scene object, the scene gets GCed!
            this.cachedReference = null;
        }

        public void deleteCacheIfNeeded() {
            // Delete the scene reference if allowed:
            if (this.isDeletable)
                this.deleteCache();
            // If this was the only reference to the scene object, the scene gets GCed!
            System.gc();
        }
        // endregion

    }

    public class SceneManSettingBuilder {
    }

    public static class SceneManagerSettings {
        public final OnSceneSwitch ON_SCENE_SWITCH = new OnSceneSwitch();

        private class OnSceneSwitch {
            public boolean doClear = false,
                    completelyResetCam = true;

            private OnSceneSwitch() {
            }
        }

    }
    // endregion

    public final AssetManager PERSISTENT_ASSETS;

    // region `private` ~~/ `protected`~~ fields.
    /**
     * This {@code HashMap} contains cached data about each {@code NerdScene} class
     * any {@code NerdSceneManager} instance has cached or ran.<br>
     * <br>
     * Actual "caching" of a {@code NerdScene} is when its corresponding
     * {@code SceneCache}'s {@code cachedReference} is not {@code null}.
     */

    // The initial capacity is `2` here to aid performance, since, till the first
    // scene switch, the JIT does no optimization. The one after that should be fast
    // enough.
    private final HashMap<Class<? extends NerdScene>, SceneData> SCENE_CLASS_TO_CACHE = new HashMap<>(2);
    private final AssetManKey ASSET_MAN_KEY;
    private final Sketch SKETCH;

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
    private final SceneManager.SceneManagerSettings SETTINGS;
    private SceneKey currSceneKey;
    private NerdScene currScene;
    private int currSceneStartMillis;
    // endregion

    // region Constructors.
    public SceneManager(Sketch p_sketch) {
        this(p_sketch, new SceneManager.SceneManagerSettings());
    }

    public SceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        this.SKETCH = p_sketch;
        this.SETTINGS = p_settings;
        this.ASSET_MAN_KEY = new AssetManKey(p_sketch);
        this.PERSISTENT_ASSETS = new AssetManager(this.ASSET_MAN_KEY);

        this.initSceneListeners();
    }
    // endregion

    // region Queries.
    // Older implementation:
    /*
     * @SuppressWarnings("unchecked")
     * public Class<? extends Scene>[] getSceneClasses() {
     * return (Class<? extends Scene>[]) this.SCENE_CLASSES.toArray();
     * }
     */

    @SuppressWarnings("unchecked")
    public HashSet<Class<? extends NerdScene>> knownScenes() {
        // Here's what's happening here:

        // HashSet<Class<? extends Scene>> toRetCloneOf = (HashSet<Class<? extends
        // Scene>>)
        // this.SCENE_CACHE.keySet();
        // return (HashSet<Class<? extends Scene>>) toRetCloneOf.clone();

        // Cast the `keySet`, clone it, and cast the clone:

        return ((HashSet<Class<? extends NerdScene>>)

        ((HashSet<Class<? extends NerdScene>>) (this.SCENE_CLASS_TO_CACHE.keySet())).clone());
    }

    public int getCurrSceneStartMillis() {
        return this.currSceneStartMillis;
    }

    public int sinceSceneStarted() {
        return this.SKETCH.millis() - this.currSceneStartMillis;
    }
    // endregion

    // region App workflow callbacks.
    public void setup() {
        if (this.currScene != null)
            this.setupCurrentScene();
    }

    public void pre() {
        if (this.currScene != null)
            this.currScene.runPre(this.currSceneKey);
    }

    public void draw() {
        if (this.currScene != null)
            this.currScene.runDraw(this.currSceneKey);
    }

    public void post() {
        if (this.PERSISTENT_ASSETS != null)
            this.PERSISTENT_ASSETS.updatePreviousLoadState(this.ASSET_MAN_KEY);

        if (this.currScene != null)
            this.currScene.runPost(this.currSceneKey);
    }
    // endregion

    // region `Scene`-operations.
    private void initSceneListeners() {
        final SceneManager SCENE_MAN = this;

        this.mouseListener = this.SKETCH.new SketchMouseListener() {
            @Override
            public void mousePressed() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mousePressed();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mousePressed();
            }

            @Override
            public void mouseReleased() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseReleased();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseReleased();
            }

            @Override
            public void mouseMoved() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseMoved();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseMoved();
            }

            @Override
            public void mouseClicked() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseClicked();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseClicked();
            }

            @Override
            public void mouseDragged() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseDragged();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseDragged();
            }

            @Override
            public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.mouseWheel(p_mouseEvent);
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
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
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchStarted();
            }

            @Override
            public void touchMoved() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.touchMoved();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchMoved();
            }

            @Override
            public void touchEnded() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.touchEnded();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
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
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.focusLost();
            }

            @Override
            public void resized() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.resized();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.resized();
            }

            @Override
            public void focusGained() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.focusGained();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
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
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyTyped();
            }

            @Override
            public void keyPressed() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.keyPressed();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyPressed();
            }

            @Override
            public void keyReleased() {
                if (SCENE_MAN.currScene == null)
                    return;

                SCENE_MAN.currScene.keyReleased();
                for (NerdLayer l : SCENE_MAN.currScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyReleased();
            }

        };
    }

    public void loadSceneAssetsAsync(Class<? extends NerdScene> p_sceneClass) {
        if (!this.hasCached(p_sceneClass))
            this.cacheScene(p_sceneClass, true);

        // TODO: Caching entire scene objects takes memory. Find an alternative!

        if (this.hasCompletedPreload(p_sceneClass))
            return;

        Thread thread = new Thread(() -> {
            this.loadSceneAssets(p_sceneClass);
        });

        thread.setName("AssetLoader_" + this.getClass().getSimpleName());
        thread.start();
    }

    public synchronized void loadSceneAssets(Class<? extends NerdScene> p_sceneClass) {
        SceneData cache = this.SCENE_CLASS_TO_CACHE.get(p_sceneClass);

        if (cache != null)
            if (cache.hasCompletedPreload)
                return;

        cache.cachedReference.preload();
        cache.hasCompletedPreload = true;
    }

    public synchronized void loadSceneAssets(NerdScene p_scene, SceneManager.SceneKey p_key) {
        if (p_scene == null)
            return;

        if (p_scene.hasCompletedPreload())
            return;

        p_scene.runPreload(p_key);
    }

    public boolean hasCompletedPreload(Class<? extends NerdScene> p_sceneClass) {
        return this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference.hasCompletedPreload();
    }

    public void restartScene() {
        if (this.currSceneClass == null)
            return;

        SceneData cache = this.SCENE_CLASS_TO_CACHE.remove(this.currSceneClass);
        this.currSceneKey = new SceneKey(this, this.currSceneClass);
        NerdScene toUse = this.constructScene(cache.CONSTRUCTOR, this.currSceneKey);
        this.SCENE_CLASS_TO_CACHE.put(this.currSceneClass, cache);

        this.setScene(toUse);
    }

    public void startPreviousScene() {
        if (this.prevSceneClass == null)
            return;

        SceneData cache = this.SCENE_CLASS_TO_CACHE.remove(this.prevSceneClass);
        this.currSceneKey = new SceneKey(this, this.prevSceneClass);
        NerdScene toUse = this.constructScene(cache.CONSTRUCTOR, this.currSceneKey);
        this.SCENE_CLASS_TO_CACHE.put(this.prevSceneClass, cache);

        this.setScene(toUse);
    }

    public void startScene(Class<? extends NerdScene> p_sceneClass) {
        if (p_sceneClass == null)
            throw new NullPointerException("`SceneManager::startScene()` was `null`.");

        this.startSceneImpl(p_sceneClass);

        /*
         * // This is where `HashSets` shine more than `ArrayList`s!:
         * if (this.SCENE_CLASSES.add(p_sceneClass))
         * this.startSceneImpl(p_sceneClass);
         * else
         * throw new IllegalArgumentException("""
         * Use `SceneManager::restartScene()
         * to instantiate a `Scene` more than once!""");
         */
    }

    public boolean hasCached(Class<? extends NerdScene> p_sceneClass) {
        return !this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cacheIsNull();

        // Ugh, -_- this is cheating...:
        // .cachedReference != null;
    }

    public void cacheScene(Class<? extends NerdScene> p_sceneClass, boolean p_isDeletable) {
        if (this.SCENE_CLASS_TO_CACHE.containsKey(p_sceneClass))
            return;

        Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);
        if (sceneConstructor == null)
            throw new IllegalArgumentException("""
                    The passed class's constructor could not be accessed.""");

        SceneKey sceneKey = new SceneKey(this, p_sceneClass);
        NerdScene toCache = this.constructScene(sceneConstructor, sceneKey);
        if (toCache == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.SCENE_CLASS_TO_CACHE.put(p_sceneClass, new SceneData(
                p_sceneClass, sceneConstructor, toCache, p_isDeletable));
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

    // *The method:*
    /**
     * Ensures scene is cached, then starts it.
     * The {@code boolean} argument tells if the scene is to be deleted once exited.
     */
    public void startCached(Class<? extends NerdScene> p_sceneClass, boolean p_isDeletable) {
        if (!this.hasCached(p_sceneClass)) {
            System.out.println("""
                    `NerdScene` not found in cache. Starting the usual way.
                    \tPutting it in the cache.""");
            this.cacheScene(p_sceneClass, p_isDeletable);
        }

        this.startScene(p_sceneClass);
    }

    // If it ain't cached, start it normally. Tell me if it is cached.
    /**
     * If the scene is cached, resume rendering it. Start it otherwise.
     *
     * @return If the scene was cached or not.
     */
    public boolean resumeCachedScene(Class<? extends NerdScene> p_sceneClass) {
        if (this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cacheIsNull()) {
            System.out.println("`NerdScene` not in resumable state. Starting the usual way.");
            this.startScene(p_sceneClass);
            return false;
        } else {
            this.setScene(this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).cachedReference);
            return true;
        }
    }

    // region `private` Scene-operations.
    private Constructor<? extends NerdScene> getSceneConstructor(Class<? extends NerdScene> p_sceneClass) {
        Constructor<? extends NerdScene> toRet = null;

        try {
            toRet = p_sceneClass.getConstructor(SceneKey.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return toRet;
    }

    private NerdScene constructScene(Constructor<? extends NerdScene> p_sceneConstructor, SceneKey p_sceneKey) {
        NerdScene toRet = null;

        try {
            toRet = (NerdScene) p_sceneConstructor.newInstance(p_sceneKey);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return toRet;
    }

    // Yes, this checks for errors.
    private void startSceneImpl(Class<? extends NerdScene> p_sceneClass) {
        Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

        if (sceneConstructor == null)
            throw new IllegalArgumentException("""
                    The passed class's constructor could not be accessed.""");

        this.currSceneKey = new SceneKey(this, p_sceneClass);
        NerdScene toStart = this.constructScene(sceneConstructor, this.currSceneKey);

        if (toStart == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.setScene(toStart);

        // Don't worry about concurrency, vvv *this* vvv is `final`! ^-^
        if (!this.SCENE_CLASS_TO_CACHE.containsKey(p_sceneClass)) {
            this.SCENE_CLASS_TO_CACHE.put(p_sceneClass, new SceneData(p_sceneClass, sceneConstructor, toStart));
        }
    }

    // The scene-deleter!!!
    private void setScene(NerdScene p_currentScene) {
        // region `this.settings.onSceneSwitch` tasks.
        if (this.SETTINGS.ON_SCENE_SWITCH.doClear)
            this.SKETCH.clear();

        if (this.SETTINGS.ON_SCENE_SWITCH.completelyResetCam)
            this.SKETCH.currentCamera.completeReset();
        // endregion

        this.prevSceneClass = this.currSceneClass;
        if (this.prevSceneClass != null) {
            this.currScene.runOnSceneExit(this.currSceneKey);

            this.SCENE_CLASS_TO_CACHE.get(this.prevSceneClass)
                    .deleteCacheIfNeeded();

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

        this.currScene = p_currentScene;
        this.setupCurrentScene();
    }

    // Set the time, *then* call `SceneManager::runSetup()`.
    private void setupCurrentScene() {
        this.loadSceneAssets(this.currScene, this.currSceneKey);

        // Helps in resetting style and transformation info across scenes! YAY!:
        if (this.prevSceneClass != null)
            this.SKETCH.pop();

        this.SKETCH.push();

        this.currSceneStartMillis = this.SKETCH.millis();
        this.currScene.runSetup(this.currSceneKey);
    }
    // endregion
    // endregion

    // region Getters.
    public Sketch getSketch() {
        return this.SKETCH;
    }

    public NerdScene getCurrScene() {
        return this.currScene;
    }

    public Class<? extends NerdScene> getCurrSceneClass() {
        return this.currSceneClass;
    }

    public Class<? extends NerdScene> getPrevSceneClass() {
        return this.prevSceneClass;
    }
    // endregion

}
