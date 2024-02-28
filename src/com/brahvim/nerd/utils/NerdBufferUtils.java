package com.brahvim.nerd.utils;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;

public final class NerdBufferUtils {

	private NerdBufferUtils() {
		NerdReflectionUtils.rejectStaticClassInstantiationFor(this);
	}

	// Generics won't make a difference LOL.
	// By the way, `Object[]` STILL disallow `Object`s, which is awesome!

	// region Array-copying.
	public static <ArrayT> ArrayT[] arrayCopy(
			final ArrayT[] p_sourceArray,
			final int p_sourceArrayFirstElementPosition,
			final ArrayT[] p_destinationArray,
			final int p_destinationArrayFirstElementPosition,
			final int p_numElementsToCopy) {
		System.arraycopy(p_sourceArray,
				p_sourceArrayFirstElementPosition, p_destinationArray,
				p_destinationArrayFirstElementPosition, p_numElementsToCopy);
		return p_destinationArray;
	}

	public static <ArrayT> ArrayT[] arrayCopy(
			final ArrayT[] p_sourceArray, final ArrayT[] p_destinationArray, final int p_numElementsToCopy) {
		System.arraycopy(p_sourceArray, 0, p_destinationArray, 0, p_numElementsToCopy);
		return p_destinationArray;
	}

	public static <ArrayT> ArrayT[] arrayCopy(
			final ArrayT[] p_sourceArray, final ArrayT[] p_destinationArray) {
		System.arraycopy(p_sourceArray, 0, p_destinationArray, 0, p_sourceArray.length);
		return p_destinationArray;
	}

	public static <ArrayT> ArrayT[][] arrayCopy(
			final ArrayT[][] p_sourceArray,
			final ArrayT[][] p_destinationArray,
			final IntFunction<ArrayT[]> p_arrayConstructor) {
		for (int i = 0; i < p_sourceArray.length; i++) {
			final int columns = p_sourceArray[i].length;
			p_destinationArray[i] = p_arrayConstructor.apply(columns);
			System.arraycopy(p_sourceArray[i], 0, p_destinationArray[i], 0, columns);
		}

		return p_destinationArray;
	}
	// endregion

	// region Integers.
	public static byte[] toByteArray(final int p_number) {
		return ByteBuffer.allocate(Integer.BYTES).putInt(p_number).array();
	}

	public static byte[] toByteArray(final int... p_numbers) {
		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * p_numbers.length);

		for (final var i : p_numbers)
			buffer.putInt(i).array();

		return buffer.array();
	}

	public static byte[] toByteArray(final int[]... p_numberArrays) {
		// Stop worrying. This is all about preventing large space complexity.
		int totalNums = 0;
		// What do you want me to do? Use a `List` and then call `List::toArray()`?
		// Re-allocate those arrays completely?

		for (final var a : p_numberArrays)
			totalNums += a.length;

		final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * totalNums);

		for (final var a : p_numberArrays)
			for (final var i : a)
				buffer.putInt(i).array();

		return buffer.array();
	}
	// endregion

	// region Bytes.
	public static byte[] toByteArray(final byte p_number) {
		return new byte[] { p_number };
	}

	// 🤣!!! ...but yeah, people! Use this for consistency.
	public static byte[] toByteArray(final byte... p_numbers) {
		return p_numbers;
	}

	public static byte[] toByteArray(final byte[]... p_numberArrays) {
		int counter = 0; // Using just one variable.

		// Gett'em 'num of numbers'!:
		for (final var a : p_numberArrays)
			counter += a.length;

		final byte[] toRet = new byte[counter];

		counter = 0; // Yeah, re-use variables! Less space complexity!1!!

		for (final var a : p_numberArrays)
			for (final var i : a)
				toRet[counter++] = i;
		// Take THAT, `List` allocators!

		return toRet;
	}
	// endregion

	// region Longs.
	public static byte[] toByteArray(final long p_number) {
		return ByteBuffer.allocate(Long.BYTES).putLong(p_number).array();
	}

	public static byte[] toByteArray(final long... p_numbers) {
		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * p_numbers.length);

		for (final var l : p_numbers)
			buffer.putLong(l).array();

		return buffer.array();
	}

	public static byte[] toByteArray(final long[]... p_numberArrays) {
		// Stop worrying. This is all about preventing large space complexity.
		int totalNums = 0;
		// What do you want me to do? Use a `List` and then call `List::toArray()`?
		// Re-allocate those arrays completely?

		for (final var a : p_numberArrays)
			totalNums += a.length;

		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * totalNums);

		for (final var a : p_numberArrays)
			for (final var l : a)
				buffer.putLong(l).array();

		return buffer.array();
	}
	// endregion

	// region Shorts.
	public static byte[] toByteArray(final short p_number) {
		return ByteBuffer.allocate(Short.BYTES).putShort(p_number).array();
	}

	public static byte[] toByteArray(final short... p_numbers) {
		final ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES * p_numbers.length);

		for (final var s : p_numbers)
			buffer.putShort(s).array();

		return buffer.array();
	}

	public static byte[] toByteArray(final short[]... p_numberArrays) {
		// Stop worrying. This is all about preventing large space complexity.
		int totalNums = 0;
		// What do you want me to do? Use a `List` and then call `List::toArray()`?
		// Re-allocate those arrays completely?

		for (final var a : p_numberArrays)
			totalNums += a.length;

		final ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES * totalNums);

		for (final var a : p_numberArrays)
			for (final var s : a)
				buffer.putShort(s).array();

		return buffer.array();
	}
	// endregion

	// region Floats.
	public static byte[] toByteArray(final float p_number) {
		return ByteBuffer.allocate(Float.BYTES).putFloat(p_number).array();
	}

	public static byte[] toByteArray(final float... p_numbers) {
		final ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * p_numbers.length);

		for (final var f : p_numbers)
			buffer.putFloat(f).array();

		return buffer.array();
	}

	public static byte[] toByteArray(final float[]... p_numberArrays) {
		// Stop worrying. This is all about preventing large space complexity.
		int totalNums = 0;
		// What do you want me to do? Use a `List` and then call `List::toArray()`?
		// Re-allocate those arrays completely?

		for (final var a : p_numberArrays)
			totalNums += a.length;

		final ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * totalNums);

		for (final var a : p_numberArrays)
			for (final var f : a)
				buffer.putFloat(f).array();

		return buffer.array();
	}
	// endregion

	// region Doubles.
	public static byte[] toByteArray(final double p_number) {
		return ByteBuffer.allocate(Double.BYTES).putDouble(p_number).array();
	}

	public static byte[] toByteArray(final double... p_numbers) {
		final ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES * p_numbers.length);

		for (final var d : p_numbers)
			buffer.putDouble(d).array();

		return buffer.array();
	}

	public static byte[] toByteArray(final double[]... p_numberArrays) {
		// Stop worrying. This is all about preventing large space complexity.
		int totalNums = 0;
		// What do you want me to do? Use a `List` and then call `List::toArray()`?
		// Re-allocate those arrays completely?

		for (final var a : p_numberArrays)
			totalNums += a.length;

		final ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES * totalNums);

		for (final var a : p_numberArrays)
			for (final var d : a)
				buffer.putDouble(d).array();

		return buffer.array();
	}
	// endregion

}
