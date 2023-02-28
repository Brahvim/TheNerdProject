package com.brahvim.nerd.openal.al_ext_efx.al_filter;

import com.brahvim.nerd.openal.AlSource;
import com.brahvim.nerd.openal.NerdAl;

public class AlAuxiliarySendFilter extends AlFilter {

	public AlAuxiliarySendFilter(NerdAl p_alMan) {
		super(p_alMan);
	}

	// region Mass source attachment.
	public void attachToSources(AlSource... p_sources) {
		for (AlSource s : p_sources)
			if (s != null)
				s.attachAuxiliarySendFilter(this);
	}

	public void detachFromSources(AlSource... p_sources) {
		for (AlSource s : p_sources)
			if (s != null)
				if (s.getAuxiliarySendFilter() == this)
					s.detachAuxiliarySendFilter();
	}
	// endregion

}
