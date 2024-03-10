package com.brahvim.nerd.framework.shapes;

import com.brahvim.nerd.processing_wrapper.NerdShape;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PShapeSVG;
import processing.core.PVector;

public class NerdSvgShape extends NerdShape {

    final PShapeSVG SVG_SHAPE;

    public NerdSvgShape(final PShape p_shape) {
        super(p_shape);
        this.SVG_SHAPE = (PShapeSVG) p_shape;
    }

    // region `PShapeSVG` methods.
    @Override
    public NerdSvgShape addChild(final PShape who) {
        this.SVG_SHAPE.addChild(who);
        return this;
    }

    @Override
    public NerdSvgShape addChild(final PShape who, final int idx) {
        this.SVG_SHAPE.addChild(who, idx);
        return this;
    }

    @Override
    public NerdSvgShape addName(final String nom, final PShape shape) {
        this.SVG_SHAPE.addName(nom, shape);
        return this;
    }

    @Override
    public NerdSvgShape ambient(final int rgb) {
        this.SVG_SHAPE.ambient(rgb);
        return this;
    }

    @Override
    public NerdSvgShape ambient(final float gray) {
        this.SVG_SHAPE.ambient(gray);
        return this;
    }

    @Override
    public NerdSvgShape ambient(final float x, final float y, final float z) {
        this.SVG_SHAPE.ambient(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape applyMatrix(final PMatrix source) {
        this.SVG_SHAPE.applyMatrix(source);
        return this;
    }

    @Override
    public NerdSvgShape applyMatrix(final PMatrix2D source) {
        this.SVG_SHAPE.applyMatrix(source);
        return this;
    }

    @Override
    public NerdSvgShape applyMatrix(final PMatrix3D source) {
        this.SVG_SHAPE.applyMatrix(source);
        return this;
    }

    @Override
    public NerdSvgShape applyMatrix(final float n00, final float n01, final float n02, final float n10,
            final float n11,
            final float n12) {
        this.SVG_SHAPE.applyMatrix(n00, n01, n02, n10, n11, n12);
        return this;
    }

    // @Override
    // public NerdSvgPShape applyMatrix(final float n00, final float n01, final
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
    public NerdSvgShape attrib(final String name, final float... values) {
        this.SVG_SHAPE.attrib(name, values);
        return this;
    }

    @Override
    public NerdSvgShape attrib(final String name, final int... values) {
        this.SVG_SHAPE.attrib(name, values);
        return this;
    }

    @Override
    public NerdSvgShape attrib(final String name, final boolean... values) {
        this.SVG_SHAPE.attrib(name, values);
        return this;
    }

    @Override
    public NerdSvgShape attribColor(final String name, final int color) {
        this.SVG_SHAPE.attribColor(name, color);
        return this;
    }

    @Override
    public NerdSvgShape attribNormal(final String name, final float nx, final float ny, final float nz) {
        this.SVG_SHAPE.attribNormal(name, nx, ny, nz);
        return this;
    }

    @Override
    public NerdSvgShape attribPosition(final String name, final float x, final float y, final float z) {
        this.SVG_SHAPE.attribPosition(name, x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape beginContour() {
        this.SVG_SHAPE.beginContour();
        return this;
    }

    // @Override
    // protected NerdSvgPShape beginContourImpl() {
    // this.SHAPE.beginContourImpl();
    // return this;
    // }

    @Override
    public NerdSvgShape beginShape() {
        this.SVG_SHAPE.beginShape();
        return this;
    }

    @Override
    public NerdSvgShape beginShape(final int kind) {
        this.SVG_SHAPE.beginShape(kind);
        return this;
    }

    @Override
    public NerdSvgShape bezierDetail(final int detail) {
        this.SVG_SHAPE.bezierDetail(detail);
        return this;
    }

    // @Override
    // public NerdSvgPShape bezierVertex(final float x2, final float y2, final float
    // x3, final float y3, final float x4,
    // final float y4) {
    // return this;
    // this.SHAPE.bezierVertex(x2, y2, x3, y3, x4, y4);
    // }

    // @Override
    // public NerdSvgPShape bezierVertex(final float x2, final float y2, final float
    // z2, final float x3, final float y3,
    // final float z3, final float x4, final float y4, final float z4) {
    // return this;
    // this.SHAPE.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    // }

    // @Override
    // protected NerdSvgPShape checkMatrix(final int dimensions) {
    // this.SHAPE.checkMatrix(dimensions);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final int rgb) {
    // this.SHAPE.colorCalc(rgb);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final float gray) {
    // this.SHAPE.colorCalc(gray);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final int rgb, final float alpha) {
    // this.SHAPE.colorCalc(rgb, alpha);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final float gray, final float alpha) {
    // this.SHAPE.colorCalc(gray, alpha);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final float x, final float y, final float
    // z) {
    // this.SHAPE.colorCalc(x, y, z);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalc(final float x, final float y, final float
    // z, final float a) {
    // this.SHAPE.colorCalc(x, y, z, a);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape colorCalcARGB(final int argb, final float alpha) {
    // this.SHAPE.colorCalcARGB(argb, alpha);
    // return this;
    // }

    @Override
    public NerdSvgShape colorMode(final int mode) {
        this.SVG_SHAPE.colorMode(mode);
        return this;
    }

    @Override
    public NerdSvgShape colorMode(final int mode, final float max) {
        this.SVG_SHAPE.colorMode(mode, max);
        return this;
    }

    @Override
    public NerdSvgShape colorMode(final int mode, final float maxX, final float maxY, final float maxZ) {
        this.SVG_SHAPE.colorMode(mode, maxX, maxY, maxZ);
        return this;
    }

    @Override
    public NerdSvgShape colorMode(final int mode, final float maxX, final float maxY, final float maxZ,
            final float maxA) {
        this.SVG_SHAPE.colorMode(mode, maxX, maxY, maxZ, maxA);
        return this;
    }

    @Override
    public boolean contains(final float x, final float y) {
        return this.SVG_SHAPE.contains(x, y);
    }

    // @Override
    // protected NerdSvgPShape crop() {
    // this.SHAPE.crop();
    // return this;
    // }

    @Override
    public NerdSvgShape curveDetail(final int detail) {
        this.SVG_SHAPE.curveDetail(detail);
        return this;
    }

    @Override
    public NerdSvgShape curveTightness(final float tightness) {
        this.SVG_SHAPE.curveTightness(tightness);
        return this;
    }

    @Override
    public NerdSvgShape curveVertex(final float x, final float y) {
        this.SVG_SHAPE.curveVertex(x, y);
        return this;
    }

    @Override
    public NerdSvgShape curveVertex(final float x, final float y, final float z) {
        this.SVG_SHAPE.curveVertex(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape disableStyle() {
        this.SVG_SHAPE.disableStyle();
        return this;
    }

    @Override
    public NerdSvgShape draw(final PGraphics g) {
        this.SVG_SHAPE.draw(g);
        return this;
    }

    // @Override
    // protected NerdSvgPShape drawGeometry(final PGraphics g) {
    // this.SHAPE.drawGeometry(g);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape drawGroup(final PGraphics g) {
    // this.SHAPE.drawGroup(g);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape drawImpl(final PGraphics g) {
    // this.SHAPE.drawImpl(g);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape drawPath(final PGraphics g) {
    // this.SHAPE.drawPath(g);
    // return this;
    // }

    // @Override
    // protected NerdSvgPShape drawPrimitive(final PGraphics g) {
    // this.SHAPE.drawPrimitive(g);
    // return this;
    // }

    @Override
    public NerdSvgShape emissive(final int rgb) {
        this.SVG_SHAPE.emissive(rgb);
        return this;
    }

    @Override
    public NerdSvgShape emissive(final float gray) {
        this.SVG_SHAPE.emissive(gray);
        return this;
    }

    @Override
    public NerdSvgShape emissive(final float x, final float y, final float z) {
        this.SVG_SHAPE.emissive(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape enableStyle() {
        this.SVG_SHAPE.enableStyle();
        return this;
    }

    @Override
    public NerdSvgShape endContour() {
        this.SVG_SHAPE.endContour();
        return this;
    }

    // @Override
    // protected NerdSvgPShape endContourImpl() {
    // this.SHAPE.endContourImpl();
    // return this;
    // }

    @Override
    public NerdSvgShape endShape() {
        this.SVG_SHAPE.endShape();
        return this;
    }

    @Override
    public NerdSvgShape endShape(final int mode) {
        this.SVG_SHAPE.endShape(mode);
        return this;
    }

    @Override
    public NerdSvgShape fill(final int rgb) {
        this.SVG_SHAPE.fill(rgb);
        return this;
    }

    @Override
    public NerdSvgShape fill(final float gray) {
        this.SVG_SHAPE.fill(gray);
        return this;
    }

    @Override
    public NerdSvgShape fill(final int rgb, final float alpha) {
        this.SVG_SHAPE.fill(rgb, alpha);
        return this;
    }

    @Override
    public NerdSvgShape fill(final float gray, final float alpha) {
        this.SVG_SHAPE.fill(gray, alpha);
        return this;
    }

    @Override
    public NerdSvgShape fill(final float x, final float y, final float z) {
        this.SVG_SHAPE.fill(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape fill(final float x, final float y, final float z, final float a) {
        this.SVG_SHAPE.fill(x, y, z, a);
        return this;
    }

    @Override
    public PShape findChild(final String target) {
        return this.SVG_SHAPE.findChild(target);
    }

    @Override
    public int getAmbient(final int index) {
        return this.SVG_SHAPE.getAmbient(index);
    }

    @Override
    public PShape getChild(final int index) {
        return this.SVG_SHAPE.getChild(index);
    }

    @Override
    public PShape getChild(final String target) {
        return this.SVG_SHAPE.getChild(target);
    }

    @Override
    public int getChildCount() {
        return this.SVG_SHAPE.getChildCount();
    }

    @Override
    public int getChildIndex(final PShape who) {
        return this.SVG_SHAPE.getChildIndex(who);
    }

    @Override
    public PShape[] getChildren() {
        return this.SVG_SHAPE.getChildren();
    }

    @Override
    public float getDepth() {
        return this.SVG_SHAPE.getDepth();
    }

    @Override
    public int getEmissive(final int index) {
        return this.SVG_SHAPE.getEmissive(index);
    }

    @Override
    public int getFamily() {
        return this.SVG_SHAPE.getFamily();
    }

    @Override
    public int getFill(final int index) {
        return this.SVG_SHAPE.getFill(index);
    }

    @Override
    public float getHeight() {
        return this.SVG_SHAPE.getHeight();
    }

    @Override
    public int getKind() {
        return this.SVG_SHAPE.getKind();
    }

    @Override
    public String getName() {
        return this.SVG_SHAPE.getName();
    }

    @Override
    public PVector getNormal(final int index) {
        return this.SVG_SHAPE.getNormal(index);
    }

    @Override
    public PVector getNormal(final int index, final PVector vec) {
        return this.SVG_SHAPE.getNormal(index, vec);
    }

    @Override
    public float getNormalX(final int index) {
        return this.SVG_SHAPE.getNormalX(index);
    }

    @Override
    public float getNormalY(final int index) {
        return this.SVG_SHAPE.getNormalY(index);
    }

    @Override
    public float getNormalZ(final int index) {
        return this.SVG_SHAPE.getNormalZ(index);
    }

    @Override
    public float getParam(final int index) {
        return this.SVG_SHAPE.getParam(index);
    }

    @Override
    public float[] getParams() {
        return this.SVG_SHAPE.getParams();
    }

    @Override
    public float[] getParams(final float[] target) {
        return this.SVG_SHAPE.getParams(target);
    }

    @Override
    public PShape getParent() {
        return this.SVG_SHAPE.getParent();
    }

    @Override
    public float getShininess(final int index) {
        return this.SVG_SHAPE.getShininess(index);
    }

    @Override
    public int getSpecular(final int index) {
        return this.SVG_SHAPE.getSpecular(index);
    }

    @Override
    public int getStroke(final int index) {
        return this.SVG_SHAPE.getStroke(index);
    }

    @Override
    public float getStrokeWeight(final int index) {
        return this.SVG_SHAPE.getStrokeWeight(index);
    }

    @Override
    public PShape getTessellation() {
        return this.SVG_SHAPE.getTessellation();
    }

    @Override
    public float getTextureU(final int index) {
        return this.SVG_SHAPE.getTextureU(index);
    }

    @Override
    public float getTextureV(final int index) {
        return this.SVG_SHAPE.getTextureV(index);
    }

    @Override
    public int getTint(final int index) {
        return this.SVG_SHAPE.getTint(index);
    }

    @Override
    public PVector getVertex(final int index) {
        return this.SVG_SHAPE.getVertex(index);
    }

    @Override
    public PVector getVertex(final int index, final PVector vec) {
        return this.SVG_SHAPE.getVertex(index, vec);
    }

    @Override
    public int getVertexCode(final int index) {
        return this.SVG_SHAPE.getVertexCode(index);
    }

    @Override
    public int getVertexCodeCount() {
        return this.SVG_SHAPE.getVertexCodeCount();
    }

    @Override
    public int[] getVertexCodes() {
        return this.SVG_SHAPE.getVertexCodes();
    }

    @Override
    public int getVertexCount() {
        return this.SVG_SHAPE.getVertexCount();
    }

    @Override
    public float getVertexX(final int index) {
        return this.SVG_SHAPE.getVertexX(index);
    }

    @Override
    public float getVertexY(final int index) {
        return this.SVG_SHAPE.getVertexY(index);
    }

    @Override
    public float getVertexZ(final int index) {
        return this.SVG_SHAPE.getVertexZ(index);
    }

    @Override
    public float getWidth() {
        return this.SVG_SHAPE.getWidth();
    }

    @Override
    public boolean is2D() {
        return this.SVG_SHAPE.is2D();
    }

    @Override
    public boolean is3D() {
        return this.SVG_SHAPE.is3D();
    }

    @Override
    public boolean isClosed() {
        return this.SVG_SHAPE.isClosed();
    }

    @Override
    public boolean isVisible() {
        return this.SVG_SHAPE.isVisible();
    }

    @Override
    public NerdSvgShape noFill() {
        this.SVG_SHAPE.noFill();
        return this;
    }

    @Override
    public NerdSvgShape noStroke() {
        this.SVG_SHAPE.noStroke();
        return this;
    }

    @Override
    public NerdSvgShape noTexture() {
        this.SVG_SHAPE.noTexture();
        return this;
    }

    @Override
    public NerdSvgShape noTint() {
        this.SVG_SHAPE.noTint();
        return this;
    }

    @Override
    public NerdSvgShape normal(final float nx, final float ny, final float nz) {
        this.SVG_SHAPE.normal(nx, ny, nz);
        return this;
    }

    @Override
    protected NerdSvgShape post(final PGraphics g) {
        // this.SHAPE.post(g);
        return this;
    }

    @Override
    protected NerdSvgShape pre(final PGraphics g) {
        // this.SHAPE.pre(g);
        return this;
    }

    @Override
    public NerdSvgShape quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
        this.SVG_SHAPE.quadraticVertex(cx, cy, x3, y3);
        return this;
    }

    @Override
    public NerdSvgShape quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3,
            final float z3) {
        this.SVG_SHAPE.quadraticVertex(cx, cy, cz, x3, y3, z3);
        return this;
    }

    @Override
    public NerdSvgShape removeChild(final int idx) {
        this.SVG_SHAPE.removeChild(idx);
        return this;
    }

    @Override
    public NerdSvgShape resetMatrix() {
        this.SVG_SHAPE.resetMatrix();
        return this;
    }

    @Override
    public NerdSvgShape rotate(final float angle) {
        this.SVG_SHAPE.rotate(angle);
        return this;
    }

    @Override
    public NerdSvgShape rotate(final float angle, final float v0, final float v1, final float v2) {
        this.SVG_SHAPE.rotate(angle, v0, v1, v2);
        return this;
    }

    @Override
    public NerdSvgShape rotateX(final float angle) {
        this.SVG_SHAPE.rotateX(angle);
        return this;
    }

    @Override
    public NerdSvgShape rotateY(final float angle) {
        this.SVG_SHAPE.rotateY(angle);
        return this;
    }

    @Override
    public NerdSvgShape rotateZ(final float angle) {
        this.SVG_SHAPE.rotateZ(angle);
        return this;
    }

    @Override
    public NerdSvgShape scale(final float s) {
        this.SVG_SHAPE.scale(s);
        return this;
    }

    @Override
    public NerdSvgShape scale(final float x, final float y) {
        this.SVG_SHAPE.scale(x, y);
        return this;
    }

    @Override
    public NerdSvgShape scale(final float x, final float y, final float z) {
        this.SVG_SHAPE.scale(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape set3D(final boolean val) {
        this.SVG_SHAPE.set3D(val);
        return this;
    }

    @Override
    public NerdSvgShape setAmbient(final int ambient) {
        this.SVG_SHAPE.setAmbient(ambient);
        return this;
    }

    @Override
    public NerdSvgShape setAmbient(final int index, final int ambient) {
        this.SVG_SHAPE.setAmbient(index, ambient);
        return this;
    }

    @Override
    public NerdSvgShape setAttrib(final String name, final int index, final float... values) {
        this.SVG_SHAPE.setAttrib(name, index, values);
        return this;
    }

    @Override
    public NerdSvgShape setAttrib(final String name, final int index, final int... values) {
        this.SVG_SHAPE.setAttrib(name, index, values);
        return this;
    }

    @Override
    public NerdSvgShape setAttrib(final String name, final int index, final boolean... values) {
        this.SVG_SHAPE.setAttrib(name, index, values);
        return this;
    }

    @Override
    public NerdSvgShape setEmissive(final int emissive) {
        this.SVG_SHAPE.setEmissive(emissive);
        return this;
    }

    @Override
    public NerdSvgShape setEmissive(final int index, final int emissive) {
        this.SVG_SHAPE.setEmissive(index, emissive);
        return this;
    }

    @Override
    public NerdSvgShape setFamily(final int family) {
        this.SVG_SHAPE.setFamily(family);
        return this;
    }

    @Override
    public NerdSvgShape setFill(final boolean fill) {
        this.SVG_SHAPE.setFill(fill);
        return this;
    }

    @Override
    public NerdSvgShape setFill(final int fill) {
        this.SVG_SHAPE.setFill(fill);
        return this;
    }

    @Override
    public NerdSvgShape setFill(final int index, final int fill) {
        this.SVG_SHAPE.setFill(index, fill);
        return this;
    }

    @Override
    public NerdSvgShape setKind(final int kind) {
        this.SVG_SHAPE.setKind(kind);
        return this;
    }

    @Override
    public NerdSvgShape setName(final String name) {
        this.SVG_SHAPE.setName(name);
        return this;
    }

    @Override
    public NerdSvgShape setNormal(final int index, final float nx, final float ny, final float nz) {
        this.SVG_SHAPE.setNormal(index, nx, ny, nz);
        return this;
    }

    // @Override
    // protected NerdSvgPShape setParams(final float[] source) {
    // this.SHAPE.setParams(source);
    // return this;
    // }

    @Override
    public NerdSvgShape setPath(final int vcount, final float[][] verts) {
        this.SVG_SHAPE.setPath(vcount, verts);
        return this;
    }

    // @Override
    // protected NerdSvgPShape setPath(final int vcount, final float[][] verts,
    // final int ccount, final int[] codes) {
    // this.SHAPE.setPath(vcount, verts, ccount, codes);
    // return this;
    // }

    @Override
    public NerdSvgShape setShininess(final float shine) {
        this.SVG_SHAPE.setShininess(shine);
        return this;
    }

    @Override
    public NerdSvgShape setShininess(final int index, final float shine) {
        this.SVG_SHAPE.setShininess(index, shine);
        return this;
    }

    @Override
    public NerdSvgShape setSpecular(final int specular) {
        this.SVG_SHAPE.setSpecular(specular);
        return this;
    }

    @Override
    public NerdSvgShape setSpecular(final int index, final int specular) {
        this.SVG_SHAPE.setSpecular(index, specular);
        return this;
    }

    @Override
    public NerdSvgShape setStroke(final boolean stroke) {
        this.SVG_SHAPE.setStroke(stroke);
        return this;
    }

    @Override
    public NerdSvgShape setStroke(final int stroke) {
        this.SVG_SHAPE.setStroke(stroke);
        return this;
    }

    @Override
    public NerdSvgShape setStroke(final int index, final int stroke) {
        this.SVG_SHAPE.setStroke(index, stroke);
        return this;
    }

    @Override
    public NerdSvgShape setStrokeCap(final int cap) {
        this.SVG_SHAPE.setStrokeCap(cap);
        return this;
    }

    @Override
    public NerdSvgShape setStrokeJoin(final int join) {
        this.SVG_SHAPE.setStrokeJoin(join);
        return this;
    }

    @Override
    public NerdSvgShape setStrokeWeight(final float weight) {
        this.SVG_SHAPE.setStrokeWeight(weight);
        return this;
    }

    @Override
    public NerdSvgShape setStrokeWeight(final int index, final float weight) {
        this.SVG_SHAPE.setStrokeWeight(index, weight);
        return this;
    }

    @Override
    public NerdSvgShape setTexture(final PImage tex) {
        this.SVG_SHAPE.setTexture(tex);
        return this;
    }

    @Override
    public NerdSvgShape setTextureMode(final int mode) {
        this.SVG_SHAPE.setTextureMode(mode);
        return this;
    }

    @Override
    public NerdSvgShape setTextureUV(final int index, final float u, final float v) {
        this.SVG_SHAPE.setTextureUV(index, u, v);
        return this;
    }

    @Override
    public NerdSvgShape setTint(final boolean tint) {
        this.SVG_SHAPE.setTint(tint);
        return this;
    }

    @Override
    public NerdSvgShape setTint(final int fill) {
        this.SVG_SHAPE.setTint(fill);
        return this;
    }

    @Override
    public NerdSvgShape setTint(final int index, final int tint) {
        this.SVG_SHAPE.setTint(index, tint);
        return this;
    }

    @Override
    public NerdSvgShape setVertex(final int index, final PVector vec) {
        this.SVG_SHAPE.setVertex(index, vec);
        return this;
    }

    @Override
    public NerdSvgShape setVertex(final int index, final float x, final float y) {
        this.SVG_SHAPE.setVertex(index, x, y);
        return this;
    }

    @Override
    public NerdSvgShape setVertex(final int index, final float x, final float y, final float z) {
        this.SVG_SHAPE.setVertex(index, x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape setVisible(final boolean visible) {
        this.SVG_SHAPE.setVisible(visible);
        return this;
    }

    @Override
    public NerdSvgShape shininess(final float shine) {
        this.SVG_SHAPE.shininess(shine);
        return this;
    }

    // @Override
    // protected NerdSvgPShape solid(final boolean solid) {
    // this.SHAPE.solid(solid);
    // return this;
    // }

    @Override
    public NerdSvgShape specular(final int rgb) {
        this.SVG_SHAPE.specular(rgb);
        return this;
    }

    @Override
    public NerdSvgShape specular(final float gray) {
        this.SVG_SHAPE.specular(gray);
        return this;
    }

    @Override
    public NerdSvgShape specular(final float x, final float y, final float z) {
        this.SVG_SHAPE.specular(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final int rgb) {
        this.SVG_SHAPE.stroke(rgb);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final float gray) {
        this.SVG_SHAPE.stroke(gray);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final int rgb, final float alpha) {
        this.SVG_SHAPE.stroke(rgb, alpha);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final float gray, final float alpha) {
        this.SVG_SHAPE.stroke(gray, alpha);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final float x, final float y, final float z) {
        this.SVG_SHAPE.stroke(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape stroke(final float x, final float y, final float z, final float alpha) {
        this.SVG_SHAPE.stroke(x, y, z, alpha);
        return this;
    }

    @Override
    public NerdSvgShape strokeCap(final int cap) {
        this.SVG_SHAPE.strokeCap(cap);
        return this;
    }

    @Override
    public NerdSvgShape strokeJoin(final int join) {
        this.SVG_SHAPE.strokeJoin(join);
        return this;
    }

    @Override
    public NerdSvgShape strokeWeight(final float weight) {
        this.SVG_SHAPE.strokeWeight(weight);
        return this;
    }

    // @Override
    // protected NerdSvgPShape styles(final PGraphics g) {
    // this.SHAPE.styles(g);
    // return this;
    // }

    @Override
    public NerdSvgShape texture(final PImage tex) {
        this.SVG_SHAPE.texture(tex);
        return this;
    }

    @Override
    public NerdSvgShape textureMode(final int mode) {
        this.SVG_SHAPE.textureMode(mode);
        return this;
    }

    @Override
    public NerdSvgShape tint(final int rgb) {
        this.SVG_SHAPE.tint(rgb);
        return this;
    }

    @Override
    public NerdSvgShape tint(final float gray) {
        this.SVG_SHAPE.tint(gray);
        return this;
    }

    @Override
    public NerdSvgShape tint(final int rgb, final float alpha) {
        this.SVG_SHAPE.tint(rgb, alpha);
        return this;
    }

    @Override
    public NerdSvgShape tint(final float gray, final float alpha) {
        this.SVG_SHAPE.tint(gray, alpha);
        return this;
    }

    @Override
    public NerdSvgShape tint(final float x, final float y, final float z) {
        this.SVG_SHAPE.tint(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape tint(final float x, final float y, final float z, final float alpha) {
        this.SVG_SHAPE.tint(x, y, z, alpha);
        return this;
    }

    @Override
    public NerdSvgShape translate(final float x, final float y) {
        this.SVG_SHAPE.translate(x, y);
        return this;
    }

    @Override
    public NerdSvgShape translate(final float x, final float y, final float z) {
        this.SVG_SHAPE.translate(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape vertex(final float x, final float y) {
        this.SVG_SHAPE.vertex(x, y);
        return this;
    }

    @Override
    public NerdSvgShape vertex(final float x, final float y, final float z) {
        this.SVG_SHAPE.vertex(x, y, z);
        return this;
    }

    @Override
    public NerdSvgShape vertex(final float x, final float y, final float u, final float v) {
        this.SVG_SHAPE.vertex(x, y, u, v);
        return this;
    }

    @Override
    public NerdSvgShape vertex(final float x, final float y, final float z, final float u, final float v) {
        this.SVG_SHAPE.vertex(x, y, z, u, v);
        return this;
    }
    // endregion

}
