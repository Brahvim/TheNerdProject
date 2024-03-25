package com.brahvim.nerd.framework.dod_cameras;

import java.util.Objects;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PVector;

public class NerdAbstractCamera {

    // region Fields.
    public boolean doAutoAspect = true;
    public float
    /*   */ aspect /* `= 1`? */,
            fov = NerdP3dGraphics.DEFAULT_CAMERA_FOV,
            far = NerdP3dGraphics.DEFAULT_CAMERA_FAR,
            near = NerdP3dGraphics.DEFAULT_CAMERA_NEAR,
            mouseZ = NerdP3dGraphics.DEFAULT_CAMERA_MOUSE_Z;

    private PVector position, orientation;
    private Consumer<NerdP3dGraphics> projectionFunction;
    // endregion

    public NerdAbstractCamera() {
        this.position = NerdP3dGraphics.setAsCameraDefaultPosition();
        this.orientation = NerdP3dGraphics.setAsCameraDefaultOrientation();
    }

    public NerdAbstractCamera(final PVector p_position) {
        this.position = p_position;
        this.orientation = NerdP3dGraphics.setAsCameraDefaultOrientation();
    }

    public NerdAbstractCamera(final PVector p_position, final PVector p_orientation) {
        this.position = p_position;
        this.orientation = p_orientation;
    }

    // region Getters.
    public PVector getPosition() {
        return this.position;
    }

    public PVector getOrientation() {
        return this.orientation;
    }

    public Consumer<NerdP3dGraphics> getProjectionFunction() {
        return this.projectionFunction;
    }
    // endregion

    // region Setters.
    public NerdAbstractCamera setFov(final float p_fov) {
        this.fov = p_fov;
        return this;
    }

    public NerdAbstractCamera setAspect(final float p_aspect) {
        this.aspect = p_aspect;
        return this;
    }

    public NerdAbstractCamera setFar(final float p_far) {
        this.far = p_far;
        return this;
    }

    public NerdAbstractCamera setNear(final float p_near) {
        this.near = p_near;
        return this;
    }

    public NerdAbstractCamera setMouseZ(final float p_mouseZ) {
        this.mouseZ = p_mouseZ;
        return this;
    }

    public PVector setPosition(final PVector p_position) {
        final PVector toRet = this.position;
        this.position = Objects.requireNonNull(p_position);
        return toRet;
    }

    public PVector setOrientation(final PVector p_orientation) {
        final PVector toRet = this.orientation;
        this.orientation = Objects.requireNonNull(p_orientation);
        return toRet;
    }

    public Consumer<NerdP3dGraphics> setProjectionFunction(final Consumer<NerdP3dGraphics> p_projectionFunction) {
        this.projectionFunction = p_projectionFunction;
        return this.projectionFunction;
    }
    // endregion

}
