package com.ferdousanam.classes;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection {
    private final ConnectionThread connThread = new ConnectionThread();
    private final Consumer<Serializable> onReceiveCallback;

    public NetworkConnection(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    public void startConnection() throws Exception {
        connThread.start();
    }

    public void send(Serializable data) throws Exception {
        connThread.out.writeObject(data);
    }

    public void closeConnection() throws Exception {
        connThread.socket.close();
    }

    protected abstract boolean isServer();

    public abstract String getIP();

    protected abstract int getPort();

    public Socket getSocket() {
        return connThread.socket;
    }

    private class ConnectionThread extends Thread {
        private Socket socket;
        private ObjectOutputStream out;

        // TCP - slow, reliable
        // UDP - fast, unreliable

        @Override
        public void run() {
            try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                 Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while (true) {
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }
            } catch (Exception e) {
                DataPacket closePacket = new DataPacket("Connection closed".getBytes());
                onReceiveCallback.accept(closePacket);
            }
        }
    }
}
