package com.brahvim.nerd.io.net.tcp.implementations.no_ssl;

import java.net.ServerSocket;
import java.util.function.Function;

import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpClient;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer;

public class NerdTcpNoSslServer extends NerdAbstractTcpServer {

	public NerdTcpNoSslServer(final int p_port) {
		super(p_port);
	}

	public NerdTcpNoSslServer(final int p_port, final Function<NerdAbstractTcpClient, Boolean> p_invitationCallback) {
		super(p_port, p_invitationCallback);
	}

	public NerdTcpNoSslServer(final int p_port, final int p_maxConnReqsAtOnce) {
		super(p_port, p_maxConnReqsAtOnce);
	}

	public NerdTcpNoSslServer(final int p_port, final int p_maxConnReqsAtOnce,
			final Function<NerdAbstractTcpClient, Boolean> p_connectionCallback) {
		super(p_port, p_maxConnReqsAtOnce, p_connectionCallback);
	}

	@Override
	protected ServerSocket createSocketForConstructor(final int p_port) {
		try {
			return new ServerSocket(p_port);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected ServerSocket createSocketForConstructor(final int p_port, final int p_maxConnReqsAtOnce) {
		try {
			return new ServerSocket(p_port, p_maxConnReqsAtOnce);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
