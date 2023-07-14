package com.brahvim.nerd.framework.ecs;

import com.brahvim.nerd.processing_wrapper.NerdModuleSettings;

public class NerdEcsModuleSettings extends NerdModuleSettings<NerdEcsModule> {

	public Class<? extends NerdEcsSystem<?>>[] ecsSystemsOrder;

}
