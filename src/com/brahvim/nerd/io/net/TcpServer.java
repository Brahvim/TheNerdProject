package com.brahvim.nerd.io.net;

import java.io.IOException;
import java.net.ServerSocket;

public class TcpServer {

	private ServerSocket socket;

	public TcpServer() {
		try {
			this.socket = new ServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TcpServer(final int p_port) {
		try {
			this.socket = new ServerSocket(p_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TcpServer(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			this.socket = new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
