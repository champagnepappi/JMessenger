package com.almasb.jm.client.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.almasb.jm.common.DataMessage;

public class MessagePane extends ScrollPane {

    private VBox vbox = new VBox(10);

    public MessagePane() {
        super();
        vbox.setAlignment(Pos.TOP_LEFT);
        setContent(vbox);
        this.vvalueProperty().bind(vbox.heightProperty());
    }

    public void appendMessage(DataMessage message) {
        MessageNode node = new MessageNode(message);
        vbox.getChildren().add(node);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.4), node);
        ft.setFromValue(0);
        ft.setToValue(1);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.4), node);
        tt.setFromX(-200);
        tt.setToX(0);

        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.play();
    }
}
