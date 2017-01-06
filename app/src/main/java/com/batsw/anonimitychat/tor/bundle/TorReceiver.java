package com.batsw.anonimitychat.tor.bundle;

import android.os.StrictMode;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tudor on 12/16/2016.
 */

public class TorReceiver {

    private static final String LOG = "TorReceiver";

    ServerSocket providerSocket;
    Socket connection = null;
    ObjectOutputStream out;
    DataInputStream dataInputStream;
    String message;

    public static final int LOCAL_PORT = 8080;

    public void run() {
        try {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            providerSocket = new ServerSocket(LOCAL_PORT, 10);
            Log.i(LOG, "Waiting for connection");
            connection = providerSocket.accept();
            Log.i(LOG, "Connection received from " + connection.getInetAddress().getHostName());
            dataInputStream = new DataInputStream(connection.getInputStream());

            while (true) {
                String incomingMessageFromServer = "";
                try {
                    if (dataInputStream != null) {
                        incomingMessageFromServer = dataInputStream.readUTF();
                        Log.i(LOG, "Message Receved___" + incomingMessageFromServer);
                    }
                } catch (IOException e) {
                    //TODO : I don't think the connetion should be closed ... EVER ... for the future
                    Log.e(LOG, "error: " + e.getMessage(), e);
                    connection.close();
                    break;
                }
            }

        } catch (
                IOException ioException
                )

        {
            Log.i(LOG, "error: " + ioException.getMessage(), ioException);
        } finally

        {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                Log.e(LOG, "error: " + ioException.getMessage(), ioException);
            }
        }
    }
}
