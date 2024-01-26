package com.brahvim.nerd.math.timing;

import java.io.PrintStream;
import java.util.function.Supplier;

public class NerdMillisTimer extends NerdAbstractTimer {

	// region Constructors.
	public NerdMillisTimer(final String p_eventNameToLog) {
		super(p_eventNameToLog);
	}

	public NerdMillisTimer(final String p_eventNameToLog, final PrintStream p_streamToLogTo) {
		super(p_eventNameToLog, p_streamToLogTo);
	}

	// public NerdMillisTimer() {
	// super();
	// // this.restart();
	// }
	// endregion

	@Override
	public Supplier<Long> supplyTimeProviderFunction() {
		return System::currentTimeMillis;
	}

	@Override
	public String supplyUnitName() {
		return "ms";
	}

}
