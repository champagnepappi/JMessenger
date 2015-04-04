package com.almasb.jm.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ClientLoginController {

    private ClientApp app;

    @FXML
    private TextField fieldIP;

    public void setApp(ClientApp app) {
        this.app = app;
    }

    public void connect() {
        app.connect(fieldIP.getText());
    }
}
