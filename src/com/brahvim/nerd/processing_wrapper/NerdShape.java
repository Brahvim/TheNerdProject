package com.brahvim.nerd.processing_wrapper;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PVector;

public class NerdShape {

	final PShape SHAPE;

	public NerdShape(final PShape p_shape) {
		this.SHAPE = p_shape;
	}

	// region `PShape` methods.
	public NerdShape addChild(final PShape who) {
		this.SHAPE.addChild(who);
		return this;
	}

	public NerdShape addChild(final PShape who, final int idx) {
		this.SHAPE.addChild(who, idx);
		return this;
	}

	public NerdShape addName(final String nom, final PShape shape) {
		this.SHAPE.addName(nom, shape);
		return this;
	}

	public NerdShape ambient(final int rgb) {
		this.SHAPE.ambient(rgb);
		return this;
	}

	public NerdShape ambient(final float gray) {
		this.SHAPE.ambient(gray);
		return this;
	}

	public NerdShape ambient(final float x, final float y, final float z) {
		this.SHAPE.ambient(x, y, z);
		return this;
	}

	public NerdShape applyMatrix(final PMatrix source) {
		this.SHAPE.applyMatrix(source);
		return this;
	}

	public NerdShape applyMatrix(final PMatrix2D source) {
		this.SHAPE.applyMatrix(source);
		return this;
	}

	public NerdShape applyMatrix(final PMatrix3D source) {
		this.SHAPE.applyMatrix(source);
		return this;
	}

	public NerdShape applyMatrix(final float n00, final float n01, final float n02, final float n10, final float n11,
			final float n12) {
		this.SHAPE.applyMatrix(n00, n01, n02, n10, n11, n12);
		return this;
	}

	public NerdShape applyMatrix(final float n00, final float n01, final float n02, final float n03, final float n10,
			final float n11, final float n12, final float n13,
			final float n20, final float n21, final float n22, final float n23, final float n30, final float n31,
			final float n32, final float n33) {
		this.SHAPE.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
		return this;
	}

	public NerdShape attrib(final String name, final float... values) {
		this.SHAPE.attrib(name, values);
		return this;
	}

	public NerdShape attrib(final String name, final int... values) {
		this.SHAPE.attrib(name, values);
		return this;
	}

	public NerdShape attrib(final String name, final boolean... values) {
		this.SHAPE.attrib(name, values);
		return this;
	}

	public NerdShape attribColor(final String name, final int color) {
		this.SHAPE.attribColor(name, color);
		return this;
	}

	public NerdShape attribNormal(final String name, final float nx, final float ny, final float nz) {
		this.SHAPE.attribNormal(name, nx, ny, nz);
		return this;
	}

	public NerdShape attribPosition(final String name, final float x, final float y, final float z) {
		this.SHAPE.attribPosition(name, x, y, z);
		return this;
	}

	public NerdShape beginContour() {
		this.SHAPE.beginContour();
		return this;
	}

	// protected NerdShape beginContourImpl() {
	// this.SHAPE.beginContourImpl();
	// return this;
	// }

	public NerdShape beginShape() {
		this.SHAPE.beginShape();
		return this;
	}

	public NerdShape beginShape(final int kind) {
		this.SHAPE.beginShape(kind);
		return this;
	}

	public NerdShape bezierDetail(final int detail) {
		this.SHAPE.bezierDetail(detail);
		return this;
	}

	public NerdShape bezierVertex(final float x2, final float y2, final float x3, final float y3, final float x4,
			final float y4) {
		this.SHAPE.bezierVertex(x2, y2, x3, y3, x4, y4);
		return this;
	}

	public NerdShape bezierVertex(final float x2, final float y2, final float z2, final float x3, final float y3,
			final float z3, final float x4, final float y4, final float z4) {
		this.SHAPE.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
		return this;
	}

	// protected NerdShape checkMatrix(final int dimensions) {
	// this.SHAPE.checkMatrix(dimensions);
	// return this;
	// }

