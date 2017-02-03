package com.batsw.anonimitychat.tor.bundle;

import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;

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

import socks.Socks5Proxy;
import socks.SocksSocket;

/**
 * Created by tudor on 12/16/2016.
 */

public class TorPublisher implements Serializable {

    private static final String LOG = "TorPublisher";

    public static String mDestinationAddress = "";

    private Socket mSocketConnection;
    private Thread mMessageReceiverThread = null;

    //TODO: remove when finished testing
    private String message = "";

    private OutputStream mOutputStream;
    private DataOutputStream mDataOutputStream;
    private DataInputStream mDataInputStream;

    private ChatActivity mChatActivity = null;

    public TorPublisher(String partnerHostName) {
        mDestinationAddress = partnerHostName;
    }

    public void run() {
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

            mMessageReceiverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    createMessageReceivingLoop();
                }
            });
            mMessageReceiverThread.start();

            //TODO: see if I need to keep resources available ... or simple reference would do ...

//            do {
//                message = "hello from Android";
//                mDataOutputStream.writeUTF((message + ChatModelConstants.MESSAGE_EOL));
//                mDataOutputStream.flush();
//
//                Log.i(LOG, "message sent to destination");
//
//                message = "bye";
//                mDataOutputStream.writeUTF(message + EOL);
//                mDataOutputStream.flush();
//
//                Log.i(LOG, "message sent to destination");
//            } while (!message.equals("bye"));

        } catch (UnknownHostException unknownHost) {
            Log.e(LOG, "You are trying to connect to an unknown host! " + unknownHost.getStackTrace().toString(), unknownHost);
            unknownHost.printStackTrace();
        } catch (IOException ioException) {
            Log.e(LOG, "error: " + ioException.getMessage(), ioException);

        } finally {
//            try {
//                if (mDataOutputStream != null)
//                    mDataOutputStream.close();
//
//                if (mSocketConnection != null)
//                    mSocketConnection.close();
//            } catch (IOException ioException) {
//                Log.e(LOG, "error: " + ioException.getMessage(), ioException);
//            }
        }
    }

    //TODO: use it later
    public void sendMessage(String message) {
        Log.i(LOG, "sendMessage -> ENTER");

        String preparedMessage = message + ChatModelConstants.MESSAGE_EOL;

        try {
            mDataOutputStream.writeUTF(preparedMessage);
            mDataOutputStream.flush();
        } catch (IOException ioException) {
            Log.e(LOG, "error when sending message: " + ioException.getMessage(), ioException);
        }

        Log.i(LOG, "sendMessage -> LEAVE");
    }

    /**
     *
     */
    public void closeCommunication() {
        Log.i(LOG, "closeCommunication -> ENTER");

//        if (mMessageReceiverThread.isAlive()) mMessageReceiverThread.stop();

        try {
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

                   final ChatMessage receivedChatMessage = new ChatMessage(incomingMessage, ChatMessageType.PARTNER, System.currentTimeMillis());

                    mChatActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatActivity.addPartnerMessageToMessageList(receivedChatMessage);
                        }
                    });

                    // notify that a message came
                    /// i need a signature of the message to print it on the right chat .....

//                    if (incomingMessage.equals("something...."))
//                        break;
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

    //notific adaptorul respectiv ca a mai venit un mesaj ..... hmmmmm
    //notificarea vine prin activity .... oare e bine????
    public void setChatActivity(ChatActivity chatActivity){
        mChatActivity = chatActivity;
    }
}
