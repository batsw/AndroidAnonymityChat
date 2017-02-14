package com.batsw.anonimitychat.tor.connections;

/**
 * Created by tudor on 2/13/2017.
 */

public interface ITorConnection {

    public void sendMessage();

    public void closeConnection();

    public void getConnection();

}
