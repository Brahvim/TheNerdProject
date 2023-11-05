package com.brahvim.nerd.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents an operation that accepts four input arguments and returns no result. This is the four-arity
 * specialization of {@link Consumer}. Unlike most other functional interfaces, {@link NerdQuadConsumer} is expected to
 * operate via side-effects.
 * <p>
 * This is a {@link FunctionalInterface} whose functional method is
 * {@link NerdQuadConsumer#accept(Object, Object, Object, Object)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @param <W> the type of the fourth argument to the operation
 * @see Consumer
 * @see BiConsumer
 * @see NerdTriConsumer
 */
@FunctionalInterface
public interface NerdQuadConsumer<T, U, V, W> {

	/**
	 * Performs this operation on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @param v the third input argument
	 * @param w the fourth input argument
	 */
	public void accept(T t, U u, V v, W w);

	/**
	 * Returns a composed {@link NerdQuadConsumer} that performs, in sequence, this operation followed by the
	 * {@code p_after} operation. If performing either operation throws an exception, it is relayed to the caller of the
	 * composed operation. If performing this operation throws an exception, the {@code p_after} operation will not be
	 * performed.
	 *
	 * @param p_after the operation to perform after this operation
	 * @return a composed {@link NerdQuadConsumer} that performs in sequence this operation followed by the
	 *         {@code p_after} operation
	 * @throws NullPointerException if {@code p_after} is {@code null}
	 */
	default NerdQuadConsumer<T, U, V, W> andThen(
			final NerdQuadConsumer<? super T, ? super U, ? super V, ? super W> p_after) {
		java.util.Objects.requireNonNull(p_after);

		return (a, b, c, d) -> {
			this.accept(a, b, c, d);
			p_after.accept(a, b, c, d);
		};
	}

}
