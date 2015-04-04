package com.almasb.jm.server;

import java.util.Scanner;

import com.almasb.jm.common.Config;
import com.almasb.jm.common.DataMessage;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        Serializer.registerClass(DataMessage.class);

        Server server = Network.createServer(Config.NAME, Config.VERSION, Config.TCP_PORT, Config.UDP_PORT);
        server.start();

        MessageHandler handler = new MessageHandler();
        server.addMessageListener(handler, DataMessage.class);

        Scanner scan = new Scanner(System.in);
        scan.nextLine();
        scan.close();
        server.close();
    }
}
