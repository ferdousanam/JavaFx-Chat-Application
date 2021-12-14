package com.ferdousanam.javafxchatappwithoutfxml;

import com.ferdousanam.classes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatApplication extends Application {
    private final boolean isServer = true;

    private final TextArea messages = new TextArea();
    private final NetworkConnection connection = isServer ? createServer() : createClient();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.setTitle(isServer ? "Server" : "Client");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Parent createContent() {
        messages.setFont(Font.font(36));
        messages.setPrefHeight(550);
        messages.setEditable(false);

        TextField input = new TextField();
        input.setOnAction(event -> {
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
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
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
}
