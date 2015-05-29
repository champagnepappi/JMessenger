package com.almasb.jm.client;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import com.almasb.jm.client.ui.MessagePane;
import com.almasb.jm.common.Account;
import com.almasb.jm.common.DataMessage;

public class ClientUIController {

    private JMessengerApp app;

    @FXML
    private MessagePane messagePane;
    @FXML
    private TextField fieldMessage;
    @FXML
    private TextField fieldAddUser;
    @FXML
    private ListView<Node> users;

    private Account account;
    private Node selected;

    public void setApp(JMessengerApp app) {
        this.app = app;
        app.registerMessageHandler((source, message) -> {
            Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
        });
    }

    public void init(Account account, Set<Account> friends) {
        this.account = account;
        for (Account friend : friends) {
            Text text = new Text(friend.getName());
            text.setUserData(friend);
            users.getItems().add(text);
        }

        users.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            selected = newValue;
        });

        if (users.getItems().size() > 0)
            users.getSelectionModel().selectFirst();
    }

    public void send() {
        DataMessage message = new DataMessage(account.getName(), account.getEmail(), fieldMessage.getText());
        app.sendMessage(message, (Account) selected.getUserData());
        Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
        fieldMessage.clear();
    }

    public void postMessage(DataMessage message) {
        String email = message.getEmail();
        for (Node n : users.getItems()) {
            Account acc = (Account) n.getUserData();
            if (email.equals(acc.getEmail())) {
                // TODO: post message from correct user
                Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
                break;
            }
        }

        // TODO: unknown user message
        Platform.runLater(() -> messagePane.appendMessage((DataMessage)message));
    }

    public void addUser() {
        account.addFriend(fieldAddUser.getText());
        app.getAccount(fieldAddUser.getText()).ifPresent(acc -> {
            Text text = new Text(acc.getName());
            text.setUserData(acc);
            users.getItems().add(text);
            app.connectTo(acc);
        });
    }
}
