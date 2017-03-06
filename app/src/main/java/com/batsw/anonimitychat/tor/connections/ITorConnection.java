package com.batsw.anonimitychat.tor.connections;

import java.util.concurrent.ExecutorService;

/**
 * Created by tudor on 2/13/2017.
 */

public interface ITorConnection {

    public void sendMessage(String message);

    public void closeConnection();

    public void createConnection();

    public boolean isAlive();
    public ExecutorService getMessageReceivingThread();


}
