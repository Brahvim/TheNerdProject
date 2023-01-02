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
        private Constructor<? extends Scene> constructor;
        private Scene cachedReference;
        private boolean isDeletable;

        private SceneCache(Constructor<? extends Scene> p_constructor, Scene p_cachedReference) {
            this.constructor = p_constructor;
            this.cachedReference = p_cachedReference;
        }

        private SceneCache(Constructor<? extends Scene> p_constructor,
                Scene p_cachedReference, boolean p_isDeletable) {
            this.constructor = p_constructor;
            this.isDeletable = p_isDeletable;
            this.cachedReference = p_cachedReference;
        }

        // region Getters.
        public Constructor<? extends Scene> getConstructor() {
            return this.constructor;
        }

        public boolean isDeletable() {
            return this.isDeletable;
        }

        public Scene getCache(SceneKey p_initializer) {
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
    private final HashMap<Class<? extends Scene>, SceneCache> SCENE_CACHE = new HashMap<>();
    private final SceneManager.SceneManagerSettings settings;
    private final SceneManager.SceneKey sceneKey;

    /**
     * Keeping this just-in-case. It would otherwise be passed
     * straight to the {@linkplain SceneManager.SceneKey}
     * constructor instead.
     */
    private final Sketch SKETCH;

    private Class<? extends Scene> currentSceneClass, previousSceneClass;
    private Scene currentScene;
    private int sceneStartMillis;
    // endregion

    public SceneManager(Sketch p_sketch) {
        this.SKETCH = p_sketch;
        this.settings = new SceneManagerSettings();
        this.sceneKey = new SceneManager.SceneKey(this);
    }

    public SceneManager(Sketch p_sketch, SceneManagerSettings p_settings) {
        this.SKETCH = p_sketch;
        this.settings = p_settings;
        this.sceneKey = new SceneManager.SceneKey(this);
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
    public HashSet<Class<? extends Scene>> knownScenes() {
        // Here's what's happening here:

        // HashSet<Class<? extends Scene>> toRet = (HashSet<Class<? extends Scene>>)
        // this.SCENE_CACHE.keySet();
        // return (HashSet<Class<? extends Scene>>) toRet.clone();

        // Cast the `keySet`, clone it, and cast the clone:

        return ((HashSet<Class<? extends Scene>>)

        ((HashSet<Class<? extends Scene>>) (this.SCENE_CACHE.keySet())).clone());
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
            this.currentScene.runPre(this.sceneKey);
    }

    public void draw() {
        if (this.currentScene != null)
            this.currentScene.runDraw(this.sceneKey);
    }

    public void post() {
        if (this.currentScene != null)
            this.currentScene.runPost(this.sceneKey);
    }
    // endregion

    // region Mouse events.
    public void mousePressed() {
        if (this.currentScene == null)
            return;

        this.currentScene.mousePressed();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mousePressed();
    }

    public void mouseReleased() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseReleased();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseReleased();
    }

    public void mouseMoved() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseMoved();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseMoved();
    }

    public void mouseClicked() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseClicked();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseClicked();
    }

    public void mouseDragged() {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseDragged();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.mouseDragged();
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
        if (this.currentScene == null)
            return;

        this.currentScene.mouseWheel(p_mouseEvent);
        for (Layer l : this.currentScene.getAllLayers())
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
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.keyTyped();
    }

    public void keyPressed() {
        if (this.currentScene == null)
            return;

        this.currentScene.keyPressed();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.keyPressed();
    }

    public void keyReleased() {
        if (this.currentScene == null)
            return;

        this.currentScene.keyReleased();
        for (Layer l : this.currentScene.getAllLayers())
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
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.touchStarted();
    }

    public void touchMoved() {
        if (this.currentScene == null)
            return;

        this.currentScene.touchMoved();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.touchMoved();
    }

    public void touchEnded() {
        if (this.currentScene == null)
            return;

        this.currentScene.touchEnded();
        for (Layer l : this.currentScene.getAllLayers())
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
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.focusLost();
    }

    public void focusGained() {
        if (this.currentScene == null)
            return;

        this.currentScene.focusGained();
        for (Layer l : this.currentScene.getAllLayers())
            if (l != null)
                if (l.isActive())
                    l.focusGained();
    }
    // endregion
    // endregion

    // region `Scene`-operations.
    public void cacheScene(Class<? extends Scene> p_sceneClass, boolean p_isDeletable) {
        if (this.SCENE_CACHE.containsKey(p_sceneClass))
            return;

        Constructor<? extends Scene> sceneConstructor = this.getSceneConstructor(p_sceneClass);
        if (sceneConstructor == null)
            throw new IllegalArgumentException("""
                    The passed class's constructor could not be accessed.""");

        Scene toCache = this.constructScene(sceneConstructor);
        if (toCache == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.SCENE_CACHE.put(p_sceneClass, new SceneCache(sceneConstructor, toCache, p_isDeletable));
    }

    public void restartScene() {
        if (this.currentSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CACHE.remove(this.currentSceneClass);
        Scene toUse = this.constructScene(cache.constructor);
        this.SCENE_CACHE.put(this.currentSceneClass, cache);

        this.setScene(toUse);
    }

    public void startPreviousScene() {
        if (this.previousSceneClass == null)
            return;

        SceneCache cache = this.SCENE_CACHE.remove(this.previousSceneClass);
        Scene toUse = this.constructScene(cache.constructor);
        this.SCENE_CACHE.put(this.previousSceneClass, cache);

        this.setScene(toUse);
    }

    public void startScene(Class<? extends Scene> p_sceneClass) {
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
    public boolean startFromCacheIfCan(Class<? extends Scene> p_sceneClass) {
        if (this.SCENE_CACHE.keySet().contains(p_sceneClass)) {
            this.setScene(this.SCENE_CACHE.get(p_sceneClass).getCache(this.sceneKey));
            return true;
        } else {
            System.out.println("Scene not found in cache. Starting the usual way.");
            this.startScene(p_sceneClass);
            return false;
        }
    }

    // region `private` Scene-operations.
    private Constructor<? extends Scene> getSceneConstructor(Class<? extends Scene> p_sceneClass) {
        Constructor<? extends Scene> toRet = null;

        try {
            toRet = p_sceneClass.getConstructor(SceneKey.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return toRet;
    }

    private Scene constructScene(Constructor<? extends Scene> p_sceneConstructor) {
        Scene toRet = null;

        try {
            toRet = (Scene) p_sceneConstructor.newInstance(this.sceneKey);
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
    private void startSceneImpl(Class<? extends Scene> p_sceneClass) {
        Constructor<? extends Scene> sceneConstructor = this.getSceneConstructor(p_sceneClass);

        if (sceneConstructor == null)
            throw new IllegalArgumentException("""
                    The passed class's constructor could not be accessed.""");

        Scene toStart = this.constructScene(sceneConstructor);

        if (toStart == null)
            throw new RuntimeException("The scene could not be constructed.");

        this.setScene(toStart);

        // Don't worry about concurrency, vvv *this* vvv is `final`! ^-^
        if (!this.SCENE_CACHE.containsKey(p_sceneClass)) {
            this.SCENE_CACHE.put(p_sceneClass, new SceneCache(sceneConstructor, toStart));
        }
    }

    // The scene-deleter!!!
    private void setScene(Scene p_currentScene) {
        // region `this.settings.onSceneSwitch` tasks.
        if (this.settings.onSceneSwitch.doClear)
            this.SKETCH.clear();

        if (this.settings.onSceneSwitch.completelyResetCam)
            this.SKETCH.currentCamera.completeReset();
        // endregion

        this.previousSceneClass = this.currentSceneClass;
        if (this.previousSceneClass != null) {
            this.currentScene.runOnSceneExit(this.sceneKey);

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
        this.currentScene.runSetup(this.sceneKey);
    }
    // endregion
    // endregion

    // region Getters.
    public Sketch getSketch() {
        return this.SKETCH;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public Class<? extends Scene> getCurrentSceneClass() {
        return this.currentSceneClass;
    }

    public Class<? extends Scene> getPreviousSceneClass() {
        return this.previousSceneClass;
    }
    // endregion

}
