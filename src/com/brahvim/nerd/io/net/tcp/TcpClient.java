package com.brahvim.nerd.io.net.tcp;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient extends AbstractTcpClient {

	public TcpClient(final String p_serverIp, final int p_myPort) {
		super(p_serverIp, p_myPort);
	}

	public TcpClient(final Socket p_socket) {
		super(p_socket);
	}

	@Override
	public TcpClient send(final String p_data) {
		return this.send(p_data.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public TcpClient send(final byte[] p_data) {
		try {
			this.socket.getOutputStream().write(p_data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

}
