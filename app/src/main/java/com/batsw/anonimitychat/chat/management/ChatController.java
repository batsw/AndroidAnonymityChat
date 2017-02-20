package com.batsw.anonimitychat.chat.management;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.activity.ChatActivityManagerImpl;
import com.batsw.anonimitychat.chat.management.activity.IChatActivityManager;
import com.batsw.anonimitychat.chat.persistence.PersistenceManager;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

import java.util.UUID;

/**
 * Created by tudor on 2/10/2017.
 */

public class ChatController {

    private static final String CHAT_CONTROLLER_LOG = ChatController.class.getSimpleName();

    private IChatActivityManager mChatActivityManager;
    private PersistenceManager mPrsistenceManager;

    private static ChatController mInstance;

    private ChatController() {
    }

    public static synchronized ChatController getInstance() {
        if (mInstance == null) {
            mInstance = new ChatController();
        }
        return mInstance;
    }

    /**
     * preparing the concurrent resources
     */
    private void init() {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER");

        mPrsistenceManager = new PersistenceManager();
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE");
    }

    //TODO: create the ChatDetail if it not found in the PersistenceManager
    private void createChatDetail() {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER");
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE");
    }

    public void sendMessage() {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER");
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE");
    }

    public void getPartnerAddress() {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER");
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE");
    }

    public void destroyed() {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER");

        Log.i(CHAT_CONTROLLER_LOG, "Not implemented yet!");

        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE");
    }

    public ChatDetail getChatDetail(String partnerAddress) {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER partnerAddress=" + partnerAddress);

        ChatDetail retVal = null;

        if (mPrsistenceManager.isPartnerInTheList(partnerAddress)) {
            retVal = mPrsistenceManager.getPartnerDetail(partnerAddress);
        } else {

            // means that the contact is new and it will be added with DEFAULT prameters in the Partners List
            ChatDetail newChatDetail = new ChatDetail(partnerAddress, new String(), null, generateSessionId(), false);

            mPrsistenceManager.addPartnerToList(newChatDetail);

            retVal = newChatDetail;
        }

        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private long generateSessionId() {
        Log.i(CHAT_CONTROLLER_LOG, "generateSessionId -> ENTER");

        long retVal = UUID.randomUUID().timestamp();

        Log.i(CHAT_CONTROLLER_LOG, "generateSessionId -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public ChatDetail getChatDetail(long sessionId) {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER sessionId=" + sessionId);

        ChatDetail retVal = null;

        if (mPrsistenceManager.isPartnerInTheList(sessionId)) {
            retVal = mPrsistenceManager.getPartnerDetail(sessionId);
        }

        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public ChatDetail getChatDetail(ITorConnection torConnection) {
        return null;
    }

    /**
     * This method is starting a ChatActivity from whatever Activity
     *
     * @param currentContext
     */
    public void startChatActivity(Context currentContext, String partnerHostName) {
        Log.i(CHAT_CONTROLLER_LOG, "startChatActivity -> ENTER currentContext=" + currentContext + " ,partnerHostName=" + partnerHostName);


        Intent chatActivityIntent = ChatActivity.makeIntent(currentContext);

        //TODO: how to manage chatDetail and session ID
        chatActivityIntent.putExtra(ChatModelConstants.CHAT_ACTIVITY_INTENT_EXTRA_KEY, ChatController.getInstance().getChatDetail(partnerHostName).getSessionId());

        currentContext.startActivity(chatActivityIntent);
        Log.i(CHAT_CONTROLLER_LOG, "startChatActivity -> LEAVE");
    }

    /**
     * This method must be used when Tor Bundle is stopping or it is commanded to stop.<br>
     */
    public void cleanResources() {
        Log.i(CHAT_CONTROLLER_LOG, "cleanResources -> ENTER");

        Log.i(CHAT_CONTROLLER_LOG, "Not implemented yet!");

        Log.i(CHAT_CONTROLLER_LOG, "cleanResources -> LEAVE");
    }
}