	// protected NerdShape colorCalc(final int rgb) {
	// this.SHAPE.colorCalc(rgb);
	// return this;
	// }

	// protected NerdShape colorCalc(final float gray) {
	// this.SHAPE.colorCalc(gray);
	// return this;
	// }

	// protected NerdShape colorCalc(final int rgb, final float alpha) {
	// this.SHAPE.colorCalc(rgb, alpha);
	// return this;
	// }

	// protected NerdShape colorCalc(final float gray, final float alpha) {
	// this.SHAPE.colorCalc(gray, alpha);
	// return this;
	// }

	// protected NerdShape colorCalc(final float x, final float y, final float z) {
	// this.SHAPE.colorCalc(x, y, z);
	// return this;
	// }

	// protected NerdShape colorCalc(final float x, final float y, final float z,
	// final float a) {
	// this.SHAPE.colorCalc(x, y, z, a);
	// return this;
	// }

	// protected NerdShape colorCalcARGB(final int argb, final float alpha) {
	// this.SHAPE.colorCalcARGB(argb, alpha);
	// return this;
	// }

	public NerdShape colorMode(final int mode) {
		this.SHAPE.colorMode(mode);
		return this;
	}

	public NerdShape colorMode(final int mode, final float max) {
		this.SHAPE.colorMode(mode, max);
		return this;
	}

	public NerdShape colorMode(final int mode, final float maxX, final float maxY, final float maxZ) {
		this.SHAPE.colorMode(mode, maxX, maxY, maxZ);
		return this;
	}

	public NerdShape colorMode(final int mode, final float maxX, final float maxY, final float maxZ, final float maxA) {
		this.SHAPE.colorMode(mode, maxX, maxY, maxZ, maxA);
		return this;
	}

	public boolean contains(final float x, final float y) {
		return this.SHAPE.contains(x, y);
	}

	// protected NerdShape crop() {
	// this.SHAPE.crop();
	// return this;
	// }

	public NerdShape curveDetail(final int detail) {
		this.SHAPE.curveDetail(detail);
		return this;
	}

	public NerdShape curveTightness(final float tightness) {
		this.SHAPE.curveTightness(tightness);
		return this;
	}

	public NerdShape curveVertex(final float x, final float y) {
		this.SHAPE.curveVertex(x, y);
		return this;
	}

	public NerdShape curveVertex(final float x, final float y, final float z) {
		this.SHAPE.curveVertex(x, y, z);
		return this;
	}

	public NerdShape disableStyle() {
		this.SHAPE.disableStyle();
		return this;
	}

	public NerdShape draw(final PGraphics g) {
		this.SHAPE.draw(g);
		return this;
	}

	// protected NerdShape drawGeometry(final PGraphics g) {
	// this.SHAPE.drawGeometry(g);
	// return this;
	// }

	// protected NerdShape drawGroup(final PGraphics g) {
	// this.SHAPE.drawGroup(g);
	// return this;
	// }

	// protected NerdShape drawImpl(final PGraphics g) {
	// this.SHAPE.drawImpl(g);
	// return this;
	// }

	// protected NerdShape drawPath(final PGraphics g) {
	// this.SHAPE.drawPath(g);
	// return this;
	// }

	// protected NerdShape drawPrimitive(final PGraphics g) {
	// this.SHAPE.drawPrimitive(g);
	// return this;
	// }

	public NerdShape emissive(final int rgb) {
		this.SHAPE.emissive(rgb);
		return this;
	}

	public NerdShape emissive(final float gray) {
		this.SHAPE.emissive(gray);
		return this;
	}

	public NerdShape emissive(final float x, final float y, final float z) {
		this.SHAPE.emissive(x, y, z);
		return this;
	}

	public NerdShape enableStyle() {
		this.SHAPE.enableStyle();
		return this;
	}

	public NerdShape endContour() {
		this.SHAPE.endContour();
		return this;
	}

	// protected NerdShape endContourImpl() {
	// this.SHAPE.endContourImpl();
	// return this;
	// }

