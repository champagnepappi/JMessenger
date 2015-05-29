package com.almasb.jm.common;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class DataMessage extends AbstractMessage {
    private String name;
    private String message;
    private String email;

    public DataMessage() {
    }

    public DataMessage(String name, String email, String message) {
        setName(name);
        setEmail(email);
        setMessage(message);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name + ": " + message;
    }
}
