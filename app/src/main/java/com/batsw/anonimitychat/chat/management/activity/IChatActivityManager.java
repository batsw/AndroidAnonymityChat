package com.batsw.anonimitychat.chat.management.activity;

/**
 * Created by tudor on 2/10/2017.
 */

public interface IChatActivityManager extends IViewModel {

    /**
     * Chat related methods
     */


//public void manageMessages();

    public void sendMessage();

    public void showReceivedMessage();

    public void openHistory();

}
