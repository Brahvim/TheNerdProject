package com.brahvim.nerd.framework.dod_cameras;

import java.util.Objects;
import java.util.function.Consumer;

import com.brahvim.nerd.framework.graphics_backends.NerdP3dGraphics;

import processing.core.PApplet;
import processing.core.PVector;

public class NerdAbstractCamera {

    // region Fields.
    public boolean doAutoAspect;
    public float fov, far, near, aspect;

    private Consumer<NerdP3dGraphics> projection;
    private PVector position, orientation, clearColor;
    // endregion

    public NerdAbstractCamera() {
        // Initialize camera properties with default values
        this.position = new PVector();
        this.clearColor = new PVector();
        this.orientation = new PVector(0, 1, 0);
        this.fov = PApplet.radians(60);
        this.near = 0.1f;
        this.far = 10000;
        this.doAutoAspect = true;
        this.aspect = 1;
    }

    public boolean isDoAutoAspect() {
        return this.doAutoAspect;
    }

    public void setDoAutoAspect(final boolean doAutoAspect) {
        this.doAutoAspect = doAutoAspect;
    }

    public void setAspect(final float aspect) {
        this.aspect = aspect;
    }

    public PVector getPosition() {
        return this.position;
    }

    public PVector setPosition(final PVector p_position) {
        final PVector toRet = this.position;
        this.position = Objects.requireNonNull(p_position);
        return toRet;
    }

    public PVector getOrientation() {
        return this.orientation;
    }

    public PVector setOrientation(final PVector p_orientation) {
        final PVector toRet = this.orientation;
        this.orientation = Objects.requireNonNull(p_orientation);
        return toRet;
    }

    public PVector getClearColor() {
        return this.clearColor;
    }

    public PVector setClearColor(final PVector p_clearColor) {
        final PVector toRet = this.clearColor;
        this.clearColor = Objects.requireNonNull(p_clearColor);
        return toRet;
    }

    public Consumer<NerdP3dGraphics> getProjection() {
        return this.projection;
    }

    public void setProjection(final Consumer<NerdP3dGraphics> p_projectionFunction) {
        this.projection = p_projectionFunction;
    }

}