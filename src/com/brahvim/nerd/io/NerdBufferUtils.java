package com.brahvim.nerd.io;

import java.nio.ByteBuffer;

public final class NerdBufferUtils {
	private NerdBufferUtils() {
	}

	public static byte[] toByteArray(final int p_number) {
		return ByteBuffer.allocate(Integer.BYTES).putInt(p_number).array();
	}

	public static byte[] toByteArray(final byte p_number) {
		return new byte[] { p_number };
	}

	public static byte[] toByteArray(final long p_number) {
		return ByteBuffer.allocate(Long.BYTES).putLong(p_number).array();
	}

	public static byte[] toByteArray(final short p_number) {
		return ByteBuffer.allocate(Short.BYTES).putShort(p_number).array();
	}

	public static byte[] toByteArray(final float p_number) {
		return ByteBuffer.allocate(Float.BYTES).putFloat(p_number).array();
	}

	public static byte[] toByteArray(final double p_number) {
		return ByteBuffer.allocate(Double.BYTES).putDouble(p_number).array();
	}

}
