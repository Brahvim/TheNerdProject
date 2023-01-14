package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.io.asset_loader.NerdAssetManager;
import com.brahvim.nerd.misc.NerdKey;
import com.brahvim.nerd.processing_wrapper.Sketch;

public class NerdSceneManager {
    // region Inner classes.
    public class SceneKey extends NerdKey {
        private final NerdSceneManager MANAGER;
        public final Class<? extends NerdScene> INTENDED_USER_CLASS;

        private SceneKey(NerdSceneManager p_sceneManager,
                Class<? extends NerdScene> p_sceneThatWillUseThis) {
            this.MANAGER = p_sceneManager;
            this.INTENDED_USER_CLASS = p_sceneThatWillUseThis;
        }

        public NerdSceneManager getSceneManager() {
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

    public class SceneCache {
        private final Constructor<? extends NerdScene> CONSTRUCTOR;
        private final Class<? extends NerdScene> SCENE_CLASS;
        private NerdScene cachedReference;
        private boolean isDeletable, hasCompletedPreload;

        private SceneCache(Class<? extends NerdScene> p_sceneClass,
                Constructor<? extends NerdScene> p_constructor,
                NerdScene p_cachedReference) {
            this.SCENE_CLASS = p_sceneClass;
            this.CONSTRUCTOR = p_constructor;
            this.cachedReference = p_cachedReference;
        }

        private SceneCache(Class<? extends NerdScene> p_sceneClass,
                Constructor<? extends NerdScene> p_constructor,
                NerdScene p_cachedReference, boolean p_isDeletable) {
            this.SCENE_CLASS = p_sceneClass;
            this.CONSTRUCTOR = p_constructor;
            this.isDeletable = p_isDeletable;
            this.cachedReference = p_cachedReference;
        }

        // region Getters.
        public Constructor<? extends NerdScene> getSceneConstructor() {
            return this.CONSTRUCTOR;
        }

        public boolean isDeletable() {
            return this.isDeletable;
        }

        public boolean hasCompletedPreload() {
            return this.hasCompletedPreload;
        }

        public NerdScene getCache(SceneKey p_key) {
            if (p_key == null) {
                throw new IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
            }
            if (p_key.isUsed()) {
                throw new IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
            }
            if (p_key.INTENDED_USER_CLASS.equals(this.SCENE_CLASS)) {
                throw new IllegalArgumentException("Only `NerdSceneManager`s may use this method.");
            }

            return this.cachedReference;
        }
        // endregion

        // region Cache deletion.
        public void deleteCache() {
            // If this was the only reference to the scene object, the scene gets GCed!
            this.cachedReference = null;
        }

        public void deleteCacheIfCan() {
            // Delete the scene reference if allowed:
            if (this.isDeletable)
                this.deleteCache();
            // If this was the only reference to the scene object, the scene gets GCed!
            System.gc();

        }
        // endregion

    }

    public class SceneManagerSettings {
        public OnSceneSwitch onSceneSwitch = new OnSceneSwitch();

        private class OnSceneSwitch {
            public boolean doClear = false,
                    completelyResetCam = true;

            private OnSceneSwitch() {
            }
        }

    }
    // endregion

    public final NerdAssetManager PERSISTENT_ASSETS;

    // region `private` ~~/ `protected`~~ fields.
    /**
     * This {@code HashMap} contains cached data about each {@code NerdScene} class
     * any {@code NerdSceneManager} instance has cached or ran.<br>
     * <br>
     * Actual "caching" of a {@code NerdScene} is when its corresponding
     * {@code SceneCache}'s {@code cachedReference} is not {@code null}.
     */
    private final HashMap<Class<? extends NerdScene>, SceneCache> SCENE_CLASS_TO_CACHE = new HashMap<>();

    /**
     * Keeping this just-in-case. It would otherwise be passed
     * straight to the {@linkplain NerdSceneManager.SceneKey}
     * constructor instead.
     */
    private final Sketch SKETCH;

    // region Sketch Event Listeners.
    @SuppressWarnings("unused")
    private final Sketch.SketchMouseListener MOUSE_LISTENER;

    @SuppressWarnings("unused")
    private final Sketch.SketchTouchListener TOUCH_LISTENER;

    @SuppressWarnings("unused")
    private final Sketch.SketchWindowListener WINDOW_LISTENER;

    @SuppressWarnings("unused")
    private final Sketch.SketchKeyboardListener KEYBOARD_LISTENER;
    // endregion

    private Class<? extends NerdScene> currentSceneClass, previousSceneClass;
    private NerdSceneManager.SceneManagerSettings settings;
    private SceneKey currentSceneKey;
    private NerdScene currentScene;
    private int sceneStartMillis;
    // endregion

    // region Constructors.
    public NerdSceneManager(Sketch p_sketch) {
        this.SKETCH = p_sketch;
        this.settings = new SceneManagerSettings();
        this.PERSISTENT_ASSETS = new NerdAssetManager(p_sketch);

        final NerdSceneManager SCENE_MAN = this;

        // region Sketch Event Listeners initialization.
        this.MOUSE_LISTENER = this.SKETCH.new SketchMouseListener() {
            @Override
            public void mousePressed() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mousePressed();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mousePressed();
            }

            @Override
            public void mouseReleased() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mouseReleased();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseReleased();
            }

            @Override
            public void mouseMoved() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mouseMoved();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseMoved();
            }

