package com.almasb.jm.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.almasb.jm.common.Config;
import com.almasb.jm.common.DataMessage;
import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;

public class ClientApp extends Application {

    private List<MessageListener<Client> > handlers = new ArrayList<>();
    private Client client;

    private Scene scene;
    private Parent rootLogin, rootUI;

    @Override
    public void init() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client_login.fxml"));
        rootLogin = loader.load();
        loader.<ClientLoginController>getController().setApp(this);

        loader = new FXMLLoader(getClass().getResource("client_ui.fxml"));
        rootUI = loader.load();
        loader.<ClientUIController>getController().setApp(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(rootLogin);

        //primaryStage.setOnCloseRequest(event -> client.close());
        primaryStage.setTitle("JMessenger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void registerMessageHandler(MessageListener<Client> handler) {
        handlers.add(handler);
    }

    public void connect(String host) {
        try {
//            client = Network.connectToServer(Config.NAME, Config.VERSION, host, Config.TCP_PORT, Config.UDP_PORT);
//            handlers.forEach(handler -> client.addMessageListener(handler, DataMessage.class));
//            client.start();

            scene.setRoot(rootUI);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(DataMessage message) {
        client.send(message);
    }

    public Client getClient() {
        return client;
    }

    public static void main(String[] args) {
        Serializer.registerClass(DataMessage.class);
        launch(args);
    }
}
