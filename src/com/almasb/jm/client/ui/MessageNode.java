package com.almasb.jm.client.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.almasb.jm.common.DataMessage;

public class MessageNode extends StackPane {

    public MessageNode(DataMessage message) {
        Text text = new Text(message.toString());
        text.setFont(Font.font(18));
        text.setFill(Color.WHITE);

        Rectangle bg = new Rectangle();
        bg.setOpacity(0.4);
        bg.setFill(Color.BLUE);
        bg.setArcWidth(35);
        bg.setArcHeight(35);
        bg.setWidth(200);
        bg.setHeight(50);
        bg.setTranslateX(-10);

        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(bg, text);
    }
}
