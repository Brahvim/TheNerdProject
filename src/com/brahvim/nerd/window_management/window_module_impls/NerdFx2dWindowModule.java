package com.brahvim.nerd.window_management.window_module_impls;

import java.awt.Point;

import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.window_management.NerdWindowModule;

import processing.core.PVector;
import processing.javafx.PGraphicsFX2D;
import processing.javafx.PSurfaceFX;

public class NerdFx2dWindowModule extends NerdWindowModule<PGraphicsFX2D> {

    protected PSurfaceFX surface;

    public NerdFx2dWindowModule(final NerdSketch<PGraphicsFX2D> p_sketch) {
        super(p_sketch);
    }

    @Override
    protected void preSetupImpl() {
        this.surface = (PSurfaceFX) super.SKETCH.getSurface();
        this.surface.setIcon(super.iconImage);
    }

    @Override
    protected void centerWindowImpl() {
        final Point dimensions = super.displays.getCurrentMonitorDimensions();
        super.sketchSurface.setLocation(
                (int) (dimensions.x * 0.5f - super.width),
                (int) (dimensions.y * 0.5f - super.q3y));
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getX() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getY() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point getSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PVector getSizeAsPVector() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Point getPosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PVector getPositionAsPVector() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getNativeObject() {
        return this.surface.getNative();
    }

    @Override
    public boolean isResizable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAlwaysOnTop() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean setAlwaysOnTop(final boolean p_state) {
        this.surface.setAlwaysOnTop(p_state);
        return true;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setName(final String p_name) {
        this.surface.setTitle(p_name);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setSize(final PVector p_size) {
        this.surface.setSize((int) p_size.x, (int) p_size.y);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setResizable(final boolean p_state) {
        this.surface.setResizable(p_state);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setSize(final int p_x, final int p_y) {
        this.surface.setSize(p_x, p_y);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setSize(final float p_x, final float p_y) {
        this.surface.setSize((int) p_x, (int) p_y);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setPosition(final PVector p_position) {
        this.surface.setLocation((int) p_position.x, (int) p_position.y);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setPosition(final int p_x, final int p_y) {
        this.surface.setLocation(p_x, p_y);
        return this;
    }

    @Override
    public NerdWindowModule<PGraphicsFX2D> setPosition(final float p_x, final float p_y) {
        this.surface.setLocation((int) p_x, (int) p_y);
        return this;
    }

    @Override
    protected void postImpl() {
        // ¯\_(ツ)_/¯
    }

}
