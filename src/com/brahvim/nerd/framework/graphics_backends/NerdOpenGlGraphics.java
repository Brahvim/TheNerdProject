package com.brahvim.nerd.framework.graphics_backends;

import com.brahvim.nerd.processing_wrapper.NerdSketch;

import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import processing.opengl.PShader;

public abstract class NerdOpenGlGraphics<SketchPGraphicsT extends PGraphicsOpenGL>
		extends NerdFramebufferBackedGraphics<SketchPGraphicsT> {

	// region Inner classes.
	/**
	 * Performs
	 */
	public class PglPush implements AutoCloseable {

		public final PGL pgl;

		public PglPush() {
			this.pgl = NerdOpenGlGraphics.this.beginPGL();
		}

		@Override
		public void close() throws Exception {
			NerdOpenGlGraphics.this.endPGL();
		}

	}
	// endregion

	// region Utilitarian constructors.
	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final int p_width, final int p_height, final String p_renderer, final String p_path) {
		super(p_sketch, p_width, p_height, p_renderer, p_path);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final int p_width, final int p_height, final String p_renderer) {
		super(p_sketch, p_width, p_height, p_renderer);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_width, final int p_height) {
		super(p_sketch, p_width, p_height);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final float p_width, final float p_height) {
		super(p_sketch, p_width, p_height);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final float p_size) {
		super(p_sketch, p_size);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch, final int p_size) {
		super(p_sketch, p_size);
	}

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch) {
		super(p_sketch);
	}
	// endregion

	protected NerdOpenGlGraphics(
			final NerdSketch<SketchPGraphicsT> p_sketch,
			final SketchPGraphicsT p_pGraphicsToWrap) {
		super(p_sketch, p_pGraphicsToWrap);
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.GRAPHICS.ellipse(0, 0, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}

	public void ellipse(final float p_x, final float p_y, final float p_z, final PVector p_dimensions) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.GRAPHICS.ellipse(0, 0, p_dimensions.x, p_dimensions.y);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_width, p_height);
		this.GRAPHICS.popMatrix();
	}

	public void rect(final float p_x, final float p_y, final float p_z, final float p_width, final float p_height,
			final float p_radius) {
		this.GRAPHICS.pushMatrix();
		this.translate(p_x, p_y, p_z);
		this.GRAPHICS.rect(0, 0, p_width, p_height, p_radius);
		this.GRAPHICS.popMatrix();
	}

	public void ambient(final int rgb) {
		this.GRAPHICS.ambient(rgb);
	}

	public void ambient(final float gray) {
		this.GRAPHICS.ambient(gray);
	}

	public void ambient(final float v1, final float v2, final float v3) {
		this.GRAPHICS.ambient(v1, v2, v3);
	}

	public void ambientLight(final float v1, final float v2, final float v3) {
		this.GRAPHICS.ambientLight(v1, v2, v3);
	}

	public void ambientLight(final float v1, final float v2, final float v3,
			final float x, final float y,
			final float z) {
		this.GRAPHICS.ambientLight(v1, v2, v3, x, y, z);
	}

	public void applyMatrix(final PMatrix source) {
		this.GRAPHICS.applyMatrix(source);
	}

	public void applyMatrix(final PMatrix2D source) {
		this.GRAPHICS.applyMatrix(source);
	}

	public void applyMatrix(final PMatrix3D source) {
		this.GRAPHICS.applyMatrix(source);
	}

	public void applyMatrix(final float n00, final float n01, final float n02,
			final float n10, final float n11,
			final float n12) {
		this.GRAPHICS.applyMatrix(n00, n01, n02, n10, n11, n12);
	}

	public void applyMatrix(final float n00, final float n01, final float n02,
			final float n03, final float n10,
			final float n11, final float n12, final float n13, final float n20, final float n21, final float n22,
			final float n23, final float n30, final float n31, final float n32, final float n33) {
		this.GRAPHICS.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21,
				n22, n23, n30, n31, n32, n33);
	}

	public void attrib(final String name, final float... values) {
		this.GRAPHICS.attrib(name, values);
	}

	public void attrib(final String name, final int... values) {
		this.GRAPHICS.attrib(name, values);
	}

	public void attrib(final String name, final boolean... values) {
		this.GRAPHICS.attrib(name, values);
	}

	public void attribColor(final String name, final int color) {
		this.GRAPHICS.attribColor(name, color);
	}

	public void attribNormal(final String name, final float nx, final float ny,
			final float nz) {
		this.GRAPHICS.attribNormal(name, nx, ny, nz);
	}

	public void attribPosition(final String name, final float x, final float y,
			final float z) {
		this.GRAPHICS.attribPosition(name, x, y, z);
	}

	public PGL beginPGL() {
		return this.GRAPHICS.beginPGL();
	}

	public void endPGL() {
		this.GRAPHICS.endPGL();
	}

	public void curveVertex(final float x, final float y, final float z) {
		this.GRAPHICS.curveVertex(x, y, z);
	}

	public void filter(final PShader shader) {
		this.GRAPHICS.filter(shader);
	}

	public float modelX(final float x, final float y, final float z) {
		return this.GRAPHICS.modelX(x, y, z);
	}

	public float modelY(final float x, final float y, final float z) {
		return this.GRAPHICS.modelY(x, y, z);
	}

	public float modelZ(final float x, final float y, final float z) {
		return this.GRAPHICS.modelZ(x, y, z);
	}

	public void point(final float x, final float y, final float z) {
		this.GRAPHICS.point(x, y, z);
	}

	public void pointLight(final float v1, final float v2, final float v3,
			final float x, final float y, final float z) {
		this.GRAPHICS.pointLight(v1, v2, v3, x, y, z);
	}

	public void rotate(final float angle, final float x, final float y, final float z) {
		this.GRAPHICS.rotate(angle, x, y, z);
	}

	public void scale(final float x, final float y, final float z) {
		this.GRAPHICS.scale(x, y, z);
	}

	public float screenX(final float x, final float y) {
		return this.GRAPHICS.screenX(x, y);
	}

	public float screenX(final float x, final float y, final float z) {
		return this.GRAPHICS.screenX(x, y, z);
	}

	public float screenY(final float x, final float y) {
		return this.GRAPHICS.screenY(x, y);
	}

	public float screenY(final float x, final float y, final float z) {
		return this.GRAPHICS.screenY(x, y, z);
	}

	public float screenZ(final float x, final float y, final float z) {
		return this.GRAPHICS.screenZ(x, y, z);
	}

	public void spotLight(final float v1, final float v2, final float v3, final float x, final float y, final float z,
			final float nx, final float ny, final float nz, final float angle, final float concentration) {
		this.GRAPHICS.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle,
				concentration);
	}

	public void text(final char c, final float x, final float y, final float z) {
		this.GRAPHICS.text(c, x, y, z);
	}

	public void text(final int num, final float x, final float y, final float z) {
		this.GRAPHICS.text(num, x, y, z);
	}

	public void text(final float num, final float x, final float y, final float z) {
		this.GRAPHICS.text(num, x, y, z);
	}

	public void text(final String str, final float x, final float y, final float z) {
		this.GRAPHICS.text(str, x, y, z);
	}

	public void text(final char[] chars, final int start, final int stop, final float x, final float y, final float z) {
		this.GRAPHICS.text(chars, start, stop, x, y, z);
	}

	public void translate(final float x, final float y, final float z) {
		this.GRAPHICS.translate(x, y, z);
	}

	public void vertex(final float x, final float y, final float z) {
		this.GRAPHICS.vertex(x, y, z);
	}

	public void vertex(final float x, final float y, final float z, final float u, final float v) {
		this.GRAPHICS.vertex(x, y, z, u, v);
	}

	public PShader loadShader(final String fragFilename) {
		return this.GRAPHICS.loadShader(fragFilename);
	}

	public PShader loadShader(final String fragFilename, final String vertFilename) {
		return this.GRAPHICS.loadShader(fragFilename, vertFilename);
	}

	public void shader(final PShader shader) {
		this.GRAPHICS.shader(shader);
	}

	public void shader(final PShader shader, final int kind) {
		this.GRAPHICS.shader(shader, kind);
	}

	public void frustum(final float left, final float right, final float bottom, final float top, final float near,
			final float far) {
		this.GRAPHICS.frustum(left, right, bottom, top, near, far);
	}

	public PMatrix getMatrix() {
		return this.GRAPHICS.getMatrix();
	}

	public PMatrix2D getMatrix(final PMatrix2D target) {
		return this.GRAPHICS.getMatrix(target);
	}

	public PMatrix3D getMatrix(final PMatrix3D target) {
		return this.GRAPHICS.getMatrix(target);
	}

	public void line(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
		this.GRAPHICS.line(x1, y1, z1, x2, y2, z2);
	}

	public void lightFalloff(final float constant, final float linear, final float quadratic) {
		this.GRAPHICS.lightFalloff(constant, linear, quadratic);
	}

	public void lightSpecular(final float v1, final float v2, final float v3) {
		this.GRAPHICS.lightSpecular(v1, v2, v3);
	}

	public void lights() {
		this.GRAPHICS.lights();
	}

	public void noLights() {
		this.GRAPHICS.noLights();
	}

	public void ortho() {
		this.GRAPHICS.ortho();
	}

	public void ortho(final float left, final float right, final float bottom, final float top, final float near,
			final float far) {
		this.GRAPHICS.ortho(left, right, bottom, top, near, far);
	}

	public void perspective() {
		this.GRAPHICS.perspective();
	}

	public void perspective(final float fovy, final float aspect, final float zNear, final float zFar) {
		this.GRAPHICS.perspective(fovy, aspect, zNear, zFar);
	}

	public void printCamera() {
		this.GRAPHICS.printCamera();
	}

	public void printMatrix() {
		this.GRAPHICS.printMatrix();
	}

	public void printProjection() {
		this.GRAPHICS.printProjection();
	}

	public void quadraticVertex(final float cx, final float cy, final float cz,
			final float x3, final float y3,
			final float z3) {
		this.GRAPHICS.quadraticVertex(cx, cy, cz, x3, y3, z3);
	}

	public void setMatrix(final PMatrix source) {
		this.GRAPHICS.setMatrix(source);
	}

	public void setMatrix(final PMatrix2D source) {
		this.GRAPHICS.setMatrix(source);
	}

	public void setMatrix(final PMatrix3D source) {
		this.GRAPHICS.setMatrix(source);
	}

	public void box(final float size) {
		this.GRAPHICS.box(size);
	}

	public void box(final float w, final float h, final float d) {
		this.GRAPHICS.box(w, h, d);
	}

	public void camera() {
		this.GRAPHICS.camera();
	}

	public void camera(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY,
			final float centerZ, final float upX, final float upY, final float upZ) {
		this.GRAPHICS.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}

	public void circle(final float x, final float y, final float extent) {
		this.GRAPHICS.circle(x, y, extent);
	}

	public void sphere(final float r) {
		this.GRAPHICS.sphere(r);
	}

	public void sphereDetail(final int res) {
		this.GRAPHICS.sphereDetail(res);
	}

	public void sphereDetail(final int ures, final int vres) {
		this.GRAPHICS.sphereDetail(ures, vres);
	}

	@Override
	public void updatePixels(final int x, final int y, final int w, final int h) {
		this.GRAPHICS.updatePixels(x, y, w, h);
	}

	@Override
	public int[] loadPixels() {
		super.GRAPHICS.loadPixels();
		return super.GRAPHICS.pixels;
	}

	@Override
	public void updatePixels() {
		super.GRAPHICS.updatePixels();
	}

	// region Not in `SVG`/`PDF`, but are here.
	@Override
	public boolean save(final String filename) {
		return this.GRAPHICS.save(filename);
	}

	@Override
	public void blend(final int sx, final int sy, final int sw, final int sh,
			final int dx, final int dy, final int dw,
			final int dh, final int mode) {
		this.GRAPHICS.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	@Override
	public void blend(final PImage src, final int sx, final int sy, final int sw,
			final int sh, final int dx,
			final int dy, final int dw, final int dh, final int mode) {
		this.GRAPHICS.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
	}

	@Override
	public void copy(final int sx, final int sy, final int sw, final int sh,
			final int dx, final int dy, final int dw,
			final int dh) {
		this.GRAPHICS.copy(sx, sy, sw, sh, dx, dy, dw, dh);
	}

	@Override
	public void copy(final PImage src, final int sx, final int sy, final int sw,
			final int sh, final int dx,
			final int dy, final int dw, final int dh) {
		this.GRAPHICS.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
	}

	@Override
	public void filter(final int kind) {
		this.GRAPHICS.filter(kind);
	}

	@Override
	public void filter(final int kind, final float param) {
		this.GRAPHICS.filter(kind, param);
	}

	@Override
	public PImage get() {
		return this.GRAPHICS.get();
	}

	@Override
	public int get(final int x, final int y) {
		return this.GRAPHICS.get(x, y);
	}

	@Override
	public PImage get(final int x, final int y, final int w, final int h) {
		return this.GRAPHICS.get(x, y, w, h);

	}

	@Override
	public void mask(final int[] maskArray) {
		this.GRAPHICS.mask(maskArray);
	}

	@Override
	public void mask(final PImage img) {
		this.GRAPHICS.mask(img);
	}
	// endregion

}