	public NerdShape endShape() {
		this.SHAPE.endShape();
		return this;
	}

	public NerdShape endShape(final int mode) {
		this.SHAPE.endShape(mode);
		return this;
	}

	public NerdShape fill(final int rgb) {
		this.SHAPE.fill(rgb);
		return this;
	}

	public NerdShape fill(final float gray) {
		this.SHAPE.fill(gray);
		return this;
	}

	public NerdShape fill(final int rgb, final float alpha) {
		this.SHAPE.fill(rgb, alpha);
		return this;
	}

	public NerdShape fill(final float gray, final float alpha) {
		this.SHAPE.fill(gray, alpha);
		return this;
	}

	public NerdShape fill(final float x, final float y, final float z) {
		this.SHAPE.fill(x, y, z);
		return this;
	}

	public NerdShape fill(final float x, final float y, final float z, final float a) {
		this.SHAPE.fill(x, y, z, a);
		return this;
	}

	public PShape findChild(final String target) {
		return this.SHAPE.findChild(target);
	}

	public int getAmbient(final int index) {
		return this.SHAPE.getAmbient(index);
	}

	public PShape getChild(final int index) {
		return this.SHAPE.getChild(index);
	}

	public PShape getChild(final String target) {
		return this.SHAPE.getChild(target);
	}

	public int getChildCount() {
		return this.SHAPE.getChildCount();
	}

	public int getChildIndex(final PShape who) {
		return this.SHAPE.getChildIndex(who);
	}

	public PShape[] getChildren() {
		return this.SHAPE.getChildren();
	}

	public float getDepth() {
		return this.SHAPE.getDepth();
	}

	public int getEmissive(final int index) {
		return this.SHAPE.getEmissive(index);
	}

	public int getFamily() {
		return this.SHAPE.getFamily();
	}

	public int getFill(final int index) {
		return this.SHAPE.getFill(index);
	}

	public float getHeight() {
		return this.SHAPE.getHeight();
	}

	public int getKind() {
		return this.SHAPE.getKind();
	}

	public String getName() {
		return this.SHAPE.getName();
	}

	public PVector getNormal(final int index) {
		return this.SHAPE.getNormal(index);
	}

	public PVector getNormal(final int index, final PVector vec) {
		return this.SHAPE.getNormal(index, vec);
	}

	public float getNormalX(final int index) {
		return this.SHAPE.getNormalX(index);
	}

	public float getNormalY(final int index) {
		return this.SHAPE.getNormalY(index);
	}

	public float getNormalZ(final int index) {
		return this.SHAPE.getNormalZ(index);
	}

	public float getParam(final int index) {
		return this.SHAPE.getParam(index);
	}

	public float[] getParams() {
		return this.SHAPE.getParams();
	}

	public float[] getParams(final float[] target) {
		return this.SHAPE.getParams(target);
	}

	public PShape getParent() {
		return this.SHAPE.getParent();
	}

	public float getShininess(final int index) {
		return this.SHAPE.getShininess(index);
	}

	public int getSpecular(final int index) {
		return this.SHAPE.getSpecular(index);
	}

	public int getStroke(final int index) {
		return this.SHAPE.getStroke(index);
	}

	public float getStrokeWeight(final int index) {
		return this.SHAPE.getStrokeWeight(index);
	}

	public PShape getTessellation() {
		return this.SHAPE.getTessellation();
	}

	public float getTextureU(final int index) {
		return this.SHAPE.getTextureU(index);
	}

	public float getTextureV(final int index) {
		return this.SHAPE.getTextureV(index);
	}

	public int getTint(final int index) {
		return this.SHAPE.getTint(index);
	}

	public PVector getVertex(final int index) {
		return this.SHAPE.getVertex(index);
	}

	public PVector getVertex(final int index, final PVector vec) {
		return this.SHAPE.getVertex(index, vec);
	}

	public int getVertexCode(final int index) {
		return this.SHAPE.getVertexCode(index);
	}

