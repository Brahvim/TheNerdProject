package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.processing_wrapper.Sketch;

public class SceneManager {
    // region Inner classes.
    public class SceneKey {
        private SceneManager manager;
        // private Class<? extends Scene> sceneClass;

        private SceneKey(SceneManager p_sceneManager) {
            this.manager = p_sceneManager;
        }

        public SceneManager getSceneManager() {
            return this.manager;
        }

        /*
         * public Class<? extends Scene> getSceneClass() {
         * return this.sceneClass;
         * }
         */

    }

    public class SceneCache {
        private Constructor<? extends NerdScene> constructor;
        private NerdScene cachedReference;
        private boolean isDeletable;

        private SceneCache(Constructor<? extends NerdScene> p_constructor, NerdScene p_cachedReference) {
            this.constructor = p_constructor;
            this.cachedReference = p_cachedReference;
        }

        private SceneCache(Constructor<? extends NerdScene> p_constructor,
                NerdScene p_cachedReference, boolean p_isDeletable) {
            this.constructor = p_constructor;
            this.isDeletable = p_isDeletable;
            this.cachedReference = p_cachedReference;
        }

        // region Getters.
        public Constructor<? extends NerdScene> getConstructor() {
            return this.constructor;
        }

        public boolean isDeletable() {
            return this.isDeletable;
        }

