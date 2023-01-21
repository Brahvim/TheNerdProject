package com.brahvim.nerd.processing_wrapper;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class NerdGraphics extends PGraphics {
    public NerdGraphics() {
    }

    public void translate(PVector p_translation) {
        super.translate(p_translation.x, p_translation.y, p_translation.z);
    }

    public void perspective(NerdCamera p_cam) {
        super.perspective(p_cam.fov,
                (float) super.width / (float) super.height,
                p_cam.near, p_cam.far);
    }

    public void camera(NerdCamera p_cam) {
        super.camera(p_cam.pos.x, p_cam.pos.y, p_cam.pos.z,
                p_cam.center.x, p_cam.center.y, p_cam.center.z,
                p_cam.up.x, p_cam.up.y, p_cam.up.z);
    }

    public void ortho(NerdCamera p_cam) {
        super.ortho(-super.width * 0.5f, super.width * 0.5f, -super.height * 0.5f,
                super.height * 0.5f, p_cam.near, p_cam.far);
    }

    public void image(PImage p_image) {
        super.image(p_image, 0, 0);
    }

    @Override
    public void push() {
        super.pushMatrix();
        super.pushStyle();
    }

    @Override
    public void pop() {
        super.popStyle();
        super.popMatrix();
    }

}
