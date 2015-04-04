package com.almasb.jm.server;

import com.almasb.jm.common.DataMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class MessageHandler implements MessageListener<HostedConnection> {
    @Override
    public void messageReceived(HostedConnection source, Message m) {
        if (m instanceof DataMessage) {
            source.setAttribute("name", ((DataMessage) m).getName());

            System.out.println("Received: " + m);

            source.getServer().broadcast(m);
        }
        else {
            System.out.println("Received odd message: " + m);
        }
    }
}
