package com.brahvim.nerd.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

// [ http://gafter.blogspot.com/2006/12/super-type-tokens.html ]
public abstract class NerdTypeReference<T> { // NOSONAR, this is THE trick!

    protected final Type type;

    protected NerdTypeReference() {
        final Type superclass = this.getClass().getGenericSuperclass();

        if (superclass instanceof Class)
            throw new IllegalStateException("Missing type parameter.");

        final ParameterizedType parameterizedType = (ParameterizedType) superclass;
        this.type = parameterizedType.getActualTypeArguments()[0];
    }

    public Type getType() {
        return this.type;
    }

}
