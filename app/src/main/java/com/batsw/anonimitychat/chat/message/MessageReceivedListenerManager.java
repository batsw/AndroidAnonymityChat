package com.batsw.anonimitychat.chat.message;

import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.chat.ChatListAdapter;
import com.batsw.anonimitychat.persistence.entities.DBChatMessageEntity;

import java.util.HashMap;

/**
 * Created by tudor on 2/21/2017.
 */

public class MessageReceivedListenerManager {
    private static final String LOG = MessageReceivedListenerManager.class.getSimpleName();

    private HashMap<Long, IMessageReceivedListener> mMessageReceivedListenerMap = null;

    public MessageReceivedListenerManager() {
        mMessageReceivedListenerMap = new HashMap<>();
    }

    public void addTorBundleListener(IMessageReceivedListener messageReceivedListener, long sessionId) {
        Log.i(LOG, "addTorBundleListener -> ENTER messageReceivedListener=" + messageReceivedListener + ", sessionId=" + sessionId);

        if (!mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.put(sessionId, messageReceivedListener);
        }

        Log.i(LOG, "addTorBundleListener -> LEAVE");
    }

    public void messageReceived(String message, long sessionId) {
        Log.i(LOG, "messageReceived -> ENTER message=" + message + ", sessionId=" + sessionId);
        final long messageTimestamp = System.currentTimeMillis();

        ChatMessage chatMessage = new ChatMessage(message, ChatMessageType.PARTNER, messageTimestamp);

        IMessageReceivedListener messageReceivedListener = mMessageReceivedListenerMap.get(sessionId);
        if (messageReceivedListener != null) {

            DBChatMessageEntity chatMessageEntity = new DBChatMessageEntity();
            chatMessageEntity.setSessionId(sessionId);
            chatMessageEntity.setMessage(message);
            chatMessageEntity.setChatMessageType(chatMessage.getChatMessageType());
            chatMessageEntity.setTimestamp(messageTimestamp);
            AppController.getInstanceParameterized(null).addMessageToChatHistory(chatMessageEntity);

            messageReceivedListener.showReceivedMessage(chatMessage);
        }
        Log.i(LOG, "messageReceived -> LEAVE");
    }

    public void removeTorBundleListener(long sessionId) {
        Log.i(LOG, "removeTorBundleListener -> ENTER sessionId=" + sessionId);

        if (mMessageReceivedListenerMap.containsKey(sessionId)) {
            mMessageReceivedListenerMap.remove(sessionId);
        }

        Log.i(LOG, "removeTorBundleListener -> LEAVE");
    }
}
