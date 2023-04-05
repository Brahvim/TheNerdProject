package com.brahvim.nerd.papplet_wrapper.ext;

import com.brahvim.nerd.papplet_wrapper.CustomSketchBuilder;

public abstract class NerdExt {

	protected final CustomSketchBuilder BUILDER;

	public NerdExt(final CustomSketchBuilder p_builder) {
		this.BUILDER = p_builder;
		this.addLibrary();
	}

	public abstract NerdExt getLibraryObject();

	protected abstract void addLibrary();

}
