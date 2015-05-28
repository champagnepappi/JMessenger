package com.almasb.jm.client;

import java.util.Arrays;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.almasb.jm.common.Account;
import com.almasb.jm.common.Config;

public class ClientLoginController {

    private JMessengerApp app;

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignUp;

    public void setApp(JMessengerApp app) {
        this.app = app;
    }

    public void signUp() {
        // TODO: check email valid / pass valid
        Optional<Account> account = app.getAccount(email.getText());
        if (account.isPresent()) {
            // TODO: show error acc exists
        }
        else {
            Optional<String> ip = Config.getExternalIP();
            if (ip.isPresent()) {
                Account acc = new Account(email.getText(), password.getText(), "Debug1", ip.get(), Config.TCP_PORT, Arrays.asList("test@test.com", "hi@test.com"));
                boolean ok = app.createAccount(acc);
                if (ok) {
                    // TODO: show account created
                    System.out.println("Account created");
                }
                else {
                    // TODO: show error couldnt create
                }
            }
            else {
                // TODO: show error no ip
            }
        }
    }

    public void signIn() {
        Optional<Account> account = app.getAccount(email.getText());
        if (account.isPresent()) {
            Account acc = account.get();
            if (password.getText().equals(acc.getPassword())) {

                System.out.println(acc.getName() + " " + acc.getEmail() + " " + acc.getIp());
                System.out.println(acc.getFriends().toString());

//                app.startServer();
//                app.showUIMain();
            }
        }
        else {
            // TODO: show error acc doesnt exist
        }
    }
}
