package com.brahvim.nerd.math.collision;

import processing.core.PApplet;
import processing.core.PVector;

// NOOOO!!1! NOT `NerdEllipse`!
// PLEASE make a "ShapeCreator" class if you ever decide to have
// separate classes for quads and squares, or ellipses and circles.
// Perhaps use some shape interface? (BUT THE METHODS!!!)
// Whatever can be used to solve the problem here, remember the problem itself:
// THE SHAPE OBJECT CANNOT BE CHANGED!

public class NerdCircle {
    // region Fields.
    public PVector pos;
    public float radius;
    // endregion

    // region Constructors.
    public NerdCircle(final PVector p_center, final float p_radius) {
        this.radius = p_radius;
        this.pos = p_center.copy();
    }

    public NerdCircle(final float p_x, final float p_y, final float p_radius) {
        this.radius = p_radius;
        this.pos = new PVector(p_x, p_y);
    }

    public NerdCircle(final PVector p_topLeft, final PVector p_botRight) {
        // Yes, yes, this is the same checking as `NerdQuad::isSquare()`!:
        final float sideX = PApplet.abs(p_botRight.x - p_topLeft.x);
        final float sideY = PApplet.abs(p_botRight.y - p_topLeft.y);

        if (sideX != sideY)
            throw new IllegalArgumentException(
                    "Cannot construct a `NerdCircle` from a rectangular `NerdQuad`!");

        this.pos = new PVector(p_topLeft.x + sideX, p_topLeft.y + sideY);
    }

    public NerdCircle(final NerdQuad p_rect) {
        this(p_rect.start, p_rect.end);
    }

    public NerdCircle(final NerdCircle p_circle) {
        this.radius = p_circle.radius;
        this.pos = p_circle.pos.copy();
    }
    // endregion

    public float getDiameter() {
        // Using the same type is an optimization, I guess?
        // (The JIT could've do it ._.)
        return this.radius * 2.0f;
    }

    public NerdQuad getBoundingBox() {
        return new NerdQuad(this);
    }

    public boolean contains(final PVector p_point) {
        return CollisionAlgorithms.ptCircle(p_point, this.pos, this.radius);
    }
}
