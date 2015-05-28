package com.almasb.jm.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.almasb.jm.common.Account;
import com.almasb.jm.common.Config;
import com.almasb.jm.common.DataMessage;
import com.almasb.jm.dbms.DBMS;
import com.almasb.jm.dbms.OrchestrateDBMS;
import com.almasb.jm.server.MessageHandler;
import com.jme3.network.Client;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

/*
 * 1. Login
 * 2. Connect to global DBMS and send our info
 * 3. Start server on our machine
 * 4. Check if friends are online
 *
 *
 */

public class JMessengerApp extends Application {

    private List<MessageListener<Client> > handlers = new ArrayList<>();
    //private Client client;
    private DBMS dbms;

    private Scene scene;
    private Parent rootLogin, rootUI;

    @Override
    public void init() throws Exception {
        dbms = new OrchestrateDBMS();

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

        primaryStage.setOnCloseRequest(event -> dbms.close());
        primaryStage.setTitle("JMessenger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Optional<Account> getAccount(String email) {
        return dbms.getEntry(email);
    }

    public boolean createAccount(Account account) {
        return dbms.addEntry(account);
    }





    public void registerMessageHandler(MessageListener<Client> handler) {
        handlers.add(handler);
    }

    public void connect(String host) {
        try {
//            client = Network.connectToServer(Config.NAME, Config.VERSION, host, Config.TCP_PORT, Config.UDP_PORT);
//            handlers.forEach(handler -> client.addMessageListener(handler, DataMessage.class));
//            client.start();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void sendMessage(DataMessage message) {
//        client.send(message);
//    }
//
//    public Client getClient() {
//        return client;
//    }

    // TODO: throw exception to top level?
    public void startServer() {
        Server server;
        try {
            // TODO: randomize ports?
            server = Network.createServer(Config.NAME, Config.VERSION, Config.TCP_PORT, Config.UDP_PORT);
            server.start();

            MessageHandler handler = new MessageHandler();
            server.addMessageListener(handler, DataMessage.class);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showUILogin() {
        scene.setRoot(rootLogin);
    }

    public void showUIMain() {
        scene.setRoot(rootUI);
    }

    public static void main(String[] args) {
        Serializer.registerClass(DataMessage.class);
        launch(args);
    }
}
