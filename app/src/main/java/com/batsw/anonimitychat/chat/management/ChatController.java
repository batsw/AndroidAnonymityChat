package com.batsw.anonimitychat.chat.management;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.listener.IIncomingConnectionListener;
import com.batsw.anonimitychat.chat.management.connection.ChatConnectionManagerImpl;
import com.batsw.anonimitychat.chat.management.connection.IChatConnectionManager;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.chat.persistence.PersistenceManager;
import com.batsw.anonimitychat.chat.util.ConnectionType;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

import java.util.UUID;

/**
 * Created by tudor on 2/10/2017.
 */

public class ChatController implements IIncomingConnectionListener {

    private static final String CHAT_CONTROLLER_LOG = ChatController.class.getSimpleName();

    private PersistenceManager mPersistenceManager;

    private IChatConnectionManager mChatConnectionManager;

    private static ChatController mInstance;

    private static Context mCurrentActivityContext = null;

    private static boolean isIncomingChatConnection = false;

    private String mMyTorAddress = "";

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

        mPersistenceManager = new PersistenceManager();

        mChatConnectionManager = new ChatConnectionManagerImpl();

        Log.i(CHAT_CONTROLLER_LOG, "init -> LEAVE");
    }

    public void initializeChatConnectionManagement() {
        Log.i(CHAT_CONTROLLER_LOG, "initializeChatConnectionManagement -> ENTER");

        mChatConnectionManager.initializeConnectionManagement();

        Log.i(CHAT_CONTROLLER_LOG, "initializeChatConnectionManagement -> LEAVE");
    }

    public void establishConnectionToPartner(IMessageReceivedListener messageReceivedListener, long sessionId) {
        ChatDetail chatDetail = this.getChatDetail(sessionId);

        ITorConnection partnerConnection = mChatConnectionManager.getConnection(chatDetail, messageReceivedListener);
        if (partnerConnection.isAlive()) {
            chatDetail.setIsAlive(true);
            chatDetail.setTorConnection(partnerConnection);
        }
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

        if (mPersistenceManager.isPartnerInTheList(partnerAddress)) {
            retVal = mPersistenceManager.getPartnerDetail(partnerAddress);
            retVal.setmConnectionType(ConnectionType.NO_CONNECTION);
        } else {

            // means that the contact is new and it will be added with DEFAULT prameters in the Contacts List
            // default nickName for address is the address itself
            //TODO: differentiate between the two connection types
            ChatDetail newChatDetail = new ChatDetail(partnerAddress, partnerAddress, null, ConnectionType.NO_CONNECTION, generateSessionId(), false);

            mPersistenceManager.addPartnerToList(newChatDetail);

            retVal = newChatDetail;
        }

        if (!isIncomingChatConnection) {
            retVal.setmConnectionType(ConnectionType.USER);
        } else {
            retVal.setmConnectionType(ConnectionType.PARTNER);

            isIncomingChatConnection = false;
        }

        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private long generateSessionId() {
        Log.i(CHAT_CONTROLLER_LOG, "generateSessionId -> ENTER");

        long retVal = UUID.randomUUID().getMostSignificantBits();

        Log.i(CHAT_CONTROLLER_LOG, "generateSessionId -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public ChatDetail getChatDetail(long sessionId) {
        Log.i(CHAT_CONTROLLER_LOG, "getChatDetail -> ENTER sessionId=" + sessionId);

        ChatDetail retVal = null;

        if (mPersistenceManager.isPartnerInTheList(sessionId)) {
            retVal = mPersistenceManager.getPartnerDetail(sessionId);
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

        chatActivityIntent.putExtra(ChatModelConstants.CHAT_ACTIVITY_INTENT_EXTRA_KEY, ChatController.getInstance().getChatDetailForChatAction(partnerHostName).getSessionId());

        chatActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        currentContext.startActivity(chatActivityIntent);
        Log.i(CHAT_CONTROLLER_LOG, "startChatActivity -> LEAVE");
    }

    @Override
    public void triggerIncomingPartnerConnectionEvent(String partnerHostName) {
        Log.i(CHAT_CONTROLLER_LOG, "triggerIncomingPartnerConnectionEvent -> ENTER partnerHostName=" + partnerHostName);

        //TODO: trigger the creation of a POP-up on the screen to let the USER decide whether to continue the
        if (partnerHostName != null || !partnerHostName.isEmpty()) {

            isIncomingChatConnection = true;

            ChatController.getInstance().startChatActivity(mCurrentActivityContext, partnerHostName);
        }
        Log.i(CHAT_CONTROLLER_LOG, "triggerIncomingPartnerConnectionEvent -> LEAVE");
    }

    //TODO: check this ...
    public void setCurrentActivityContext(Context context) {
        Log.i(CHAT_CONTROLLER_LOG, "setCurrentActivityContext -> ENTER context=" + context);

        mCurrentActivityContext = context;

        Log.i(CHAT_CONTROLLER_LOG, "setCurrentActivityContext -> LEAVE");
    }

    public void setMyAddress(String myTorAddress) {
        mMyTorAddress = myTorAddress;
    }

    public String getMyAddress() {
        return mMyTorAddress;
    }

    public static void cleanUp() {
        if (mInstance != null) {
            mInstance.destroy();
            mInstance = null;
        }
    }

    private void destroy() {
        mPersistenceManager = null;
        mChatConnectionManager = null;
    }
}
