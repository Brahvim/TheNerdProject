package com.brahvim.nerd.papplet_wrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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

public class PAppWrap extends PApplet {

    @Override
    public void ambient(int rgb) {
        super.ambient(rgb);
    }

    @Override
    public void ambient(float gray) {
        super.ambient(gray);
    }

    @Override
    public void ambient(float v1, float v2, float v3) {
        super.ambient(v1, v2, v3);
    }

    @Override
    public void ambientLight(float v1, float v2, float v3) {
        super.ambientLight(v1, v2, v3);
    }

    @Override
    public void ambientLight(float v1, float v2, float v3, float x, float y, float z) {
        super.ambientLight(v1, v2, v3, x, y, z);
    }

    @Override
    public void applyMatrix(PMatrix source) {
        super.applyMatrix(source);
    }

    @Override
    public void applyMatrix(PMatrix2D source) {
        super.applyMatrix(source);
    }

    @Override
    public void applyMatrix(PMatrix3D source) {
        super.applyMatrix(source);
    }

    @Override
    public void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12) {
        super.applyMatrix(n00, n01, n02, n10, n11, n12);
    }

    @Override
    public void applyMatrix(float n00, float n01, float n02, float n03, float n10, float n11, float n12, float n13,
            float n20, float n21, float n22, float n23, float n30, float n31, float n32, float n33) {
        super.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
    }

    @Override
    public void arc(float a, float b, float c, float d, float start, float stop) {
        super.arc(a, b, c, d, start, stop);
    }

    @Override
    public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
        super.arc(a, b, c, d, start, stop, mode);
    }

    @Override
    public void attrib(String name, float... values) {
        super.attrib(name, values);
    }

    @Override
    public void attrib(String name, int... values) {
        super.attrib(name, values);
    }

    @Override
    public void attrib(String name, boolean... values) {
        super.attrib(name, values);
    }

    @Override
    public void attribColor(String name, int color) {
        super.attribColor(name, color);
    }

    @Override
    public void attribNormal(String name, float nx, float ny, float nz) {
        super.attribNormal(name, nx, ny, nz);
    }

    @Override
    public void attribPosition(String name, float x, float y, float z) {
        super.attribPosition(name, x, y, z);
    }

    @Override
    public void background(int rgb) {
        super.background(rgb);
    }

    @Override
    public void background(float gray) {
        super.background(gray);
    }

    @Override
    public void background(PImage image) {
        super.background(image);
    }

    @Override
    public void background(int rgb, float alpha) {
        super.background(rgb, alpha);
    }

    @Override
    public void background(float gray, float alpha) {
        super.background(gray, alpha);
    }

    @Override
    public void background(float v1, float v2, float v3) {
        super.background(v1, v2, v3);
    }

    @Override
    public void background(float v1, float v2, float v3, float alpha) {
        super.background(v1, v2, v3, alpha);
    }

    @Override
    public void beginCamera() {
        super.beginCamera();
    }

    @Override
    public void beginContour() {
        super.beginContour();
    }

    @Override
    public PGL beginPGL() {
        return super.beginPGL();
    }

    @Override
    public void beginRaw(PGraphics rawGraphics) {
        super.beginRaw(rawGraphics);
    }

    @Override
    public PGraphics beginRaw(String renderer, String filename) {
        return super.beginRaw(renderer, filename);
    }

    @Override
    public void beginRecord(PGraphics recorder) {
        super.beginRecord(recorder);
    }

    @Override
    public PGraphics beginRecord(String renderer, String filename) {
        return super.beginRecord(renderer, filename);
    }

    @Override
    public void beginShape() {
        super.beginShape();
    }

    @Override
    public void beginShape(int kind) {
        super.beginShape(kind);
    }

    @Override
    public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        super.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    @Override
    public void bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,
            float x4, float y4, float z4) {
        super.bezier(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    @Override
    public void bezierDetail(int detail) {
        super.bezierDetail(detail);
    }

    @Override
    public float bezierPoint(float a, float b, float c, float d, float t) {
        return super.bezierPoint(a, b, c, d, t);
    }

    @Override
    public float bezierTangent(float a, float b, float c, float d, float t) {
        return super.bezierTangent(a, b, c, d, t);
    }

    @Override
    public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4) {
        super.bezierVertex(x2, y2, x3, y3, x4, y4);
    }

    @Override
    public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        super.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    @Override
    public void blend(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        super.blend(sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    @Override
    public void blend(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode) {
        super.blend(src, sx, sy, sw, sh, dx, dy, dw, dh, mode);
    }

    @Override
    public void blendMode(int mode) {
        super.blendMode(mode);
    }

    @Override
    public void box(float size) {
        super.box(size);
    }

    @Override
    public void box(float w, float h, float d) {
        super.box(w, h, d);
    }

    @Override
    public void camera() {
        super.camera();
    }

    @Override
    public void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX,
            float upY, float upZ) {
        super.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    @Override
    public void circle(float x, float y, float extent) {
        super.circle(x, y, extent);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void clip(float a, float b, float c, float d) {
        super.clip(a, b, c, d);
    }

    @Override
    public void colorMode(int mode) {
        super.colorMode(mode);
    }

    @Override
    public void colorMode(int mode, float max) {
        super.colorMode(mode, max);
    }

    @Override
    public void colorMode(int mode, float max1, float max2, float max3) {
        super.colorMode(mode, max1, max2, max3);
    }

    @Override
    public void colorMode(int mode, float max1, float max2, float max3, float maxA) {
        super.colorMode(mode, max1, max2, max3, maxA);
    }

    @Override
    public PImage copy() {
        return super.copy();
    }

    @Override
    public void copy(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        super.copy(sx, sy, sw, sh, dx, dy, dw, dh);
    }

    @Override
    public void copy(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh) {
        super.copy(src, sx, sy, sw, sh, dx, dy, dw, dh);
    }

    @Override
    public PFont createFont(String name, float size) {
        return super.createFont(name, size);
    }

    @Override
    public PFont createFont(String name, float size, boolean smooth) {
        return super.createFont(name, size, smooth);
    }

    @Override
    public PFont createFont(String name, float size, boolean smooth, char[] charset) {
        return super.createFont(name, size, smooth, charset);
    }

    @Override
    public PGraphics createGraphics(int w, int h) {
        return super.createGraphics(w, h);
    }

    @Override
    public PGraphics createGraphics(int w, int h, String renderer) {
        return super.createGraphics(w, h, renderer);
    }

    @Override
    public PGraphics createGraphics(int w, int h, String renderer, String path) {
        return super.createGraphics(w, h, renderer, path);
    }

    @Override
    public PImage createImage(int w, int h, int format) {
        return super.createImage(w, h, format);
    }

    @Override
    public InputStream createInput(String filename) {
        return super.createInput(filename);
    }

    @Override
    public InputStream createInputRaw(String filename) {
        return super.createInputRaw(filename);
    }

    @Override
    public OutputStream createOutput(String filename) {
        return super.createOutput(filename);
    }

    @Override
    public BufferedReader createReader(String filename) {
        return super.createReader(filename);
    }

    @Override
    public PShape createShape() {
        return super.createShape();
    }

    @Override
    public PShape createShape(int type) {
        return super.createShape(type);
    }

    @Override
    public PShape createShape(int kind, float... p) {
        return super.createShape(kind, p);
    }

    @Override
    public PrintWriter createWriter(String filename) {
        return super.createWriter(filename);
    }

    @Override
    public void cursor() {
        super.cursor();
    }

    @Override
    public void cursor(int kind) {
        super.cursor(kind);
    }

    @Override
    public void cursor(PImage img) {
        super.cursor(img);
    }

    @Override
    public void cursor(PImage img, int x, int y) {
        super.cursor(img, x, y);
    }

    @Override
    public void curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        super.curve(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    @Override
    public void curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,
            float x4, float y4, float z4) {
        super.curve(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
    }

    @Override
    public void curveDetail(int detail) {
        super.curveDetail(detail);
    }

    @Override
    public float curvePoint(float a, float b, float c, float d, float t) {
        return super.curvePoint(a, b, c, d, t);
    }

    @Override
    public float curveTangent(float a, float b, float c, float d, float t) {
        return super.curveTangent(a, b, c, d, t);
    }

    @Override
    public void curveTightness(float tightness) {
        super.curveTightness(tightness);
    }

    @Override
    public void curveVertex(float x, float y) {
        super.curveVertex(x, y);
    }

    @Override
    public void curveVertex(float x, float y, float z) {
        super.curveVertex(x, y, z);
    }

    @Override
    public File dataFile(String where) {
        return super.dataFile(where);
    }

    @Override
    public String dataPath(String where) {
        return super.dataPath(where);
    }

    @Override
    public void delay(int napTime) {
        super.delay(napTime);
    }

    @Override
    public void die(String what) {
        super.die(what);
    }

    @Override
    public void die(String what, Exception e) {
        super.die(what, e);
    }

    @Override
    public void directionalLight(float v1, float v2, float v3, float nx, float ny, float nz) {
        super.directionalLight(v1, v2, v3, nx, ny, nz);
    }

    @Override
    public int displayDensity() {
        return super.displayDensity();
    }

    @Override
    public int displayDensity(int display) {
        return super.displayDensity(display);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void edge(boolean edge) {
        super.edge(edge);
    }

    @Override
    public void ellipse(float a, float b, float c, float d) {
        super.ellipse(a, b, c, d);
    }

    @Override
    public void ellipseMode(int mode) {
        super.ellipseMode(mode);
    }

    @Override
    public void emissive(int rgb) {
        super.emissive(rgb);
    }

    @Override
    public void emissive(float gray) {
        super.emissive(gray);
    }

    @Override
    public void emissive(float v1, float v2, float v3) {
        super.emissive(v1, v2, v3);
    }

    @Override
    public void endCamera() {
        super.endCamera();
    }

    @Override
    public void endContour() {
        super.endContour();
    }

    @Override
    public void endPGL() {
        super.endPGL();
    }

    @Override
    public void endRaw() {
        super.endRaw();
    }

    @Override
    public void endRecord() {
        super.endRecord();
    }

    @Override
    public void endShape() {
        super.endShape();
    }

    @Override
    public void endShape(int mode) {
        super.endShape(mode);
    }

    @Override
    public void exit() {
        super.exit();
    }

    @Override
    public void exitActual() {
        super.exitActual();
    }

    @Override
    public boolean exitCalled() {
        return super.exitCalled();
    }

    @Override
    public void fill(int rgb) {
        super.fill(rgb);
    }

    @Override
    public void fill(float gray) {
        super.fill(gray);
    }

    @Override
    public void fill(int rgb, float alpha) {
        super.fill(rgb, alpha);
    }

    @Override
    public void fill(float gray, float alpha) {
        super.fill(gray, alpha);
    }

    @Override
    public void fill(float v1, float v2, float v3) {
        super.fill(v1, v2, v3);
    }

    @Override
    public void fill(float v1, float v2, float v3, float alpha) {
        super.fill(v1, v2, v3, alpha);
    }

    @Override
    public void filter(PShader shader) {
        super.filter(shader);
    }

    @Override
    public void filter(int kind) {
        super.filter(kind);
    }

    @Override
    public void filter(int kind, float param) {
        super.filter(kind, param);
    }

    @Override
    public void flush() {
        super.flush();
    }

    @Override
    public void focusGained() {
        super.focusGained();
    }

    @Override
    public void focusLost() {
        super.focusLost();
    }

    @Override
    public void frameMoved(int x, int y) {
        super.frameMoved(x, y);
    }

    @Override
    public void frameRate(float fps) {
        super.frameRate(fps);
    }

    @Override
    public void frameResized(int w, int h) {
        super.frameResized(w, h);
    }

    @Override
    public void frustum(float left, float right, float bottom, float top, float near, float far) {
        super.frustum(left, right, bottom, top, near, far);
    }

    @Override
    public void fullScreen() {
        super.fullScreen();
    }

    @Override
    public void fullScreen(int display) {
        super.fullScreen(display);
    }

    @Override
    public void fullScreen(String renderer) {
        super.fullScreen(renderer);
    }

    @Override
    public void fullScreen(String renderer, int display) {
        super.fullScreen(renderer, display);
    }

    @Override
    public PImage get() {
        return super.get();
    }

    @Override
    public int get(int x, int y) {
        return super.get(x, y);
    }

    @Override
    public PImage get(int x, int y, int w, int h) {
        return super.get(x, y, w, h);
    }

    @Override
    public PGraphics getGraphics() {
        return super.getGraphics();
    }

    @Override
    public PMatrix getMatrix() {
        return super.getMatrix();
    }

    @Override
    public PMatrix2D getMatrix(PMatrix2D target) {
        return super.getMatrix(target);
    }

    @Override
    public PMatrix3D getMatrix(PMatrix3D target) {
        return super.getMatrix(target);
    }

    @Override
    public PSurface getSurface() {
        return super.getSurface();
    }

    @Override
    public void handleDraw() {
        super.handleDraw();
    }

    @Override
    public void hint(int which) {
        super.hint(which);
    }

    @Override
    public void image(PImage img, float a, float b) {
        super.image(img, a, b);
    }

    @Override
    public void image(PImage img, float a, float b, float c, float d) {
        super.image(img, a, b, c, d);
    }

    @Override
    public void image(PImage img, float a, float b, float c, float d, int u1, int v1, int u2, int v2) {
        super.image(img, a, b, c, d, u1, v1, u2, v2);
    }

    @Override
    public void imageMode(int mode) {
        super.imageMode(mode);
    }

    @Override
    public String insertFrame(String what) {
        return super.insertFrame(what);
    }

    @Override
    public boolean isLooping() {
        return super.isLooping();
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);
    }

    @Override
    public void keyReleased() {
        super.keyReleased();
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);
    }

    @Override
    public void keyTyped() {
        super.keyTyped();
    }

    @Override
    public void keyTyped(KeyEvent event) {
        super.keyTyped(event);
    }

    @Override
    public int lerpColor(int c1, int c2, float amt) {
        return super.lerpColor(c1, c2, amt);
    }

    @Override
    public void lightFalloff(float constant, float linear, float quadratic) {
        super.lightFalloff(constant, linear, quadratic);
    }

    @Override
    public void lightSpecular(float v1, float v2, float v3) {
        super.lightSpecular(v1, v2, v3);
    }

    @Override
    public void lights() {
        super.lights();
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        super.line(x1, y1, x2, y2);
    }

    @Override
    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
        super.line(x1, y1, z1, x2, y2, z2);
    }

    @Override
    public void link(String url) {
        super.link(url);
    }

    @Override
    public File[] listFiles(String path, String... options) {
        return super.listFiles(path, options);
    }

    @Override
    public String[] listPaths(String path, String... options) {
        return super.listPaths(path, options);
    }

    @Override
    public byte[] loadBytes(String filename) {
        return super.loadBytes(filename);
    }

    @Override
    public PFont loadFont(String filename) {
        return super.loadFont(filename);
    }

    @Override
    public PImage loadImage(String filename) {
        return super.loadImage(filename);
    }

    @Override
    public PImage loadImage(String filename, String extension) {
        return super.loadImage(filename, extension);
    }

    @Override
    public JSONArray loadJSONArray(String filename) {
        return super.loadJSONArray(filename);
    }

    @Override
    public JSONObject loadJSONObject(String filename) {
        return super.loadJSONObject(filename);
    }

    @Override
    public void loadPixels() {
        super.loadPixels();
    }

    @Override
    public PShader loadShader(String fragFilename) {
        return super.loadShader(fragFilename);
    }

    @Override
    public PShader loadShader(String fragFilename, String vertFilename) {
        return super.loadShader(fragFilename, vertFilename);
    }

    @Override
    public PShape loadShape(String filename) {
        return super.loadShape(filename);
    }

    @Override
    public PShape loadShape(String filename, String options) {
        return super.loadShape(filename, options);
    }

    @Override
    public String[] loadStrings(String filename) {
        return super.loadStrings(filename);
    }

    @Override
    public Table loadTable(String filename) {
        return super.loadTable(filename);
    }

    @Override
    public Table loadTable(String filename, String options) {
        return super.loadTable(filename, options);
    }

    @Override
    public XML loadXML(String filename) {
        return super.loadXML(filename);
    }

    @Override
    public XML loadXML(String filename, String options) {
        return super.loadXML(filename, options);
    }

    @Override
    public synchronized void loop() {
        super.loop();
    }

    @Override
    public void mask(PImage img) {
        super.mask(img);
    }

    @Override
    public void method(String name) {
        super.method(name);
    }

    @Override
    public int millis() {
        return super.millis();
    }

    @Override
    public float modelX(float x, float y, float z) {
        return super.modelX(x, y, z);
    }

    @Override
    public float modelY(float x, float y, float z) {
        return super.modelY(x, y, z);
    }

    @Override
    public float modelZ(float x, float y, float z) {
        return super.modelZ(x, y, z);
    }

    @Override
    public void mouseClicked() {
        super.mouseClicked();
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        super.mouseClicked(event);
    }

    @Override
    public void mouseDragged() {
        super.mouseDragged();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
    }

    @Override
    public void mouseEntered() {
        super.mouseEntered();
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        super.mouseEntered(event);
    }

    @Override
    public void mouseExited() {
        super.mouseExited();
    }

    @Override
    public void mouseExited(MouseEvent event) {
        super.mouseExited(event);
    }

    @Override
    public void mouseMoved() {
        super.mouseMoved();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        super.mouseMoved(event);
    }

    @Override
    public void mousePressed() {
        super.mousePressed();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
    }

    @Override
    public void mouseReleased() {
        super.mouseReleased();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
    }

    @Override
    public void mouseWheel() {
        super.mouseWheel();
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);
    }

    @Override
    public void noClip() {
        super.noClip();
    }

    @Override
    public void noCursor() {
        super.noCursor();
    }

    @Override
    public void noFill() {
        super.noFill();
    }

    @Override
    public void noLights() {
        super.noLights();
    }

    @Override
    public synchronized void noLoop() {
        super.noLoop();
    }

    @Override
    public void noSmooth() {
        super.noSmooth();
    }

    @Override
    public void noStroke() {
        super.noStroke();
    }

    @Override
    public void noTexture() {
        super.noTexture();
    }

    @Override
    public void noTint() {
        super.noTint();
    }

    @Override
    public float noise(float x) {
        return super.noise(x);
    }

    @Override
    public float noise(float x, float y) {
        return super.noise(x, y);
    }

    @Override
    public float noise(float x, float y, float z) {
        return super.noise(x, y, z);
    }

    @Override
    public void noiseDetail(int lod) {
        super.noiseDetail(lod);
    }

    @Override
    public void noiseDetail(int lod, float falloff) {
        super.noiseDetail(lod, falloff);
    }

    @Override
    public void noiseSeed(long seed) {
        super.noiseSeed(seed);
    }

    @Override
    public void normal(float nx, float ny, float nz) {
        super.normal(nx, ny, nz);
    }

    @Override
    public void orientation(int which) {
        super.orientation(which);
    }

    @Override
    public void ortho() {
        super.ortho();
    }

    @Override
    public void ortho(float left, float right, float bottom, float top) {
        super.ortho(left, right, bottom, top);
    }

    @Override
    public void ortho(float left, float right, float bottom, float top, float near, float far) {
        super.ortho(left, right, bottom, top, near, far);
    }

    @Override
    public JSONArray parseJSONArray(String input) {
        return super.parseJSONArray(input);
    }

    @Override
    public JSONObject parseJSONObject(String input) {
        return super.parseJSONObject(input);
    }

    @Override
    public XML parseXML(String xmlString) {
        return super.parseXML(xmlString);
    }

    @Override
    public XML parseXML(String xmlString, String options) {
        return super.parseXML(xmlString, options);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void perspective() {
        super.perspective();
    }

    @Override
    public void perspective(float fovy, float aspect, float zNear, float zFar) {
        super.perspective(fovy, aspect, zNear, zFar);
    }

    @Override
    public void pixelDensity(int density) {
        super.pixelDensity(density);
    }

    @Override
    public void point(float x, float y) {
        super.point(x, y);
    }

    @Override
    public void point(float x, float y, float z) {
        super.point(x, y, z);
    }

    @Override
    public void pointLight(float v1, float v2, float v3, float x, float y, float z) {
        super.pointLight(v1, v2, v3, x, y, z);
    }

    @Override
    public void pop() {
        super.pop();
    }

    @Override
    public void popMatrix() {
        super.popMatrix();
    }

    @Override
    public void popStyle() {
        super.popStyle();
    }

    @Override
    public void postEvent(Event pe) {
        super.postEvent(pe);
    }

    @Override
    public void printCamera() {
        super.printCamera();
    }

    @Override
    public void printMatrix() {
        super.printMatrix();
    }

    @Override
    public void printProjection() {
        super.printProjection();
    }

    @Override
    public void push() {
        super.push();
    }

    @Override
    public void pushMatrix() {
        super.pushMatrix();
    }

    @Override
    public void pushStyle() {
        super.pushStyle();
    }

    @Override
    public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        super.quad(x1, y1, x2, y2, x3, y3, x4, y4);
    }

    @Override
    public void quadraticVertex(float cx, float cy, float x3, float y3) {
        super.quadraticVertex(cx, cy, x3, y3);
    }

    @Override
    public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3) {
        super.quadraticVertex(cx, cy, cz, x3, y3, z3);
    }

    @Override
    public void rect(float a, float b, float c, float d) {
        super.rect(a, b, c, d);
    }

    @Override
    public void rect(float a, float b, float c, float d, float r) {
        super.rect(a, b, c, d, r);
    }

    @Override
    public void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl) {
        super.rect(a, b, c, d, tl, tr, br, bl);
    }

    @Override
    public void rectMode(int mode) {
        super.rectMode(mode);
    }

    @Override
    public synchronized void redraw() {
        super.redraw();
    }

    @Override
    public void registerMethod(String methodName, Object target) {
        super.registerMethod(methodName, target);
    }

    @Override
    public PImage requestImage(String filename) {
        return super.requestImage(filename);
    }

    @Override
    public PImage requestImage(String filename, String extension) {
        return super.requestImage(filename, extension);
    }

    @Override
    public void resetMatrix() {
        super.resetMatrix();
    }

    @Override
    public void resetShader() {
        super.resetShader();
    }

    @Override
    public void resetShader(int kind) {
        super.resetShader(kind);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void rotate(float angle) {
        super.rotate(angle);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        super.rotate(angle, x, y, z);
    }

    @Override
    public void rotateX(float angle) {
        super.rotateX(angle);
    }

    @Override
    public void rotateY(float angle) {
        super.rotateY(angle);
    }

    @Override
    public void rotateZ(float angle) {
        super.rotateZ(angle);
    }

    @Override
    public void save(String filename) {
        super.save(filename);
    }

    @Override
    public void saveBytes(String filename, byte[] data) {
        super.saveBytes(filename, data);
    }

    @Override
    public File saveFile(String where) {
        return super.saveFile(where);
    }

    @Override
    public void saveFrame() {
        super.saveFrame();
    }

    @Override
    public void saveFrame(String filename) {
        super.saveFrame(filename);
    }

    @Override
    public boolean saveJSONArray(JSONArray json, String filename) {
        return super.saveJSONArray(json, filename);
    }

    @Override
    public boolean saveJSONArray(JSONArray json, String filename, String options) {
        return super.saveJSONArray(json, filename, options);
    }

    @Override
    public boolean saveJSONObject(JSONObject json, String filename) {
        return super.saveJSONObject(json, filename);
    }

    @Override
    public boolean saveJSONObject(JSONObject json, String filename, String options) {
        return super.saveJSONObject(json, filename, options);
    }

    @Override
    public String savePath(String where) {
        return super.savePath(where);
    }

    @Override
    public boolean saveStream(String target, String source) {
        return super.saveStream(target, source);
    }

    @Override
    public boolean saveStream(File target, String source) {
        return super.saveStream(target, source);
    }

    @Override
    public boolean saveStream(String target, InputStream source) {
        return super.saveStream(target, source);
    }

    @Override
    public void saveStrings(String filename, String[] data) {
        super.saveStrings(filename, data);
    }

    @Override
    public boolean saveTable(Table table, String filename) {
        return super.saveTable(table, filename);
    }

    @Override
    public boolean saveTable(Table table, String filename, String options) {
        return super.saveTable(table, filename, options);
    }

    @Override
    public boolean saveXML(XML xml, String filename) {
        return super.saveXML(xml, filename);
    }

    @Override
    public boolean saveXML(XML xml, String filename, String options) {
        return super.saveXML(xml, filename, options);
    }

    @Override
    public void scale(float s) {
        super.scale(s);
    }

    @Override
    public void scale(float x, float y) {
        super.scale(x, y);
    }

    @Override
    public void scale(float x, float y, float z) {
        super.scale(x, y, z);
    }

    @Override
    public float screenX(float x, float y) {
        return super.screenX(x, y);
    }

    @Override
    public float screenX(float x, float y, float z) {
        return super.screenX(x, y, z);
    }

    @Override
    public float screenY(float x, float y) {
        return super.screenY(x, y);
    }

    @Override
    public float screenY(float x, float y, float z) {
        return super.screenY(x, y, z);
    }

    @Override
    public float screenZ(float x, float y, float z) {
        return super.screenZ(x, y, z);
    }

    @Override
    public void selectFolder(String prompt, String callback) {
        super.selectFolder(prompt, callback);
    }

    @Override
    public void selectFolder(String prompt, String callback, File file) {
        super.selectFolder(prompt, callback, file);
    }

    @Override
    public void selectFolder(String prompt, String callback, File file, Object callbackObject) {
        super.selectFolder(prompt, callback, file, callbackObject);
    }

    @Override
    public void selectInput(String prompt, String callback) {
        super.selectInput(prompt, callback);
    }

    @Override
    public void selectInput(String prompt, String callback, File file) {
        super.selectInput(prompt, callback, file);
    }

    @Override
    public void selectInput(String prompt, String callback, File file, Object callbackObject) {
        super.selectInput(prompt, callback, file, callbackObject);
    }

    @Override
    public void selectOutput(String prompt, String callback) {
        super.selectOutput(prompt, callback);
    }

    @Override
    public void selectOutput(String prompt, String callback, File file) {
        super.selectOutput(prompt, callback, file);
    }

    @Override
    public void selectOutput(String prompt, String callback, File file, Object callbackObject) {
        super.selectOutput(prompt, callback, file, callbackObject);
    }

    @Override
    public void set(int x, int y, int c) {
        super.set(x, y, c);
    }

    @Override
    public void set(int x, int y, PImage img) {
        super.set(x, y, img);
    }

    @Override
    public void setMatrix(PMatrix source) {
        super.setMatrix(source);
    }

    @Override
    public void setMatrix(PMatrix2D source) {
        super.setMatrix(source);
    }

    @Override
    public void setMatrix(PMatrix3D source) {
        super.setMatrix(source);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    @Override
    public void settings() {
        super.settings();
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void shader(PShader shader) {
        super.shader(shader);
    }

    @Override
    public void shader(PShader shader, int kind) {
        super.shader(shader, kind);
    }

    @Override
    public void shape(PShape shape) {
        super.shape(shape);
    }

    @Override
    public void shape(PShape shape, float x, float y) {
        super.shape(shape, x, y);
    }

    @Override
    public void shape(PShape shape, float a, float b, float c, float d) {
        super.shape(shape, a, b, c, d);
    }

    @Override
    public void shapeMode(int mode) {
        super.shapeMode(mode);
    }

    @Override
    public void shearX(float angle) {
        super.shearX(angle);
    }

    @Override
    public void shearY(float angle) {
        super.shearY(angle);
    }

    @Override
    public void shininess(float shine) {
        super.shininess(shine);
    }

    @Override
    public void size(int width, int height) {
        super.size(width, height);
    }

    @Override
    public void size(int width, int height, String renderer) {
        super.size(width, height, renderer);
    }

    @Override
    public void size(int width, int height, String renderer, String path) {
        super.size(width, height, renderer, path);
    }

    @Override
    public File sketchFile(String where) {
        return super.sketchFile(where);
    }

    @Override
    public String sketchPath() {
        return super.sketchPath();
    }

    @Override
    public String sketchPath(String where) {
        return super.sketchPath(where);
    }

    @Override
    public void smooth() {
        super.smooth();
    }

    @Override
    public void smooth(int level) {
        super.smooth(level);
    }

    @Override
    public void specular(int rgb) {
        super.specular(rgb);
    }

    @Override
    public void specular(float gray) {
        super.specular(gray);
    }

    @Override
    public void specular(float v1, float v2, float v3) {
        super.specular(v1, v2, v3);
    }

    @Override
    public void sphere(float r) {
        super.sphere(r);
    }

    @Override
    public void sphereDetail(int res) {
        super.sphereDetail(res);
    }

    @Override
    public void sphereDetail(int ures, int vres) {
        super.sphereDetail(ures, vres);
    }

    @Override
    public void spotLight(float v1, float v2, float v3, float x, float y, float z, float nx, float ny, float nz,
            float angle, float concentration) {
        super.spotLight(v1, v2, v3, x, y, z, nx, ny, nz, angle, concentration);
    }

    @Override
    public void square(float x, float y, float extent) {
        super.square(x, y, extent);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void stroke(int rgb) {
        super.stroke(rgb);
    }

    @Override
    public void stroke(float gray) {
        super.stroke(gray);
    }

    @Override
    public void stroke(int rgb, float alpha) {
        super.stroke(rgb, alpha);
    }

    @Override
    public void stroke(float gray, float alpha) {
        super.stroke(gray, alpha);
    }

    @Override
    public void stroke(float v1, float v2, float v3) {
        super.stroke(v1, v2, v3);
    }

    @Override
    public void stroke(float v1, float v2, float v3, float alpha) {
        super.stroke(v1, v2, v3, alpha);
    }

    @Override
    public void strokeCap(int cap) {
        super.strokeCap(cap);
    }

    @Override
    public void strokeJoin(int join) {
        super.strokeJoin(join);
    }

    @Override
    public void strokeWeight(float weight) {
        super.strokeWeight(weight);
    }

    @Override
    public void style(PStyle s) {
        super.style(s);
    }

    @Override
    public void text(char c, float x, float y) {
        super.text(c, x, y);
    }

    @Override
    public void text(String str, float x, float y) {
        super.text(str, x, y);
    }

    @Override
    public void text(int num, float x, float y) {
        super.text(num, x, y);
    }

    @Override
    public void text(float num, float x, float y) {
        super.text(num, x, y);
    }

    @Override
    public void text(char c, float x, float y, float z) {
        super.text(c, x, y, z);
    }

    @Override
    public void text(String str, float x, float y, float z) {
        super.text(str, x, y, z);
    }

    @Override
    public void text(int num, float x, float y, float z) {
        super.text(num, x, y, z);
    }

    @Override
    public void text(float num, float x, float y, float z) {
        super.text(num, x, y, z);
    }

    @Override
    public void text(char[] chars, int start, int stop, float x, float y) {
        super.text(chars, start, stop, x, y);
    }

    @Override
    public void text(String str, float x1, float y1, float x2, float y2) {
        super.text(str, x1, y1, x2, y2);
    }

    @Override
    public void text(char[] chars, int start, int stop, float x, float y, float z) {
        super.text(chars, start, stop, x, y, z);
    }

    @Override
    public void textAlign(int alignX) {
        super.textAlign(alignX);
    }

    @Override
    public void textAlign(int alignX, int alignY) {
        super.textAlign(alignX, alignY);
    }

    @Override
    public float textAscent() {
        return super.textAscent();
    }

    @Override
    public float textDescent() {
        return super.textDescent();
    }

    @Override
    public void textFont(PFont which) {
        super.textFont(which);
    }

    @Override
    public void textFont(PFont which, float size) {
        super.textFont(which, size);
    }

    @Override
    public void textLeading(float leading) {
        super.textLeading(leading);
    }

    @Override
    public void textMode(int mode) {
        super.textMode(mode);
    }

    @Override
    public void textSize(float size) {
        super.textSize(size);
    }

    @Override
    public float textWidth(char c) {
        return super.textWidth(c);
    }

    @Override
    public float textWidth(String str) {
        return super.textWidth(str);
    }

    @Override
    public float textWidth(char[] chars, int start, int length) {
        return super.textWidth(chars, start, length);
    }

    @Override
    public void texture(PImage image) {
        super.texture(image);
    }

    @Override
    public void textureMode(int mode) {
        super.textureMode(mode);
    }

    @Override
    public void textureWrap(int wrap) {
        super.textureWrap(wrap);
    }

    @Override
    public void thread(String name) {
        super.thread(name);
    }

    @Override
    public void tint(int rgb) {
        super.tint(rgb);
    }

    @Override
    public void tint(float gray) {
        super.tint(gray);
    }

    @Override
    public void tint(int rgb, float alpha) {
        super.tint(rgb, alpha);
    }

    @Override
    public void tint(float gray, float alpha) {
        super.tint(gray, alpha);
    }

    @Override
    public void tint(float v1, float v2, float v3) {
        super.tint(v1, v2, v3);
    }

    @Override
    public void tint(float v1, float v2, float v3, float alpha) {
        super.tint(v1, v2, v3, alpha);
    }

    @Override
    public void translate(float x, float y) {
        super.translate(x, y);
    }

    @Override
    public void translate(float x, float y, float z) {
        super.translate(x, y, z);
    }

    @Override
    public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        super.triangle(x1, y1, x2, y2, x3, y3);
    }

    @Override
    public void unregisterMethod(String name, Object target) {
        super.unregisterMethod(name, target);
    }

    @Override
    public void updatePixels() {
        super.updatePixels();
    }

    @Override
    public void updatePixels(int x1, int y1, int x2, int y2) {
        super.updatePixels(x1, y1, x2, y2);
    }

    @Override
    public void vertex(float[] v) {
        super.vertex(v);
    }

    @Override
    public void vertex(float x, float y) {
        super.vertex(x, y);
    }

    @Override
    public void vertex(float x, float y, float z) {
        super.vertex(x, y, z);
    }

    @Override
    public void vertex(float x, float y, float u, float v) {
        super.vertex(x, y, u, v);
    }

    @Override
    public void vertex(float x, float y, float z, float u, float v) {
        super.vertex(x, y, z, u, v);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // region `protected` methods.
    @Override
    protected PGraphics createPrimaryGraphics() {
        return super.createPrimaryGraphics();
    }

    @Override
    protected void dequeueEvents() {
        super.dequeueEvents();
    }

    @Override
    protected void handleKeyEvent(KeyEvent event) {
        super.handleKeyEvent(event);
    }

    @Override
    protected void handleMethods(String methodName) {
        super.handleMethods(methodName);
    }

    @Override
    protected void handleMethods(String methodName, Object[] args) {
        super.handleMethods(methodName, args);
    }

    @Override
    protected void handleMouseEvent(MouseEvent event) {
        super.handleMouseEvent(event);
    }

    @Override
    protected PSurface initSurface() {
        return super.initSurface();
    }

    @Override
    protected PImage loadImageIO(String filename) {
        return super.loadImageIO(filename);
    }

    @Override
    protected PImage loadImageTGA(String filename) throws IOException {
        return super.loadImageTGA(filename);
    }

    @Override
    protected PGraphics makeGraphics(int w, int h, String renderer, String path, boolean primary) {
        return super.makeGraphics(w, h, renderer, path, primary);
    }

    @Override
    protected void printStackTrace(Throwable t) {
        super.printStackTrace(t);
    }

    @Override
    protected void runSketch() {
        super.runSketch();
    }

    @Override
    protected void runSketch(String[] args) {
        super.runSketch(args);
    }

    @Override
    protected void showSurface() {
        super.showSurface();
    }

    @Override
    protected void startSurface() {
        super.startSurface();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    @Deprecated
    protected void finalize() throws Throwable {
        super.finalize();
    }
    // endregion

}
