package com.brahvim.nerd.io.class_loaders;

import com.brahvim.nerd.scene_api.NerdScene;

public class SceneClassLoader extends LoadeableClass<NerdScene> {

	public SceneClassLoader(String p_urlString, String p_fullyQualifiedName) {
		super(p_urlString, p_fullyQualifiedName);
	}

}
