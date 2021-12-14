package com.ferdousanam.javafxchatapp;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = isServer ? createServer() : createClient();
        try {
            connection.startConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws Exception {
        System.out.println("closeConnection");
//        connection.closeConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
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
