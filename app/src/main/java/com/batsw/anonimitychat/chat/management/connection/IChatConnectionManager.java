package com.batsw.anonimitychat.chat.management.connection;

import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

/**
 * Created by tudor on 2/10/2017.
 */

public interface IChatConnectionManager {

    public ITorConnection getConnection(ChatDetail chatDetail, IMessageReceivedListener messageReceivedListener);

    public void closeConnection(ChatDetail chatDetail);
}
