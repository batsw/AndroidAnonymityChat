package com.batsw.anonimitychat.chat.management;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.activity.ChatActivityManagerImpl;
import com.batsw.anonimitychat.chat.management.activity.IChatActivityManager;
import com.batsw.anonimitychat.chat.management.connection.ChatConnectionManagerImpl;
import com.batsw.anonimitychat.chat.management.connection.IChatConnectionManager;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.chat.persistence.PersistenceManager;
import com.batsw.anonimitychat.chat.util.ConnectionType;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

import java.util.UUID;

/**
 * Created by tudor on 2/10/2017.
 */

public class ChatController {

    private static final String CHAT_CONTROLLER_LOG = ChatController.class.getSimpleName();

    private PersistenceManager mPrsistenceManager;

    private IChatConnectionManager mChatConnectionManager;

    private static ChatController mInstance;

    private ChatController() {

        init();
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
        Log.i(CHAT_CONTROLLER_LOG, "init -> ENTER");

        mPrsistenceManager = new PersistenceManager();

        mChatConnectionManager = new ChatConnectionManagerImpl();

        Log.i(CHAT_CONTROLLER_LOG, "init -> LEAVE");
    }

    public void establishConnectionToPartner(IMessageReceivedListener messageReceivedListener, long sesssionId) {
        ChatDetail chatDetail = this.getChatDetail(sesssionId);

        ITorConnection partnerConnection = mChatConnectionManager.getConnection(chatDetail, messageReceivedListener);
        chatDetail.setTorConnection(partnerConnection);
    }

    public void sendMessage(long sessionId, String message) {
        Log.i(CHAT_CONTROLLER_LOG, "sendMessage -> ENTER sessionId=" + sessionId + " ; message=" + message);

        ChatDetail chatDetail = this.getChatDetail(sessionId);
        if (chatDetail.isAlive()) {

            chatDetail.getTorConnection().sendMessage(message);
        }

        Log.i(CHAT_CONTROLLER_LOG, "sendMessage -> LEAVE");
    }

    private ChatDetail getChatDetailForChatAction(String partnerAddress) {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER partnerAddress=" + partnerAddress);

        ChatDetail retVal = null;

        if (mPrsistenceManager.isPartnerInTheList(partnerAddress)) {
            retVal = mPrsistenceManager.getPartnerDetail(partnerAddress);
        } else {

            // means that the contact is new and it will be added with DEFAULT prameters in the Contacts List
            // default nickName for address is the address itself
            //TODO: differentiate between the two connection types
            ChatDetail newChatDetail = new ChatDetail(partnerAddress, partnerAddress, null, ConnectionType.USER, generateSessionId(), false);

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

    public void stoppedChatActivity(IMessageReceivedListener messageReceivedListener, long sessionId) {
        Log.i(CHAT_CONTROLLER_LOG, "stoppedChatActivity -> ENTER");

        ChatDetail chatDetail = this.getChatDetail(sessionId);
        mChatConnectionManager.closeConnection(chatDetail);

        Log.i(CHAT_CONTROLLER_LOG, "stoppedChatActivity -> LEAVE");
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
        chatActivityIntent.putExtra(ChatModelConstants.CHAT_ACTIVITY_INTENT_EXTRA_KEY, ChatController.getInstance().getChatDetailForChatAction(partnerHostName).getSessionId());

        currentContext.startActivity(chatActivityIntent);
        Log.i(CHAT_CONTROLLER_LOG, "startChatActivity -> LEAVE");
    }

    /**
     * This method must be used when Tor Bundle is stopping or it is commanded to stop.<br>
     */
    public void clearResources() {
        Log.i(CHAT_CONTROLLER_LOG, "cleanResources -> ENTER");

        Log.i(CHAT_CONTROLLER_LOG, "Not implemented yet!");

        Log.i(CHAT_CONTROLLER_LOG, "cleanResources -> LEAVE");
    }
}
