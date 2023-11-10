package com.brahvim.nerd.math;

import processing.core.PVector;

/**
 * A class that extends {@link PVector} and performs {@code null}-checks
 * wherever known. Graceful degradation by returning a newly-allocated empty
 * {@link PVector}, or returning {@code 0} is opted for when the check reports
 * as positive.
 *
 * @implNote If any argument called {@code target} exists, it is already
 *           checked! No matter it is a {@code float[]} or another
 *           {@link PVector}.
 */
public class NerdVector extends PVector {

    // region The non-static stuff.
    @Override
    public PVector add(final PVector v) {
        if (v == null)
            return new PVector();
        return super.add(v);
    }

    @Override
    public PVector add(final float x, final float y) {
        return super.add(x, y);
    }

    @Override
    public PVector add(final float x, final float y, final float z) {
        return super.add(x, y, z);
    }

    @Override
    public float[] array() {
        return super.array();
    }

    @Override
    public PVector copy() {
        return super.copy();
    }

    @Override
    public PVector cross(final PVector v) {
        if (v == null)
            return new PVector();
        return super.cross(v);
    }

    @Override
    public PVector cross(final PVector v, final PVector target) {
        if (v == null)
            return new PVector();

        // The original source code performs this check too! Yay...:
        // https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/core/PVector.java#L762
        // if (target == null)
        // return new PVector();

        return super.cross(v, target);
    }

    @Override
    public float dist(final PVector v) {
        if (v == null)
            return 0;
        return super.dist(v);
    }

    @Override
    public PVector div(final float n) {
        return super.div(n);
    }

    @Override
    public float dot(final PVector v) {
        if (v == null)
            return 0;
        return super.dot(v);
    }

    @Override
    public float dot(final float x, final float y, final float z) {
        return super.dot(x, y, z);
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    @Override
    @Deprecated
    public PVector get() {
        return super.get();
    }

    // The original source code checks for `null` values in this method! Yay...:
    // https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/core/PVector.java#L384
    // @Override
    // public float[] get(final float[] target) {
    // if (target == null)
    // return new float[0];
    // return super.get(target);
    // }

    @Override
    public float heading() {
        return super.heading();
    }

    @Override
    @Deprecated
    public float heading2D() {
        return super.heading2D();
    }

    @Override
    public PVector lerp(final PVector v, float amt) {
        if (v == null)
            return new PVector();
        return super.lerp(v, amt);
    }

    @Override
    public PVector lerp(final float x, final float y, final float z, float amt) {
        return super.lerp(x, y, z, amt);
    }

    @Override
    public PVector limit(final float max) {
        return super.limit(max);
    }

    @Override
    public float mag() {
        return super.mag();
    }

    @Override
    public float magSq() {
        return super.magSq();
    }

    @Override
    public PVector mult(final float n) {
        return super.mult(n);
    }

    @Override
    public PVector normalize() {
        return super.normalize();
    }

    // Yo! The this method, too, checks if the argument is `null`!:
    // https://github.com/processing/processing/blob/459853d0dcdf1e1648b1049d3fdbb4bf233fded8/core/src/processing/core/PVector.java#L814

    // @Override
    // public PVector normalize(final PVector target) {
    // if (target == null)
    // return new PVector();
    // return super.normalize(target);
    // }

    @Override
    public PVector rotate(final float theta) {
        return super.rotate(theta);
    }

    @Override
    public PVector set(final PVector v) {
        if (v == null)
            return new PVector();
        return super.set(v);
    }

    @Override
    public PVector set(final float[] source) {
        return super.set(source);
    }

    @Override
    public PVector set(final float x, final float y) {
        return super.set(x, y);
    }

    @Override
    public PVector set(final float x, final float y, final float z) {
        return super.set(x, y, z);
    }

    @Override
    public PVector setMag(final float len) {
        return super.setMag(len);
    }

    @Override
    public PVector setMag(final PVector target, final float len) {
        return super.setMag(target, len);
    }

    @Override
    public PVector sub(final PVector v) {
        if (v == null)
            return new PVector();
        return super.sub(v);
    }

    @Override
    public PVector sub(final float x, final float y) {
        return super.sub(x, y);
    }

    @Override
    public PVector sub(final float x, final float y, final float z) {
        return super.sub(x, y, z);
    }
    // endregion

    // region Static methods.
    // region Methods that may never return `null`.
    // public static processing.core.PVector random2D() { }

    // public static processing.core.PVector random2D(processing.core.PApplet
    // parent) { }

    // public static processing.core.PVector random2D(processing.core.PVector
    // target) { }

    // public static processing.core.PVector random2D(processing.core.PVector
    // target, processing.core.PApplet parent) { }

    // public static processing.core.PVector random3D() { }

    // public static processing.core.PVector random3D(processing.core.PApplet
    // parent) { }

    // public static processing.core.PVector random3D(processing.core.PVector
    // target) { }

    // public static processing.core.PVector random3D(processing.core.PVector
    // target, processing.core.PApplet parent) { }

    // public static processing.core.PVector fromAngle(float angle) { }

    // public static processing.core.PVector fromAngle(float angle,
    // processing.core.PVector target) { }
    // endregion

    public static processing.core.PVector add(processing.core.PVector v1, processing.core.PVector v2) {
        return v1 == null || v2 == null ? new PVector() : PVector.add(v1, v2);
    }

    public static processing.core.PVector add(processing.core.PVector v1, processing.core.PVector v2,
            processing.core.PVector target) {
        return v1 == null || v2 == null ? new PVector() : PVector.add(v1, v2, target);
    }

    public static processing.core.PVector sub(processing.core.PVector v1, processing.core.PVector v2) {
        return v1 == null || v2 == null ? new PVector() : PVector.sub(v1, v2);
    }

    public static processing.core.PVector sub(processing.core.PVector v1, processing.core.PVector v2,
            processing.core.PVector target) {
        return v1 == null || v2 == null ? new PVector() : PVector.sub(v1, v2, target);
    }

    public static processing.core.PVector mult(processing.core.PVector v, float n) {
        return v == null ? new PVector() : PVector.mult(v, n);
    }

    public static processing.core.PVector mult(processing.core.PVector v, float n, processing.core.PVector target) {
        return v == null ? new PVector() : PVector.mult(v, n, target);
    }

    public static processing.core.PVector div(processing.core.PVector v, float n) {
        return v == null ? new PVector() : PVector.div(v, n);
    }

    public static processing.core.PVector div(processing.core.PVector v, float n, processing.core.PVector target) {
        return v == null ? new PVector() : PVector.div(v, n, target);
    }

    public static float dist(processing.core.PVector v1, processing.core.PVector v2) {
        return v1 == null || v2 == null ? 0 : PVector.dist(v1, v2);
    }

    public static float dot(processing.core.PVector v1, processing.core.PVector v2) {
        return v1 == null || v2 == null ? 0 : PVector.dot(v1, v2);
    }

    public static processing.core.PVector cross(
            processing.core.PVector v1,
            processing.core.PVector v2, processing.core.PVector target) {
        return v1 == null || v2 == null ? new PVector() : PVector.cross(v1, v2, target);
    }

    public static processing.core.PVector lerp(processing.core.PVector v1, processing.core.PVector v2, float amt) {
        return v1 == null || v2 == null ? new PVector() : PVector.lerp(v1, v2, amt);
    }

    public static float angleBetween(processing.core.PVector v1, processing.core.PVector v2) {
        return v1 == null || v2 == null ? 0 : PVector.angleBetween(v1, v2);
    }
    // endregion

}
