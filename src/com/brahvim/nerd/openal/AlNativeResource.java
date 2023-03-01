package com.brahvim.nerd.openal;

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

public abstract class AlNativeResource {
    public final static ArrayList<AlNativeResource> RESOURCES = new ArrayList<>();
    public final static ReferenceQueue<AlNativeResource> REFERENCE_QUEUE = new ReferenceQueue<>();

    public AlNativeResource() {
        AlNativeResource.RESOURCES.add(this);
    }

    public void disposeFromSuper() {
        this.dispose();
    }

    protected abstract void dispose();

}