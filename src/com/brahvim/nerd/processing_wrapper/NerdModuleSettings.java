package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.utils.NerdReflectionUtils;

public abstract class NerdModuleSettings<ModuleT extends NerdModule> { // NOSONAR

    public Class<? extends ModuleT> getModuleClass() {
        return NerdReflectionUtils.getFirstTypeArg(this.getClass());
    }

}
