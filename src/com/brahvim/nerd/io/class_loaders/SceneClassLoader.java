package com.brahvim.nerd.io.class_loaders;

import com.brahvim.nerd.api.scene_api.NerdScene;

public class SceneClassLoader extends LoadeableClass<NerdScene> {

	public SceneClassLoader(final String p_urlString, final String p_fullyQualifiedName) {
		super(p_urlString, p_fullyQualifiedName);
	}

}
