package com.almasb.jm.client;

import java.util.Arrays;
import java.util.Optional;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

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
    @FXML
    private Region veil;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label error;

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
        runTask(new SignInTask());
    }

    private void runTask(Task<?> task) {
        progress.visibleProperty().bind(task.runningProperty());
        veil.visibleProperty().bind(task.runningProperty());
        error.textProperty().bind(task.messageProperty());

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private class SignInTask extends Task<Account> {
        @Override
        protected Account call() throws Exception {
            Optional<Account> account = app.getAccount(email.getText());

            Account acc = account.orElseThrow(() -> new Exception("Account not found"));

            if (password.getText().equals(acc.getPassword())) {
                System.out.println(acc.getName() + " " + acc.getEmail() + " " + acc.getIp());
                System.out.println(acc.getFriends().toString());
            }
            else {
                throw new Exception("Wrong password");
            }

            return acc;
        }

        @Override
        protected void succeeded() {
            Account acc = getValue();

            // TODO: sign in complete
//          app.startServer();
//          app.showUIMain();
        }

        @Override
        protected void failed() {
            updateMessage("Sign in failed: " + getException().getMessage());
        }
    }
}
