package com.brahvim.nerd.processing_wrapper;

import java.util.LinkedHashSet;
import java.util.function.Function;

import com.brahvim.nerd.framework.ecs.NerdEcsModule;
import com.brahvim.nerd.framework.scene_api.NerdScenesModule;
import com.brahvim.nerd.processing_wrapper.window_man_subs.NerdGlWindowModule;
import com.brahvim.nerd.processing_wrapper.window_man_subs.NerdJava2dWindowModule;

public class NerdSketchBuilder extends NerdCustomSketchBuilder {

	@Override
	protected NerdSketch buildImpl(final String[] p_javaMainArgs) {
		return new NerdSketch(super.SKETCH_KEY);
	}

	@Override
	protected Function<NerdSketch, LinkedHashSet<NerdModule>> supplyNerdModulesOrder() {
		return s -> {
			final LinkedHashSet<NerdModule> toRet = new LinkedHashSet<>();
			toRet.add(new NerdDisplayModule(s, super.SKETCH_KEY));

			toRet.add(s.SKETCH_SETTINGS.USES_OPENGL
					? new NerdGlWindowModule(s, super.SKETCH_KEY)
					: new NerdJava2dWindowModule(s, super.SKETCH_KEY));

			toRet.add(new NerdInputModule(s, super.SKETCH_KEY));

			// Extensions!:
			toRet.add(new NerdCallbacksModule(s, super.SKETCH_KEY));

			// User callbacks!:
			toRet.add(new NerdCallbacksModule(s, super.SKETCH_KEY));

			// `NerdScene`s and `NerdLayer`s!:
			toRet.add(new NerdEcsModule(s, super.SKETCH_KEY));
			toRet.add(new NerdScenesModule(s, super.SKETCH_KEY));
			return toRet;
		};
	}

}
