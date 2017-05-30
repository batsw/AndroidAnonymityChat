package com.batsw.anonimitychat.tor.connections;

import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import socks.Socks5Proxy;
import socks.SocksSocket;

/**
 * Created by tudor on 12/16/2016.
 */

public class TorPublisher implements ITorConnection, Serializable {

    private static final String LOG = TorPublisher.class.getSimpleName();

    public String mDestinationAddress = "";
    public MessageReceivedListenerManager mMessageReceivedListenerManager;
    private long mSessionId;

    private ExecutorService mMessageReceivingExecutor;

    private Socket mSocketConnection;

    private OutputStream mOutputStream;
    private DataOutputStream mDataOutputStream;
    private DataInputStream mDataInputStream;

    boolean mIsConnected = false;

    public TorPublisher(MessageReceivedListenerManager messageReceivedListenerManager, String partnerHostName, long sessionId) {
        mMessageReceivedListenerManager = messageReceivedListenerManager;
        mDestinationAddress = partnerHostName;
        mSessionId = sessionId;

    }

    @Override
    public void createConnection() {
        Log.i(LOG, "createConnection -> ENTER");

        mIsConnected = establishConnectionToPartner();
        if (mIsConnected == true) {
            startMessageReceivingThread();
        }

        Log.i(LOG, "createConnection -> LEAVE");
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

        mMessageReceivingExecutor.shutdown();
        mMessageReceivingExecutor.shutdownNow();
        mMessageReceivingExecutor = null;

        closeCommunication();

        Log.i(LOG, "closeConnection -> LEAVE");
    }

    @Override
    public boolean isAlive() {
        return mIsConnected;
    }

    @Override
    public ExecutorService getMessageReceivingThread() {
        return mMessageReceivingExecutor;
    }

    private boolean establishConnectionToPartner() {
        Log.i(LOG, "establishConnectionToPartner -> ENTER");
        boolean retVal = false;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Socks5Proxy socks5Proxy = new Socks5Proxy("127.0.0.1", TorConstants.TOR_BUNDLE_INTERNAL_SOCKS_PORT);
            socks5Proxy.resolveAddrLocally(false);
            mSocketConnection = new SocksSocket(socks5Proxy, mDestinationAddress, TorConstants.TOR_BUNDLE_EXTERNAL_PORT);
            Log.i(LOG, "Connected to target address");

            mOutputStream = mSocketConnection.getOutputStream();
            mDataOutputStream = new DataOutputStream(mOutputStream);

            mDataInputStream = new DataInputStream(mSocketConnection.getInputStream());

            // first after connecting, send my address to partner
            sendMessage(ChatModelConstants.FIRST_CHAT_MESSAGE + ChatController.getInstance().getMyAddress());

        } catch (UnknownHostException unknownHost) {
            Log.e(LOG, "You are trying to connect to an unknown host! " + unknownHost.getStackTrace().toString(), unknownHost);

            retVal = false;

        } catch (IOException ioException) {
            Log.e(LOG, "error: " + ioException.getMessage(), ioException);

            retVal = false;
        }

        if ((mDataInputStream != null) && (mDataOutputStream != null)) {
            retVal = true;
        }

        Log.i(LOG, "establishConnectionToPartner -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private void startMessageReceivingThread() {

        mMessageReceivingExecutor = Executors.newSingleThreadScheduledExecutor();
        mMessageReceivingExecutor.submit(new Runnable() {
            @Override
            public void run() {
                createMessageReceivingLoop();
            }
        });
    }

    /**
     *
     */
    private void closeCommunication() {
        Log.i(LOG, "closeCommunication -> ENTER");

        try {

            sendMessage(ChatModelConstants.MESSAGE_END_CHAT);

            if (mDataInputStream != null) mDataInputStream.close();
            if (mDataOutputStream != null) mDataOutputStream.close();
            if (mOutputStream != null) mOutputStream.close();
            if (mSocketConnection != null) mSocketConnection.close();

        } catch (IOException e) {
            Log.e(LOG, "error when closing the communication" + e.getMessage(), e);
        }
        Log.i(LOG, "closeCommunication -> LEAVE");
    }

    /**
     * This is the actual message receiver thread
     */
    //TODO: I need to implement a "finished" message to break the infinite loop.
    //this would happen when the USER has finished chatting and he wants to exit this activity
    // DO i need it? I will shut down the thread when closing the activity ....
    private void createMessageReceivingLoop() {
        String incomingMessage = "";
        while (true) {
            try {
                if (mDataInputStream != null) {
                    incomingMessage = mDataInputStream.readUTF();
                    Log.i(LOG, "Message Receved___" + incomingMessage);

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
}
