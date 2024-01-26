package com.brahvim.nerd.math.timing;

import java.io.PrintStream;
import java.util.function.Supplier;

public class NerdNanosTimer extends NerdAbstractTimer {

	// region Constructors.
	public NerdNanosTimer(final String p_eventNameToLog) {
		super(p_eventNameToLog);
	}

	public NerdNanosTimer(final String p_eventNameToLog, final PrintStream p_streamToLogTo) {
		super(p_eventNameToLog, p_streamToLogTo);
	}

	// public NerdNanosTimer() {
	// super();
	// // this.restart();
	// }
	// endregion

	@Override
	public Supplier<Long> supplyTimeProviderFunction() {
		return System::nanoTime;
	}

	@Override
	public String supplyUnitName() {
		return "ns";
	}

}
