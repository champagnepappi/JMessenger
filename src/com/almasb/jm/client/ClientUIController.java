package com.almasb.jm.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.almasb.jm.client.ui.MessagePane;
import com.almasb.jm.common.DataMessage;

public class ClientUIController {

    private ClientApp app;

    @FXML
    private MessagePane messagePane;
    @FXML
    private TextField fieldMessage;
    @FXML
    private ListView<Node> users;

    public void setApp(ClientApp app) {
        this.app = app;
        app.registerMessageHandler((source, message) -> {
            Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
        });
    }

    public void send() {
        DataMessage message = new DataMessage("Test", fieldMessage.getText());
//        app.sendMessage(message);
        Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
        fieldMessage.clear();
    }
}
