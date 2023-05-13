package com.brahvim.nerd.framework;

import java.util.Objects;

/**
 * Represents an operation that accepts three input arguments and returns no
 * result. This is the three-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code TriConsumer} is expected
 * to operate via side-effects.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 *
 * @see @link Consumer
 * @see BiConsumer
 */
@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code p_after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation. If performing this operation throws an exception,
     * the {@code p_after} operation will not be performed.
     *
     * @param p_after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     *         operation followed by the {@code p_after} operation
     * @throws NullPointerException if {@code p_after} is null
     */
    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> p_after) {
        Objects.requireNonNull(p_after);

        return (l, c, r) -> {
            this.accept(l, c, r);
            p_after.accept(l, c, r);
        };
    }

}
