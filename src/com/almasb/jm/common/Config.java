package com.almasb.jm.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;

public class Config {
    public static final String NAME = "JMessenger_Server";
    public static final int VERSION = 1;
    public static final int TCP_PORT = 5555;
    public static final int UDP_PORT = 5555;

    public static Optional<String> getExternalIP() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL("http://icanhazip.com/").openStream()))) {
            return Optional.of(in.readLine());
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
