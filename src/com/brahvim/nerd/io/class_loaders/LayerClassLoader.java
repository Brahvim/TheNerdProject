package com.brahvim.nerd.io.class_loaders;

import com.brahvim.nerd.scene_api.NerdLayer;

public class LayerClassLoader extends LoadeableClass<NerdLayer> {

	public LayerClassLoader(final String p_urlString, final String p_fullyQualifiedName) {
		super(p_urlString, p_fullyQualifiedName);
	}

}
