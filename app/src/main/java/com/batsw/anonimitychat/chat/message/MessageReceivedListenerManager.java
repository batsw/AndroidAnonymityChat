package com.batsw.anonimitychat.chat.message;

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

    public void messageReceived(String message) {
        ChatMessage chatMessage = new ChatMessage(message, ChatMessageType.PARTNER, System.currentTimeMillis());
        for (IMessageReceivedListener torStatusListener : mMessageReceivedListenerMap.values()) {
            torStatusListener.showReceivedMessage(chatMessage);
        }
    }

    public void removeTorBundleListener(long sessionId) {
        if (mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.remove(sessionId);
        }
    }
}
