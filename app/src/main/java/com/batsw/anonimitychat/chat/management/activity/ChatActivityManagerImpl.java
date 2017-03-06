package com.batsw.anonimitychat.chat.management.activity;

import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;

/**
 * Created by tudor on 2/11/2017.
 */

public class ChatActivityManagerImpl implements IChatActivityManager, IMessageReceivedListener {

    private static final String CHAT_ACTIVITY_MANAGER_TAG = ChatActivityManagerImpl.class.getSimpleName();

    private ChatActivity mChatActivity = null;

    private long mSessionId = 0L;

    public ChatActivityManagerImpl() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "ChatActivityManagerImpl -> ENTER");

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "ChatActivityManagerImpl -> LEAVE");
    }

    @Override
    public void onCreate() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onCreate -> ENTER");

        //Preparing TorConnection with partner
        ChatController.getInstance().establishConnectionToPartner(this, mSessionId);

        //TODO .........
        //If the connection could not be established means that the partner is offline

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onCreate -> LEAVE");
    }

    @Override
    public void onPause() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onPause -> ENTER");
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onPause -> LEAVE");
    }

    @Override
    public void onResume() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onResume -> ENTER");
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onResume -> LEAVE");

    }

    @Override
    public void onDestroy() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onDestroy -> ENTER");

        ChatController.getInstance().stoppedChatActivity(this, mSessionId);

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onDestroy -> LEAVE");
    }

    @Override
    public void sendMessage(String message) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "sendMessage -> ENTER message=" + message);

        ChatController.getInstance().sendMessage(mSessionId, message);

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "sendMessage -> LEAVE");
    }

    @Override
    public void showReceivedMessage(final ChatMessage partnerMessage) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "showReceivedMessage -> ENTER");

        mChatActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatActivity.showReceivedPartnerMessage(partnerMessage);
            }
        });

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "showReceivedMessage -> LEAVE");
    }

    @Override
    public void loadHistory() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "openHistory -> ENTER");
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "openHistory -> LEAVE");
    }

    @Override
    public void configureChatDetail(long sessionId) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "getChatDetail -> ENTER sessionId=" + sessionId);

        mSessionId = sessionId;

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "getChatDetail -> LEAVE");
    }

    @Override
    public void setChatActivity(ChatActivity chatActivity) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "setChatActivity -> ENTER chatActivity=" + chatActivity);

        mChatActivity = chatActivity;

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "setChatActivity -> LEAVE");
    }
}
