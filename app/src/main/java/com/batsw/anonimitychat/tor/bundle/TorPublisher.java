package com.batsw.anonimitychat.tor.bundle;

import android.os.StrictMode;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by tudor on 12/16/2016.
 */

public class TorPublisher {

    private static final String LOG = "TorPublisher";

    public static String destinationAddress = "";

    private static Socket socket;
    private static PrintWriter out;

    //    public static final int LOCAL_PORT = 8080;
//    public static final int LOCAL_PORT = 44444;
    //    public static final int LOCAL_PORT = 11158;
    public static final int INBOUND_PORT = 80;
    public static final int SOCKS_PORT = 11158;

    Socket requestSocket;
    ObjectOutputStream outputStream;
    String message = "";

    DataOutputStream writeMessage;
    private OutputStream outToServer;

    public TorPublisher(String partnerHostName) {
        destinationAddress = partnerHostName;
    }

    public void run() {
        try {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }


//            final Random rndForTorCircuits = new Random();
//            String user = rndForTorCircuits.nextInt(100000) + "";
//            String pass = rndForTorCircuits.nextInt(100000) + "";
//            ProxyInfo proxyInfo = new ProxyInfo(ProxyInfo.ProxyType.SOCKS5, "127.0.0.1", SOCKS_PORT, user, pass);
//            socket = proxyInfo.getSocketFactory().createSocket("127.0.0.1", SOCKS_PORT);
//            socket.connect(InetSocketAddress.createUnresolved(destinationAddress, 80));
//            Log.i(LOG, "Connected to target address");
//            outToServer = socket.getOutputStream();

            // 1. creating a socket to connect to the server
//            Proxy torProxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", SOCKS_PORT));
//            requestSocket = new Socket(torProxy);
//            Log.i(LOG, "destinationAddress: " + destinationAddress);
//
//            requestSocket.connect(InetSocketAddress.createUnresolved(destinationAddress,
//                    44444));

            SocketAddress address = new InetSocketAddress("127.0.0.1", SOCKS_PORT);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
            requestSocket = new Socket(proxy);
            InetSocketAddress dest = new InetSocketAddress(destinationAddress, INBOUND_PORT);
            requestSocket.connect(dest);

//            requestSocket.connect(InetSocketAddress.createUnresolved(destinationAddress, 80));
            Log.i(LOG, "Connected to target address");

            outToServer = requestSocket.getOutputStream();

            writeMessage = new DataOutputStream(outToServer);

            do {
                message = "hello from Android Galaxy S2";
                String EOL = System.getProperty("line.separator");
                writeMessage.writeUTF((message + EOL));
                writeMessage.flush();
//                sendMessage(message);

                Log.i(LOG, "message sent to destination");

                message = "bye";
                writeMessage.writeUTF(message + EOL);
                writeMessage.flush();
//                sendMessage(message);
                Log.i(LOG, "message sent to destination");
            } while (!message.equals("bye"));

        } catch (UnknownHostException unknownHost) {
            Log.e(LOG, "You are trying to connect to an unknown host! " + unknownHost.getMessage(), unknownHost);
        } catch (IOException ioException) {
            Log.e(LOG, "error: " + ioException.getMessage(), ioException);
        }
//        } finally {
//            // 4: Closing connection
//            try {
//                out.close();
//                requestSocket.close();
//            } catch (IOException ioException) {
//                Log.e(LOG, "error: " + ioException.getMessage(), ioException);
//            }
//        }
    }

    private void sendMessage(String msg) {
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
        } catch (IOException ioException) {
            Log.e(LOG, "error when sending message: " + ioException.getMessage(), ioException);
        }
    }
}
