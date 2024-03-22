package com.brahvim.nerd.framework.shapes;

import com.brahvim.nerd.processing_wrapper.NerdShape;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PShapeOBJ;
import processing.core.PVector;

public class NerdObjShape extends NerdShape {

	final PShapeOBJ OBJ_SHAPE;

	public NerdObjShape(final PShape p_shape) {
		super(p_shape);
		this.OBJ_SHAPE = (PShapeOBJ) p_shape;
	}

	// region `PShapeOBJ` methods.
	@Override
	public NerdObjShape addChild(final PShape who) {
		this.OBJ_SHAPE.addChild(who);
		return this;
	}

	@Override
	public NerdObjShape addChild(final PShape who, final int idx) {
		this.OBJ_SHAPE.addChild(who, idx);
		return this;
	}

	@Override
	public NerdObjShape addName(final String nom, final PShape shape) {
		this.OBJ_SHAPE.addName(nom, shape);
		return this;
	}

	@Override
	public NerdObjShape ambient(final int rgb) {
		this.OBJ_SHAPE.ambient(rgb);
		return this;
	}

	@Override
	public NerdObjShape ambient(final float gray) {
		this.OBJ_SHAPE.ambient(gray);
		return this;
	}

	@Override
	public NerdObjShape ambient(final float x, final float y, final float z) {
		this.OBJ_SHAPE.ambient(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape applyMatrix(final PMatrix source) {
		this.OBJ_SHAPE.applyMatrix(source);
		return this;
	}

	@Override
	public NerdObjShape applyMatrix(final PMatrix2D source) {
		this.OBJ_SHAPE.applyMatrix(source);
		return this;
	}

	@Override
	public NerdObjShape applyMatrix(final PMatrix3D source) {
		this.OBJ_SHAPE.applyMatrix(source);
		return this;
	}

	@Override
	public NerdObjShape applyMatrix(final float n00, final float n01, final float n02, final float n10,
			final float n11,
			final float n12) {
		this.OBJ_SHAPE.applyMatrix(n00, n01, n02, n10, n11, n12);
		return this;
	}

	// @Override
	// public NerdObjPShape applyMatrix(final float n00, final float n01, final
	// float n02, final float n03,
	// final float n10,
	// final float n11, final float n12, final float n13,
	// final float n20, final float n21, final float n22, final float n23, final
	// float n30, final float n31,
	// final float n32, final float n33) {
	// this.SHAPE.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22,
	// n23, n30, n31, n32, n33);
	// return this;
	// }

	@Override
	public NerdObjShape attrib(final String name, final float... values) {
		this.OBJ_SHAPE.attrib(name, values);
		return this;
	}

	@Override
	public NerdObjShape attrib(final String name, final int... values) {
		this.OBJ_SHAPE.attrib(name, values);
		return this;
	}

	@Override
	public NerdObjShape attrib(final String name, final boolean... values) {
		this.OBJ_SHAPE.attrib(name, values);
		return this;
	}

	@Override
	public NerdObjShape attribColor(final String name, final int color) {
		this.OBJ_SHAPE.attribColor(name, color);
		return this;
	}

	@Override
	public NerdObjShape attribNormal(final String name, final float nx, final float ny, final float nz) {
		this.OBJ_SHAPE.attribNormal(name, nx, ny, nz);
		return this;
	}

	@Override
	public NerdObjShape attribPosition(final String name, final float x, final float y, final float z) {
		this.OBJ_SHAPE.attribPosition(name, x, y, z);
		return this;
	}

	@Override
	public NerdObjShape beginContour() {
		this.OBJ_SHAPE.beginContour();
		return this;
	}

	// @Override
	// protected NerdObjPShape beginContourImpl() {
	// this.SHAPE.beginContourImpl();
	// return this;
	// }

	@Override
	public NerdObjShape beginShape() {
		this.OBJ_SHAPE.beginShape();
		return this;
	}

	@Override
	public NerdObjShape beginShape(final int kind) {
		this.OBJ_SHAPE.beginShape(kind);
		return this;
	}

	@Override
	public NerdObjShape bezierDetail(final int detail) {
		this.OBJ_SHAPE.bezierDetail(detail);
		return this;
	}

	// @Override
	// public NerdObjPShape bezierVertex(final float x2, final float y2, final float
	// x3, final float y3, final float x4,
	// final float y4) {
	// return this;
	// this.SHAPE.bezierVertex(x2, y2, x3, y3, x4, y4);
	// }

	// @Override
	// public NerdObjPShape bezierVertex(final float x2, final float y2, final float
	// z2, final float x3, final float y3,
	// final float z3, final float x4, final float y4, final float z4) {
	// return this;
	// this.SHAPE.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
	// }

	// @Override
	// protected NerdObjPShape checkMatrix(final int dimensions) {
	// this.SHAPE.checkMatrix(dimensions);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final int rgb) {
	// this.SHAPE.colorCalc(rgb);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final float gray) {
	// this.SHAPE.colorCalc(gray);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final int rgb, final float alpha) {
	// this.SHAPE.colorCalc(rgb, alpha);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final float gray, final float alpha) {
	// this.SHAPE.colorCalc(gray, alpha);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final float x, final float y, final float
	// z) {
	// this.SHAPE.colorCalc(x, y, z);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalc(final float x, final float y, final float
	// z, final float a) {
	// this.SHAPE.colorCalc(x, y, z, a);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape colorCalcARGB(final int argb, final float alpha) {
	// this.SHAPE.colorCalcARGB(argb, alpha);
	// return this;
	// }

	@Override
	public NerdObjShape colorMode(final int mode) {
		this.OBJ_SHAPE.colorMode(mode);
		return this;
	}

	// @Override
	// public NerdObjShape colorMode(final int mode, final float max) {
	// this.OBJ_SHAPE.colorMode(mode, max);
	// return this;
	// }

	// @Override
	// public NerdObjShape colorMode(final int mode, final float maxX, final float
	// maxY, final float maxZ) {
	// this.OBJ_SHAPE.colorMode(mode, maxX, maxY, maxZ);
	// return this;
	// }

	// @Override
	// public NerdObjShape colorMode(final int mode, final float maxX, final float
	// maxY, final float maxZ,
	// final float maxA) {
	// this.OBJ_SHAPE.colorMode(mode, maxX, maxY, maxZ, maxA);
	// return this;
	// }

	@Override
	public boolean contains(final float x, final float y) {
		return this.OBJ_SHAPE.contains(x, y);
	}

	// @Override
	// protected NerdObjPShape crop() {
	// this.SHAPE.crop();
	// return this;
	// }

	@Override
	public NerdObjShape curveDetail(final int detail) {
		this.OBJ_SHAPE.curveDetail(detail);
		return this;
	}

	@Override
	public NerdObjShape curveTightness(final float tightness) {
		this.OBJ_SHAPE.curveTightness(tightness);
		return this;
	}

	@Override
	public NerdObjShape curveVertex(final float x, final float y) {
		this.OBJ_SHAPE.curveVertex(x, y);
		return this;
	}

	@Override
	public NerdObjShape curveVertex(final float x, final float y, final float z) {
		this.OBJ_SHAPE.curveVertex(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape disableStyle() {
		this.OBJ_SHAPE.disableStyle();
		return this;
	}

	@Override
	public NerdObjShape draw(final PGraphics g) {
		this.OBJ_SHAPE.draw(g);
		return this;
	}

	// @Override
	// protected NerdObjPShape drawGeometry(final PGraphics g) {
	// this.SHAPE.drawGeometry(g);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape drawGroup(final PGraphics g) {
	// this.SHAPE.drawGroup(g);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape drawImpl(final PGraphics g) {
	// this.SHAPE.drawImpl(g);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape drawPath(final PGraphics g) {
	// this.SHAPE.drawPath(g);
	// return this;
	// }

	// @Override
	// protected NerdObjPShape drawPrimitive(final PGraphics g) {
	// this.SHAPE.drawPrimitive(g);
	// return this;
	// }

	@Override
	public NerdObjShape emissive(final int rgb) {
		this.OBJ_SHAPE.emissive(rgb);
		return this;
	}

	@Override
	public NerdObjShape emissive(final float gray) {
		this.OBJ_SHAPE.emissive(gray);
		return this;
	}

	@Override
	public NerdObjShape emissive(final float x, final float y, final float z) {
		this.OBJ_SHAPE.emissive(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape enableStyle() {
		this.OBJ_SHAPE.enableStyle();
		return this;
	}

	@Override
	public NerdObjShape endContour() {
		this.OBJ_SHAPE.endContour();
		return this;
	}

	// @Override
	// protected NerdObjPShape endContourImpl() {
	// this.SHAPE.endContourImpl();
	// return this;
	// }

	@Override
	public NerdObjShape endShape() {
		this.OBJ_SHAPE.endShape();
		return this;
	}

	@Override
	public NerdObjShape endShape(final int mode) {
		this.OBJ_SHAPE.endShape(mode);
		return this;
	}

	@Override
	public NerdObjShape fill(final int rgb) {
		this.OBJ_SHAPE.fill(rgb);
		return this;
	}

	@Override
	public NerdObjShape fill(final float gray) {
		this.OBJ_SHAPE.fill(gray);
		return this;
	}

	@Override
	public NerdObjShape fill(final int rgb, final float alpha) {
		this.OBJ_SHAPE.fill(rgb, alpha);
		return this;
	}

	@Override
	public NerdObjShape fill(final float gray, final float alpha) {
		this.OBJ_SHAPE.fill(gray, alpha);
		return this;
	}

	@Override
	public NerdObjShape fill(final float x, final float y, final float z) {
		this.OBJ_SHAPE.fill(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape fill(final float x, final float y, final float z, final float a) {
		this.OBJ_SHAPE.fill(x, y, z, a);
		return this;
	}

	@Override
	public PShape findChild(final String target) {
		return this.OBJ_SHAPE.findChild(target);
	}

	@Override
	public int getAmbient(final int index) {
		return this.OBJ_SHAPE.getAmbient(index);
	}

	@Override
	public PShape getChild(final int index) {
		return this.OBJ_SHAPE.getChild(index);
	}

	@Override
	public PShape getChild(final String target) {
		return this.OBJ_SHAPE.getChild(target);
	}

	@Override
	public int getChildCount() {
		return this.OBJ_SHAPE.getChildCount();
	}

	@Override
	public int getChildIndex(final PShape who) {
		return this.OBJ_SHAPE.getChildIndex(who);
	}

	@Override
	public PShape[] getChildren() {
		return this.OBJ_SHAPE.getChildren();
	}

	@Override
	public float getDepth() {
		return this.OBJ_SHAPE.getDepth();
	}

	@Override
	public int getEmissive(final int index) {
		return this.OBJ_SHAPE.getEmissive(index);
	}

	@Override
	public int getFamily() {
		return this.OBJ_SHAPE.getFamily();
	}

	@Override
	public int getFill(final int index) {
		return this.OBJ_SHAPE.getFill(index);
	}

	@Override
	public float getHeight() {
		return this.OBJ_SHAPE.getHeight();
	}

	@Override
	public int getKind() {
		return this.OBJ_SHAPE.getKind();
	}

	@Override
	public String getName() {
		return this.OBJ_SHAPE.getName();
	}

	@Override
	public PVector getNormal(final int index) {
		return this.OBJ_SHAPE.getNormal(index);
	}

	@Override
	public PVector getNormal(final int index, final PVector vec) {
		return this.OBJ_SHAPE.getNormal(index, vec);
	}

	@Override
	public float getNormalX(final int index) {
		return this.OBJ_SHAPE.getNormalX(index);
	}

	@Override
	public float getNormalY(final int index) {
		return this.OBJ_SHAPE.getNormalY(index);
	}

	@Override
	public float getNormalZ(final int index) {
		return this.OBJ_SHAPE.getNormalZ(index);
	}

	@Override
	public float getParam(final int index) {
		return this.OBJ_SHAPE.getParam(index);
	}

	@Override
	public float[] getParams() {
		return this.OBJ_SHAPE.getParams();
	}

	@Override
	public float[] getParams(final float[] target) {
		return this.OBJ_SHAPE.getParams(target);
	}

	@Override
	public PShape getParent() {
		return this.OBJ_SHAPE.getParent();
	}

	@Override
	public float getShininess(final int index) {
		return this.OBJ_SHAPE.getShininess(index);
	}

	@Override
	public int getSpecular(final int index) {
		return this.OBJ_SHAPE.getSpecular(index);
	}

	@Override
	public int getStroke(final int index) {
		return this.OBJ_SHAPE.getStroke(index);
	}

	@Override
	public float getStrokeWeight(final int index) {
		return this.OBJ_SHAPE.getStrokeWeight(index);
	}

	@Override
	public PShape getTessellation() {
		return this.OBJ_SHAPE.getTessellation();
	}

	@Override
	public float getTextureU(final int index) {
		return this.OBJ_SHAPE.getTextureU(index);
	}

	@Override
	public float getTextureV(final int index) {
		return this.OBJ_SHAPE.getTextureV(index);
	}

	@Override
	public int getTint(final int index) {
		return this.OBJ_SHAPE.getTint(index);
	}

	@Override
	public PVector getVertex(final int index) {
		return this.OBJ_SHAPE.getVertex(index);
	}

	@Override
	public PVector getVertex(final int index, final PVector vec) {
		return this.OBJ_SHAPE.getVertex(index, vec);
	}

	@Override
	public int getVertexCode(final int index) {
		return this.OBJ_SHAPE.getVertexCode(index);
	}

	@Override
	public int getVertexCodeCount() {
		return this.OBJ_SHAPE.getVertexCodeCount();
	}

	@Override
	public int[] getVertexCodes() {
		return this.OBJ_SHAPE.getVertexCodes();
	}

	@Override
	public int getVertexCount() {
		return this.OBJ_SHAPE.getVertexCount();
	}

	@Override
	public float getVertexX(final int index) {
		return this.OBJ_SHAPE.getVertexX(index);
	}

	@Override
	public float getVertexY(final int index) {
		return this.OBJ_SHAPE.getVertexY(index);
	}

	@Override
	public float getVertexZ(final int index) {
		return this.OBJ_SHAPE.getVertexZ(index);
	}

	@Override
	public float getWidth() {
		return this.OBJ_SHAPE.getWidth();
	}

	@Override
	public boolean is2D() {
		return this.OBJ_SHAPE.is2D();
	}

	@Override
	public boolean is3D() {
		return this.OBJ_SHAPE.is3D();
	}

	@Override
	public boolean isClosed() {
		return this.OBJ_SHAPE.isClosed();
	}

	@Override
	public boolean isVisible() {
		return this.OBJ_SHAPE.isVisible();
	}

	@Override
	public NerdObjShape noFill() {
		this.OBJ_SHAPE.noFill();
		return this;
	}

	@Override
	public NerdObjShape noStroke() {
		this.OBJ_SHAPE.noStroke();
		return this;
	}

	@Override
	public NerdObjShape noTexture() {
		this.OBJ_SHAPE.noTexture();
		return this;
	}

	@Override
	public NerdObjShape noTint() {
		this.OBJ_SHAPE.noTint();
		return this;
	}

	@Override
	public NerdObjShape normal(final float nx, final float ny, final float nz) {
		this.OBJ_SHAPE.normal(nx, ny, nz);
		return this;
	}

	@Override
	protected NerdObjShape post(final PGraphics g) {
		// this.SHAPE.post(g);
		return this;
	}

	@Override
	protected NerdObjShape pre(final PGraphics g) {
		// this.SHAPE.pre(g);
		return this;
	}

	@Override
	public NerdObjShape quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
		this.OBJ_SHAPE.quadraticVertex(cx, cy, x3, y3);
		return this;
	}

	@Override
	public NerdObjShape quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3,
			final float z3) {
		this.OBJ_SHAPE.quadraticVertex(cx, cy, cz, x3, y3, z3);
		return this;
	}

	@Override
	public NerdObjShape removeChild(final int idx) {
		this.OBJ_SHAPE.removeChild(idx);
		return this;
	}

	@Override
	public NerdObjShape resetMatrix() {
		this.OBJ_SHAPE.resetMatrix();
		return this;
	}

	@Override
	public NerdObjShape rotate(final float angle) {
		this.OBJ_SHAPE.rotate(angle);
		return this;
	}

	@Override
	public NerdObjShape rotate(final float angle, final float v0, final float v1, final float v2) {
		this.OBJ_SHAPE.rotate(angle, v0, v1, v2);
		return this;
	}

	@Override
	public NerdObjShape rotateX(final float angle) {
		this.OBJ_SHAPE.rotateX(angle);
		return this;
	}

	@Override
	public NerdObjShape rotateY(final float angle) {
		this.OBJ_SHAPE.rotateY(angle);
		return this;
	}

	@Override
	public NerdObjShape rotateZ(final float angle) {
		this.OBJ_SHAPE.rotateZ(angle);
		return this;
	}

	@Override
	public NerdObjShape scale(final float s) {
		this.OBJ_SHAPE.scale(s);
		return this;
	}

	@Override
	public NerdObjShape scale(final float x, final float y) {
		this.OBJ_SHAPE.scale(x, y);
		return this;
	}

	@Override
	public NerdObjShape scale(final float x, final float y, final float z) {
		this.OBJ_SHAPE.scale(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape set3D(final boolean val) {
		this.OBJ_SHAPE.set3D(val);
		return this;
	}

	@Override
	public NerdObjShape setAmbient(final int ambient) {
		this.OBJ_SHAPE.setAmbient(ambient);
		return this;
	}

	@Override
	public NerdObjShape setAmbient(final int index, final int ambient) {
		this.OBJ_SHAPE.setAmbient(index, ambient);
		return this;
	}

	@Override
	public NerdObjShape setAttrib(final String name, final int index, final float... values) {
		this.OBJ_SHAPE.setAttrib(name, index, values);
		return this;
	}

	@Override
	public NerdObjShape setAttrib(final String name, final int index, final int... values) {
		this.OBJ_SHAPE.setAttrib(name, index, values);
		return this;
	}

	@Override
	public NerdObjShape setAttrib(final String name, final int index, final boolean... values) {
		this.OBJ_SHAPE.setAttrib(name, index, values);
		return this;
	}

	@Override
	public NerdObjShape setEmissive(final int emissive) {
		this.OBJ_SHAPE.setEmissive(emissive);
		return this;
	}

	@Override
	public NerdObjShape setEmissive(final int index, final int emissive) {
		this.OBJ_SHAPE.setEmissive(index, emissive);
		return this;
	}

	@Override
	public NerdObjShape setFamily(final int family) {
		this.OBJ_SHAPE.setFamily(family);
		return this;
	}

	@Override
	public NerdObjShape setFill(final boolean fill) {
		this.OBJ_SHAPE.setFill(fill);
		return this;
	}

	@Override
	public NerdObjShape setFill(final int fill) {
		this.OBJ_SHAPE.setFill(fill);
		return this;
	}

	@Override
	public NerdObjShape setFill(final int index, final int fill) {
		this.OBJ_SHAPE.setFill(index, fill);
		return this;
	}

	@Override
	public NerdObjShape setKind(final int kind) {
		this.OBJ_SHAPE.setKind(kind);
		return this;
	}

	@Override
	public NerdObjShape setName(final String name) {
		this.OBJ_SHAPE.setName(name);
		return this;
	}

	@Override
	public NerdObjShape setNormal(final int index, final float nx, final float ny, final float nz) {
		this.OBJ_SHAPE.setNormal(index, nx, ny, nz);
		return this;
	}

	// @Override
	// protected NerdObjPShape setParams(final float[] source) {
	// this.SHAPE.setParams(source);
	// return this;
	// }

	@Override
	public NerdObjShape setPath(final int vcount, final float[][] verts) {
		this.OBJ_SHAPE.setPath(vcount, verts);
		return this;
	}

	// @Override
	// protected NerdObjPShape setPath(final int vcount, final float[][] verts,
	// final int ccount, final int[] codes) {
	// this.SHAPE.setPath(vcount, verts, ccount, codes);
	// return this;
	// }

	@Override
	public NerdObjShape setShininess(final float shine) {
		this.OBJ_SHAPE.setShininess(shine);
		return this;
	}

	@Override
	public NerdObjShape setShininess(final int index, final float shine) {
		this.OBJ_SHAPE.setShininess(index, shine);
		return this;
	}

	@Override
	public NerdObjShape setSpecular(final int specular) {
		this.OBJ_SHAPE.setSpecular(specular);
		return this;
	}

	@Override
	public NerdObjShape setSpecular(final int index, final int specular) {
		this.OBJ_SHAPE.setSpecular(index, specular);
		return this;
	}

	@Override
	public NerdObjShape setStroke(final boolean stroke) {
		this.OBJ_SHAPE.setStroke(stroke);
		return this;
	}

	@Override
	public NerdObjShape setStroke(final int stroke) {
		this.OBJ_SHAPE.setStroke(stroke);
		return this;
	}

	@Override
	public NerdObjShape setStroke(final int index, final int stroke) {
		this.OBJ_SHAPE.setStroke(index, stroke);
		return this;
	}

	@Override
	public NerdObjShape setStrokeCap(final int cap) {
		this.OBJ_SHAPE.setStrokeCap(cap);
		return this;
	}

	@Override
	public NerdObjShape setStrokeJoin(final int join) {
		this.OBJ_SHAPE.setStrokeJoin(join);
		return this;
	}

	@Override
	public NerdObjShape setStrokeWeight(final float weight) {
		this.OBJ_SHAPE.setStrokeWeight(weight);
		return this;
	}

	@Override
	public NerdObjShape setStrokeWeight(final int index, final float weight) {
		this.OBJ_SHAPE.setStrokeWeight(index, weight);
		return this;
	}

	@Override
	public NerdObjShape setTexture(final PImage tex) {
		this.OBJ_SHAPE.setTexture(tex);
		return this;
	}

	@Override
	public NerdObjShape setTextureMode(final int mode) {
		this.OBJ_SHAPE.setTextureMode(mode);
		return this;
	}

	@Override
	public NerdObjShape setTextureUV(final int index, final float u, final float v) {
		this.OBJ_SHAPE.setTextureUV(index, u, v);
		return this;
	}

	@Override
	public NerdObjShape setTint(final boolean tint) {
		this.OBJ_SHAPE.setTint(tint);
		return this;
	}

	@Override
	public NerdObjShape setTint(final int fill) {
		this.OBJ_SHAPE.setTint(fill);
		return this;
	}

	@Override
	public NerdObjShape setTint(final int index, final int tint) {
		this.OBJ_SHAPE.setTint(index, tint);
		return this;
	}

	@Override
	public NerdObjShape setVertex(final int index, final PVector vec) {
		this.OBJ_SHAPE.setVertex(index, vec);
		return this;
	}

	@Override
	public NerdObjShape setVertex(final int index, final float x, final float y) {
		this.OBJ_SHAPE.setVertex(index, x, y);
		return this;
	}

	@Override
	public NerdObjShape setVertex(final int index, final float x, final float y, final float z) {
		this.OBJ_SHAPE.setVertex(index, x, y, z);
		return this;
	}

	@Override
	public NerdObjShape setVisible(final boolean visible) {
		this.OBJ_SHAPE.setVisible(visible);
		return this;
	}

	@Override
	public NerdObjShape shininess(final float shine) {
		this.OBJ_SHAPE.shininess(shine);
		return this;
	}

	// @Override
	// protected NerdObjPShape solid(final boolean solid) {
	// this.SHAPE.solid(solid);
	// return this;
	// }

	@Override
	public NerdObjShape specular(final int rgb) {
		this.OBJ_SHAPE.specular(rgb);
		return this;
	}

	@Override
	public NerdObjShape specular(final float gray) {
		this.OBJ_SHAPE.specular(gray);
		return this;
	}

	@Override
	public NerdObjShape specular(final float x, final float y, final float z) {
		this.OBJ_SHAPE.specular(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape stroke(final int rgb) {
		this.OBJ_SHAPE.stroke(rgb);
		return this;
	}

	@Override
	public NerdObjShape stroke(final float gray) {
		this.OBJ_SHAPE.stroke(gray);
		return this;
	}

	@Override
	public NerdObjShape stroke(final int rgb, final float alpha) {
		this.OBJ_SHAPE.stroke(rgb, alpha);
		return this;
	}

	@Override
	public NerdObjShape stroke(final float gray, final float alpha) {
		this.OBJ_SHAPE.stroke(gray, alpha);
		return this;
	}

	@Override
	public NerdObjShape stroke(final float x, final float y, final float z) {
		this.OBJ_SHAPE.stroke(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape stroke(final float x, final float y, final float z, final float alpha) {
		this.OBJ_SHAPE.stroke(x, y, z, alpha);
		return this;
	}

	@Override
	public NerdObjShape strokeCap(final int cap) {
		this.OBJ_SHAPE.strokeCap(cap);
		return this;
	}

	@Override
	public NerdObjShape strokeJoin(final int join) {
		this.OBJ_SHAPE.strokeJoin(join);
		return this;
	}

	@Override
	public NerdObjShape strokeWeight(final float weight) {
		this.OBJ_SHAPE.strokeWeight(weight);
		return this;
	}

	// @Override
	// protected NerdObjPShape styles(final PGraphics g) {
	// this.SHAPE.styles(g);
	// return this;
	// }

	@Override
	public NerdObjShape texture(final PImage tex) {
		this.OBJ_SHAPE.texture(tex);
		return this;
	}

	@Override
	public NerdObjShape textureMode(final int mode) {
		this.OBJ_SHAPE.textureMode(mode);
		return this;
	}

	@Override
	public NerdObjShape tint(final int rgb) {
		this.OBJ_SHAPE.tint(rgb);
		return this;
	}

	@Override
	public NerdObjShape tint(final float gray) {
		this.OBJ_SHAPE.tint(gray);
		return this;
	}

	@Override
	public NerdObjShape tint(final int rgb, final float alpha) {
		this.OBJ_SHAPE.tint(rgb, alpha);
		return this;
	}

	@Override
	public NerdObjShape tint(final float gray, final float alpha) {
		this.OBJ_SHAPE.tint(gray, alpha);
		return this;
	}

	@Override
	public NerdObjShape tint(final float x, final float y, final float z) {
		this.OBJ_SHAPE.tint(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape tint(final float x, final float y, final float z, final float alpha) {
		this.OBJ_SHAPE.tint(x, y, z, alpha);
		return this;
	}

	@Override
	public NerdObjShape translate(final float x, final float y) {
		this.OBJ_SHAPE.translate(x, y);
		return this;
	}

	@Override
	public NerdObjShape translate(final float x, final float y, final float z) {
		this.OBJ_SHAPE.translate(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape vertex(final float x, final float y) {
		this.OBJ_SHAPE.vertex(x, y);
		return this;
	}

	@Override
	public NerdObjShape vertex(final float x, final float y, final float z) {
		this.OBJ_SHAPE.vertex(x, y, z);
		return this;
	}

	@Override
	public NerdObjShape vertex(final float x, final float y, final float u, final float v) {
		this.OBJ_SHAPE.vertex(x, y, u, v);
		return this;
	}

	@Override
	public NerdObjShape vertex(final float x, final float y, final float z, final float u, final float v) {
		this.OBJ_SHAPE.vertex(x, y, z, u, v);
		return this;
	}
	// endregion

}
