package com.brahvim.nerd.math;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

// Dr. Andrew Marsh's `gluUnProject()` code! ":D!~
// http://andrewmarsh.com/blog/2011/12/04/gluunproject-p3d-and-opengl-sketches/
// Note: `6:14` AM, `18/December/2022`: I removed the `bValid` field entirely.

public class Unprojector {

  // region Fields.
  // These store information about the sketch's viewport:
  private int width, height;

  // We maintain a refernce to the projection matrix:
  private final PMatrix3D matrix = new PMatrix3D();

  // used for intermediate calculations.
  private final float[] in = new float[3], out = new float[4];

  // Store the near and far ray positions:
  private final PVector startPos = new PVector(), endPos = new PVector();
  // endregion

  /**
   * Call this to capture the selection matrix after
   * you have called `perspective()` or `ortho()` and applied your
   * pan, zoom and camera angles - but before you start drawing
   * or playing with the matrices any further.
   */
  public void captureViewMatrix(PGraphics3D p_graphicsBuffer) {
    // Brahvim: "Couldn't we do just this in today's modern world?:"
    this.matrix.set(p_graphicsBuffer.projmodelview);
    this.matrix.invert();
    this.width = p_graphicsBuffer.width;
    this.height = p_graphicsBuffer.height;
  }

  public boolean gluUnProject(float p_winx, float p_winy, float p_winz, PVector p_result) {
    // region Transform to NDCs (`(-1, 1)`):
    this.in[0] = (p_winx / this.width) * 2.0f - 1.0f;
    this.in[1] = (p_winy / this.height) * 2.0f - 1.0f;
    this.in[2] = PApplet.constrain(p_winz, 0, 1) * 2.0f - 1.0f;
    // endregion

    // region Calculate homogeneous coordinates.
    this.out[0] = this.matrix.m00 * this.in[0]
        + this.matrix.m01 * this.in[1]
        + this.matrix.m02 * this.in[2]
        + this.matrix.m03;

    this.out[1] = this.matrix.m10 * this.in[0]
        + this.matrix.m11 * this.in[1]
        + this.matrix.m12 * this.in[2]
        + this.matrix.m13;

    this.out[2] = this.matrix.m20 * this.in[0]
        + this.matrix.m21 * this.in[1]
        + this.matrix.m22 * this.in[2]
        + this.matrix.m23;

    this.out[3] = this.matrix.m30 * this.in[0]
        + this.matrix.m31 * this.in[1]
        + this.matrix.m32 * this.in[2]
        + this.matrix.m33;
    // endregion

    // Check for an invalid result:
    if (this.out[3] == 0.0f) {
      p_result.set(0, 0, 0);
      return false;
    }

    // region Scale to world coordinates.
    this.out[3] = 1.0f / this.out[3];
    p_result.x = this.out[0] * this.out[3];
    p_result.y = this.out[1] * this.out[3];
    p_result.z = this.out[2] * out[3];
    // endregion

    return true;
  }

  // Calculate positions on the near and far 3D frustum planes.
  public boolean calculatePickPoints(float p_x, float p_y) {
    // Have to do both in order to reset the `PVector` in case of an error.
    return this.gluUnProject(p_x, p_y, 0, this.startPos)
        && this.gluUnProject(p_x, p_y, 1, this.endPos);
  }

}
