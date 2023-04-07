package com.brahvim.nerd.io.net.tcp;

public class AbstractTcpPacket {

	private static final long serialVersionUID = 96435543;

	protected final byte[] data;

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
