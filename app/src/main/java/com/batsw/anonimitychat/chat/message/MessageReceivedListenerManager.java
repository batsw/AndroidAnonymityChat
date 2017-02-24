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

    public void messageReceived(String message, long sessionId) {
        ChatMessage chatMessage = new ChatMessage(message, ChatMessageType.PARTNER, System.currentTimeMillis());

        IMessageReceivedListener messageReceivedListener = mMessageReceivedListenerMap.get(sessionId);
        if (messageReceivedListener != null) {
            messageReceivedListener.showReceivedMessage(chatMessage);
        }
    }

    public void removeTorBundleListener(long sessionId) {
        if (mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.remove(sessionId);
        }
    }
}
