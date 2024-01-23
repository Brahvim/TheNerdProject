package com.brahvim.nerd.processing_wrapper;

import com.brahvim.nerd.utils.NerdReflectionUtils;

public abstract class NerdModuleSettings<ModuleT extends NerdModule> { // NOSONAR

    @SuppressWarnings("unchecked")
    public Class<ModuleT> getModuleClass() {
        return (Class<ModuleT>) NerdReflectionUtils.getFirstTypeArg(this.getClass());
    }

}
