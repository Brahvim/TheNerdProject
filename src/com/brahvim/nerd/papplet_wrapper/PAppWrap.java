package com.brahvim.nerd.papplet_wrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PMatrix;
import processing.core.PMatrix2D;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.core.PStyle;
import processing.core.PSurface;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.data.Table;
import processing.data.XML;
import processing.event.Event;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;

public class PAppWrap {

    protected final PApplet applet;

    public PAppWrap() {
        this.applet = new PApplet();
    }

    public void ambient(final int rgb) {
        this.applet.ambient(rgb);
    }

    public void ambient(final float gray) {
        this.applet.ambient(gray);
    }

    public void ambient(final float v1, final float v2, final float v3) {
        this.applet.ambient(v1, v2, v3);
    }

    public void ambientLight(final float v1, final float v2, final float v3) {
        this.applet.ambientLight(v1, v2, v3);
    }

    public void ambientLight(final float v1, final float v2, final float v3, final float x, final float y,
            final float z) {
        this.applet.ambientLight(v1, v2, v3, x, y, z);
    }

    public void applyMatrix(final PMatrix source) {
        this.applet.applyMatrix(source);
    }

    public void applyMatrix(final PMatrix2D source) {
        this.applet.applyMatrix(source);
    }

    public void applyMatrix(final PMatrix3D source) {
        this.applet.applyMatrix(source);
    }

    public void applyMatrix(final float n00, final float n01, final float n02, final float n10, final float n11,
            final float n12) {
        this.applet.applyMatrix(n00, n01, n02, n10, n11, n12);
    }

