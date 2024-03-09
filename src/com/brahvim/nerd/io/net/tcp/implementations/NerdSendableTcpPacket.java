package com.brahvim.nerd.io.net.tcp.implementations;

import java.util.Arrays;

import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpPacket;

public class NerdSendableTcpPacket extends NerdAbstractTcpPacket {

	public NerdSendableTcpPacket(final byte[] p_data) {
		super(p_data);
	}

	public NerdSendableTcpPacket setData(final byte[] p_data) {
		super.data = p_data;
		return this;
	}

	// (I wonder who'll use this.)
	public NerdSendableTcpPacket setDataLength(final int p_size) {
		if (super.data.length != p_size)
			super.data = Arrays.copyOf(super.data, p_size);
		return this;
	}

}