	public int getVertexCodeCount() {
		return this.SHAPE.getVertexCodeCount();
	}

	public int[] getVertexCodes() {
		return this.SHAPE.getVertexCodes();
	}

	public int getVertexCount() {
		return this.SHAPE.getVertexCount();
	}

	public float getVertexX(final int index) {
		return this.SHAPE.getVertexX(index);
	}

	public float getVertexY(final int index) {
		return this.SHAPE.getVertexY(index);
	}

	public float getVertexZ(final int index) {
		return this.SHAPE.getVertexZ(index);
	}

	public float getWidth() {
		return this.SHAPE.getWidth();
	}

	public boolean is2D() {
		return this.SHAPE.is2D();
	}

	public boolean is3D() {
		return this.SHAPE.is3D();
	}

	public boolean isClosed() {
		return this.SHAPE.isClosed();
	}

	public boolean isVisible() {
		return this.SHAPE.isVisible();
	}

	public NerdShape noFill() {
		this.SHAPE.noFill();
		return this;
	}

	public NerdShape noStroke() {
		this.SHAPE.noStroke();
		return this;
	}

	public NerdShape noTexture() {
		this.SHAPE.noTexture();
		return this;
	}

	public NerdShape noTint() {
		this.SHAPE.noTint();
		return this;
	}

	public NerdShape normal(final float nx, final float ny, final float nz) {
		this.SHAPE.normal(nx, ny, nz);
		return this;
	}

	// TODO: Thinkity-think!

	protected NerdShape post(final PGraphics g) {
		// this.SHAPE.post(g);
		return this;
	}

	protected NerdShape pre(final PGraphics g) {
		// this.SHAPE.pre(g);
		return this;
	}

