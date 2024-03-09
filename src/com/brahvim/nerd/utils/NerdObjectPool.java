package com.brahvim.nerd.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.UnaryOperator;

/**
 * A class that abstracts away concept of object pooling.
 * It also provides a collection to pull objects from the pool.
 *
 * <br>
 * </br>
 *
 * <h3>Terminology:</h3>
 * <ul>
 * <li>To bring objects from the pool to the collection is called "promotion".
 * <li>To return objects back to the pool is called "demotion".
 * </ul>
 *
 * <br>
 * </br>
 *
 * <h3>Manner of promotion:</h3>
 * You may promote objects in two manners:
 * <ul>
 * <li>By adding them one-by-one, or,
 * <li>Specifying how many objects to promote.
 * </ul>
 *
 * <br>
 * </br>
 *
 * <h3>Callbacks:</h3>
 * Callbacks to modify or swap objects when their state of promotion changes
 * are also provided. They may be specified in constructors, or set using:
 * <ul>
 * <li>{@linkplain NerdObjectPool#setDemotionCallback(UnaryOperator)
 * NerdObjectPool::setDemotionCallback(UnaryOperator)}, and
 * <li>{@linkplain NerdObjectPool#setDPromotionCallback(UnaryOperator)
 * NerdObjectPool::setPromotionCallback(UnaryOperator)}.
 * </ul>
 */
public class NerdObjectPool<PoolObjectT> {

    private abstract sealed class NerdObjectPoolIterator implements Iterator<PoolObjectT> /* permits a */ {

        // `public`! Path to freedom!:
        protected final Iterator<PoolObjectT> ITERATOR;
        // Nobody can access it without reflection, though...

        protected PoolObjectT current;

        private NerdObjectPoolIterator(final Collection<PoolObjectT> p_underlyingCollection) {
            this.ITERATOR = p_underlyingCollection.iterator();
        }

        @Override
        public boolean hasNext() {
            return this.ITERATOR.hasNext();
        }

        @Override
        public PoolObjectT next() {
            this.current = this.ITERATOR.next();
            return this.current;
        }

    }

    private final class NerdObjectPoolPromotedObjectsIterator extends NerdObjectPoolIterator {

        public NerdObjectPoolPromotedObjectsIterator() {
            super(NerdObjectPool.this.PROMOTED);
        }

        @Override
        public void remove() {
            super.ITERATOR.remove();
            NerdObjectPool.this.POOL.add(super.current);
        }

    }

    private final class NerdObjectPoolDemotedObjectsIterator extends NerdObjectPoolIterator {

        public NerdObjectPoolDemotedObjectsIterator() {
            super(NerdObjectPool.this.POOL);
        }

        @Override
        public void remove() {
            super.ITERATOR.remove();
            NerdObjectPool.this.PROMOTED.add(super.current);
        }

    }

    // region Fields.
    protected final Queue<PoolObjectT> POOL;
    protected final List<PoolObjectT> PROMOTED;

    protected UnaryOperator<PoolObjectT>
    /*   */ demotionCallback,
            promotionCallback;
    // endregion.

    // region Constructors.
    public NerdObjectPool() {
        this(0);
    }

    public NerdObjectPool(final int p_initialCapacity) {
        this(p_initialCapacity, UnaryOperator.identity());
    }

    public NerdObjectPool(final int p_initialCapacity, final UnaryOperator<PoolObjectT> p_promotionCallback) {
        this(p_initialCapacity, p_promotionCallback, UnaryOperator.identity());
    }

    public NerdObjectPool(final int p_initialCapacity,
            final UnaryOperator<PoolObjectT> p_promotionCallback,
            final UnaryOperator<PoolObjectT> p_demotionCallback) {
        this.demotionCallback = p_demotionCallback;
        this.promotionCallback = p_promotionCallback;
        this.POOL = new ArrayDeque<>(p_initialCapacity);
        this.PROMOTED = new ArrayList<>(p_initialCapacity);
    }
    // endregion

    // region Adding stuff.

    /**
     * Promotes the given number of objects from the pool to the collection in loop,
     * till either:
     *
     * <ul>
     * <li>The given target is reached,
     * <li>The pool is emptied.
     * </ul>
     *
     * {@linkplain UnaryOperator#apply(Object)
     * UnaryOperator<PoolObjectT>::apply(PoolObjectT)} is
     * called on {@linkplain NerdObjectPool#promotionCallback
     * NerdObjectPool::promotionCallback}, passing in each object promoted.
     *
     * @param p_target is the target to reach.
     * @return The number of objects that were actually promoted.
     */
    public int promote(final int p_target) {
        final int toPromote = Math.min(this.POOL.size(), p_target);
        for (int i = 0; i < toPromote; i++)
            this.PROMOTED.add(
                    this.promotionCallback.apply(
                            this.POOL.poll()));
        return toPromote;
    }

