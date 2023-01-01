package com.brahvim.nerd.api;

import processing.core.PVector;

// "pls no instance" ; ~ ;)
public abstract class VecUtilsPVector {
    public static PVector normalize(PVector p_vec) {
        return new PVector(p_vec.x, p_vec.y, p_vec.z).normalize();
    }

    public static PVector rotate(PVector p_vec, float p_angleRad) {
        PVector ret = new PVector(p_vec.x, p_vec.y, p_vec.z);
        ret.rotate(p_angleRad);
        return ret;
    }

    // region `vecLerp()` overloads.
    public static PVector vecLerp(PVector p_from, PVector p_to, float p_lerpAmt) {
        return new PVector(p_from.x + (p_to.x - p_from.x) * p_lerpAmt,
                p_from.y + (p_to.y - p_from.y) * p_lerpAmt,
                p_from.z + (p_to.z - p_from.z) * p_lerpAmt);
    }

    public static void vecLerp(PVector p_from, PVector p_to, float p_lerpAmt, PVector p_out) {
        if (p_out == null)
            p_out = new PVector();
        // ...this method remains unused in the engine. It's for users! :sparkles:

        p_out.set(p_from.x + (p_to.x - p_from.x) * p_lerpAmt,
                p_from.y + (p_to.y - p_from.y) * p_lerpAmt,
                p_from.z + (p_to.z - p_from.z) * p_lerpAmt);
    }
    // endregion

}
