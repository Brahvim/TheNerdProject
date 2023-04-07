package com.brahvim.nerd.io.net.tcp;

public class ReceivableTcpPacket extends AbstractTcpPacket {

	private static final long serialVersionUID = 56456546;
	private transient final TcpServer.TcpServerClient client;

	public ReceivableTcpPacket(final TcpServer.TcpServerClient p_client, final byte[] p_data) {
		super(p_data);
		this.client = p_client;
	}

	public TcpServer.TcpServerClient getClient() {
		return this.client;
	}

}