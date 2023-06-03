package com.brahvim.nerd.io.net.tcp;

public class NerdAbstractTcpPacket {

	protected byte[] data;

	public NerdAbstractTcpPacket(final byte[] p_data) {
		this.data = p_data;
	}

	public int getDataLength() {
		return this.data.length;
	}

	public byte[] getData() {
		return this.data;
	}

}
