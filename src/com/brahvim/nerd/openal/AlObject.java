package com.brahvim.nerd.openal;

// TODO: Write this class :joy:
// TODO: Use JDK 9 "modules" to hide this!!!

import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

public abstract class AlObject {
    public final static ReferenceQueue<AlObject> REFERENCE_QUEUE = new ReferenceQueue<>();
    public final static ArrayList<AlObject> RESOURCES = new ArrayList<>();

    private boolean hasDisposed;

    public AlObject() {
        AlObject.RESOURCES.add(this);
    }

    public boolean isDisposed() {
        return this.hasDisposed;
    }

    public void dispose() {
        this.disposeImpl();
        this.hasDisposed = true;
    }

    protected abstract void disposeImpl();

}