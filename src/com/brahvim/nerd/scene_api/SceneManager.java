package com.brahvim.nerd.scene_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import com.brahvim.nerd.Sketch;

public class SceneManager {
    // region `private` ~~/ `protected`~~ fields.
    private final HashSet<Class<? extends Scene>> SCENE_CLASSES;
    private final HashMap<Class<? extends Scene>, Constructor<? extends Scene>> SCENE_CONSTRUCTORS;
    private final SceneManager.SceneInitializer runner;
    private Scene currentScene, previousScene;

    /**
     * Keeping this just-in-case. It would otherwise be passed
     * straight to the {@linkplain SceneManager.SceneInitializer}
     * constructor instead.
     */
    private final Sketch sketch;
    // endregion

    public class SceneInitializer {
        private SceneManager manager;

        private SceneInitializer(SceneManager p_sceneManager) {
            this.manager = p_sceneManager;
        }

        public SceneManager getSceneManager() {
            return this.manager;
        }
    }

    public SceneManager(Sketch p_sketch) {
        this.sketch = p_sketch;
        this.runner = new SceneManager.SceneInitializer(this);

        this.SCENE_CONSTRUCTORS = new HashMap<>();
        this.SCENE_CLASSES = new HashSet<>();
    }

    // region `Scene`-callbacks.
    // region App workflow:
    public void setup() {
        if (this.currentScene != null)
            this.currentScene.runSetup(this.runner);
    }

    public void pre() {
        if (this.currentScene != null)
            this.currentScene.runPre(this.runner);
    }

    public void draw() {
        if (this.currentScene != null)
            this.currentScene.runDraw(this.runner);
    }

    public void post() {
        if (this.currentScene != null)
            this.currentScene.runPost(this.runner);
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
    // endregion

    // region `Scene`-operations.
    /*
     * @SuppressWarnings("unchecked")
     * public Class<? extends Scene>[] getSceneClasses() {
     * return (Class<? extends Scene>[]) this.SCENE_CLASSES.toArray();
     * }
     */

    public HashSet<Class<? extends Scene>> getSceneClasses() {
        return this.SCENE_CLASSES;
    }

    public void restartScene() {
        this.setCurrentAndPreviousScene(this.currentScene);
    }

    public void startPreviousScene() {
        this.setCurrentAndPreviousScene(this.previousScene);
    }

    public void setScene(Class<? extends Scene> p_sceneClass) {
        if (p_sceneClass == null)
            throw new NullPointerException("`SceneManager::startScene()` will not take `null`s!");

        this.SCENE_CLASSES.add(p_sceneClass);
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

    // region `private` Scene-operations.
    private void startSceneImpl(Class<? extends Scene> p_sceneClass) {
        Scene toStart = null;
        Constructor<? extends Scene> sceneConstructor = null;

        // region Getting the constructor.
        try {
            sceneConstructor = p_sceneClass.getConstructor(SceneInitializer.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        // endregion

        // region Constructing the `Scene`.
        try {
            toStart = (Scene) sceneConstructor.newInstance(this.runner);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        // endregion

        this.setCurrentAndPreviousScene(toStart);

        // Don't worry about concurrency, vvv *this* vvv is `final`! ^-^
        this.SCENE_CONSTRUCTORS.put(p_sceneClass, sceneConstructor);
    }

    private void setCurrentAndPreviousScene(Scene p_currentScene) {
        this.previousScene = this.currentScene;
        if (this.previousScene != null) {
            this.previousScene.runOnSceneExit(this.runner);
        }

        this.currentScene = p_currentScene;
        this.currentScene.runSetup(this.runner);
    }
    // endregion
    // endregion

    // region Getters.
    public Sketch getSketch() {
        return this.sketch;
    }

    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public Scene getPreviousScene() {
        return this.previousScene;
    }
    // endregion

}