package com.brahvim.nerd.io.net.tcp;

public class AbstractTcpPacket {

	protected byte[] data;

	public AbstractTcpPacket(final byte[] p_data) {
		this.data = p_data;
	}

	public int getLength() {
		return this.data.length;
	}

	public byte[] getData() {
		return this.data;
	}

}
