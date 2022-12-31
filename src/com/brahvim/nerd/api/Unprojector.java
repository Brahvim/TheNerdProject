package com.brahvim.nerd.api;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

// Dr. Andrew Marsh's `gluUnProject()` code! ":D!~
// [http://andrewmarsh.com/blog/2011/12/04/gluunproject-p3d-and-opengl-sketches/]
// Note: `6:14` AM, `18/December/2022`: I removed the `bValid` field entirely.

//public class Selection_in_P3D_OPENGL_A3D {
public class Unprojector {
        // region Fields.
        // Maintain own projection matrix.
        private PMatrix3D pMatrix = new PMatrix3D();

        private int[] aiViewport = new int[4];
        // ^^^ `ai` stands for "Array of Integers", apparently.

        // Store the near and far ray positions.
        public PVector ptStartPos = new PVector();
        public PVector ptEndPos = new PVector();
        // endregion

        public PMatrix3D getMatrix() {
                return this.pMatrix;
        }

        // Maintain own viewport data.
        public int[] getViewport() {
                return this.aiViewport;
        }

        public void captureViewMatrix(PGraphics3D p_g3d) {
                // Call this to capture the selection matrix after
                // you have called `perspective()` or `ortho()` and applied your
                // pan, zoom and camera angles - but before you start drawing
                // or playing with the matrices any further.

                // Check for a valid 3D canvas.

                // Capture current projection matrix.
                // this.pMatrix.set(p_g3d.projection);

                // Multiply by current modelview matrix.
                // this.pMatrix.apply(p_g3d.modelview);

                // Invert the resultant matrix.
                // this.pMatrix.invert();

                // Brahvim: "Couldn't we do this in today's modern world?:"
                this.pMatrix.set(p_g3d.projmodelview);
                this.pMatrix.invert();

                // Store the viewport.
                this.aiViewport[0] = 0;
                this.aiViewport[1] = 0;
                this.aiViewport[2] = p_g3d.width;
                this.aiViewport[3] = p_g3d.height;
        }

        /**
         * Yes, the {@linkplain PVector} giving the first three `float` values may be
         * the same you feed
         * in for {@code p_result}. You're giving me just values, bruh! It won't cause
         * issues!
         * Ti's NOT pointers or references!
         */
        public boolean gluUnProject(float p_winx, float p_winy, float p_winz, PVector p_result) {
                // "A `memset()` is definitely better. Put these into the class?" - Brahvim.
                // Note: One day, I actually benchmarked it - allocating a new array is TWO
                // orders of magnitude faster!
                float[] in = new float[4];
                float[] out = new float[4];

                // Transform to NDCs (`(-1, 1)`):
                in[0] = ((p_winx - (float) this.aiViewport[0]) / (float) this.aiViewport[2])
                                * 2.0f - 1.0f;
                in[1] = ((p_winy - (float) this.aiViewport[1]) / (float) this.aiViewport[3])
                                * 2.0f - 1.0f;
                in[2] = PApplet.constrain(p_winz, 0, 1) * 2.0f - 1.0f;
                in[3] = 1.0f;

                // Calculate homogeneous coordinates:
                out[0] = this.pMatrix.m00 * in[0]
                                + this.pMatrix.m01 * in[1]
                                + this.pMatrix.m02 * in[2]
                                + this.pMatrix.m03 * in[3];
                out[1] = this.pMatrix.m10 * in[0]
                                + this.pMatrix.m11 * in[1]
                                + this.pMatrix.m12 * in[2]
                                + this.pMatrix.m13 * in[3];
                out[2] = this.pMatrix.m20 * in[0]
                                + this.pMatrix.m21 * in[1]
                                + this.pMatrix.m22 * in[2]
                                + this.pMatrix.m23 * in[3];
                out[3] = this.pMatrix.m30 * in[0]
                                + this.pMatrix.m31 * in[1]
                                + this.pMatrix.m32 * in[2]
                                + this.pMatrix.m33 * in[3];

                // Check for an invalid result:
                if (out[3] == 0.0f) {
                        p_result.x = 0.0f;
                        p_result.y = 0.0f;
                        p_result.z = 0.0f;
                        return false;
                }

                // Scale to world coordinates:
                out[3] = 1.0f / out[3];
                p_result.x = out[0] * out[3];
                p_result.y = out[1] * out[3];
                p_result.z = out[2] * out[3];
                return true;
        }

        // Calculate positions on the near and far 3D frustum planes.
        public boolean calculatePickPoints(float p_x, float p_y) {
                // Have to do both in order to reset the `PVector` in case of an error.
                // this.bValid = true;

                return this.gluUnProject(p_x, p_y, 1, this.ptEndPos)
                                && this.gluUnProject(p_x, p_y, 0, this.ptStartPos);

                // This was "optimized" code to return stuff:
                /*
                 * return //(this.bValid =
                 * this.gluUnProject(p_x, p_y, 0, this.ptStartPos) &&
                 * this.gluUnProject(p_x, p_y, 1, this.ptEndPos);//);
                 */

                // ...original versions of it:
                /*
                 * // ~~Brahvim: "Can't we optimize this?"...~~
                 * // I DARE NOT SAY THAT LOL.
                 * //this.bValid = gluUnProject(p_x, p_y, 0, this.ptStartPos);
                 * //this.bValid = gluUnProject(p_x, p_y, 1, this.ptEndPos);
                 * //return this.bValid;
                 * // ...I guess I actually did it!
                 * 
                 * // Original version:
                 * //if (!gluUnProject(p_x, p_y, 0, this.ptStartPos))
                 * //this.bValid = false;
                 * //if (!gluUnProject(p_x, p_y, 1, this.ptEndPos))
                 * //this.bValid = false;
                 * //return this.bValid;
                 */
        }

}
