package com.brahvim.nerd.processing_wrapper.graphics_backends.interfaces;

import java.awt.Image;

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
import processing.opengl.PGL;
import processing.opengl.PShader;

public interface GraphicsOnlyItf {

        void ambient(int rgb);

        void ambient(float gray);

        void ambient(float v1, float v2, float v3);

        void ambientLight(float v1, float v2, float v3);

        void ambientLight(float v1, float v2, float v3, float x, float y, float z);

        void applyMatrix(PMatrix source);

        void applyMatrix(PMatrix2D source);

        void applyMatrix(PMatrix3D source);

        void applyMatrix(float n00, float n01, float n02, float n10, float n11, float n12);

        void applyMatrix(float n00, float n01, float n02, float n03, float n10, float n11, float n12, float n13,
                        float n20, float n21, float n22, float n23, float n30, float n31, float n32, float n33);

        void arc(float a, float b, float c, float d, float start, float stop);

        void arc(float a, float b, float c, float d, float start, float stop, int mode);

        void attrib(String name, float... values);

        void attrib(String name, int... values);

        void attrib(String name, boolean... values);

        void attribColor(String name, int color);

        void attribNormal(String name, float nx, float ny, float nz);

        void attribPosition(String name, float x, float y, float z);

        void background(int rgb);

        void background(float gray);

        void background(PImage image);

        void background(int rgb, float alpha);

        void background(float gray, float alpha);

        void background(float v1, float v2, float v3);

        void background(float v1, float v2, float v3, float alpha);

        void beginCamera();

        void beginContour();

        void beginDraw();

        PGL beginPGL();

        void beginRaw(PGraphics rawGraphics);

        void beginShape();

        void beginShape(int kind);

        void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

        void bezier(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,
                        float x4, float y4, float z4);

        void bezierDetail(int detail);

        float bezierPoint(float a, float b, float c, float d, float t);

        float bezierTangent(float a, float b, float c, float d, float t);

