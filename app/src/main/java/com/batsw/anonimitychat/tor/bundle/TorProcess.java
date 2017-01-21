package com.batsw.anonimitychat.tor.bundle;

import android.util.Log;

import com.batsw.anonimitychat.tor.listener.TorBundleListenerManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by tudor on 12/13/2016.
 */

public class TorProcess implements Runnable {

    private static final String LOG = "TorProcess";

    private TorBundleListenerManager mTorBundleListenerManager;
    private String mCommand;
    private Process torProcess;
    private int mPID = 0;

    public TorProcess(String command, TorBundleListenerManager torBundleListenerManager) {
        mCommand = command;
        mTorBundleListenerManager = torBundleListenerManager;

        Log.i(LOG, "configured the TOR process thread");
    }

    @Override
    public void run() {
        Log.i(LOG, "starting the TOR process WITH command: " + " \n" + mCommand);

        try {
            torProcess = Runtime.getRuntime().exec(mCommand);
            Log.i(LOG, "TOR prccess launched");

            String torProcessString = torProcess.toString();

            int torPID = Integer.parseInt(torProcessString.substring(12, torProcessString.length() - 1));
            Log.i(LOG, "TOR process successfully started: " + torPID);
            mPID = torPID;

            Scanner torLogMessages = new Scanner(torProcess.getInputStream());

            Log.i(LOG, "TOR process scanner has something: " + torLogMessages.hasNextLine());

            while (torLogMessages.hasNextLine()) {
                String torData = torLogMessages.nextLine();

                //Waits for the Ready signal: Bootstrapped 100%: Done
                String formattedTorLog = torData.substring(29, torData.length());
                mTorBundleListenerManager.statusMessageReceived(formattedTorLog);

                Log.i(LOG, "TOR process LOG: " + torData);
            }

        } catch (IOException e) {

            Log.i(LOG, "TOR process is closing");
            if (torProcess != null)
                torProcess.destroy();
            Log.e(LOG, "ERROR when launching TOR Bundle" + e.getMessage(), e);
        }
    }

    public void stopTorProcess() {
        torProcess.destroy();
    }

    public Process getTorProcess() {
        return torProcess;
    }

    public int getPID() {
        return mPID;
    }
}
