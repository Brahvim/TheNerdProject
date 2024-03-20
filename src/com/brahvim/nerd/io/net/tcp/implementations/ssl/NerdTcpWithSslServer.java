package com.brahvim.nerd.io.net.tcp.implementations.ssl;

import java.net.ServerSocket;
import java.util.function.Function;

import javax.net.ssl.SSLServerSocketFactory;

import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpClient;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer;

public class NerdTcpWithSslServer extends NerdAbstractTcpServer {

	public NerdTcpWithSslServer(final int p_port) {
		super(p_port);
	}

	public NerdTcpWithSslServer(final int p_port, final int p_maxConnReqsAtOnce) {
		super(p_port, p_maxConnReqsAtOnce);
	}

	public NerdTcpWithSslServer(final int p_port, final Function<NerdAbstractTcpClient, Boolean> p_invitationCallback) {
		super(p_port, p_invitationCallback);
	}

	public NerdTcpWithSslServer(final int p_port, final int p_maxConnReqsAtOnce,
			final Function<NerdAbstractTcpClient, Boolean> p_connectionCallback) {
		super(p_port, p_maxConnReqsAtOnce, p_connectionCallback);
	}

	@Override
	protected ServerSocket createSocketForConstructor(final int p_port) {
		try {
			return SSLServerSocketFactory.getDefault().createServerSocket(p_port);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected ServerSocket createSocketForConstructor(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			return SSLServerSocketFactory.getDefault().createServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
