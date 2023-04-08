package com.brahvim.nerd.io.net.tcp;

public class SendableTcpPacket extends AbstractTcpPacket {

	public SendableTcpPacket(final byte[] p_data) {
		super(p_data);
	}

	public SendableTcpPacket setData(final byte[] p_data) {
		super.data = p_data;
		return this;
	}

	// (I wonder who'll use this.)
	public SendableTcpPacket setDataLength(final int p_size) {
		System.arraycopy(new byte[p_size], 0, super.data, 0, p_size);
		return this;
	}

}
