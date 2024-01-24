package com.brahvim.nerd.io.net.tcp.abstracts;

public abstract class NerdAbstractTcpPacket {

	protected byte[] data;

	protected NerdAbstractTcpPacket(final byte[] p_data) {
		this.data = p_data;
	}

	public int getDataLength() {
		return this.data.length;
	}

	public byte[] getData() {
		return this.data;
	}

}
