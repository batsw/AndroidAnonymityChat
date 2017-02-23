package com.batsw.anonimitychat.chat.management.connection;

import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

/**
 * Created by tudor on 2/11/2017.
 */

public class ChatConnectionManagerImpl implements IChatConnectionManager {

    private static final String CHAT_CONNECTION_MANAGER_LOG = ChatConnectionManagerImpl.class.getSimpleName();

    private MessageReceivedListenerManager mMessageReceivedListenerManager = null;

    public ChatConnectionManagerImpl() {
        mMessageReceivedListenerManager = new MessageReceivedListenerManager();
    }

    @Override
    public ITorConnection getConnection(ChatDetail chatDetail, IMessageReceivedListener messageReceivedListener) {



        mMessageReceivedListenerManager.addTorBundleListener(messageReceivedListener, chatDetail.getSessionId());

        return null;
    }

    @Override
    public void closeConnection(ChatDetail chatDetail) {



        mMessageReceivedListenerManager.removeTorBundleListener(chatDetail.getSessionId());
    }
}