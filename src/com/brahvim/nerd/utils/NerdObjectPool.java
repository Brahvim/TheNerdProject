package com.brahvim.nerd.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.UnaryOperator;

public class NerdObjectPool<PoolObjectT> {

    // region Fields.
    protected final Queue<PoolObjectT> POOL;
    protected final List<PoolObjectT> ACTIVE;

    protected UnaryOperator<PoolObjectT>
    /*   */ promotionCallback = UnaryOperator.identity(),
            demotionCallback = UnaryOperator.identity();
    // endregion.

    // region Constructors.
    public NerdObjectPool() {
        this.POOL = new ArrayDeque<>(0);
        this.ACTIVE = new ArrayList<>(0);
    }

    public NerdObjectPool(final int p_initialCapacity) {
        this.POOL = new ArrayDeque<>(p_initialCapacity);
        this.ACTIVE = new ArrayList<>(p_initialCapacity);
    }

    public NerdObjectPool(final int p_initialCapacity, final UnaryOperator<PoolObjectT> p_promotionCallback) {
        this.promotionCallback = p_promotionCallback;
        this.POOL = new ArrayDeque<>(p_initialCapacity);
        this.ACTIVE = new ArrayList<>(p_initialCapacity);
    }

    public NerdObjectPool(final int p_initialCapacity,
            final UnaryOperator<PoolObjectT> p_promotionCallback,
            final UnaryOperator<PoolObjectT> p_demotionCallback) {
        this.demotionCallback = p_demotionCallback;
        this.promotionCallback = p_promotionCallback;
        this.POOL = new ArrayDeque<>(p_initialCapacity);
        this.ACTIVE = new ArrayList<>(p_initialCapacity);
    }
    // endregion

    // region Adding stuff.
    public int promote(final int p_howMany) {
        final int toPromote = Math.min(this.POOL.size(), p_howMany);
        for (int i = 0; i < toPromote; i++)
            this.ACTIVE.add(
                    this.promotionCallback.apply(
                            this.POOL.poll()));
        return toPromote;
    }

    public void promote(final PoolObjectT p_object) {
        this.POOL.remove(p_object);
        this.ACTIVE.add(p_object);

        this.promotionCallback.apply(p_object);
    }
    // endregion

    // region Callbacks.
    public UnaryOperator<PoolObjectT> getDemotionCallback() {
        return this.demotionCallback;
    }

    public UnaryOperator<PoolObjectT> getPromotionCallback() {
        return this.promotionCallback;
    }

    public void setDemotionCallback(final UnaryOperator<PoolObjectT> p_demotionCallback) {
        Objects.requireNonNull(p_demotionCallback, "Callback object is `null`.");
        this.demotionCallback = p_demotionCallback;
    }

    public void setPromotionCallback(final UnaryOperator<PoolObjectT> p_promotionCallback) {
        Objects.requireNonNull(p_promotionCallback, "Callback object is `null`.");
        this.promotionCallback = p_promotionCallback;
    }
    // endregion

    // region Getters.
    public Queue<PoolObjectT> getDemoted() {
        return this.POOL;
    }

    public List<PoolObjectT> getPromoted() {
        return this.ACTIVE;
    }
    // endregion

    // region Removal.
    public void clearAll() {
        this.POOL.clear();
        this.ACTIVE.clear();
    }

    public void clearDemoted() {
        this.POOL.clear();
    }

    public void clearPromoted() {
        this.ACTIVE.clear();
    }

    public void demote(final PoolObjectT p_object) {
        this.ACTIVE.remove(p_object);
        this.POOL.add(p_object);

        this.demotionCallback.apply(p_object);
    }

    public void remove(final PoolObjectT p_object) {
        this.ACTIVE.remove(p_object);
        this.POOL.remove(p_object);
    }
    // endregion

    // region Status.
    public int numObjects() {
        return this.POOL.size();
    }

    public int numDemoted() {
        return this.POOL.size();
    }

    public int numPromoted() {
        return this.ACTIVE.size();
    }
    // endregion

}
