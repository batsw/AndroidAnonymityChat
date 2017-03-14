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

/**
 * Created by tudor on 3/13/2017.
 */

public class TorConnectionReceiver {

    private static final String LOG = TorConnectionReceiver.class.getSimpleName();

    private Thread mWaitingForPartnerThread = null;

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

        try {
            mProviderSocket = new ServerSocket(TorConstants.TOR_BUNDLE_INTERNAL_HIDDEN_SERVICES_PORT, 10);
        } catch (IOException e) {
            Log.i(LOG, "error  when creating Server Socket: " + e.getMessage(), e);
        }

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

        Log.i(LOG, "waitForIncomingConnection -> ENTER");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //TODO: must see when breaking...
        while (true) {
            //always listening for new connections. When a new on comes start a new message receiving thread
            try {
                mCurrentPartnerConnection = mProviderSocket.accept();

                if (mCurrentPartnerConnection != mPreviousPartnerConnection) {

                    mPreviousPartnerConnection = mCurrentPartnerConnection;

                    Log.i(LOG, "Connection received from " + mCurrentPartnerConnection.getInetAddress().getHostName());

                    new TorReceiverDelegator(mCurrentPartnerConnection, mIncomingConnectionListenerManager, mMessageReceivedListenerManager);

//                    DataInputStream dataInputStream = new DataInputStream(mCurrentPartnerConnection.getInputStream());
//
//                    OutputStream outputStream = mCurrentPartnerConnection.getOutputStream();
//
//                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//
//                    startMessageReceivingThread(dataInputStream);
//
//                    //TODO: ...reevaluate
//                    //maybe a wait of 1-2 seconds to receive the address
//                    Thread.sleep(2000);
//
//                    if (!mPartnerAddress.isEmpty()) {
//
//                        TorReceiverDelegator torReceiverDelegator = new TorReceiverDelegator(mCurrentPartnerConnection, dataInputStream, dataOutputStream, mPartnerAddress);
//
//                        mIsConnected = true;
//                        mIncomingConnectionListenerManager.triggerPartnerChatRequest(torReceiverDelegator);
//                    }
                }
            } catch (IOException ioException) {
                Log.i(LOG, "error: " + ioException.getMessage(), ioException);
            }
        }
    }

    public Thread getWaitingForConnectionThread() {
        return mWaitingForPartnerThread;
    }
}
