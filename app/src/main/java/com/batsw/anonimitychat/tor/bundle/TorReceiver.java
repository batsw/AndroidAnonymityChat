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


//            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
//            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    while (true) {
//
////            while (bufferedReader.ready()) {
//                        String message = null;
//                        try {
//                            message = bufferedReader.readLine();
//                            if (message != null)
//                                Log.i(LOG, "BR Message Receved___" + message);
//                        } catch (IOException e) {
//                            Log.e(LOG, "error: " + e.getMessage(), e);
//                        }
//                    }
//
//                }
//            }).start();
//
//            final BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        try {
//
//                            int message = bufferedInputStream.read();
//                            if (message > 0)
//                                Log.i(LOG, "BIS Message Receved___" + message);
//                        } catch (IOException e) {
//                            Log.e(LOG, "error: " + e.getMessage(), e);
//                        }
//                    }
//
//                }
//            }).start();

            //SCANNER
//            final InputStream inputStream = connection.getInputStream();
//
//
//            final Scanner scanner = new Scanner(connection.getInputStream(), "UTF8");
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        while (scanner.hasNext()) {
//                            Log.i(LOG, "Scanner Message Receved_11__" + scanner.nextLine());
//                            scanner.reset();
//                        }
//                    }
//                }
//            }).start();


//DATAINPUTSTREAM

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

            //Obj input stream
            //must inject a breaking logic
//            final ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());
//
//            while (true) {
//                while (objectInputStream.available() > 0) {
//                    String message = objectInputStream.readUTF();
//                    Log.i(LOG, "Message Receved___" + message);
//                }

//            //SCANNER
//            while (true) {
//                while (scanner.hasNext()) {
//                    Log.i(LOG, "Message Receved___" + scanner.next());
//                    scanner.reset();
//                }
//            }

//
//            } catch (IOException ioException) {
//                Log.e(LOG, "error: " + ioException.getMessage(), ioException);
//            }

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
