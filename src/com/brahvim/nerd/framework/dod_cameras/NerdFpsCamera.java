package com.brahvim.nerd.framework.dod_cameras;

public class NerdFpsCamera extends NerdFlyCamera {

    private float height;

    public float getHeight() {
        return this.height;
    }

    public void moveY(final float p_velY) {
        this.height += p_velY;
    }

    public void setHeight(final float p_height) {
        this.height = p_height;
    }

}
