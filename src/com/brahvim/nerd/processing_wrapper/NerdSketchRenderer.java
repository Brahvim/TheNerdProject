package com.brahvim.nerd.processing_wrapper;

import processing.core.PConstants;

public enum NerdSketchRenderer {

	P3D(),
	P2D(),
	PDF(),
	SVG(),
	FX2D(),
	JAVA2D(),
	GENERIC();

	// region Class stuff.
	public String toPConstant() {
		return switch (this) {
			case GENERIC -> "";
			case P3D -> PConstants.P3D;
			case P2D -> PConstants.P2D;
			case PDF -> PConstants.PDF;
			case SVG -> PConstants.SVG;
			case FX2D -> PConstants.FX2D;
			case JAVA2D -> PConstants.JAVA2D;
		};
	}
	// endregion

}
