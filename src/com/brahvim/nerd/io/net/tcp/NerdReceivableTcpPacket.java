package com.brahvim.nerd.io.net.tcp;

public class NerdReceivableTcpPacket extends NerdAbstractTcpPacket {

	private final NerdTcpServer.NerdTcpServerClient client;

	public NerdReceivableTcpPacket(final NerdTcpServer.NerdTcpServerClient p_client, final byte[] p_data) {
		super(p_data);
		this.client = p_client;
	}

	public NerdTcpServer.NerdTcpServerClient getSender() {
		return this.client;
	}

}
