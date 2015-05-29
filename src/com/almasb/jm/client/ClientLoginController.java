package com.almasb.jm.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import com.almasb.jm.common.Account;
import com.almasb.jm.common.Config;

public class ClientLoginController {

    private JMessengerApp app;

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private TextField name;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnSignUp;
    @FXML
    private Region veil;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label message;

    public void initialize() {
        BooleanProperty emailValid = new SimpleBooleanProperty();
        BooleanProperty passwordValid = new SimpleBooleanProperty();
        BooleanProperty nameValid = new SimpleBooleanProperty();

        email.textProperty().addListener((obs, old, newValue) -> {
            emailValid.set(!newValue.isEmpty() && newValue.contains("@"));
        });

        password.textProperty().addListener((obs, old, newValue) -> {
            passwordValid.set(!newValue.isEmpty() && newValue.length() >= 4);
        });

        name.textProperty().addListener((obs, old, newValue) -> {
            nameValid.set(!newValue.isEmpty() && newValue.length() >= 2);
        });

        btnSignIn.disableProperty().bind(emailValid.not().or(passwordValid.not()).or(nameValid.not()));
        btnSignUp.disableProperty().bind(emailValid.not().or(passwordValid.not()).or(nameValid.not()));
    }

    public void setApp(JMessengerApp app) {
        this.app = app;
    }

    public void signUp() {
        runTask(new SignUpTask());
    }

    public void signIn() {
        runTask(new SignInTask());
    }

    private void runTask(Task<?> task) {
        progress.visibleProperty().bind(task.runningProperty());
        veil.visibleProperty().bind(task.runningProperty());
        message.textProperty().bind(task.messageProperty());

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private class SignUpTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            Optional<Account> account = app.getAccount(email.getText());

            if (account.isPresent()) {
                throw new Exception("Account already exists");
            }
            else {
                Optional<String> ip = Config.getExternalIP();
                if (ip.isPresent()) {
                    Account acc = new Account(email.getText(), password.getText(), name.getText(),
                            ip.get(), Config.TCP_PORT, new ArrayList<>(Arrays.asList("1@1")));
                    boolean ok = app.createAccount(acc);
                    if (!ok) {
                        throw new Exception("Server failed to respond");
                    }
                }
                else {
                    throw new Exception("Failed to resolve IP");
                }
            }

            return null;
        }

        @Override
        protected void succeeded() {
            message.setTextFill(Color.GREEN);
            updateMessage("Sign up complete");
        }

        @Override
        protected void failed() {
            message.setTextFill(Color.RED);
            updateMessage("Sign up failed: " + getException().getMessage());
        }
    }

    private class SignInTask extends Task<Account> {
        @Override
        protected Account call() throws Exception {
            Optional<Account> account = app.getAccount(email.getText());

            Account acc = account.orElseThrow(() -> new Exception("Account not found"));

            if (!password.getText().equals(acc.getPassword())) {
                throw new Exception("Wrong password");
            }

            return acc;
        }

        @Override
        protected void succeeded() {
            Account acc = getValue();
//
//            System.out.println(acc.getName() + " " + acc.getEmail() + " " + acc.getIp());
//            System.out.println(acc.getFriends().toString());

            app.startServer(acc);
            app.showUIMain();
        }

        @Override
        protected void failed() {
            message.setTextFill(Color.RED);
            updateMessage("Sign in failed: " + getException().getMessage());
        }
    }
}
