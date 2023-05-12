package com.brahvim.nerd.framework.ecs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;

public abstract class NerdEcsSystem<SystemComponentsT extends NerdEcsComponent> {

    private final Class<SystemComponentsT> COMPONENT_TYPE_CLASS;

    @SuppressWarnings("unchecked")
    protected NerdEcsSystem() {
        // ...Trivial reflection tricks are where ChatGPT is my best friend ._.
        final Type myGenericSuperclass = this.getClass().getGenericSuperclass();

        if (!(myGenericSuperclass instanceof ParameterizedType)) {
            throw new RuntimeException(String.format(
                    "Sorry, but not specifying the generic type argument in your"
                            + "`NerdEcsSystem` subclass, `%s`, is illegal.",
                    this.getClass().getSimpleName()));
        }

        this.COMPONENT_TYPE_CLASS = (Class<SystemComponentsT>) ((ParameterizedType) myGenericSuperclass)
                .getActualTypeArguments()[0];
    }

    protected void update(final Collection<SystemComponentsT> p_components) {
    }

    public Class<SystemComponentsT> getComponentTypeClass() {
        return this.COMPONENT_TYPE_CLASS;
    }

    // region Events.
    // region Mouse events.
    public void mousePressed(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void mouseReleased(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void mouseMoved(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void mouseClicked(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void mouseDragged(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    // @SuppressWarnings("unused")
    public void mouseWheel(final processing.event.MouseEvent p_mouseEvent,
            /* final */ HashSet<SystemComponentsT> p_components) {
    }
    // endregion

    // region Keyboard events.
    public void keyTyped(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void keyPressed(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void keyReleased(/* final */ HashSet<SystemComponentsT> p_components) {
    }
    // endregion

    // region Touch events.
    public void touchStarted(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void touchMoved(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void touchEnded(/* final */ HashSet<SystemComponentsT> p_components) {
    }
    // endregion

    // region Window focus events.
    public void focusLost(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void exit(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void resized(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void focusGained(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void monitorChanged(/* final */ HashSet<SystemComponentsT> p_components) {
    }

    public void fullscreenChanged(final boolean p_state, /* final */ HashSet<SystemComponentsT> p_components) {
    }
    // endregion
    // endregion

}