    /**
     * Promotes an object to the promoted objects collection, removing it from the
     * pool. If the object doesn't exist in the pool, it is simply added to the
     * promoted objects' collection anyway.
     *
     * @param p_object is the object to promote.
     */
    public void promote(final PoolObjectT p_object) {
        this.POOL.remove(p_object);
        this.PROMOTED.add(p_object);

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

    /**
     * Sets what code is to be called when an object gets demoted.
     *
     * @param p_demotionCallback is the {@link UnaryOperator} instance / lambda /
     *                           implementing method to be used as a demotion
     *                           callback. It must not be {@code null}.
     *
     * @throws NullPointerException if {@code p_demotionCallback} is {@code null}.
     */
    public void setDemotionCallback(final UnaryOperator<PoolObjectT> p_demotionCallback) {
        Objects.requireNonNull(p_demotionCallback, "Callback object is `null`.");
        this.demotionCallback = p_demotionCallback;
    }

    /**
     * Sets what code is to be called when an object gets promoted.
     *
     * @param p_demotionCallback is the {@link UnaryOperator} instance / lambda /
     *                           implementing method to be used as a promotion
     *                           callback. It must not be {@code null}.
     *
     * @throws NullPointerException if {@code p_promotionCallback} is {@code null}.
     */
    public void setPromotionCallback(final UnaryOperator<PoolObjectT> p_promotionCallback) {
        Objects.requireNonNull(p_promotionCallback, "Callback object is `null`.");
        this.promotionCallback = p_promotionCallback;
    }
    // endregion

    // region Iteration.
    /**
     * Allows [only] forward iteration over all demoted objects; objects that are in
     * the pool.
     */
    public Iterable<PoolObjectT> allDemoted() {
        return NerdObjectPoolDemotedObjectsIterator::new;
    }

    /** Allows [only] forward iteration over all promoted objects. */
    public Iterable<PoolObjectT> allPromoted() {
        return NerdObjectPoolPromotedObjectsIterator::new;
    }

    /** Allows iteration over all demoted objects; objects that are in the pool. */
    public Iterator<PoolObjectT> demotedObjectsIterator() {
        return this.new NerdObjectPoolDemotedObjectsIterator();
    }

    /**
     * Allows iteration over all promoted objects. It is not possible to remove
     * objects from this.
     */
    public Iterator<PoolObjectT> promotedObjectsIterator() {
        return this.new NerdObjectPoolPromotedObjectsIterator();
    }
    // endregion

    // region Removal.
    /** Removes all promoted and demoted objects. */
    public void clearAll() {
        this.POOL.clear();
        this.PROMOTED.clear();
    }

    /** Removes all demoted objects. */
    public void clearDemoted() {
        this.POOL.clear();
    }

    /** Removes all promoted objects. */
    public void clearPromoted() {
        this.PROMOTED.clear();
    }

    /**
     * Demotes an object; that is, sends it back to the pool, removing it from the
     * promoted objects collection.
     *
     * {@linkplain UnaryOperator#apply(Object)
     * UnaryOperator<PoolObjectT>::apply(PoolObjectT)} is
     * called on {@linkplain NerdObjectPool#demotionCallback
     * NerdObjectPool::demotionCallback}, passing in each object demoted.
     */
    public void demote(final PoolObjectT p_object) {
        this.PROMOTED.remove(p_object);
        this.POOL.add(p_object);

        this.demotionCallback.apply(p_object);
    }

    /**
     *
     * Removes the passed object from both the pool and the promoted objects
     * collections.
     *
     * @param p_object is the object to remove.
     */
    public void remove(final Object p_object) {
        this.PROMOTED.remove(p_object);
        this.POOL.remove(p_object);
    }
    // endregion

    // region Status.
    /** @return The total number of objects, regardless of promotion status. */
    public int numObjects() {
        return this.POOL.size() + this.PROMOTED.size();
    }

    /**
     * @return A {@code boolean} reporting if the number of objects, regardless of
     *         their status of promotion,is {@code 0}.
     */
    public boolean isEmpty() {
        return this.noneDemoted() && this.nonePromoted();
        // ^^^ This should behave the same as:
        // return this.POOL.isEmpty() && this.ACTIVE.isEmpty();
    }

    /** @return The total number of demoted objects. */
    public int numDemoted() {
        return this.POOL.size();
    }

    /** @return The total number of promoted objects. */
    public int numPromoted() {
        return this.PROMOTED.size();
    }

    /**
     * @return A {@code boolean} reporting if the number of demoted objects (objects
     *         available in the pool) is {@code 0}.
     */
    public boolean noneDemoted() {
        return this.POOL.isEmpty();
    }

    /**
     * @return A {@code boolean} reporting if the number of promoted objects
     *         (objects not available in the pool / available in the promoted
     *         objects collection) is {@code 0}.
     */
    public boolean nonePromoted() {
        return this.PROMOTED.isEmpty();
    }
    // endregion

}
