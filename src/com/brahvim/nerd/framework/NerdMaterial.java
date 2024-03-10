package com.brahvim.nerd.framework;

import com.brahvim.nerd.framework.colors.NerdNoAlphaColor;

public class NerdMaterial {

    public float shininess;
    public NerdNoAlphaColor ambience, emission, specular;

    public NerdMaterial(
            final float p_shininess,
            final NerdNoAlphaColor p_ambience,
            final NerdNoAlphaColor p_emission,
            final NerdNoAlphaColor p_specular) {
        this.ambience = p_ambience;
        this.emission = p_emission;
        this.specular = p_specular;
        this.shininess = p_shininess;
    }

}