        public NerdScene getCache(SceneKey p_initializer) {
            if (p_initializer == null)
                throw new IllegalArgumentException("Only `SceneManager`s may use this method.");
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

    // region `private` ~~/ `protected`~~ fields.
    private final HashMap<Class<? extends NerdScene>, SceneCache> SCENE_CACHE = new HashMap<>();
    private final SceneManager.SceneKey SCENE_KEY;

    /**
     * Keeping this just-in-case. It would otherwise be passed
     * straight to the {@linkplain SceneManager.SceneKey}
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
    private SceneManager.SceneManagerSettings settings;
    private int sceneStartMillis;
    private NerdScene currentScene;
    // endregion

    public SceneManager(Sketch p_sketch) {
        this.SKETCH = p_sketch;
        this.settings = new SceneManagerSettings();
        this.SCENE_KEY = new SceneManager.SceneKey(this);

        final SceneManager SCENE_MAN = this;

        // region Sketch Event Listeners initialization.
        this.MOUSE_LISTENER = this.SKETCH.new SketchMouseListener() {
            @Override
            public void mouseClicked() {
                SCENE_MAN.mouseClicked();
            }

            @Override
            public void mouseDragged() {
                SCENE_MAN.mouseDragged();
            }

            @Override
            public void mouseReleased() {
                SCENE_MAN.mouseReleased();
            }
        };

        this.TOUCH_LISTENER = this.SKETCH.new SketchTouchListener() {
            @Override
            public void touchStarted() {
                SCENE_MAN.touchStarted();
            }

            @Override
            public void touchMoved() {
                SCENE_MAN.touchMoved();
            }

            @Override
            public void touchEnded() {
                SCENE_MAN.touchEnded();
            }
        };

        this.WINDOW_LISTENER = this.SKETCH.new SketchWindowListener() {
            @Override
            public void focusGained() {
                SCENE_MAN.focusGained();
            }

            @Override
            public void focusLost() {
                SCENE_MAN.focusLost();
            }

            @Override
            public void resized() {
                SCENE_MAN.resized();
            }
        };

        this.KEYBOARD_LISTENER = this.SKETCH.new SketchKeyboardListener() {
            @Override
            public void keyPressed() {
                SCENE_MAN.keyPressed();
            }

            @Override
            public void keyTyped() {
                SCENE_MAN.keyTyped();
            }

            @Override
            public void keyReleased() {
                SCENE_MAN.keyReleased();
            }
        };
        // endregion

    }

    public SceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        this(p_sketch);
        this.settings = p_settings;
    }

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

        ((HashSet<Class<? extends NerdScene>>) (this.SCENE_CACHE.keySet())).clone());
    }

    public int getSceneStartMillis() {
        return this.sceneStartMillis;
    }

    public int sinceSceneStarted() {
        return this.SKETCH.millis() - this.sceneStartMillis;
    }
    // endregion

    // region `Scene`-callbacks.
    // region App workflow:
    public void setup() {
        if (this.currentScene != null)
            this.setupCurrentScene();
    }

    public void pre() {
        if (this.currentScene != null)
            this.currentScene.runPre(this.SCENE_KEY);
    }

    public void draw() {
        if (this.currentScene != null)
            this.currentScene.runDraw(this.SCENE_KEY);
    }

    public void post() {
        if (this.currentScene != null)
            this.currentScene.runPost(this.SCENE_KEY);
    }
    // endregion

    // region Mouse events.
    public void mousePressed() {
        if (this.currentScene == null)
            return;

        this.currentScene.mousePressed();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mousePressed();
    }

    public void mouseReleased() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseReleased();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseReleased();
    }

    public void mouseMoved() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseMoved();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseMoved();
    }

    public void mouseClicked() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseClicked();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseClicked();
    }

    public void mouseDragged() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseDragged();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseDragged();
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseWheel(p_mouseEvent);
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseWheel(p_mouseEvent);
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
        if (this.currentScene == null)
            return;

        this.currentScene.keyTyped();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.keyTyped();
    }

    public void keyPressed() {
        if (this.currentScene == null)
            return;

        this.currentScene.keyPressed();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.keyPressed();
    }

    public void keyReleased() {
        if (this.currentScene == null)
            return;

        this.currentScene.keyReleased();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.keyReleased();
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
        if (this.currentScene == null)
            return;

        this.currentScene.touchStarted();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.touchStarted();
    }

    public void touchMoved() {
        if (this.currentScene == null)
            return;

        this.currentScene.touchMoved();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.touchMoved();
    }

    public void touchEnded() {
        if (this.currentScene == null)
            return;

        this.currentScene.touchEnded();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.touchEnded();
    }
    // endregion

    // region Window focus events.
    public void focusLost() {
        if (this.currentScene == null)
            return;

        this.currentScene.focusLost();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.focusLost();
    }

    public void resized() {
        if (this.currentScene == null)
            return;

        this.currentScene.resized();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.resized();
    }

    public void focusGained() {
        if (this.currentScene == null)
            return;

        this.currentScene.focusGained();
        for (NerdLayer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.focusGained();
    }
    // endregion
    // endregion

    // region `Scene`-operations.
    public void cacheScene(Class<? extends NerdScene> p_sceneClass, boolean p_isDeletable) {
        if (this.SCENE_CACHE.containsKey(p_sceneClass))
            return;

        Constructor<? extends NerdScene> sceneConstructor = this.getSceneConstructor(p_sceneClass);
        if (sceneConstructor == null)
            throw new IllegalArgumentException("""
                    The passed class's constructor could not be accessed.""");

        NerdScene toCache = this.constructScene(sceneConstructor);
        if (toCache == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.SCENE_CACHE.put(p_sceneClass, new SceneCache(sceneConstructor, toCache, p_isDeletable));
    }

    public void restartScene() {
        if (this.currentSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CACHE.remove(this.currentSceneClass);
        NerdScene toUse = this.constructScene(cache.constructor);
        this.SCENE_CACHE.put(this.currentSceneClass, cache);

        this.setScene(toUse);
    }

    public void startPreviousScene() {
        if (this.previousSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CACHE.remove(this.previousSceneClass);
        NerdScene toUse = this.constructScene(cache.constructor);
        this.SCENE_CACHE.put(this.previousSceneClass, cache);

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

    // "Cache if not cached" method. Experiences these problems:
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
    /*
     * // Ensures scene is cached, then starts it.
     * public void startCached(Class<? extends Scene> p_sceneClass) {
     * SceneCache cache = this.SCENE_CACHE.get(p_sceneClass);
     * 
     * if (cache == null || cache.getCache() == null) {
     * System.out.println("""
     * Scene not found in cache. Starting the usual way.
     * \n\tPutting this scene in the cache.""");
     * this.startScene(p_sceneClass);
     * 
     * /*
     * Constructor<? extends Scene> sceneConstructor =
     * this.getSceneConstructor(p_sceneClass);
     * if (sceneConstructor == null)
     * throw new IllegalArgumentException("""
     * The passed class's constructor could not be accessed.""");
     * 
     * Scene toCache = this.constructScene(sceneConstructor);
     * if (toCache == null)
     * throw new RuntimeException("The scene could not be constructed.");
     * 
     * // Will have to use the parameters here...
     * this.SCENE_CACHE.put(p_sceneClass, new SceneCache(sceneConstructor,
     * toCache));
     * // ...which is a bad idea, since it's not confirmed whether this code will
     * run.
     * 
     * <asterisk>\
     * }
     * 
     * }
     */

    // If it ain't cached, start it normally. Tell me if it is cached.
    public boolean startFromCacheIfCan(Class<? extends NerdScene> p_sceneClass) {
        if (this.SCENE_CACHE.keySet().contains(p_sceneClass)) {
            this.setScene(this.SCENE_CACHE.get(p_sceneClass).getCache(this.SCENE_KEY));
            return true;
        } else {
            System.out.println("Scene not found in cache. Starting the usual way.");
            this.startScene(p_sceneClass);
            return false;
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

    private NerdScene constructScene(Constructor<? extends NerdScene> p_sceneConstructor) {
        NerdScene toRet = null;

        try {
            toRet = (NerdScene) p_sceneConstructor.newInstance(this.SCENE_KEY);
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

        NerdScene toStart = this.constructScene(sceneConstructor);

        if (toStart == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.setScene(toStart);

        // Don't worry about concurrency, vvv *this* vvv is `final`! ^-^
        if (!this.SCENE_CACHE.containsKey(p_sceneClass)) {
            this.SCENE_CACHE.put(p_sceneClass, new SceneCache(sceneConstructor, toStart));
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
            this.currentScene.runOnSceneExit(this.SCENE_KEY);

            this.SCENE_CACHE.get(this.previousSceneClass).deleteCacheIfCan();

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
        this.currentScene.runSetup(this.SCENE_KEY);
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
