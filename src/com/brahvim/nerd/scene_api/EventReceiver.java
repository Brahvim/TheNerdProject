package com.brahvim.nerd.scene_api;

// `public` event callbacks. Can be called from anywhere-":D!
public interface EventReceiver {
    // region Mouse events.
    public default void mousePressed() {
    }

    public default void mouseReleased() {
    }

    public default void mouseMoved() {
    }

    public default void mouseClicked() {
    }

    public default void mouseDragged() {
    }

    // @SuppressWarnings("unused")
    public default void mouseWheel(processing.event.MouseEvent p_mouseEvent) {
    }
    // endregion

    // region Keyboard events.
    public default void keyTyped() {
    }

    public default void keyPressed() {
    }

    public default void keyReleased() {
    }
    // endregion

    // region Touch events.
    public default void touchStarted() {
    }

    public default void touchMoved() {
    }

    public default void touchEnded() {
    }
    // endregion

    // region Window focus events.
    public default void focusLost() {
    }

    public default void resized() {
    }

    public default void focusGained() {
    }
    // endregion

}
