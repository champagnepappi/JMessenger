package com.almasb.jm.common;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String email;
    // TODO: how is it stored on DBMS?
    private String password;

    private String name;
    private String ip;
    private int port;

    private List<String> friends;

    public Account() {}

    public Account(String email, String password, String name, String ip,
            int port, List<String> friends) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getIp() {
        return ip;
    }
    public int getPort() {
        return port;
    }
    public List<String> getFriends() {
        return new ArrayList<>(friends);
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public void addFriend(String email) {
        friends.add(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account user = (Account) o;

        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