        void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4);

        void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4);

        void blendMode(int mode);

        void box(float size);

        void box(float w, float h, float d);

        void camera();

        void camera(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX,
                        float upY, float upZ);

        void circle(float x, float y, float extent);

        void clear();

        void clip(float a, float b, float c, float d);

        void colorMode(int mode);

        void colorMode(int mode, float max);

        void colorMode(int mode, float max1, float max2, float max3);

        void colorMode(int mode, float max1, float max2, float max3, float maxA);

        PShape createShape();

        PShape createShape(int type);

        PShape createShape(int kind, float... p);

        PSurface createSurface();

        void curve(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

        void curve(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3,
                        float x4, float y4, float z4);

        void curveDetail(int detail);

        float curvePoint(float a, float b, float c, float d, float t);

        float curveTangent(float a, float b, float c, float d, float t);

        void curveTightness(float tightness);

        void curveVertex(float x, float y);

        void curveVertex(float x, float y, float z);

        void directionalLight(float v1, float v2, float v3, float nx, float ny, float nz);

        boolean displayable();

        void dispose();

        void edge(boolean edge);

        void ellipse(float a, float b, float c, float d);

        void ellipseMode(int mode);

        void emissive(int rgb);

        void emissive(float gray);

        void emissive(float v1, float v2, float v3);

        void endCamera();

        void endContour();

        void endDraw();

        void endPGL();

        void endRaw();

        void endShape();

        void endShape(int mode);

        void fill(int rgb);

        void fill(float gray);

        void fill(int rgb, float alpha);

        void fill(float gray, float alpha);

        void fill(float v1, float v2, float v3);

        void fill(float v1, float v2, float v3, float alpha);

        void filter(PShader shader);

        void flush();

        void frustum(float left, float right, float bottom, float top, float near, float far);

        Object getCache(PImage image);

        PMatrix getMatrix();

        PMatrix2D getMatrix(PMatrix2D target);

        PMatrix3D getMatrix(PMatrix3D target);

        PGraphics getRaw();

        PStyle getStyle();

        PStyle getStyle(PStyle s);

        boolean haveRaw();

        void hint(int which);

        void image(PImage img, float a, float b);

        void image(PImage img, float a, float b, float c, float d);

        void image(PImage img, float a, float b, float c, float d, int u1, int v1, int u2, int v2);

        void imageMode(int mode);

        boolean is2D();

        boolean is2X();

        boolean is3D();

        boolean isGL();

        int lerpColor(int c1, int c2, float amt);

        void lightFalloff(float constant, float linear, float quadratic);

        void lightSpecular(float v1, float v2, float v3);

        void lights();

        void line(float x1, float y1, float x2, float y2);

        void line(float x1, float y1, float z1, float x2, float y2, float z2);

        PShader loadShader(String fragFilename);

        PShader loadShader(String fragFilename, String vertFilename);

        PShape loadShape(String filename);

        PShape loadShape(String filename, String options);

        float modelX(float x, float y, float z);

        float modelY(float x, float y, float z);

        float modelZ(float x, float y, float z);

        void noClip();

        void noFill();

        void noLights();

        void noSmooth();

        void noStroke();

        void noTexture();

        void noTint();

        void normal(float nx, float ny, float nz);

        void ortho();

        void ortho(float left, float right, float bottom, float top);

        void ortho(float left, float right, float bottom, float top, float near, float far);

        void perspective();

        void perspective(float fovy, float aspect, float zNear, float zFar);

        void point(float x, float y);

        void point(float x, float y, float z);

        void pointLight(float v1, float v2, float v3, float x, float y, float z);

        void pop();

        void popMatrix();

        void popStyle();

        void printCamera();

        void printMatrix();

        void printProjection();

        void push();

        void pushMatrix();

        void pushStyle();

        void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

        void quadraticVertex(float cx, float cy, float x3, float y3);

        void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3);

        void rect(float a, float b, float c, float d);

        void rect(float a, float b, float c, float d, float r);

        void rect(float a, float b, float c, float d, float tl, float tr, float br, float bl);

        void rectMode(int mode);

        void removeCache(PImage image);

        void resetMatrix();

        void resetShader();

        void resetShader(int kind);

        void rotate(float angle);

        void rotate(float angle, float x, float y, float z);

        void rotateX(float angle);

        void rotateY(float angle);

        void rotateZ(float angle);

        boolean save(String filename);

        void scale(float s);

        void scale(float x, float y);

        void scale(float x, float y, float z);

        float screenX(float x, float y);

        float screenX(float x, float y, float z);

        float screenY(float x, float y);

        float screenY(float x, float y, float z);

        float screenZ(float x, float y, float z);

        void setCache(PImage image, Object storage);

        void setMatrix(PMatrix source);

        void setMatrix(PMatrix2D source);

        void setMatrix(PMatrix3D source);

        void setParent(PApplet parent);

        void setPath(String path);

        void setPrimary(boolean primary);

        void setSize(int w, int h);

        void shader(PShader shader);

        void shader(PShader shader, int kind);

        void shape(PShape shape);

        void shape(PShape shape, float x, float y);

        void shape(PShape shape, float a, float b, float c, float d);

        void shapeMode(int mode);

        void shearX(float angle);

        void shearY(float angle);

        void shininess(float shine);

        void smooth();

        void smooth(int quality);

        void specular(int rgb);

        void specular(float gray);

        void specular(float v1, float v2, float v3);

        void sphere(float r);

        void sphereDetail(int res);

        void sphereDetail(int ures, int vres);

        void spotLight(float v1, float v2, float v3, float x, float y, float z, float nx, float ny, float nz,
                        float angle, float concentration);

        void square(float x, float y, float extent);

        void stroke(int rgb);

        void stroke(float gray);

        void stroke(int rgb, float alpha);

        void stroke(float gray, float alpha);

        void stroke(float v1, float v2, float v3);

        void stroke(float v1, float v2, float v3, float alpha);

        void strokeCap(int cap);

        void strokeJoin(int join);

        void strokeWeight(float weight);

        void style(PStyle s);

        void text(char c, float x, float y);

        void text(String str, float x, float y);

        void text(int num, float x, float y);

        void text(float num, float x, float y);

        void text(char c, float x, float y, float z);

        void text(String str, float x, float y, float z);

        void text(int num, float x, float y, float z);

        void text(float num, float x, float y, float z);

        void text(char[] chars, int start, int stop, float x, float y);

        void text(String str, float x1, float y1, float x2, float y2);

        void text(char[] chars, int start, int stop, float x, float y, float z);

        void textAlign(int alignX);

        void textAlign(int alignX, int alignY);

        float textAscent();

        float textDescent();

        void textFont(PFont which);

        void textFont(PFont which, float size);

        void textLeading(float leading);

        void textMode(int mode);

        void textSize(float size);

        float textWidth(char c);

        float textWidth(String str);

        float textWidth(char[] chars, int start, int length);

        void texture(PImage image);

        void textureMode(int mode);

        void textureWrap(int wrap);

        void tint(int rgb);

        void tint(float gray);

        void tint(int rgb, float alpha);

        void tint(float gray, float alpha);

        void tint(float v1, float v2, float v3);

        void tint(float v1, float v2, float v3, float alpha);

        void translate(float x, float y);

        void translate(float x, float y, float z);

        void triangle(float x1, float y1, float x2, float y2, float x3, float y3);

        void vertex(float[] v);

        void vertex(float x, float y);

        void vertex(float x, float y, float z);

        void vertex(float x, float y, float u, float v);

        void vertex(float x, float y, float z, float u, float v);

        void blend(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode);

        void blend(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh, int mode);

        Object clone() throws CloneNotSupportedException;

        PImage copy();

        void copy(int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh);

        void copy(PImage src, int sx, int sy, int sw, int sh, int dx, int dy, int dw, int dh);

        void filter(int kind);

        void filter(int kind, float param);

        PImage get();

        int get(int x, int y);

        PImage get(int x, int y, int w, int h);

        Image getImage();

        int getModifiedX1();

        int getModifiedX2();

        int getModifiedY1();

        int getModifiedY2();

        Object getNative();

        void init(int width, int height, int format);

        void init(int width, int height, int format, int factor);

        boolean isLoaded();

        boolean isModified();

        void loadPixels();

        void mask(int[] maskArray);

        void mask(PImage img);

        void resize(int w, int h);

        void set(int x, int y, int c);

        void set(int x, int y, PImage img);

        void setLoaded();

        void setLoaded(boolean l);

        void setModified();

        void setModified(boolean m);

        void updatePixels();

        void updatePixels(int x, int y, int w, int h);

        @Override
        boolean equals(Object obj);

        @Override
        int hashCode();

        @Override
        String toString();

}