    public void applyMatrix(final float n00, final float n01, final float n02, final float n03, final float n10,
            final float n11, final float n12, final float n13,
            final float n20, final float n21, final float n22, final float n23, final float n30, final float n31,
            final float n32, final float n33) {
        this.applet.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    public void arc(final float a, final float b, final float c, final float d, final float start, final float stop) {
        this.applet.arc(a, b, c, d, start, stop);
    }

    public void arc(final float a, final float b, final float c, final float d, final float start, final float stop,
            final int mode) {
        this.applet.arc(a, b, c, d, start, stop, mode);
    }

    public void attrib(final String name, final float... values) {
        this.applet.attrib(name, values);
    }

    public void attrib(final String name, final int... values) {
        this.applet.attrib(name, values);
    }

    public void attrib(final String name, final boolean... values) {
        this.applet.attrib(name, values);
    }

    public void attribColor(final String name, final int color) {
        this.applet.attribColor(name, color);
    }

    public void attribNormal(final String name, final float nx, final float ny, final float nz) {
        this.applet.attribNormal(name, nx, ny, nz);
    }

    public void attribPosition(final String name, final float x, final float y, final float z) {
        this.applet.attribPosition(name, x, y, z);
    }

    public void background(final int rgb) {
        this.applet.background(rgb);
    }

    public void background(final float gray) {
        this.applet.background(gray);
    }

    public void background(final PImage image) {
        this.applet.background(image);
    }

    public void background(final int rgb, final float alpha) {
        this.applet.background(rgb, alpha);
    }

    public void background(final float gray, final float alpha) {
        this.applet.background(gray, alpha);
    }

    public void background(final float v1, final float v2, final float v3) {
        this.applet.background(v1, v2, v3);
    }

    public void background(final float v1, final float v2, final float v3, final float alpha) {
        this.applet.background(v1, v2, v3, alpha);
    }

    public void beginCamera() {
        this.applet.beginCamera();
    }

    public void beginContour() {
        this.applet.beginContour();
    }

    public PGL beginPGL() {
        return this.applet.beginPGL();
    }

    public void beginRaw(final PGraphics rawGraphics) {
        this.applet.beginRaw(rawGraphics);
    }

    public PGraphics beginRaw(final String renderer, final String filename) {
        return this.applet.beginRaw(renderer, filename);
    }

    public void beginRecord(final PGraphics recorder) {
        this.applet.beginRecord(recorder);
    }

    public PGraphics beginRecord(final String renderer, final String filename) {
        return this.applet.beginRecord(renderer, filename);
    }

    public void beginShape() {
        this.applet.beginShape();
    }

    public void beginShape(final int kind) {
        this.applet.beginShape(kind);
    }

    public void bezier(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
            final float x4, final float y4) {
        this.applet.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void bezier(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
            final float x3, final float y3, final float z3,
            final float x4, final float y4, final float z4) {
        this.applet.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void bezierDetail(final int detail) {
        this.applet.bezierDetail(detail);
    }

    public float bezierPoint(final float a, final float b, final float c, final float d, final float t) {
        return this.applet.bezierPoint(a, b, c, d, t);
    }

    public float bezierTangent(final float a, final float b, final float c, final float d, final float t) {
        return this.applet.bezierTangent(a, b, c, d, t);
    }

    public void bezierVertex(final float x2, final float y2, final float x3, final float y3, final float x4,
            final float y4) {
        this.applet.bezierVertex(x2, y2, x3, y3, x4, y4);
    }

    public void bezierVertex(final float x2, final float y2, final float z2, final float x3, final float y3,
            final float z3, final float x4, final float y4, final float z4) {
        this.applet.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void blend(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw,
            final int dh, final int mode) {
        this.applet.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void blend(final PImage src, final int sx, final int sy, final int sw, final int sh, final int dx,
            final int dy,
            final int dw, final int dh, final int mode) {
        this.applet.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    public void blendMode(final int mode) {
        this.applet.blendMode(mode);
    }

    public void box(final float size) {
        this.applet.box(size);
    }

    public void box(final float w, final float h, final float d) {
        this.applet.box(w, h, d);
    }

    public void camera() {
        this.applet.camera();
    }

    public void camera(final float eyeX, final float eyeY, final float eyeZ, final float centerX, final float centerY,
            final float centerZ, final float upX,
            final float upY, final float upZ) {
        this.applet.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void circle(final float x, final float y, final float extent) {
        this.applet.circle(x, y, extent);
    }

    public void clear() {
        this.applet.clear();
    }

    public void clip(final float a, final float b, final float c, final float d) {
        this.applet.clip(a, b, c, d);
    }

    public void colorMode(final int mode) {
        this.applet.colorMode(mode);
    }

    public void colorMode(final int mode, final float max) {
        this.applet.colorMode(mode, max);
    }

    public void colorMode(final int mode, final float max1, final float max2, final float max3) {
        this.applet.colorMode(mode, max1, max2, max3);
    }

    public void colorMode(final int mode, final float max1, final float max2, final float max3, final float maxA) {
        this.applet.colorMode(mode, max1, max2, max3, maxA);
    }

    public PImage copy() {
        return this.applet.copy();
    }

    public void copy(final int sx, final int sy, final int sw, final int sh, final int dx, final int dy, final int dw,
            final int dh) {
        this.applet.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public void copy(final PImage src, final int sx, final int sy, final int sw, final int sh, final int dx,
            final int dy,
            final int dw, final int dh) {
        this.applet.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    public PFont createFont(final String name, final float size) {
        return this.applet.createFont(name, size);
    }

    public PFont createFont(final String name, final float size, final boolean smooth) {
        return this.applet.createFont(name, size, smooth);
    }

    public PFont createFont(final String name, final float size, final boolean smooth, final char[] charset) {
        return this.applet.createFont(name, size, smooth, charset);
    }

    public PGraphics createGraphics(final int w, final int h) {
        return this.applet.createGraphics(w, h);
    }

    public PGraphics createGraphics(final int w, final int h, final String renderer) {
        return this.applet.createGraphics(w, h, renderer);
    }

    public PGraphics createGraphics(final int w, final int h, final String renderer, final String path) {
        return this.applet.createGraphics(w, h, renderer, path);
    }

    public PImage createImage(final int w, final int h, final int format) {
        return this.applet.createImage(w, h, format);
    }

    public InputStream createInput(final String filename) {
        return this.applet.createInput(filename);
    }

    public InputStream createInputRaw(final String filename) {
        return this.applet.createInputRaw(filename);
    }

    public OutputStream createOutput(final String filename) {
        return this.applet.createOutput(filename);
    }

    public BufferedReader createReader(final String filename) {
        return this.applet.createReader(filename);
    }

    public PShape createShape() {
        return this.applet.createShape();
    }

    public PShape createShape(final int type) {
        return this.applet.createShape(type);
    }

    public PShape createShape(final int kind, final float... p) {
        return this.applet.createShape(kind, p);
    }

    public PrintWriter createWriter(final String filename) {
        return this.applet.createWriter(filename);
    }

    public void cursor() {
        this.applet.cursor();
    }

    public void cursor(final int kind) {
        this.applet.cursor(kind);
    }

    public void cursor(final PImage img) {
        this.applet.cursor(img);
    }

    public void cursor(final PImage img, final int x, final int y) {
        this.applet.cursor(img, x, y);
    }

    public void curve(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
            final float x4, final float y4) {
        this.applet.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void curve(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2,
            final float x3, final float y3, final float z3,
            final float x4, final float y4, final float z4) {
        this.applet.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    public void curveDetail(final int detail) {
        this.applet.curveDetail(detail);
    }

    public float curvePoint(final float a, final float b, final float c, final float d, final float t) {
        return this.applet.curvePoint(a, b, c, d, t);
    }

    public float curveTangent(final float a, final float b, final float c, final float d, final float t) {
        return this.applet.curveTangent(a, b, c, d, t);
    }

    public void curveTightness(final float tightness) {
        this.applet.curveTightness(tightness);
    }

    public void curveVertex(final float x, final float y) {
        this.applet.curveVertex(x, y);
    }

    public void curveVertex(final float x, final float y, final float z) {
        this.applet.curveVertex(x, y, z);
    }

    public File dataFile(final String where) {
        return this.applet.dataFile(where);
    }

    public String dataPath(final String where) {
        return this.applet.dataPath(where);
    }

    public void delay(final int napTime) {
        this.applet.delay(napTime);
    }

    public void die(final String what) {
        this.applet.die(what);
    }

    public void die(final String what, final Exception e) {
        this.applet.die(what, e);
    }

    public void directionalLight(final float v1, final float v2, final float v3, final float nx, final float ny,
            final float nz) {
        this.applet.directionalLight(v1, v2, v3, nx, ny, nz);
    }

    public int displayDensity() {
        return this.applet.displayDensity();
    }

    public int displayDensity(final int display) {
        return this.applet.displayDensity(display);
    }

    public void dispose() {
        this.applet.dispose();
    }

    public void draw() {
        this.applet.draw();
    }

    public void edge(final boolean edge) {
        this.applet.edge(edge);
    }

    public void ellipse(final float a, final float b, final float c, final float d) {
        this.applet.ellipse(a, b, c, d);
    }

    public void ellipseMode(final int mode) {
        this.applet.ellipseMode(mode);
    }

    public void emissive(final int rgb) {
        this.applet.emissive(rgb);
    }

    public void emissive(final float gray) {
        this.applet.emissive(gray);
    }

    public void emissive(final float v1, final float v2, final float v3) {
        this.applet.emissive(v1, v2, v3);
    }

    public void endCamera() {
        this.applet.endCamera();
    }

    public void endContour() {
        this.applet.endContour();
    }

    public void endPGL() {
        this.applet.endPGL();
    }

    public void endRaw() {
        this.applet.endRaw();
    }

    public void endRecord() {
        this.applet.endRecord();
    }

    public void endShape() {
        this.applet.endShape();
    }

    public void endShape(final int mode) {
        this.applet.endShape(mode);
    }

    public void exit() {
        this.applet.exit();
    }

    public void exitActual() {
        this.applet.exitActual();
    }

    public boolean exitCalled() {
        return this.applet.exitCalled();
    }

    public void fill(final int rgb) {
        this.applet.fill(rgb);
    }

    public void fill(final float gray) {
        this.applet.fill(gray);
    }

    public void fill(final int rgb, final float alpha) {
        this.applet.fill(rgb, alpha);
    }

    public void fill(final float gray, final float alpha) {
        this.applet.fill(gray, alpha);
    }

    public void fill(final float v1, final float v2, final float v3) {
        this.applet.fill(v1, v2, v3);
    }

    public void fill(final float v1, final float v2, final float v3, final float alpha) {
        this.applet.fill(v1, v2, v3, alpha);
    }

    public void filter(final PShader shader) {
        this.applet.filter(shader);
    }

    public void filter(final int kind) {
        this.applet.filter(kind);
    }

    public void filter(final int kind, final float param) {
        this.applet.filter(kind, param);
    }

    public void flush() {
        this.applet.flush();
    }

    public void focusGained() {
        this.applet.focusGained();
    }

    public void focusLost() {
        this.applet.focusLost();
    }

    public void frameMoved(final int x, final int y) {
        this.applet.frameMoved(x, y);
    }

    public void frameRate(final float fps) {
        this.applet.frameRate(fps);
    }

    public void frameResized(final int w, final int h) {
        this.applet.frameResized(w, h);
    }

    public void frustum(final float left, final float right, final float bottom, final float top, final float near,
            final float far) {
        this.applet.frustum(left, right, bottom, top, near, far);
    }

    public void fullScreen() {
        this.applet.fullScreen();
    }

    public void fullScreen(final int display) {
        this.applet.fullScreen(display);
    }

    public void fullScreen(final String renderer) {
        this.applet.fullScreen(renderer);
    }

    public void fullScreen(final String renderer, final int display) {
        this.applet.fullScreen(renderer, display);
    }

    public PImage get() {
        return this.applet.get();
    }

    public int get(final int x, final int y) {
        return this.applet.get(x, y);
    }

    public PImage get(final int x, final int y, final int w, final int h) {
        return this.applet.get(x, y, w, h);
    }

    public PGraphics getGraphics() {
        return this.applet.getGraphics();
    }

    public PMatrix getMatrix() {
        return this.applet.getMatrix();
    }

    public PMatrix2D getMatrix(final PMatrix2D target) {
        return this.applet.getMatrix(target);
    }

    public PMatrix3D getMatrix(final PMatrix3D target) {
        return this.applet.getMatrix(target);
    }

    public PSurface getSurface() {
        return this.applet.getSurface();
    }

    public void handleDraw() {
        this.applet.handleDraw();
    }

    public void hint(final int which) {
        this.applet.hint(which);
    }

    public void image(final PImage img, final float a, final float b) {
        this.applet.image(img, a, b);
    }

    public void image(final PImage img, final float a, final float b, final float c, final float d) {
        this.applet.image(img, a, b, c, d);
    }

    public void image(final PImage img, final float a, final float b, final float c, final float d, final int u1,
            final int v1, final int u2,
            final int v2) {
        this.applet.image(img, a, b, c, d, u1, v1, u2, v2);
    }

    public void imageMode(final int mode) {
        this.applet.imageMode(mode);
    }

    public String insertFrame(final String what) {
        return this.applet.insertFrame(what);
    }

    public boolean isLooping() {
        return this.applet.isLooping();
    }

    public void keyPressed() {
        this.applet.keyPressed();
    }

    public void keyPressed(final KeyEvent event) {
        this.applet.keyPressed(event);
    }

    public void keyReleased() {
        this.applet.keyReleased();
    }

    public void keyReleased(final KeyEvent event) {
        this.applet.keyReleased(event);
    }

    public void keyTyped() {
        this.applet.keyTyped();
    }

    public void keyTyped(final KeyEvent event) {
        this.applet.keyTyped(event);
    }

    public int lerpColor(final int c1, final int c2, final float amt) {
        return this.applet.lerpColor(c1, c2, amt);
    }

    public void lightFalloff(final float constant, final float linear, final float quadratic) {
        this.applet.lightFalloff(constant, linear, quadratic);
    }

    public void lightSpecular(final float v1, final float v2, final float v3) {
        this.applet.lightSpecular(v1, v2, v3);
    }

    public void lights() {
        this.applet.lights();
    }

    public void line(final float x1, final float y1, final float x2, final float y2) {
        this.applet.line(x1, y1, x2, y2);
    }

    public void line(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2) {
        this.applet.line(x1, y1, z1, x2, y2, z2);
    }

    public void link(final String url) {
        this.applet.link(url);
    }

    public File[] listFiles(final String path, final String... options) {
        return this.applet.listFiles(path, options);
    }

    public String[] listPaths(final String path, final String... options) {
        return this.applet.listPaths(path, options);
    }

    public byte[] loadBytes(final String filename) {
        return this.applet.loadBytes(filename);
    }

    public PFont loadFont(final String filename) {
        return this.applet.loadFont(filename);
    }

    public PImage loadImage(final String filename) {
        return this.applet.loadImage(filename);
    }

    public PImage loadImage(final String filename, final String extension) {
        return this.applet.loadImage(filename, extension);
    }

    public JSONArray loadJSONArray(final String filename) {
        return this.applet.loadJSONArray(filename);
    }

    public JSONObject loadJSONObject(final String filename) {
        return this.applet.loadJSONObject(filename);
    }

    public void loadPixels() {
        this.applet.loadPixels();
    }

    public PShader loadShader(final String fragFilename) {
        return this.applet.loadShader(fragFilename);
    }

    public PShader loadShader(final String fragFilename, final String vertFilename) {
        return this.applet.loadShader(fragFilename, vertFilename);
    }

    public PShape loadShape(final String filename) {
        return this.applet.loadShape(filename);
    }

    public PShape loadShape(final String filename, final String options) {
        return this.applet.loadShape(filename, options);
    }

    public String[] loadStrings(final String filename) {
        return this.applet.loadStrings(filename);
    }

    public Table loadTable(final String filename) {
        return this.applet.loadTable(filename);
    }

    public Table loadTable(final String filename, final String options) {
        return this.applet.loadTable(filename, options);
    }

    public XML loadXML(final String filename) {
        return this.applet.loadXML(filename);
    }

    public XML loadXML(final String filename, final String options) {
        return this.applet.loadXML(filename, options);
    }

    public synchronized void loop() {
        this.applet.loop();
    }

    public void mask(final PImage img) {
        this.applet.mask(img);
    }

    public void method(final String name) {
        this.applet.method(name);
    }

    public int millis() {
        return this.applet.millis();
    }

    public float modelX(final float x, final float y, final float z) {
        return this.applet.modelX(x, y, z);
    }

    public float modelY(final float x, final float y, final float z) {
        return this.applet.modelY(x, y, z);
    }

    public float modelZ(final float x, final float y, final float z) {
        return this.applet.modelZ(x, y, z);
    }

    public void mouseClicked() {
        this.applet.mouseClicked();
    }

    public void mouseClicked(final MouseEvent event) {
        this.applet.mouseClicked(event);
    }

    public void mouseDragged() {
        this.applet.mouseDragged();
    }

    public void mouseDragged(final MouseEvent event) {
        this.applet.mouseDragged(event);
    }

    public void mouseEntered() {
        this.applet.mouseEntered();
    }

    public void mouseEntered(final MouseEvent event) {
        this.applet.mouseEntered(event);
    }

    public void mouseExited() {
        this.applet.mouseExited();
    }

    public void mouseExited(final MouseEvent event) {
        this.applet.mouseExited(event);
    }

    public void mouseMoved() {
        this.applet.mouseMoved();
    }

    public void mouseMoved(final MouseEvent event) {
        this.applet.mouseMoved(event);
    }

    public void mousePressed() {
        this.applet.mousePressed();
    }

    public void mousePressed(final MouseEvent event) {
        this.applet.mousePressed(event);
    }

    public void mouseReleased() {
        this.applet.mouseReleased();
    }

    public void mouseReleased(final MouseEvent event) {
        this.applet.mouseReleased(event);
    }

    public void mouseWheel() {
        this.applet.mouseWheel();
    }

    public void mouseWheel(final MouseEvent event) {
        this.applet.mouseWheel(event);
    }

    public void noClip() {
        this.applet.noClip();
    }

    public void noCursor() {
        this.applet.noCursor();
    }

    public void noFill() {
        this.applet.noFill();
    }

    public void noLights() {
        this.applet.noLights();
    }

    public synchronized void noLoop() {
        this.applet.noLoop();
    }

    public void noSmooth() {
        this.applet.noSmooth();
    }

    public void noStroke() {
        this.applet.noStroke();
    }

    public void noTexture() {
        this.applet.noTexture();
    }

    public void noTint() {
        this.applet.noTint();
    }

    public float noise(final float x) {
        return this.applet.noise(x);
    }

    public float noise(final float x, final float y) {
        return this.applet.noise(x, y);
    }

    public float noise(final float x, final float y, final float z) {
        return this.applet.noise(x, y, z);
    }

    public void noiseDetail(final int lod) {
        this.applet.noiseDetail(lod);
    }

    public void noiseDetail(final int lod, final float falloff) {
        this.applet.noiseDetail(lod, falloff);
    }

    public void noiseSeed(final long seed) {
        this.applet.noiseSeed(seed);
    }

    public void normal(final float nx, final float ny, final float nz) {
        this.applet.normal(nx, ny, nz);
    }

    public void orientation(final int which) {
        this.applet.orientation(which);
    }

    public void ortho() {
        this.applet.ortho();
    }

    public void ortho(final float left, final float right, final float bottom, final float top) {
        this.applet.ortho(left, right, bottom, top);
    }

    public void ortho(final float left, final float right, final float bottom, final float top, final float near,
            final float far) {
        this.applet.ortho(left, right, bottom, top, near, far);
    }

    public JSONArray parseJSONArray(final String input) {
        return this.applet.parseJSONArray(input);
    }

    public JSONObject parseJSONObject(final String input) {
        return this.applet.parseJSONObject(input);
    }

    public XML parseXML(final String xmlString) {
        return this.applet.parseXML(xmlString);
    }

    public XML parseXML(final String xmlString, final String options) {
        return this.applet.parseXML(xmlString, options);
    }

    public void pause() {
        this.applet.pause();
    }

    public void perspective() {
        this.applet.perspective();
    }

    public void perspective(final float fovy, final float aspect, final float zNear, final float zFar) {
        this.applet.perspective(fovy, aspect, zNear, zFar);
    }

    public void pixelDensity(final int density) {
        this.applet.pixelDensity(density);
    }

    public void point(final float x, final float y) {
        this.applet.point(x, y);
    }

    public void point(final float x, final float y, final float z) {
        this.applet.point(x, y, z);
    }

    public void pointLight(final float v1, final float v2, final float v3, final float x, final float y,
            final float z) {
        this.applet.pointLight(v1, v2, v3, x, y, z);
    }

    public void pop() {
        this.applet.pop();
    }

    public void popMatrix() {
        this.applet.popMatrix();
    }

    public void popStyle() {
        this.applet.popStyle();
    }

    public void postEvent(final Event pe) {
        this.applet.postEvent(pe);
    }

    public void printCamera() {
        this.applet.printCamera();
    }

    public void printMatrix() {
        this.applet.printMatrix();
    }

    public void printProjection() {
        this.applet.printProjection();
    }

    public void push() {
        this.applet.push();
    }

    public void pushMatrix() {
        this.applet.pushMatrix();
    }

    public void pushStyle() {
        this.applet.pushStyle();
    }

    public void quad(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3,
            final float x4, final float y4) {
        this.applet.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public void quadraticVertex(final float cx, final float cy, final float x3, final float y3) {
        this.applet.quadraticVertex(cx, cy, x3, y3);
    }

    public void quadraticVertex(final float cx, final float cy, final float cz, final float x3, final float y3,
            final float z3) {
        this.applet.quadraticVertex(cx, cy, cz, x3, y3, z3);
    }

    public void rect(final float a, final float b, final float c, final float d) {
        this.applet.rect(a, b, c, d);
    }

    public void rect(final float a, final float b, final float c, final float d, final float r) {
        this.applet.rect(a, b, c, d, r);
    }

    public void rect(final float a, final float b, final float c, final float d, final float tl, final float tr,
            final float br, final float bl) {
        this.applet.rect(a, b, c, d, tl, tr, br, bl);
    }

    public void rectMode(final int mode) {
        this.applet.rectMode(mode);
    }

    public synchronized void redraw() {
        this.applet.redraw();
    }

    public void registerMethod(final String methodName, final Object target) {
        this.applet.registerMethod(methodName, target);
    }

    public PImage requestImage(final String filename) {
        return this.applet.requestImage(filename);
    }

    public PImage requestImage(final String filename, final String extension) {
        return this.applet.requestImage(filename, extension);
    }

    public void resetMatrix() {
        this.applet.resetMatrix();
    }

    public void resetShader() {
        this.applet.resetShader();
    }

    public void resetShader(final int kind) {
        this.applet.resetShader(kind);
    }

    public void resume() {
        this.applet.resume();
    }

    public void rotate(final float angle) {
        this.applet.rotate(angle);
    }

    public void rotate(final float angle, final float x, final float y, final float z) {
        this.applet.rotate(angle, x, y, z);
    }

    public void rotateX(final float angle) {
        this.applet.rotateX(angle);
    }

    public void rotateY(final float angle) {
        this.applet.rotateY(angle);
    }

    public void rotateZ(final float angle) {
        this.applet.rotateZ(angle);
    }

    public void save(final String filename) {
        this.applet.save(filename);
    }

    public void saveBytes(final String filename, final byte[] data) {
        this.applet.saveBytes(filename, data);
    }

    public File saveFile(final String where) {
        return this.applet.saveFile(where);
    }

    public void saveFrame() {
        this.applet.saveFrame();
    }

    public void saveFrame(final String filename) {
        this.applet.saveFrame(filename);
    }

    public boolean saveJSONArray(final JSONArray json, final String filename) {
        return this.applet.saveJSONArray(json, filename);
    }

    public boolean saveJSONArray(final JSONArray json, final String filename, final String options) {
        return this.applet.saveJSONArray(json, filename, options);
    }

    public boolean saveJSONObject(final JSONObject json, final String filename) {
        return this.applet.saveJSONObject(json, filename);
    }

    public boolean saveJSONObject(final JSONObject json, final String filename, final String options) {
        return this.applet.saveJSONObject(json, filename, options);
    }

    public String savePath(final String where) {
        return this.applet.savePath(where);
    }

    public boolean saveStream(final String target, final String source) {
        return this.applet.saveStream(target, source);
    }

    public boolean saveStream(final File target, final String source) {
        return this.applet.saveStream(target, source);
    }

    public boolean saveStream(final String target, final InputStream source) {
        return this.applet.saveStream(target, source);
    }

    public void saveStrings(final String filename, final String[] data) {
        this.applet.saveStrings(filename, data);
    }

    public boolean saveTable(final Table table, final String filename) {
        return this.applet.saveTable(table, filename);
    }

    public boolean saveTable(final Table table, final String filename, final String options) {
        return this.applet.saveTable(table, filename, options);
    }

    public boolean saveXML(final XML xml, final String filename) {
        return this.applet.saveXML(xml, filename);
    }

    public boolean saveXML(final XML xml, final String filename, final String options) {
        return this.applet.saveXML(xml, filename, options);
    }

    public void scale(final float s) {
        this.applet.scale(s);
    }

    public void scale(final float x, final float y) {
        this.applet.scale(x, y);
    }

    public void scale(final float x, final float y, final float z) {
        this.applet.scale(x, y, z);
    }

    public float screenX(final float x, final float y) {
        return this.applet.screenX(x, y);
    }

    public float screenX(final float x, final float y, final float z) {
        return this.applet.screenX(x, y, z);
    }

    public float screenY(final float x, final float y) {
        return this.applet.screenY(x, y);
    }

    public float screenY(final float x, final float y, final float z) {
        return this.applet.screenY(x, y, z);
    }

    public float screenZ(final float x, final float y, final float z) {
        return this.applet.screenZ(x, y, z);
    }

    public void selectFolder(final String prompt, final String callback) {
        this.applet.selectFolder(prompt, callback);
    }

    public void selectFolder(final String prompt, final String callback, final File file) {
        this.applet.selectFolder(prompt, callback, file);
    }

    public void selectFolder(final String prompt, final String callback, final File file, final Object callbackObject) {
        this.applet.selectFolder(prompt, callback, file, callbackObject);
    }

    public void selectInput(final String prompt, final String callback) {
        this.applet.selectInput(prompt, callback);
    }

    public void selectInput(final String prompt, final String callback, final File file) {
        this.applet.selectInput(prompt, callback, file);
    }

    public void selectInput(final String prompt, final String callback, final File file, final Object callbackObject) {
        this.applet.selectInput(prompt, callback, file, callbackObject);
    }

    public void selectOutput(final String prompt, final String callback) {
        this.applet.selectOutput(prompt, callback);
    }

    public void selectOutput(final String prompt, final String callback, final File file) {
        this.applet.selectOutput(prompt, callback, file);
    }

    public void selectOutput(final String prompt, final String callback, final File file, final Object callbackObject) {
        this.applet.selectOutput(prompt, callback, file, callbackObject);
    }

    public void set(final int x, final int y, final int c) {
        this.applet.set(x, y, c);
    }

    public void set(final int x, final int y, final PImage img) {
        this.applet.set(x, y, img);
    }

    public void setMatrix(final PMatrix source) {
        this.applet.setMatrix(source);
    }

    public void setMatrix(final PMatrix2D source) {
        this.applet.setMatrix(source);
    }

    public void setMatrix(final PMatrix3D source) {
        this.applet.setMatrix(source);
    }

    public void setSize(final int width, final int height) {
        this.applet.setSize(width, height);
    }

    public void settings() {
        this.applet.settings();
    }

    public void setup() {
        this.applet.setup();
    }

    public void shader(final PShader shader) {
        this.applet.shader(shader);
    }

    public void shader(final PShader shader, final int kind) {
        this.applet.shader(shader, kind);
    }

    public void shape(final PShape shape) {
        this.applet.shape(shape);
    }

    public void shape(final PShape shape, final float x, final float y) {
        this.applet.shape(shape, x, y);
    }

    public void shape(final PShape shape, final float a, final float b, final float c, final float d) {
        this.applet.shape(shape, a, b, c, d);
    }

    public void shapeMode(final int mode) {
        this.applet.shapeMode(mode);
    }

    public void shearX(final float angle) {
        this.applet.shearX(angle);
    }

    public void shearY(final float angle) {
        this.applet.shearY(angle);
    }

    public void shininess(final float shine) {
        this.applet.shininess(shine);
    }

    public void size(final int width, final int height) {
        this.applet.size(width, height);
    }

    public void size(final int width, final int height, final String renderer) {
        this.applet.size(width, height, renderer);
    }

    public void size(final int width, final int height, final String renderer, final String path) {
        this.applet.size(width, height, renderer, path);
    }

    public File sketchFile(final String where) {
        return this.applet.sketchFile(where);
    }

    public String sketchPath() {
        return this.applet.sketchPath();
    }

    public String sketchPath(final String where) {
        return this.applet.sketchPath(where);
    }

    public void smooth() {
        this.applet.smooth();
    }

    public void smooth(final int level) {
        this.applet.smooth(level);
    }

    public void specular(final int rgb) {
        this.applet.specular(rgb);
    }

    public void specular(final float gray) {
        this.applet.specular(gray);
    }

    public void specular(final float v1, final float v2, final float v3) {
        this.applet.specular(v1, v2, v3);
    }

    public void sphere(final float r) {
        this.applet.sphere(r);
    }

    public void sphereDetail(final int res) {
        this.applet.sphereDetail(res);
    }

    public void sphereDetail(final int ures, final int vres) {
        this.applet.sphereDetail(ures, vres);
    }

    public void spotLight(final float v1, final float v2, final float v3, final float x, final float y, final float z,
            final float nx, final float ny, final float nz,
            final float angle, final float concentration) {
        this.applet.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
    }

    public void square(final float x, final float y, final float extent) {
        this.applet.square(x, y, extent);
    }

    public void start() {
        this.applet.start();
    }

    public void stop() {
        this.applet.stop();
    }

    public void stroke(final int rgb) {
        this.applet.stroke(rgb);
    }

    public void stroke(final float gray) {
        this.applet.stroke(gray);
    }

    public void stroke(final int rgb, final float alpha) {
        this.applet.stroke(rgb, alpha);
    }

    public void stroke(final float gray, final float alpha) {
        this.applet.stroke(gray, alpha);
    }

    public void stroke(final float v1, final float v2, final float v3) {
        this.applet.stroke(v1, v2, v3);
    }

    public void stroke(final float v1, final float v2, final float v3, final float alpha) {
        this.applet.stroke(v1, v2, v3, alpha);
    }

    public void strokeCap(final int cap) {
        this.applet.strokeCap(cap);
    }

    public void strokeJoin(final int join) {
        this.applet.strokeJoin(join);
    }

    public void strokeWeight(final float weight) {
        this.applet.strokeWeight(weight);
    }

    public void style(final PStyle s) {
        this.applet.style(s);
    }

    public void text(final char c, final float x, final float y) {
        this.applet.text(c, x, y);
    }

    public void text(final String str, final float x, final float y) {
        this.applet.text(str, x, y);
    }

    public void text(final int num, final float x, final float y) {
        this.applet.text(num, x, y);
    }

    public void text(final float num, final float x, final float y) {
        this.applet.text(num, x, y);
    }

    public void text(final char c, final float x, final float y, final float z) {
        this.applet.text(c, x, y, z);
    }

    public void text(final String str, final float x, final float y, final float z) {
        this.applet.text(str, x, y, z);
    }

    public void text(final int num, final float x, final float y, final float z) {
        this.applet.text(num, x, y, z);
    }

    public void text(final float num, final float x, final float y, final float z) {
        this.applet.text(num, x, y, z);
    }

    public void text(final char[] chars, final int start, final int stop, final float x, final float y) {
        this.applet.text(chars, start, stop, x, y);
    }

    public void text(final String str, final float x1, final float y1, final float x2, final float y2) {
        this.applet.text(str, x1, y1, x2, y2);
    }

    public void text(final char[] chars, final int start, final int stop, final float x, final float y, final float z) {
        this.applet.text(chars, start, stop, x, y, z);
    }

    public void textAlign(final int alignX) {
        this.applet.textAlign(alignX);
    }

    public void textAlign(final int alignX, final int alignY) {
        this.applet.textAlign(alignX, alignY);
    }

    public float textAscent() {
        return this.applet.textAscent();
    }

    public float textDescent() {
        return this.applet.textDescent();
    }

    public void textFont(final PFont which) {
        this.applet.textFont(which);
    }

    public void textFont(final PFont which, final float size) {
        this.applet.textFont(which, size);
    }

    public void textLeading(final float leading) {
        this.applet.textLeading(leading);
    }

    public void textMode(final int mode) {
        this.applet.textMode(mode);
    }

    public void textSize(final float size) {
        this.applet.textSize(size);
    }

    public float textWidth(final char c) {
        return this.applet.textWidth(c);
    }

    public float textWidth(final String str) {
        return this.applet.textWidth(str);
    }

    public float textWidth(final char[] chars, final int start, final int length) {
        return this.applet.textWidth(chars, start, length);
    }

    public void texture(final PImage image) {
        this.applet.texture(image);
    }

    public void textureMode(final int mode) {
        this.applet.textureMode(mode);
    }

    public void textureWrap(final int wrap) {
        this.applet.textureWrap(wrap);
    }

    public void thread(final String name) {
        this.applet.thread(name);
    }

    public void tint(final int rgb) {
        this.applet.tint(rgb);
    }

    public void tint(final float gray) {
        this.applet.tint(gray);
    }

    public void tint(final int rgb, final float alpha) {
        this.applet.tint(rgb, alpha);
    }

    public void tint(final float gray, final float alpha) {
        this.applet.tint(gray, alpha);
    }

    public void tint(final float v1, final float v2, final float v3) {
        this.applet.tint(v1, v2, v3);
    }

    public void tint(final float v1, final float v2, final float v3, final float alpha) {
        this.applet.tint(v1, v2, v3, alpha);
    }

    public void translate(final float x, final float y) {
        this.applet.translate(x, y);
    }

    public void translate(final float x, final float y, final float z) {
        this.applet.translate(x, y, z);
    }

    public void triangle(final float x1, final float y1, final float x2, final float y2, final float x3,
            final float y3) {
        this.applet.triangle(x1, y1, x2, y2, x3, y3);
    }

    public void unregisterMethod(final String name, final Object target) {
        this.applet.unregisterMethod(name, target);
    }

    public void updatePixels() {
        this.applet.updatePixels();
    }

    public void updatePixels(final int x1, final int y1, final int x2, final int y2) {
        this.applet.updatePixels(x1, y1, x2, y2);
    }

    public void vertex(final float[] v) {
        this.applet.vertex(v);
    }

    public void vertex(final float x, final float y) {
        this.applet.vertex(x, y);
    }

    public void vertex(final float x, final float y, final float z) {
        this.applet.vertex(x, y, z);
    }

    public void vertex(final float x, final float y, final float u, final float v) {
        this.applet.vertex(x, y, u, v);
    }

    public void vertex(final float x, final float y, final float z, final float u, final float v) {
        this.applet.vertex(x, y, z, u, v);
    }

    @Override
    public boolean equals(final Object obj) {
        return this.applet.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.applet.hashCode();
    }

    @Override
    public String toString() {
        return this.applet.toString();
    }
}
