package com.brahvim.nerd.io.class_loaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.brahvim.nerd.framework.scene_layer_api.NerdScene;

import processing.core.PGraphics;

public class NerdLoadableClass<ClassT> {

	// region Fields.
	private static final List<NerdLoadableClass<?>> ALL_LOADABLE_CLASSES = new ArrayList<>();

	public URL url;
	public String qualifiedName;

	private Class<? extends ClassT> loadedClass;
	// endregion

	// region Constructors.
	public NerdLoadableClass(final URL p_url, final String p_fullyQualifiedName) {
		this.loadClass(p_url, p_fullyQualifiedName);
	}

	public NerdLoadableClass(final File p_jarOrClassFolder, final String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder.getAbsolutePath()), p_fullyQualifiedName);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public NerdLoadableClass(final String p_jarOrClassFolder, final String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder), p_fullyQualifiedName);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}
	// endregion

	// region Methods.
	@SuppressWarnings("unchecked")
	public void loadClass(final URL p_url, final String p_fullyQualifiedName) {
		this.url = p_url;
		this.qualifiedName = p_fullyQualifiedName;

		final URLClassLoader loader = new URLClassLoader(
				this.qualifiedName, new URL[] { this.url },
				ClassLoader.getSystemClassLoader());

		try (loader) { // ...Not putting that huge statement in here, y'know? :P
			this.loadedClass = (Class<? extends ClassT>) loader.loadClass(this.qualifiedName);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		NerdLoadableClass.ALL_LOADABLE_CLASSES.add(this);
	}

	@SuppressWarnings("unchecked")
	public <RetScenePGraphicsT extends PGraphics> Class<? extends NerdScene<RetScenePGraphicsT>> getLoadedClass() {
		return (Class<NerdScene<RetScenePGraphicsT>>) this.loadedClass;
	}

	public URL getUrl() {
		return this.url;
	}
	// endregion

}