	public NerdShape quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
		this.SHAPE.quadraticVertex(cx, cy, x3, y3);
		return this;
	}

	public NerdShape quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3,
			final float z3) {
		this.SHAPE.quadraticVertex(cx, cy, cz, x3, y3, z3);
		return this;
	}

	public NerdShape removeChild(final int idx) {
		this.SHAPE.removeChild(idx);
		return this;
	}

	public NerdShape resetMatrix() {
		this.SHAPE.resetMatrix();
		return this;
	}

	public NerdShape rotate(final float angle) {
		this.SHAPE.rotate(angle);
		return this;
	}

	public NerdShape rotate(final float angle, final float v0, final float v1, final float v2) {
		this.SHAPE.rotate(angle, v0, v1, v2);
		return this;
	}

	public NerdShape rotateX(final float angle) {
		this.SHAPE.rotateX(angle);
		return this;
	}

	public NerdShape rotateY(final float angle) {
		this.SHAPE.rotateY(angle);
		return this;
	}

	public NerdShape rotateZ(final float angle) {
		this.SHAPE.rotateZ(angle);
		return this;
	}

	public NerdShape scale(final float s) {
		this.SHAPE.scale(s);
		return this;
	}

	public NerdShape scale(final float x, final float y) {
		this.SHAPE.scale(x, y);
		return this;
	}

	public NerdShape scale(final float x, final float y, final float z) {
		this.SHAPE.scale(x, y, z);
		return this;
	}

	public NerdShape set3D(final boolean val) {
		this.SHAPE.set3D(val);
		return this;
	}

	public NerdShape setAmbient(final int ambient) {
		this.SHAPE.setAmbient(ambient);
		return this;
	}

	public NerdShape setAmbient(final int index, final int ambient) {
		this.SHAPE.setAmbient(index, ambient);
		return this;
	}

	public NerdShape setAttrib(final String name, final int index, final float... values) {
		this.SHAPE.setAttrib(name, index, values);
		return this;
	}

	public NerdShape setAttrib(final String name, final int index, final int... values) {
		this.SHAPE.setAttrib(name, index, values);
		return this;
	}

	public NerdShape setAttrib(final String name, final int index, final boolean... values) {
		this.SHAPE.setAttrib(name, index, values);
		return this;
	}

	public NerdShape setEmissive(final int emissive) {
		this.SHAPE.setEmissive(emissive);
		return this;
	}

	public NerdShape setEmissive(final int index, final int emissive) {
		this.SHAPE.setEmissive(index, emissive);
		return this;
	}

	public NerdShape setFamily(final int family) {
		this.SHAPE.setFamily(family);
		return this;
	}

	public NerdShape setFill(final boolean fill) {
		this.SHAPE.setFill(fill);
		return this;
	}

	public NerdShape setFill(final int fill) {
		this.SHAPE.setFill(fill);
		return this;
	}

	public NerdShape setFill(final int index, final int fill) {
		this.SHAPE.setFill(index, fill);
		return this;
	}

	public NerdShape setKind(final int kind) {
		this.SHAPE.setKind(kind);
		return this;
	}

	public NerdShape setName(final String name) {
		this.SHAPE.setName(name);
		return this;
	}

	public NerdShape setNormal(final int index, final float nx, final float ny, final float nz) {
		this.SHAPE.setNormal(index, nx, ny, nz);
		return this;
	}

	// protected NerdShape setParams(final float[] source) {
	// this.SHAPE.setParams(source);
	// return this;
	// }

	public NerdShape setPath(final int vcount, final float[][] verts) {
		this.SHAPE.setPath(vcount, verts);
		return this;
	}

	// protected NerdShape setPath(final int vcount, final float[][] verts, final
	// int ccount, final int[] codes) {
	// this.SHAPE.setPath(vcount, verts, ccount, codes);
	// return this;
	// }

	public NerdShape setShininess(final float shine) {
		this.SHAPE.setShininess(shine);
		return this;
	}

	public NerdShape setShininess(final int index, final float shine) {
		this.SHAPE.setShininess(index, shine);
		return this;
	}

	public NerdShape setSpecular(final int specular) {
		this.SHAPE.setSpecular(specular);
		return this;
	}

	public NerdShape setSpecular(final int index, final int specular) {
		this.SHAPE.setSpecular(index, specular);
		return this;
	}

	public NerdShape setStroke(final boolean stroke) {
		this.SHAPE.setStroke(stroke);
		return this;
	}

	public NerdShape setStroke(final int stroke) {
		this.SHAPE.setStroke(stroke);
		return this;
	}

	public NerdShape setStroke(final int index, final int stroke) {
		this.SHAPE.setStroke(index, stroke);
		return this;
	}

	public NerdShape setStrokeCap(final int cap) {
		this.SHAPE.setStrokeCap(cap);
		return this;
	}

	public NerdShape setStrokeJoin(final int join) {
		this.SHAPE.setStrokeJoin(join);
		return this;
	}

	public NerdShape setStrokeWeight(final float weight) {
		this.SHAPE.setStrokeWeight(weight);
		return this;
	}

	public NerdShape setStrokeWeight(final int index, final float weight) {
		this.SHAPE.setStrokeWeight(index, weight);
		return this;
	}

	public NerdShape setTexture(final PImage tex) {
		this.SHAPE.setTexture(tex);
		return this;
	}

	public NerdShape setTextureMode(final int mode) {
		this.SHAPE.setTextureMode(mode);
		return this;
	}

	public NerdShape setTextureUV(final int index, final float u, final float v) {
		this.SHAPE.setTextureUV(index, u, v);
		return this;
	}

	public NerdShape setTint(final boolean tint) {
		this.SHAPE.setTint(tint);
		return this;
	}

	public NerdShape setTint(final int fill) {
		this.SHAPE.setTint(fill);
		return this;
	}

	public NerdShape setTint(final int index, final int tint) {
		this.SHAPE.setTint(index, tint);
		return this;
	}

	public NerdShape setVertex(final int index, final PVector vec) {
		this.SHAPE.setVertex(index, vec);
		return this;
	}

	public NerdShape setVertex(final int index, final float x, final float y) {
		this.SHAPE.setVertex(index, x, y);
		return this;
	}

	public NerdShape setVertex(final int index, final float x, final float y, final float z) {
		this.SHAPE.setVertex(index, x, y, z);
		return this;
	}

	public NerdShape setVisible(final boolean visible) {
		this.SHAPE.setVisible(visible);
		return this;
	}

	public NerdShape shininess(final float shine) {
		this.SHAPE.shininess(shine);
		return this;
	}

	// protected NerdShape solid(final boolean solid) {
	// this.SHAPE.solid(solid);
	// return this;
	// }

	public NerdShape specular(final int rgb) {
		this.SHAPE.specular(rgb);
		return this;
	}

	public NerdShape specular(final float gray) {
		this.SHAPE.specular(gray);
		return this;
	}

	public NerdShape specular(final float x, final float y, final float z) {
		this.SHAPE.specular(x, y, z);
		return this;
	}

	public NerdShape stroke(final int rgb) {
		this.SHAPE.stroke(rgb);
		return this;
	}

	public NerdShape stroke(final float gray) {
		this.SHAPE.stroke(gray);
		return this;
	}

	public NerdShape stroke(final int rgb, final float alpha) {
		this.SHAPE.stroke(rgb, alpha);
		return this;
	}

	public NerdShape stroke(final float gray, final float alpha) {
		this.SHAPE.stroke(gray, alpha);
		return this;
	}

	public NerdShape stroke(final float x, final float y, final float z) {
		this.SHAPE.stroke(x, y, z);
		return this;
	}

	public NerdShape stroke(final float x, final float y, final float z, final float alpha) {
		this.SHAPE.stroke(x, y, z, alpha);
		return this;
	}

	public NerdShape strokeCap(final int cap) {
		this.SHAPE.strokeCap(cap);
		return this;
	}

	public NerdShape strokeJoin(final int join) {
		this.SHAPE.strokeJoin(join);
		return this;
	}

	public NerdShape strokeWeight(final float weight) {
		this.SHAPE.strokeWeight(weight);
		return this;
	}

	// protected NerdShape styles(final PGraphics g) {
	// this.SHAPE.styles(g);
	// return this;
	// }

	public NerdShape texture(final PImage tex) {
		this.SHAPE.texture(tex);
		return this;
	}

	public NerdShape textureMode(final int mode) {
		this.SHAPE.textureMode(mode);
		return this;
	}

	public NerdShape tint(final int rgb) {
		this.SHAPE.tint(rgb);
		return this;
	}

	public NerdShape tint(final float gray) {
		this.SHAPE.tint(gray);
		return this;
	}

	public NerdShape tint(final int rgb, final float alpha) {
		this.SHAPE.tint(rgb, alpha);
		return this;
	}

	public NerdShape tint(final float gray, final float alpha) {
		this.SHAPE.tint(gray, alpha);
		return this;
	}

	public NerdShape tint(final float x, final float y, final float z) {
		this.SHAPE.tint(x, y, z);
		return this;
	}

	public NerdShape tint(final float x, final float y, final float z, final float alpha) {
		this.SHAPE.tint(x, y, z, alpha);
		return this;
	}

	public NerdShape translate(final float x, final float y) {
		this.SHAPE.translate(x, y);
		return this;
	}

	public NerdShape translate(final float x, final float y, final float z) {
		this.SHAPE.translate(x, y, z);
		return this;
	}

	public NerdShape vertex(final float x, final float y) {
		this.SHAPE.vertex(x, y);
		return this;
	}

	public NerdShape vertex(final float x, final float y, final float z) {
		this.SHAPE.vertex(x, y, z);
		return this;
	}

	public NerdShape vertex(final float x, final float y, final float u, final float v) {
		this.SHAPE.vertex(x, y, u, v);
		return this;
	}

	public NerdShape vertex(final float x, final float y, final float z, final float u, final float v) {
		this.SHAPE.vertex(x, y, z, u, v);
		return this;
	}
	// endregion

}
