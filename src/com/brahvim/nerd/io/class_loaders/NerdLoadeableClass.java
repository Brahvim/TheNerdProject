package com.brahvim.nerd.io.class_loaders;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class NerdLoadeableClass<ClassT> {

	// region Fields.
	private static final ArrayList<NerdLoadeableClass<?>> ALL_LOADEABLE_CLASSES = new ArrayList<>();

	public URL url;
	public String qualifiedName;

	private Class<? extends ClassT> loadedClass;
	// endregion

	// region Constructors.
	public NerdLoadeableClass(final URL p_url, final String p_fullyQualifiedName) {
		this.loadClass(p_url, p_fullyQualifiedName);
	}

	public NerdLoadeableClass(final File p_jarOrClassFolder, final String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder.getAbsolutePath()), p_fullyQualifiedName);
		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public NerdLoadeableClass(final String p_jarOrClassFolder, final String p_fullyQualifiedName) {
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

		try (loader) {
			this.loadedClass = (Class<? extends ClassT>) loader.loadClass(this.qualifiedName);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		NerdLoadeableClass.ALL_LOADEABLE_CLASSES.add(this);
	}

	public Class<? extends ClassT> getLoadedClass() {
		return this.loadedClass;
	}

	public URL getUrl() {
		return this.url;
	}
	// endregion

}
