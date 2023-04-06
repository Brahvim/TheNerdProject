package com.brahvim.nerd.io.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient {

	private Socket socket;

	public TcpClient(final String p_serverIp, final int p_myPort) {
		try {
			this.socket = new Socket(p_serverIp, p_myPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TcpClient(final Socket p_socket) {
		this.socket = p_socket;
	}

	public TcpClient disconnect() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	public Socket getSocket() {
		return this.socket;
	}

}
