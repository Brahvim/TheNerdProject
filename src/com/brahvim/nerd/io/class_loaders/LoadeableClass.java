package com.brahvim.nerd.io.class_loaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class LoadeableClass<ClassT> {

	// region Fields.
	public URL url;
	public String qualifiedName;

	private static final ArrayList<LoadeableClass<?>> ALL_LOADEABLE_CLASSES = new ArrayList<>();

	private Class<? extends ClassT> loadedClass;
	// endregion

	// region Constructors.
	public LoadeableClass(final URL p_url, final String p_fullyQualifiedName) {
		this.loadClass(p_url, p_fullyQualifiedName);
	}

	public LoadeableClass(final File p_jarOrClassFolder, final String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder.getAbsolutePath()), p_fullyQualifiedName);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public LoadeableClass(final String p_jarOrClassFolder, final String p_fullyQualifiedName) {
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

		final URLClassLoader LOADER = new URLClassLoader(
				this.qualifiedName, new URL[] { this.url },
				ClassLoader.getSystemClassLoader());

		try (LOADER) {
			this.loadedClass = (Class<? extends ClassT>) LOADER.loadClass(this.qualifiedName);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		LoadeableClass.ALL_LOADEABLE_CLASSES.add(this);

	}

	public Class<? extends ClassT> getLoadedClass() {
		return this.loadedClass;
	}

	public URL getUrl() {
		return this.url;
	}
	// endregion

}
