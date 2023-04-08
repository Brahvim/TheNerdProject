package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import processing.core.PApplet;

public class NerdTcpClient extends NerdAbstractTcpClient {

	// region Construction
	public NerdTcpClient(final Socket p_socket) {
		super(p_socket);
	}

	public NerdTcpClient(final String p_serverIp, final int p_myPort) {
		super(p_serverIp, p_myPort);
	}

	public NerdTcpClient(final int p_port) {
		super(p_port);
	}
	// endregion

	@Override
	public NerdTcpClient send(final NerdAbstractTcpPacket p_packet) {
		try {
			final byte[] packData = p_packet.getData();
			final byte[] packDataLen = ByteBuffer
					.allocate(Integer.BYTES).putInt(p_packet.getDataLength()).array();

			// System.out.println("`NerdTcpClient::send()` was called.");
			super.socket.getOutputStream().write(PApplet.concat(packDataLen, packData));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public NerdTcpClient send(final String p_data) {
		return this.send(p_data.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public NerdTcpClient send(final byte[] p_data) {
		return this.send(new NerdSendableTcpPacket(p_data));
	}

}
