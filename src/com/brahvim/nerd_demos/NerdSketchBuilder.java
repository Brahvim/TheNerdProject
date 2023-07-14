package com.brahvim.nerd_demos;

import java.util.LinkedHashSet;
import java.util.function.Function;

import com.brahvim.nerd.framework.ecs.NerdEcsModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.openal.NerdOpenAlModule;
import com.brahvim.nerd.processing_wrapper.NerdCustomSketchBuilder;
import com.brahvim.nerd.processing_wrapper.NerdModule;
import com.brahvim.nerd.processing_wrapper.NerdSketch;
import com.brahvim.nerd.processing_wrapper.NerdSketchBuilderSettings;

public class NerdSketchBuilder extends NerdCustomSketchBuilder {

	@Override
	protected NerdSketch createNerdSketch(final String[] p_javaMainArgs, final NerdSketchBuilderSettings p_settings) {
		return new NerdSketch(p_settings);
	}

	@Override
	protected void supplyUserDefinedModules(final LinkedHashSet<Function<NerdSketch, NerdModule>> p_set) {
		p_set.add(NerdEcsModule::new);
		p_set.add(NerdScenesModule::new); // Removes `NerdScenesModule` from the defaults, and puts it here!

		p_set.add(a -> new NerdOpenAlModule(a, s -> { // `a` stands for... "Applet"?
			// ...for `DemoScene3`!!!:
			s.monoSources = Integer.MAX_VALUE;
			s.stereoSources = Integer.MAX_VALUE;
		}));
	}

}
