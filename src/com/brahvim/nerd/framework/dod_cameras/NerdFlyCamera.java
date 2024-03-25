package com.brahvim.nerd.framework.dod_cameras;

import java.util.Objects;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PVector;

public class NerdFlyCamera extends NerdAbstractCamera {

    // region Fields.
    private PVector front;
    private boolean pholdMouse;

    public float yaw, zoom, pitch;
    public boolean holdMouse = true;
    public boolean shouldConstrainPitch = true;
    public float mouseSensitivity = NerdP3dGraphics.DEFAULT_CAMERA_MOUSE_SENSITIVITY;
    // endregion

    public NerdFlyCamera(final PVector p_position, final PVector p_front) {
    }

    public PVector getFront() {
        return this.front;
    }

    public PVector getCenter() {
        return this.front;
    }

    public PVector setFront(final PVector p_front) {
        final PVector toRet = this.front;
        this.front = Objects.requireNonNull(p_front);
        return toRet;
    }

    public boolean hadLockedMouseLastFrame() {
        return this.pholdMouse;
    }

}
