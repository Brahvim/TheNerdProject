package com.brahvim.nerd.utils;

public class NerdSingleAssignmentBox<T> {

    final T OBJECT;

    public NerdSingleAssignmentBox(final T p_object) {
        this.OBJECT = p_object;
    }

    public T getObject() {
        return this.OBJECT;
    }

}
