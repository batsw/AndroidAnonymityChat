package com.batsw.anonimitychat.tor.connections;

import android.util.Log;

import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.listener.IncomingConnectionListenerManager;
import com.batsw.anonimitychat.chat.management.connection.ChatConnectionManagerImpl;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tudor on 3/13/2017.
 */

public class TorReceiverDelegator implements ITorConnection {

    private static final String LOG = TorReceiverDelegator.class.getSimpleName();

    private Socket mConnection;
    private MessageReceivedListenerManager mMessageReceivedListenerManager;
    private IncomingConnectionListenerManager mIncomingConnectionListenerManager;

    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;

    private String mPartnerAddress;
    private Long mSessionId;
    private boolean mIsAlive = false;
    private boolean mTriggeredChatRequest = false;

    private ExecutorService mMessageReceivingExecutor;

    public TorReceiverDelegator(Socket connection, IncomingConnectionListenerManager incomingConnectionListenerManager, MessageReceivedListenerManager messageReceivedListenerManager) {
        mConnection = connection;
        mMessageReceivedListenerManager = messageReceivedListenerManager;
        mIncomingConnectionListenerManager = incomingConnectionListenerManager;

        init();
    }

    private void init() {
        Log.i(LOG, "init -> ENTER");

        try {
            mDataInputStream = new DataInputStream(mConnection.getInputStream());
            mDataOutputStream = new DataOutputStream(mConnection.getOutputStream());

            startMessageReceivingThread(mDataInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(LOG, "init -> LEAVE");
    }

    @Override
    public void createConnection() {
        Log.i(LOG, "createConnection -> ENTER");


        Log.i(LOG, "createConnection -> LEAVE");
    }

    @Override
    public ExecutorService getMessageReceivingThread() {
        Log.i(LOG, "createConnection -> ENTER");
        Log.i(LOG, "createConnection -> LEAVE mMessageReceivingExecutor=" + mMessageReceivingExecutor);
        return mMessageReceivingExecutor;
    }

    private void startMessageReceivingThread(final DataInputStream dataInputStream) {
        Log.i(LOG, "startMessageReceivingThread -> ENTER dataInputStream=" + dataInputStream);

        mMessageReceivingExecutor = Executors.newSingleThreadScheduledExecutor();
        mMessageReceivingExecutor.submit(new Runnable() {
            @Override
            public void run() {
                createMessageReceivingLoop(dataInputStream);
            }
        });

        Log.i(LOG, "startMessageReceivingThread -> LEAVE");
    }

    /**
     * This is the actual message receiver thread
     */
    private void createMessageReceivingLoop(DataInputStream dataInputStream) {
        String incomingMessage = "";
        while (true) {
            try {
                if (dataInputStream != null) {
                    incomingMessage = dataInputStream.readUTF();
                    Log.i(LOG, "Message Receved___" + incomingMessage);

                    if (incomingMessage.contains(ChatModelConstants.FIRST_CHAT_MESSAGE)) {

                        String partnerAddress = incomingMessage.substring(ChatModelConstants.FIRST_CHAT_MESSAGE.length());
                        String sanitizedPartnerAddress = partnerAddress.trim();
                        mPartnerAddress = sanitizedPartnerAddress;

                        //triggering incomming connection received
                        if (!mPartnerAddress.isEmpty() && !mTriggeredChatRequest) {

                            mIsAlive = true;

                            ChatConnectionManagerImpl.addReceivedConnection(mPartnerAddress, this);

                            mIncomingConnectionListenerManager.triggerPartnerChatRequest(mPartnerAddress);

                            mTriggeredChatRequest = true;
                        }
                    }

                    if (!incomingMessage.equals(ChatModelConstants.MESSAGE_END_CHAT)) {
                        mMessageReceivedListenerManager.messageReceived(incomingMessage, mSessionId);
                    } else {
                        Log.i(LOG, "END CHAT Message Receved___" + incomingMessage);
                        break;
                    }
                }
            } catch (IOException e) {
                //TODO : I don't think the connection should be closed . Only when exiting the Activity
                Log.e(LOG, "error when receiving a message" + e.getMessage(), e);
                try {
                    dataInputStream.close();
                    break;
                } catch (IOException e1) {
                    Log.e(LOG, "error when closing the connection" + e1.getMessage(), e1);
                    break;
                }
            }
        }

        // after the receiving thread was broken, stop receiving
        try {
            if (dataInputStream != null)
                dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        Log.i(LOG, "sendMessage -> ENTER message=" + message);

        String preparedMessage = message + ChatModelConstants.MESSAGE_EOL;

        try {
            mDataOutputStream.writeUTF(preparedMessage);
            mDataOutputStream.flush();
        } catch (IOException ioException) {
            Log.e(LOG, "error when sending message: " + ioException.getMessage(), ioException);
        }

        Log.i(LOG, "sendMessage -> LEAVE");
    }

    @Override
    public void closeConnection() {
        Log.i(LOG, "closeConnection -> ENTER");

        if (mDataInputStream != null) {
            try {
                mDataInputStream.close();
            } catch (IOException e) {
                Log.e(LOG, "error when closing connection: " + e.getMessage(), e);
            }
        }

        mMessageReceivingExecutor.shutdown();
        mMessageReceivingExecutor.shutdownNow();

        mMessageReceivingExecutor = null;

        Log.i(LOG, "closeConnection -> LEAVE");
    }

    @Override
    public boolean isAlive() {
        return mIsAlive;
    }

    public void setSessionId(long sessionId) {
        mSessionId = sessionId;
    }
}