            @Override
            public void mouseClicked() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mouseClicked();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseClicked();
            }

            @Override
            public void mouseDragged() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mouseDragged();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseDragged();
            }

            @Override
            public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.mouseWheel(p_mouseEvent);
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.mouseWheel(p_mouseEvent);
            }

        };

        this.TOUCH_LISTENER = this.SKETCH.new SketchTouchListener() {
            @Override
            public void touchStarted() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.touchStarted();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchStarted();
            }

            @Override
            public void touchMoved() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.touchMoved();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchMoved();
            }

            @Override
            public void touchEnded() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.touchEnded();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.touchEnded();
            }

        };

        this.WINDOW_LISTENER = this.SKETCH.new SketchWindowListener() {
            @Override
            public void focusLost() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.focusLost();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.focusLost();
            }

            @Override
            public void resized() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.resized();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.resized();
            }

            @Override
            public void focusGained() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.focusGained();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.focusGained();
            }

        };

        this.KEYBOARD_LISTENER = this.SKETCH.new SketchKeyboardListener() {
            @Override
            public void keyTyped() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.keyTyped();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyTyped();
            }

            @Override
            public void keyPressed() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.keyPressed();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyPressed();
            }

            @Override
            public void keyReleased() {
                if (SCENE_MAN.currentScene == null)
                    return;

                SCENE_MAN.currentScene.keyReleased();
                for (NerdLayer l : SCENE_MAN.currentScene.allLayers())
                    if (l != null)
                        if (l.isActive())
                            l.keyReleased();
            }

        };
        // endregion

    }

    public NerdSceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        this(p_sketch);
        this.settings = p_settings;
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

    public int getSceneStartMillis() {
        return this.sceneStartMillis;
    }

    public int sinceSceneStarted() {
        return this.SKETCH.millis() - this.sceneStartMillis;
    }
    // endregion

    // region App workflow callbacks.
    public void setup() {
        if (this.currentScene != null)
            this.setupCurrentScene();
    }

    public void pre() {
        if (this.currentScene != null)
            this.currentScene.runPre(this.currentSceneKey);
    }

    public void draw() {
        if (this.currentScene != null)
            this.currentScene.runDraw(this.currentSceneKey);
    }

    public void post() {
        if (this.currentScene != null)
            this.currentScene.runPost(this.currentSceneKey);
    }
    // endregion

    // region `Scene`-operations.
    public void loadSceneAssetsAsync(Class<? extends NerdScene> p_sceneClass) {
        if (this.hasCompletedPreload(p_sceneClass))
            return;

        Thread thread = new Thread(() -> {
            this.loadSceneAssets();
        });

        thread.setName("AssetLoader_" + this.getClass().getSimpleName());
        thread.start();
    }

    public synchronized void loadSceneAssets(Class<? extends NerdScene> p_sceneClass) {

    }

    public boolean hasCompletedPreload(Class<? extends NerdScene> p_sceneClass) {
        return this.SCENE_CLASS_TO_CACHE.get(p_sceneClass).hasCompletedPreload();
    }

    public void restartScene() {
        if (this.currentSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CLASS_TO_CACHE.remove(this.currentSceneClass);
        this.currentSceneKey = new SceneKey(this, this.currentSceneClass);
        NerdScene toUse = this.constructScene(cache.CONSTRUCTOR, this.currentSceneKey);
        this.SCENE_CLASS_TO_CACHE.put(this.currentSceneClass, cache);

        this.setScene(toUse);
    }

    public void startPreviousScene() {
        if (this.previousSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CLASS_TO_CACHE.remove(this.previousSceneClass);
        this.currentSceneKey = new SceneKey(this, this.previousSceneClass);
        NerdScene toUse = this.constructScene(cache.CONSTRUCTOR, this.currentSceneKey);
        this.SCENE_CLASS_TO_CACHE.put(this.previousSceneClass, cache);

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
        return this.SCENE_CLASS_TO_CACHE.get(p_sceneClass)
                .getCache(new SceneKey(this, p_sceneClass)) != null;

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

        this.SCENE_CLASS_TO_CACHE.put(p_sceneClass, new SceneCache(
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
        if (this.SCENE_CLASS_TO_CACHE.get(p_sceneClass)
                .getCache(new SceneKey(this, p_sceneClass)) == null) {
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

        this.currentSceneKey = new SceneKey(this, p_sceneClass);
        NerdScene toStart = this.constructScene(sceneConstructor, this.currentSceneKey);

        if (toStart == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.setScene(toStart);

        // Don't worry about concurrency, vvv *this* vvv is `final`! ^-^
        if (!this.SCENE_CLASS_TO_CACHE.containsKey(p_sceneClass)) {
            this.SCENE_CLASS_TO_CACHE.put(p_sceneClass, new SceneCache(p_sceneClass, sceneConstructor, toStart));
        }
    }

    // The scene-deleter!!!
    private void setScene(NerdScene p_currentScene) {
        // region `this.settings.onSceneSwitch` tasks.
        if (this.settings.onSceneSwitch.doClear)
            this.SKETCH.clear();

        if (this.settings.onSceneSwitch.completelyResetCam)
            this.SKETCH.currentCamera.completeReset();
        // endregion

        this.previousSceneClass = this.currentSceneClass;
        if (this.previousSceneClass != null) {
            this.currentScene.runOnSceneExit(this.currentSceneKey);

            this.SCENE_CLASS_TO_CACHE.get(this.previousSceneClass)
                    .deleteCacheIfCan();

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

        this.currentScene = p_currentScene;
        this.setupCurrentScene();
    }

    // Set the time, *then* call `SceneManager::runSetup()`.
    private void setupCurrentScene() {
        this.sceneStartMillis = this.SKETCH.millis();
        this.currentScene.runSetup(this.currentSceneKey);
    }
    // endregion
    // endregion

    // region Getters.
    public Sketch getSketch() {
        return this.SKETCH;
    }

    public NerdScene getCurrentScene() {
        return this.currentScene;
    }

    public Class<? extends NerdScene> getCurrentSceneClass() {
        return this.currentSceneClass;
    }

    public Class<? extends NerdScene> getPreviousSceneClass() {
        return this.previousSceneClass;
    }
    // endregion

}
