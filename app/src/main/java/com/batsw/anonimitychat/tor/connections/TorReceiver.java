package com.batsw.anonimitychat.tor.connections;

import android.os.StrictMode;
import android.util.Log;

import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.listener.IncomingConnectionListenerManager;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tudor on 12/16/2016.
 */

public class TorReceiver implements ITorConnection {

    private static final String LOG = TorReceiver.class.getSimpleName();

    private ServerSocket mProviderSocket;
    private Socket mSocketConnection = null;
    private DataInputStream mDataInputStream;
    private DataOutputStream mDataOutputStream;
    private OutputStream mOutputStream;

    private Thread mMessageReceivingThread = null;

    private Thread mWaitingForPartnerThread = null;

    private IncomingConnectionListenerManager mIncomingConnectionListenerManager;
    private MessageReceivedListenerManager mMessageReceivedListenerManager;

    private long mSessionId = 0L;
    private boolean mIsConnected = false;

    private String mPartnerHostname = "";

    public TorReceiver(IncomingConnectionListenerManager incomingConnectionListenerManager, MessageReceivedListenerManager messageReceivedListenerManager) {
        mIncomingConnectionListenerManager = incomingConnectionListenerManager;
        mMessageReceivedListenerManager = messageReceivedListenerManager;

        init();
    }

    private void init() {
        Log.i(LOG, "init -> ENTER");

        mWaitingForPartnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                waitForIncomingConnection();
            }
        });
        mWaitingForPartnerThread.start();

        Log.i(LOG, "init -> LEAVE");
    }

    private void waitForIncomingConnection() {
        try {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            mIsConnected = false;

            mProviderSocket = new ServerSocket(TorConstants.TOR_BUNDLE_INTERNAL_HIDDEN_SERVICES_PORT, 10);
            Log.i(LOG, "Waiting for connection");
            mSocketConnection = mProviderSocket.accept();

            Log.i(LOG, "Connection received from " + mSocketConnection.getInetAddress().getHostName());
            mDataInputStream = new DataInputStream(mSocketConnection.getInputStream());

            mOutputStream = mSocketConnection.getOutputStream();

            mDataOutputStream = new DataOutputStream(mOutputStream);

            startMessageReceivingThread();

            // assuming that at this step the mPartnerHostname is received
            //TODO: think
            //maybe a wait of 1-2 seconds to receive the hostname???

            Thread.sleep(2000);

            if (!mPartnerHostname.isEmpty()) {
                mIsConnected = true;
                mIncomingConnectionListenerManager.triggerPartnerChatRequest(mPartnerHostname);
            }

        } catch (IOException ioException) {
            Log.i(LOG, "error: " + ioException.getMessage(), ioException);
        }  catch (InterruptedException interruptedException) {
            Log.i(LOG, "error at thread sleep: " + interruptedException.getMessage(), interruptedException);
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
        closeCommunication();
    }

    @Override
    public void createConnection() {
        //DO Nothing
    }

    @Override
    public boolean isAlive() {
        return mIsConnected;
    }

    @Override
    public Thread getMessageReceivingThread() {
        return mMessageReceivingThread;
    }

    private void startMessageReceivingThread() {
        mMessageReceivingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                createMessageReceivingLoop();
            }
        });
        mMessageReceivingThread.start();
    }

    /**
     * This is the actual message receiver thread
     */
    //TODO: I need to implement a "finished" message to break the infinite loop.
    //this would happen when the USER has finished chatting and he wants to exit this activity
    private void createMessageReceivingLoop() {
        String incomingMessage = "";
        while (true) {
            try {
                if (mDataInputStream != null) {
                    incomingMessage = mDataInputStream.readUTF();
                    Log.i(LOG, "Message Receved___" + incomingMessage);

                    if (incomingMessage.contains(ChatModelConstants.FIRST_CHAT_MESSAGE)) {

                        String partnerHostname = incomingMessage.substring(ChatModelConstants.FIRST_CHAT_MESSAGE.length());
                        mPartnerHostname = partnerHostname;

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
                    mSocketConnection.close();
                    break;
                } catch (IOException e1) {
                    Log.e(LOG, "error when closing the connection" + e1.getMessage(), e1);
                    break;
                }
            }
        }
    }

    /**
     *
     */
    private void closeCommunication() {
        Log.i(LOG, "closeCommunication -> ENTER");

        try {

            sendMessage(ChatModelConstants.MESSAGE_END_CHAT);

            mMessageReceivingThread.stop();

            if (mDataInputStream != null) mDataInputStream.close();
            if (mDataOutputStream != null) mDataOutputStream.close();
            if (mOutputStream != null) mOutputStream.close();
            if (mSocketConnection != null) mSocketConnection.close();

            mIsConnected = false;

        } catch (IOException e) {
            Log.e(LOG, "error when closing the communication" + e.getMessage(), e);
        }
        Log.i(LOG, "closeCommunication -> LEAVE");
    }

    public void setSessionId(long sessionId) {
        mSessionId = sessionId;
    }
}
