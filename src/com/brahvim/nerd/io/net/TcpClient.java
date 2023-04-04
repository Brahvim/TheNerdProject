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

}
