package com.brahvim.nerd.math;

import com.jogamp.opengl.GL2;

import processing.core.PVector;
import processing.opengl.PJOGL;

//public class Selection_in_OPENGL_A3D_Only
public class MouseRay {
	// Need to know Left/Right elevations.
	public static final int VIEW_PERSP = 0;
	public static final int VIEW_AXON = 1;
	public static final int VIEW_PLAN = 2;
	public static final int VIEW_FRONT = 3;
	public static final int VIEW_RIGHT = 4;
	public static final int VIEW_REAR = 5;
	public static final int VIEW_LEFT = 6;

	// Store the near and far ray positions.
	public PVector ptStartPos = new PVector();
	public PVector ptEndPos = new PVector();

	// Internal matrices and projection type.
	protected int m_iProjection = MouseRay.VIEW_PERSP;
	protected double[] m_adModelview = new double[16];
	protected double[] m_adProjection = new double[16];
	protected int[] m_aiViewport = new int[4];

	// -------------------------

	public void captureViewMatrix(GL2 pgl, int projection) {
		// Call this to capture the selection matrix after you have called perspective()
		// or ortho() and applied your pan, zoom and camera angles - but before you
		// start drawing or playing with the matrices any further.

		if (pgl != null) {
			m_iProjection = projection;
			pgl.glGetDoublev(pgl.GL_MODELVIEW_MATRIX, this.m_adModelview, 0);
			pgl.glGetDoublev(pgl.GL_PROJECTION_MATRIX, this.m_adProjection, 0);
			pgl.glGetIntegerv(pgl.GL_VIEWPORT, this.m_aiViewport, 0);
		}

	}

	// -------------------------

	public boolean calculatePickPoints(int x, int y, PJOGL pgl) { // Calculate positions on the near and far
																	// 3D frustum planes.

		if (pgl != null) {

			double[] out = new double[4];

			pgl.glu.gluUnProject((double) x, (double) y, (double) 0.0,
					this.m_adModelview, 0,
					this.m_adProjection, 0,
					this.m_aiViewport, 0,
					out, 0);

			ptStartPos.set((float) out[0], (float) out[1], (float) out[2]);
			if ((m_iProjection != MouseRay.VIEW_FRONT)
					&& (m_iProjection != MouseRay.VIEW_REAR))
				ptStartPos.y = -ptStartPos.y;

			pgl.glu.gluUnProject((double) x, (double) y, (double) 1.0,
					this.m_adModelview, 0,
					this.m_adProjection, 0,
					this.m_aiViewport, 0,
					out, 0);

			ptEndPos.set((float) out[0], (float) out[1], (float) out[2]);
			if ((m_iProjection != MouseRay.VIEW_FRONT)
					&& (m_iProjection != MouseRay.VIEW_REAR))
				ptEndPos.y = -ptEndPos.y;

			return true;

		}

		return false;

	}

}
