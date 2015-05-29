package com.almasb.jm.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private Map<Account, Client> clients = new HashMap<>();
    private DBMS dbms;
    private Server server;

    /**
     * Account for the user of this app instance
     */
    private Account account;

    private Scene scene;
    private Parent rootLogin, rootUI;

    private ClientUIController uiController;

    @Override
    public void init() throws Exception {
        dbms = new OrchestrateDBMS();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("client_login.fxml"));
        rootLogin = loader.load();
        loader.<ClientLoginController>getController().setApp(this);

        loader = new FXMLLoader(getClass().getResource("client_ui.fxml"));
        rootUI = loader.load();
        loader.<ClientUIController>getController().setApp(this);

        uiController = loader.getController();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(rootLogin);

        primaryStage.setOnCloseRequest(event -> exit());
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

    public void sendMessage(DataMessage message, Account acc) {
        // TODO: check just in case
        clients.get(acc).send(message);
    }

    // TODO: throw exception to top level?
    public void startServer(Account account) {
        this.account = account;

        try {
            // TODO: randomize ports?
//            server = Network.createServer(Config.NAME, Config.VERSION, Config.TCP_PORT, Config.UDP_PORT);
//            server.start();
//
//            server.addMessageListener((source, message) -> {
//                if (message instanceof DataMessage) {
//                    uiController.postMessage((DataMessage) message);
//                }
//            }, DataMessage.class);

            // also start clients for all known friends
            // TODO: bg thread
            for (String email : account.getFriends()) {
                getAccount(email).ifPresent(acc -> {
                    String ip = acc.getIp();
                    int port = acc.getPort();

                    if (!ip.isEmpty()) {
                        try {
                            Client client = Network.connectToServer(Config.NAME, Config.VERSION, ip, Config.TCP_PORT, Config.UDP_PORT);
                            clients.put(acc, client);
                            handlers.forEach(h -> client.addMessageListener(h, DataMessage.class));
                            client.start();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("No connection to: " + ip + " at " + port + " name " + acc.getName());
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        uiController.init(account, clients.keySet());
    }

    public void showUILogin() {
        scene.setRoot(rootLogin);
    }

    public void showUIMain() {
        scene.setRoot(rootUI);
    }

    public void connectTo(Account acc) {
        String ip = acc.getIp();
        int port = acc.getPort();

        if (!ip.isEmpty()) {
            try {
                Client client = Network.connectToServer(Config.NAME, Config.VERSION, ip, Config.TCP_PORT, Config.UDP_PORT);
                clients.put(acc, client);
                handlers.forEach(h -> client.addMessageListener(h, DataMessage.class));
                client.start();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("No connection to: " + ip + " at " + port + " name " + acc.getName());
            }
        }
    }

    private void exit() {
//        account.setIp("");
//        dbms.updateEntry(account);

        dbms.close();
        if (server != null)
            server.close();
        clients.values().forEach(Client::close);
    }

    public static void main(String[] args) {
        Serializer.registerClass(DataMessage.class);
        launch(args);
    }
}
