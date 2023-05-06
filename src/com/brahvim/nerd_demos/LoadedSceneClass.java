package com.brahvim.nerd_demos;

import com.brahvim.nerd.io.class_loaders.NerdSceneClassLoader;
import com.brahvim.nerd.papplet_wrapper.NerdSketch;
import com.brahvim.nerd.scene_api.NerdScene;

public enum LoadedSceneClass {

	DEMO_SCENE_5(
			// JARs only!
			"file:/" + NerdSketch.DATA_DIR_PATH + "DemoScene5.jar",
			"com.brahvim.nerd_tests.scenes.DemoScene5");

	// region Non-`enum` stuff.
	private final NerdSceneClassLoader SCENE_CLASS_LOADER;

	private LoadedSceneClass(final String p_urlString, final String p_fullyQualifiedName) {
		this.SCENE_CLASS_LOADER = new NerdSceneClassLoader(p_urlString, p_fullyQualifiedName);
	}

	public Class<? extends NerdScene> getSCENE_CLASS_LOADER() {
		return this.SCENE_CLASS_LOADER.getLoadedClass();
	}
	// endregion

}
