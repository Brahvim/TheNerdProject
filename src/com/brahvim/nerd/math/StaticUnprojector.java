package com.brahvim.nerd.math;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

public class StaticUnprojector {
	// True if near and far points calculated. Use `.isValid()` to access!
	private static boolean bValid = false;
  
	// Maintain own projection matrix.
	private static PMatrix3D pMatrix = new PMatrix3D();
  
	private static int[] aiViewport = new int[4];
	// ^^^ `ai` stands for "Array of Integers", apparently.
  
	// Store the near and far ray positions.
	public static PVector ptStartPos = new PVector();
	public static PVector ptEndPos = new PVector();
  
	public boolean isValid() {
	  return StaticUnprojector.bValid;
	}
  
	public static PMatrix3D getMatrix() {
	  return StaticUnprojector.pMatrix;
	}
  
	// Maintain own viewport data.
	public static int[] getViewport() {
	  return StaticUnprojector.aiViewport;
	}
  
	public static void captureViewMatrix(PGraphics3D p_g3d) {
	  // Call this to capture the selection matrix after
	  // you have called `perspective()` or `ortho()` and applied your
	  // pan, zoom and camera angles - but before you start drawing
	  // or playing with the matrices any further.
  
	  // Check for a valid 3D canvas.
  
	  // Capture current projection matrix.
	  //pMatrix.set(p_g3d.projection);
  
	  // Multiply by current modelview matrix.
	  //pMatrix.apply(p_g3d.modelview);
  
	  // Invert the resultant matrix.
	  //pMatrix.invert();
  
	  // "Couldn't we do this in today's modern world?:"
	  // - Brahvim
	  //
	  StaticUnprojector.pMatrix.set(p_g3d.projmodelview);
	  StaticUnprojector.pMatrix.invert();
  
	  // Store the viewport.
	  StaticUnprojector.aiViewport[0] = 0;
	  StaticUnprojector.aiViewport[1] = 0;
	  StaticUnprojector.aiViewport[2] = p_g3d.width;
	  StaticUnprojector.aiViewport[3] = p_g3d.height;
	}
  
	public static boolean gluUnProject(float p_winx, float p_winy, float p_winz, PVector p_result) {
	  // "A `memset()` is definitely better. Put these into the class?" - Brahvim.
	  float[] in = new float[4];
	  float[] out = new float[4];
  
	  // Transform to NDCs (`-1` to `1`):
	  in[0] = ((p_winx - (float)StaticUnprojector.aiViewport[0]) / (float)StaticUnprojector.aiViewport[2]) * 2.0f - 1.0f;
	  in[1] = ((p_winy - (float)StaticUnprojector.aiViewport[1]) / (float)StaticUnprojector.aiViewport[3]) * 2.0f - 1.0f;
	  in[2] = PApplet.constrain(p_winz, 0, 1) * 2.0f - 1.0f;
	  in[3] = 1.0f;
  
	  // Calculate homogeneous coordinates:
	  out[0] = StaticUnprojector.pMatrix.m00 * in[0]
		+ StaticUnprojector.pMatrix.m01 * in[1]
		+ StaticUnprojector.pMatrix.m02 * in[2]
		+ StaticUnprojector.pMatrix.m03 * in[3];
	  out[1] = StaticUnprojector.pMatrix.m10 * in[0]
		+ StaticUnprojector.pMatrix.m11 * in[1]
		+ StaticUnprojector.pMatrix.m12 * in[2]
		+ StaticUnprojector.pMatrix.m13 * in[3];
	  out[2] = StaticUnprojector.pMatrix.m20 * in[0]
		+ StaticUnprojector.pMatrix.m21 * in[1]
		+ StaticUnprojector.pMatrix.m22 * in[2]
		+ StaticUnprojector.pMatrix.m23 * in[3];
	  out[3] = StaticUnprojector.pMatrix.m30 * in[0]
		+ StaticUnprojector.pMatrix.m31 * in[1]
		+ StaticUnprojector.pMatrix.m32 * in[2]
		+ StaticUnprojector.pMatrix.m33 * in[3];
  
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
	public static boolean calculatePickPoints(float p_x, float p_y) {
	  StaticUnprojector.bValid = true; // Have to do both in order to reset the `PVector` in case of an error.
	  // Brahvim: "Can't we optimize this?"...
  
	  StaticUnprojector.bValid = StaticUnprojector.gluUnProject(p_x, p_y, 0, StaticUnprojector.ptStartPos);
	  StaticUnprojector.bValid = StaticUnprojector.gluUnProject(p_x, p_y, 1, StaticUnprojector.ptEndPos);
	  return StaticUnprojector.bValid;
  
	  // Original version:
	  //if (!gluUnProject(p_x, p_y, 0, ptStartPos))
	  //bValid = false;
	  //if (!gluUnProject(p_x, p_y, 1, ptEndPos))
	  //bValid = false;
	  //return bValid;
	}
  }