package com.batsw.anonimitychat.chat.message;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.persistence.entities.DBChatMessageEntity;

import java.util.HashMap;

/**
 * Created by tudor on 2/21/2017.
 */

public class MessageReceivedListenerManager {
    private HashMap<Long, IMessageReceivedListener> mMessageReceivedListenerMap = null;

    public MessageReceivedListenerManager() {
        mMessageReceivedListenerMap = new HashMap<>();
    }

    public void addTorBundleListener(IMessageReceivedListener messageReceivedListener, long sessionId) {
        if (!mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.put(sessionId, messageReceivedListener);
        }
    }

    public void messageReceived(String message, long sessionId) {

        final long messageTimestamp = System.currentTimeMillis();

        ChatMessage chatMessage = new ChatMessage(message, ChatMessageType.PARTNER, messageTimestamp);

        IMessageReceivedListener messageReceivedListener = mMessageReceivedListenerMap.get(sessionId);
        if (messageReceivedListener != null) {

            DBChatMessageEntity chatMessageEntity = new DBChatMessageEntity();
            chatMessageEntity.setSessionId(sessionId);
            chatMessageEntity.setMessage(message);
            chatMessageEntity.setTimestamp(messageTimestamp);
            AppController.getInstanceParameterized(null).addMessageToChatHistory(chatMessageEntity);

            messageReceivedListener.showReceivedMessage(chatMessage);
        }
    }

    public void removeTorBundleListener(long sessionId) {
        if (mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.remove(sessionId);
        }
    }
}
