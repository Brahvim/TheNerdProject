package com.brahvim.nerd.openal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;

// [https://www.oracle.com/technical-resources/articles/javase/finalization.html]
public final class AlNativeResource extends PhantomReference<AlResourceHolder> {
    public static int numResourcesToGc = 5;
    public final static ArrayList<AlNativeResource> RESOURCES = new ArrayList<>();

    private Runnable onGc;
    private final static ReferenceQueue<AlResourceHolder> REFERENCE_QUEUE = new ReferenceQueue<>();

    public AlNativeResource(AlResourceHolder p_reference, Runnable p_onGc) {
        super(p_reference, AlNativeResource.REFERENCE_QUEUE);
        AlNativeResource.RESOURCES.add(this);
        this.onGc = p_onGc;
    }

    public static void performNativeGc() {
        for (int i = 0; i != AlNativeResource.numResourcesToGc; i++) {
            AlNativeResource nativeObject = (AlNativeResource) AlNativeResource.REFERENCE_QUEUE.poll();
            if (nativeObject == null) {
                System.gc();
                break;
            }
            nativeObject.onGc.run();
        }
    }

}