package com.batsw.anonimitychat.tor.connections;

import android.os.StrictMode;
import android.util.Log;

import com.batsw.anonimitychat.chat.listener.IncomingConnectionListenerManager;
import com.batsw.anonimitychat.chat.message.MessageReceivedListenerManager;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tudor on 3/13/2017.
 */

public class TorConnectionReceiver {

    private static final String LOG = TorConnectionReceiver.class.getSimpleName();

    private ExecutorService mWaitingForPartnerThread = null;

    private IncomingConnectionListenerManager mIncomingConnectionListenerManager;
    private MessageReceivedListenerManager mMessageReceivedListenerManager;

    private ServerSocket mProviderSocket = null;

    private Socket mCurrentPartnerConnection = null;
    private Socket mPreviousPartnerConnection = null;

    public TorConnectionReceiver(IncomingConnectionListenerManager incomingConnectionListenerManager, MessageReceivedListenerManager messageReceivedListenerManager) {
        mIncomingConnectionListenerManager = incomingConnectionListenerManager;
        mMessageReceivedListenerManager = messageReceivedListenerManager;

        init();
    }

    private void init() {
        Log.i(LOG, "init -> ENTER");

        initSocketServer();

        mWaitingForPartnerThread = Executors.newSingleThreadScheduledExecutor();
        mWaitingForPartnerThread.submit(new Runnable() {
            @Override
            public void run() {
                waitForIncomingConnection();
            }
        });

        Log.i(LOG, "init -> LEAVE");
    }

    private void initSocketServer() {
        Log.i(LOG, "initSocketServer -> ENTER");
        try {
            mProviderSocket = new ServerSocket(TorConstants.TOR_BUNDLE_INTERNAL_HIDDEN_SERVICES_PORT, 10);
        } catch (IOException e) {
            Log.i(LOG, "error  when creating Server Socket: " + e.getMessage(), e);
        }
        Log.i(LOG, "initSocketServer -> LEAVE");
    }

    private void waitForIncomingConnection() {

        Log.i(LOG, "waitForIncomingConnection -> ENTER");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if (mProviderSocket != null) {
            while (mProviderSocket.isBound()) {
                //always listening for new connections. When a new on comes start a new message receiving thread
                try {
                    mCurrentPartnerConnection = mProviderSocket.accept();

                    if (mCurrentPartnerConnection != mPreviousPartnerConnection) {

                        mPreviousPartnerConnection = mCurrentPartnerConnection;

                        Log.i(LOG, "Connection received from " + mCurrentPartnerConnection.getInetAddress().getHostName());

                        new TorReceiverDelegator(mCurrentPartnerConnection, mIncomingConnectionListenerManager, mMessageReceivedListenerManager);
                    }
                } catch (Exception ioException) {
                    Log.i(LOG, "error handling socket: " + ioException.getMessage(), ioException);
                }
            }
        } else {
//            something has happened and the socket must reinit
            closeTorReceiver();
            init();
        }
    }

    public void closeTorReceiver() {
        Log.i(LOG, "closeTorReceiver -> ENTER");

//        try {
//            if (mProviderSocket != null && !mProviderSocket.isClosed()) {
//                mProviderSocket.close();
//            }
//
//            mProviderSocket = null;
//        } catch (IOException ioException) {
//            Log.i(LOG, "error closing the socket: " + ioException.getMessage(), ioException);
//        }

        mWaitingForPartnerThread.shutdownNow();
        mWaitingForPartnerThread = null;

        Log.i(LOG, "closeTorReceiver -> LEAVE");
    }
}
