package com.brahvim.nerd.math.collision;

import processing.core.PApplet;
import processing.core.PVector;

// Brought to you, *from* my other (currently supa'-duper secret, ";P!) project, "AGC"!:
public class NerdQuad {
    // region Fields.
    /**
     * The <i>actual</i> position of the quadrilateral.
     */
    public PVector center;

    /**
     * The position of the top-left of the quadrilateral.
     */
    public PVector start;

    /**
     * The position of the bottom-right of the quadrilateral.
     */
    public PVector end;
    // endregion

    // region Constructors.
    public NerdQuad(NerdCircle p_circ) {
        this.center = p_circ.pos.copy();

        float radius = p_circ.radius;
        this.end = new PVector(this.center.x + radius, this.center.y + radius);
        this.start = new PVector(this.center.x - radius, this.center.y - radius);
    }

    public NerdQuad(PVector p_pos, float p_side) {
        this.center = p_pos.copy();
        this.end = new PVector(this.center.x + p_side, this.center.y + p_side);
        this.start = new PVector(this.center.x - p_side, this.center.y - p_side);
    }

    public NerdQuad(PVector p_start, PVector p_end) {
        this.end = p_end;
        this.start = p_start;
        this.center = PVector.add(this.start, this.end).mult(0.5f);
    }

    public NerdQuad(float p_startX, float p_startY, float p_endX, float p_endY) {
        this.end = new PVector(p_endX, p_endY);
        this.start = new PVector(p_startX, p_startY);
        this.center = PVector.add(this.start, this.end).mult(0.5f);
    }
    // endregion

    public boolean isSquare() {
        return PApplet.abs(this.end.x - this.start.x) == PApplet.abs(this.end.y - this.start.y);
    }

    /**
     * @return The (positive) length of the side of the quadrilateral, if it is a
     *         square.<br>
     *         <br>
     *         {@code -1} if the quadrilateral isn't a square!
     */
    public float getSideIfSquare() {
        float assumedSide = PApplet.abs(this.end.x - this.start.x);
        if (assumedSide == PApplet.abs(this.end.y - this.start.y)) // Same check as `NerdQuad::isSquare()`!
            return assumedSide;
        else
            return -1.0f;
    }

    public boolean contains(PVector p_point) {
        return CollisionAlgorithms.ptRect(p_point, this.start, this.end);
    }
}
