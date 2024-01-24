package com.brahvim.nerd.processing_wrapper;

public abstract class NerdModuleSettings<ModuleT extends NerdModule> { // NOSONAR

    @SuppressWarnings("unchecked")
    public <RetModuleClassT extends ModuleT> Class<RetModuleClassT> getModuleClass() {
        return (Class<RetModuleClassT>) NerdModule.class;
    }

}
