package com.brahvim.nerd.io.class_loaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class LoadeableClass<ClassT> {

	// region Fields.
	private final static ArrayList<LoadeableClass<?>> ALL_LOADEABLE_CLASSES = new ArrayList<>();

	public final URL URL;
	public final String QUAL_NAME;

	private Class<ClassT> loadedClass;
	// endregion

	public LoadeableClass(URL p_url, String p_fullyQualifiedName) {
		this.URL = p_url;
		this.QUAL_NAME = p_fullyQualifiedName;
		LoadeableClass.ALL_LOADEABLE_CLASSES.add(this);
	}

	public LoadeableClass(String p_urlString, String p_fullyQualifiedName) {
		URL urlToSet = null;
		try {
			urlToSet = new URL(p_urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.URL = urlToSet;
		this.QUAL_NAME = p_fullyQualifiedName;
		LoadeableClass.ALL_LOADEABLE_CLASSES.add(this);
	}

	// region Methods.
	public final static void loadClasses() {
		// region Get all URLs and construct the loader.
		final URL[] URL_ARRAY = new URL[LoadeableClass.ALL_LOADEABLE_CLASSES.size()];

		for (int i = 0; i < URL_ARRAY.length; i++)
			URL_ARRAY[i] = LoadeableClass.ALL_LOADEABLE_CLASSES.get(i).getUrl();

		final URLClassLoader LOADER = new URLClassLoader(
				URL_ARRAY, ClassLoader.getSystemClassLoader());
		// endregion

		for (LoadeableClass<?> c : LoadeableClass.ALL_LOADEABLE_CLASSES) { // Link classes using `forName()`.
			try {
				final Class<?> LOADED_CLASS = Class.forName(c.QUAL_NAME, true, LOADER);
				c.setLoadedClass(LOADED_CLASS);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	// region Getters and setters.
	public Class<ClassT> getLoadedClass() {
		return this.loadedClass;
	}

	private URL getUrl() {
		return this.URL;
	}

	@SuppressWarnings("unchecked")
	// This has to be a bad idea...
	protected void setLoadedClass(Class<?> p_class) throws ClassCastException {
		this.loadedClass = (Class<ClassT>) p_class;
	}
	// endregion
	// endregion

}
