package com.brahvim.nerd.io.class_loaders;

import com.brahvim.nerd.scene_api.NerdScene;

public class NerdSceneClassLoader extends NerdLoadeableClass<NerdScene> {

	public NerdSceneClassLoader(final String p_urlString, final String p_fullyQualifiedName) {
		super(p_urlString, p_fullyQualifiedName);
	}

}