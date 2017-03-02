package com.batsw.anonimitychat.chat.management.connection;

import android.util.Log;

import com.batsw.anonimitychat.chat.listener.IncomingConnectionListenerManager;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.message.IMessageReceivedListener;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.chat.util.ConnectionType;
import com.batsw.anonimitychat.tor.connections.ITorConnection;
import com.batsw.anonimitychat.tor.connections.TorPublisher;
import com.batsw.anonimitychat.tor.connections.TorReceiver;

/**
 * Created by tudor on 2/11/2017.
 */

public class ChatConnectionManagerImpl implements IChatConnectionManager {

    private static final String CHAT_CONNECTION_MANAGER_LOG = ChatConnectionManagerImpl.class.getSimpleName();

    private MessageReceivedListenerManager mMessageReceivedListenerManager = null;

    private IncomingConnectionListenerManager mIncomingConnectionListenerManager = null;

    private ITorConnection mCurrentTorReceiver = null;

    public ChatConnectionManagerImpl() {
        mMessageReceivedListenerManager = new MessageReceivedListenerManager();

        mIncomingConnectionListenerManager = new IncomingConnectionListenerManager();
        mIncomingConnectionListenerManager.addIncomingConnectionListener(ChatController.getInstance());

        startTorReceiverThread();
    }

    @Override
    public ITorConnection getConnection(ChatDetail chatDetail, IMessageReceivedListener messageReceivedListener) {
        Log.i(CHAT_CONNECTION_MANAGER_LOG, "getConnection -> ENTER ");
        ITorConnection retVal = null;

        if (chatDetail.getConnectionType().equals(ConnectionType.USER)) {

            retVal = new TorPublisher(mMessageReceivedListenerManager, chatDetail.getPartnerAddress(), chatDetail.getSessionId());
            retVal.createConnection();

        } else if (chatDetail.getConnectionType().equals(ConnectionType.PARTNER)) {

            ((TorReceiver) mCurrentTorReceiver).setSessionId(chatDetail.getSessionId());

            retVal = mCurrentTorReceiver;


        }

        if (retVal.isAlive()) {
            mMessageReceivedListenerManager.addTorBundleListener(messageReceivedListener, chatDetail.getSessionId());
        }

        Log.i(CHAT_CONNECTION_MANAGER_LOG, "getConnection -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public void closeConnection(ChatDetail chatDetail) {

        chatDetail.setIsAlive(false);
        if (chatDetail.getConnectionType().equals(ConnectionType.USER)) {
            chatDetail.getTorConnection().closeConnection();
            chatDetail.setTorConnection(null);
            chatDetail.setmConnectionType(ConnectionType.NO_CONNECTION);
        } else if (chatDetail.getConnectionType().equals(ConnectionType.PARTNER)) {

            //TODO: manage it ....


            chatDetail.setTorConnection(null);
            chatDetail.setmConnectionType(ConnectionType.NO_CONNECTION);
        }

        mMessageReceivedListenerManager.removeTorBundleListener(chatDetail.getSessionId());
    }

    private void startTorReceiverThread(){
        new Thread(new Runnable() {
            public void run() {
                mCurrentTorReceiver = new TorReceiver(mIncomingConnectionListenerManager, mMessageReceivedListenerManager);
            }
        }).start();
    }
}