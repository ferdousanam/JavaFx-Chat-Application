package com.ferdousanam.javafxchatapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private final boolean isServer = true;

    @FXML
    private TextArea messages;

    @FXML
    private TextField input;

    @FXML
    protected void onHelloButtonClick() {
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            DataPacket packet = new DataPacket(
                    new Encryptor().enc(message.getBytes())
            );

            try {
//                connection.send(packet);
            } catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });
    }
}
