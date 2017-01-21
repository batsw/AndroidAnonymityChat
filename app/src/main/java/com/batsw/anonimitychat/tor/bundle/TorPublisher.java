package com.batsw.anonimitychat.tor.bundle;

import android.os.StrictMode;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

public class TorPublisher {

    private static final String LOG = "TorPublisher";

    public static String mDestinationAddress = "";

    private Socket mSocketConnection;

    //TODO: remove when finished testing
    private String message = "";

    private OutputStream mOutputStream;
    DataOutputStream mDataOutputStream;

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

            do {
                message = "hello from Android";
                String EOL = System.getProperty("line.separator");
                mDataOutputStream.writeUTF((message + EOL));
                mDataOutputStream.flush();

                Log.i(LOG, "message sent to destination");

                message = "bye";
                mDataOutputStream.writeUTF(message + EOL);
                mDataOutputStream.flush();

                Log.i(LOG, "message sent to destination");
            } while (!message.equals("bye"));

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
//    private void sendMessage(String msg) {
//        try {
//            outputStream.writeObject(msg);
//            outputStream.flush();
//        } catch (IOException ioException) {
//            Log.e(LOG, "error when sending message: " + ioException.getMessage(), ioException);
//        }
//    }

}
