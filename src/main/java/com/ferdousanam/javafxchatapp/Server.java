package com.ferdousanam.javafxchatapp;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection {
    private final int port;

    public Server(int port, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
