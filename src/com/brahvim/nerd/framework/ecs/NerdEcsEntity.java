package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public abstract class NerdEcsEntity implements Serializable {

    public static final long serialVersionUID = -84636463676L;
    protected final NerdEcsEntity ENTITY = this;

    protected NerdEcsManager ECS;

    private final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();

    public NerdEcsEntity() {
    }

    protected void update() {
    }

    // region Dynamic component list queries.
    @SuppressWarnings("unchecked")
    public <ComponentT extends NerdEcsComponent> ComponentT getComponent(final Class<ComponentT> p_componentClass) {
        for (final NerdEcsComponent c : this.COMPONENTS)
            if (c.getClass().equals(p_componentClass))
                return (ComponentT) c;

        return null;
    }

    public <ComponentT extends NerdEcsComponent> ComponentT addComponent(final Class<ComponentT> p_componentClass) {
        ComponentT toRet = null;

        try {
            toRet = p_componentClass.getConstructor().newInstance();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        }

        if (toRet != null)
            this.COMPONENTS.add(toRet);

        return toRet;
    }

    @SuppressWarnings("unchecked")
    public <ComponentT extends NerdEcsComponent> ComponentT removeComponent(final Class<ComponentT> p_componentClass) {
        ComponentT toRet = null;

        for (final NerdEcsComponent c : this.COMPONENTS)
            if (c.getClass().equals(p_componentClass))
                toRet = (ComponentT) c;



        if (toRet != null)
            this.COMPONENTS.remove(toRet);

        return toRet;
    }
    // endregion

    // region Events.
    // region Mouse events.
    public void mousePressed() {
    }

    public void mouseReleased() {
    }

    public void mouseMoved() {
    }

    public void mouseClicked() {
    }

    public void mouseDragged() {
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(final processing.event.MouseEvent p_mouseEvent) {
    }
    // endregion

    // region Keyboard events.
    public void keyTyped() {
    }

    public void keyPressed() {
    }

    public void keyReleased() {
    }
    // endregion

    // region Touch events.
    public void touchStarted() {
    }

    public void touchMoved() {
    }

    public void touchEnded() {
    }
    // endregion

    // region Window focus events.
    public void focusLost() {
    }

    public void exit() {
    }

    public void resized() {
    }

    public void focusGained() {
    }

    public void monitorChanged() {
    }

    public void fullscreenChanged(final boolean p_state) {
    }
    // endregion
    // endregion

}
