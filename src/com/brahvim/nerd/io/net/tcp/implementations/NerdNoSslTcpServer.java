package com.brahvim.nerd.io.net.tcp.implementations;

import java.net.ServerSocket;
import java.util.function.Function;

import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpClient;
import com.brahvim.nerd.io.net.tcp.abstracts.NerdAbstractTcpServer;

public class NerdNoSslTcpServer extends NerdAbstractTcpServer {

    public NerdNoSslTcpServer(int p_port) {
        super(p_port);
    }

    public NerdNoSslTcpServer(int p_port, Function<NerdAbstractTcpClient, Boolean> p_invitationCallback) {
        super(p_port, p_invitationCallback);
    }

    public NerdNoSslTcpServer(int p_port, int p_maxConnReqsAtOnce) {
        super(p_port, p_maxConnReqsAtOnce);
    }

    public NerdNoSslTcpServer(int p_port, int p_maxConnReqsAtOnce,
            Function<NerdAbstractTcpClient, Boolean> p_connectionCallback) {
        super(p_port, p_maxConnReqsAtOnce, p_connectionCallback);
    }

    @Override
    protected ServerSocket createSocketForConstructor(int p_port) {
        try {
            return new ServerSocket(p_port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected ServerSocket createSocketForConstructor(int p_port, int p_maxConnReqsAtOnce) {
        try {
            return new ServerSocket(p_port, p_maxConnReqsAtOnce);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
