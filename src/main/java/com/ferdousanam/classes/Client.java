package com.ferdousanam.classes;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {

    private final String ip;
    private final int port;

    public Client(String ip, int port, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}