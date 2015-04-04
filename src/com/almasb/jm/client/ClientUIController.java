package com.almasb.jm.client;

import com.almasb.jm.common.DataMessage;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientUIController {

    private ClientApp app;

    @FXML
    private TextArea areaInfo;
    @FXML
    private TextField fieldMessage;

    public void setApp(ClientApp app) {
        this.app = app;
        app.registerMessageHandler((source, message) -> {
            Platform.runLater(() -> areaInfo.appendText(message.toString() + "\n"));
        });
    }

    public void send() {
        DataMessage message = new DataMessage("Test", fieldMessage.getText());
        app.sendMessage(message);
        fieldMessage.clear();
    }
}
