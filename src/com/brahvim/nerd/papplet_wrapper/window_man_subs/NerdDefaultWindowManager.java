package com.brahvim.nerd.papplet_wrapper.window_man_subs;

import java.util.LinkedHashSet;

import com.brahvim.nerd.papplet_wrapper.NerdWindowManager;
import com.brahvim.nerd.papplet_wrapper.Sketch;
import com.brahvim.nerd.papplet_wrapper.Sketch.SketchWindowListener;

import processing.core.PVector;

public class NerdDefaultWindowManager extends NerdWindowManager {

    public NerdDefaultWindowManager(final Sketch p_sketch,
            final LinkedHashSet<SketchWindowListener> p_windowListeners) {
        super(p_sketch, p_windowListeners);
    }

    @Override
    protected void centerWindowImpl(final float p_displayWidth, final float p_displayHeight) {
        super.surface.setLocation(
                (int) (p_displayWidth * 0.5f - super.cx),
                (int) (p_displayHeight * 0.5f - super.cy));
    }

    // region Getters.
    @Override
    public boolean getAlwaysOnTop() {
        return false;
    }

    @Override
    public String getName() {
        return super.SKETCH.NAME;
    }

    @Override
    public Object getNativeObject() {
        return null;
    }

    @Override
    public PVector getPosition() {
        return null;
    }

    @Override
    public PVector getSize() {
        return null;
    }
    // endregion

    @Override
    protected void initImpl() {

    }

    @Override
    protected void postCallbackImpl() {

    }

    // region Setters.
    @Override
    public boolean setAlwaysOnTop(final boolean p_status) {
        super.surface.setAlwaysOnTop(p_status);
        return true;
    }

    @Override
    public NerdWindowManager setName(final String p_name) {
        super.surface.setTitle(p_name);
        return this;
    }

    @Override
    public NerdWindowManager setPosition(final PVector p_position) {
        super.surface.setLocation((int) p_position.x, (int) p_position.y);
        return this;
    }

    @Override
    public NerdWindowManager setPosition(final int p_x, final int p_y) {
        super.surface.setLocation(p_x, p_y);
        return this;
    }

    @Override
    public NerdWindowManager setSize(final PVector p_size) {
        super.surface.setSize((int) p_size.x, (int) p_size.y);
        return this;
    }

    @Override
    public NerdWindowManager setSize(final int p_x, final int p_y) {
        super.surface.setSize(p_x, p_y);
        return this;
    }
    // endregion

}
