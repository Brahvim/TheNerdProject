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

	private final static ArrayList<LoadeableClass<?>> ALL_LOADEABLE_CLASSES = new ArrayList<>();

	private Class<? extends ClassT> loadedClass;
	// endregion

	// region Constructors.
	public LoadeableClass(URL p_url, String p_fullyQualifiedName) {
		this.loadClass(p_url, p_fullyQualifiedName);
	}

	public LoadeableClass(File p_jarOrClassFolder, String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder.getAbsolutePath()), p_fullyQualifiedName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public LoadeableClass(String p_jarOrClassFolder, String p_fullyQualifiedName) {
		try {
			this.loadClass(new URL(p_jarOrClassFolder), p_fullyQualifiedName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	// endregion

	// region Methods.
	@SuppressWarnings("unchecked")
	public void loadClass(URL p_url, String p_fullyQualifiedName) {
		this.url = p_url;
		this.qualifiedName = p_fullyQualifiedName;

		final URLClassLoader LOADER = new URLClassLoader(
				this.qualifiedName, new URL[] { this.url },
				ClassLoader.getSystemClassLoader());

		try (LOADER) {
			this.loadedClass = (Class<? extends ClassT>) LOADER.loadClass(this.qualifiedName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
