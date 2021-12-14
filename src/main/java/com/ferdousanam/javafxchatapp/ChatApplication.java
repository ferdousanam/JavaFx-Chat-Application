package com.ferdousanam.javafxchatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatApplication extends Application {

    private FXMLLoader fxmlLoader;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(ChatApplication.class.getResource("chatapp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        stage.setTitle(ChatController.isServer ? "Server" : "Client");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        ChatController controller = fxmlLoader.getController();
        controller.closeConnection();
    }
}
