package com.ferdousanam.javafxchatapp;

import com.ferdousanam.classes.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public static final boolean isServer = false;
    private NetworkConnection connection;

    @FXML
    private TextArea messages;

    @FXML
    private TextField input;

    @FXML
    private TextField serverIp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (isServer) {
            serverIp.setManaged(false);
            connection = createServer();
            try {
                connection.startConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            serverIp.setText("127.0.0.1");
            serverIp.setVisible(true);
        }
    }

    public void closeConnection() throws Exception {
        System.out.println("closeConnection");
//        connection.closeConnection();
    }

    private void onReceiveCallback(DataPacket packet) {
        byte[] original = new Encryptor().dec(packet.getRawBytes());

        Platform.runLater(() -> {
            if (connection.getSocket() == null) {
                messages.appendText("Connection failed!\n");
            } else if (connection.getSocket().isClosed()) {
                messages.appendText("Connection closed!\n");
            } else {
                messages.appendText(new String(original) + "\n");
            }
        });
    }

    private Server createServer() {
        return new Server(55555, data -> onReceiveCallback((DataPacket) data));
    }

    private Client createClient() {
        return new Client(serverIp.getText(), 55555, data -> onReceiveCallback((DataPacket) data));
    }

    @FXML
    protected void onServerIpSetOnAction(Event event) {
        try {
            if (connection != null && connection.getSocket() != null && !connection.getSocket().isClosed()) {
                messages.appendText("Closing Connection with: " + connection.getIP() + "\n");
                connection.closeConnection();
            }

            connection = createClient();
            connection.startConnection();
            messages.appendText("Connecting with: " + serverIp.getText() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onInputSetOnAction(Event event) {
        String message = isServer ? "Server: " : "Client: ";
        message += input.getText();
        input.clear();

        messages.appendText(message + "\n");

        DataPacket packet = new DataPacket(
                new Encryptor().enc(message.getBytes())
        );

        try {
            connection.send(packet);
        } catch (Exception e) {
            messages.appendText("Failed to send\n");
        }
    }
}
