package com.brahvim.nerd.scene_api;

// `public` event callbacks. Can be called from anywhere - ":D!
// (But this interface remains at `package` level!)
/*
 * (Nobody will NEED this, that's why.)
 * (Thus-it-must, remain here.)
 * (...right?!)
 * (Hello?!)
 */

// And yes, NAMING. *I tried* (literally, the following!:).
/*
 * Original name: `EventReceiver`!
 * "UnregisteredAllSketchEventsListenerv",
 * "AnonSketchEventsListener",
 * "UnregisteredSketchEventsListener",
 * "ImplementsSketchEvents",
 * "ProbablyHasSketchEvents",
 * "ProllyHasSketchEvents",
 * "EventListener",
 * "SketchEventListener",
 * "NerdEvtListener",
 * "NerdEventListener",
 * "HasSketchEventsForSomeReason",
 * "HasSketchEvents".
 *
 * "HandlesInputEvents"     - `29` January, `2023`.
 * ..."InputEventHandling"  - `29` January, `2023`!
 *
 * See? I really DID try!
 */

// ...remember - not `public`. `package`!
/* `sealed` */ interface HardwareEventsHandler /* permits Scene, Layer */ { // "Ceiling bab iDiya?", "Yaaaa!"!

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

    public default void exit() {
    }

    public default void resized() {
    }

    public default void focusGained() {
    }
    // endregion

}
