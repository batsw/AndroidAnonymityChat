package com.batsw.anonimitychat.chat.management.activity;

import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.management.ChatDetail;

/**
 * Created by tudor on 2/11/2017.
 */

public class ChatActivityManagerImpl implements IChatActivityManager {

    private static final String CHAT_ACTIVITY_MANAGER_TAG = ChatActivityManagerImpl.class.getSimpleName();

    private ChatDetail mChatDetail;

    private ChatActivity mChatActivity = null;

    public ChatActivityManagerImpl() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "ChatActivityManagerImpl -> ENTER");

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "ChatActivityManagerImpl -> LEAVE");
    }

    private void init() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "init -> ENTER");
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "init -> LEAVE");
    }

    @Override
    public void onCreate() {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onCreate -> ENTER");
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
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "onDestroy -> LEAVE");
    }

    @Override
    public void sendMessage(String message) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "sendMessage -> ENTER message=" + message);
        mChatDetail.getTorConnection().sendMessage(message);
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "sendMessage -> LEAVE");
    }

    @Override
    public void showReceivedMessage(String partnerMessage) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "showReceivedMessage -> ENTER");

        mChatActivity.showReceivedPartnerMessage(partnerMessage);

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

        mChatDetail = ChatController.getInstance().getChatDetail(sessionId);

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "getChatDetail -> LEAVE");
    }

    @Override
    public void setChatActivity(ChatActivity chatActivity) {
        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "setChatActivity -> ENTER chatActivity=" + chatActivity);

        mChatActivity = chatActivity;

        Log.i(CHAT_ACTIVITY_MANAGER_TAG, "setChatActivity -> LEAVE");
    }
}
