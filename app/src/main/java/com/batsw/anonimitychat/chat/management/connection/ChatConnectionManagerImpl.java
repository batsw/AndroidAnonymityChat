package com.batsw.anonimitychat.chat.management.connection;

import android.util.Log;

import com.batsw.anonimitychat.chat.listener.IncomingConnectionListenerManager;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.chat.util.ConnectionType;
import com.batsw.anonimitychat.tor.connections.ITorConnection;
import com.batsw.anonimitychat.tor.connections.TorConnectionReceiver;
import com.batsw.anonimitychat.tor.connections.TorPublisher;
import com.batsw.anonimitychat.tor.connections.TorReceiverDelegator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tudor on 2/11/2017.
 */

public class ChatConnectionManagerImpl implements IChatConnectionManager {

    private static final String CHAT_CONNECTION_MANAGER_LOG = ChatConnectionManagerImpl.class.getSimpleName();

    private MessageReceivedListenerManager mMessageReceivedListenerManager = null;
    private IncomingConnectionListenerManager mIncomingConnectionListenerManager = null;

    //TODO:manage it... it is never stopped
    private TorConnectionReceiver mTorConnectionReceiver = null;

    private static Map<String, ITorConnection> mActiveConnections = new HashMap<>();

    public ChatConnectionManagerImpl() {
        mMessageReceivedListenerManager = new MessageReceivedListenerManager();
        mIncomingConnectionListenerManager = new IncomingConnectionListenerManager();
    }

    @Override
    public void initializeConnectionManagement() {
        Log.i(CHAT_CONNECTION_MANAGER_LOG, "initializeConnectionManagement -> ENTER ");

        mIncomingConnectionListenerManager.addIncomingConnectionListener(ChatController.getInstance());

        mTorConnectionReceiver = new TorConnectionReceiver(mIncomingConnectionListenerManager, mMessageReceivedListenerManager);

        Log.i(CHAT_CONNECTION_MANAGER_LOG, "initializeConnectionManagement -> LEAVE");
    }

    @Override
    public ITorConnection getConnection(ChatDetail chatDetail, IMessageReceivedListener messageReceivedListener) {
        Log.i(CHAT_CONNECTION_MANAGER_LOG, "getConnection -> ENTER ");
        ITorConnection retVal = null;

        if (chatDetail.getConnectionType().equals(ConnectionType.USER)) {

            retVal = new TorPublisher(mMessageReceivedListenerManager, chatDetail.getPartnerAddress(), chatDetail.getSessionId());
            retVal.createConnection();

        } else if (chatDetail.getConnectionType().equals(ConnectionType.PARTNER)) {

            ITorConnection torConnection = mActiveConnections.get(chatDetail.getPartnerAddress());

            ((TorReceiverDelegator) torConnection).setSessionId(chatDetail.getSessionId());

            retVal = torConnection;
        }

        ///TODO: what if is not alive?  ---
        //what if is NULL ---- waiting to crash
        if (retVal.isAlive()) {
            mMessageReceivedListenerManager.addTorBundleListener(messageReceivedListener, chatDetail.getSessionId());
        }

        Log.i(CHAT_CONNECTION_MANAGER_LOG, "getConnection -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public void closeConnection(ChatDetail chatDetail) {
        Log.i(CHAT_CONNECTION_MANAGER_LOG, "closeConnection -> ENTER chatDetail=" + chatDetail);

        chatDetail.setIsAlive(false);
        if (chatDetail.getConnectionType().equals(ConnectionType.USER)) {
            chatDetail.getTorConnection().closeConnection();
            chatDetail.setTorConnection(null);
            chatDetail.setmConnectionType(ConnectionType.NO_CONNECTION);
        } else if (chatDetail.getConnectionType().equals(ConnectionType.PARTNER)) {

            //TODO: manage it ....
            chatDetail.getTorConnection().closeConnection();

            chatDetail.setTorConnection(null);
            chatDetail.setmConnectionType(ConnectionType.NO_CONNECTION);

            mActiveConnections.remove(chatDetail.getPartnerAddress());
        }

        mMessageReceivedListenerManager.removeTorBundleListener(chatDetail.getSessionId());

        Log.i(CHAT_CONNECTION_MANAGER_LOG, "closeConnection -> LEAVE");
    }

    public static void addReceivedConnection(String partnerAddress, ITorConnection torConnection) {
        if (torConnection instanceof TorReceiverDelegator) {
            mActiveConnections.put(partnerAddress, torConnection);
        }
    }
}