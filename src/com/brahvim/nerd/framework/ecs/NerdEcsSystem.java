package com.brahvim.nerd.framework.ecs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

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
