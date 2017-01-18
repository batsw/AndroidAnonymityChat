package com.batsw.anonimitychat.tor.bundle;

import android.os.StrictMode;
import android.util.Log;

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

public class TorReceiver {

    private static final String LOG = "TorReceiver";

    private ServerSocket providerSocket;
    private Socket connection = null;
    private ObjectOutputStream out;
    private DataInputStream dataInputStream;
    private DataOutputStream outToPublisher;
    private OutputStream outputStream;

    public void run() {
        try {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            providerSocket = new ServerSocket(TorConstants.TOR_BUNDLE_INTERNAL_HIDDEN_SERVICES_PORT, 10);
            Log.i(LOG, "Waiting for connection");
            connection = providerSocket.accept();
            Log.i(LOG, "Connection received from " + connection.getInetAddress().getHostName());
            dataInputStream = new DataInputStream(connection.getInputStream());

            outputStream = connection.getOutputStream();

            outToPublisher = new DataOutputStream(outputStream);

            while (true) {
                String incomingMessageFromServer = "";
                try {
                    if (dataInputStream != null) {
                        incomingMessageFromServer = dataInputStream.readUTF();
                        Log.i(LOG, "Message Receved___" + incomingMessageFromServer);

                        // TODO delete when testing phase is DONE
                        //Sending back to sender the received message to test bidirectional communication
                        outToPublisher.writeUTF(incomingMessageFromServer);
                        outToPublisher.flush();
                        Log.i(LOG, "Message Repeated");
                    }
                } catch (IOException e) {
                    //TODO : I don't think the connection should be closed . Only when exiting the application
                    Log.e(LOG, "error: " + e.getMessage(), e);
                    connection.close();
                    break;
                }
            }

        } catch (IOException ioException) {
            Log.i(LOG, "error: " + ioException.getMessage(), ioException);
        } finally
//Epmty for now
        {
//            try {
//                providerSocket.close();
//            } catch (IOException ioException) {
//                Log.e(LOG, "error: " + ioException.getMessage(), ioException);
//            }
        }
    }
}
