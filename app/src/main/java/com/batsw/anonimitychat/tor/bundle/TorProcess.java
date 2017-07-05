package com.batsw.anonimitychat.tor.bundle;

import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.tor.listener.TorBundleListenerManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Created by tudor on 12/13/2016.
 */

public class TorProcess implements Runnable {

    private static final String LOG = "TorProcess";

    private TorBundleListenerManager mTorBundleListenerManager;
    private String mCommand;
    private Process mTorProcess;
    private int mPID = 0;

    private Scanner torLogMessages = null;

    public TorProcess(String command, TorBundleListenerManager torBundleListenerManager) {
        mCommand = command;
        mTorBundleListenerManager = torBundleListenerManager;

        Log.i(LOG, "configured the TOR process thread");
    }

    @Override
    public void run() {
        Log.i(LOG, "starting the TOR process WITH command: " + " \n" + mCommand);

        try {
            mTorProcess = Runtime.getRuntime().exec(mCommand);
            Log.i(LOG, "TOR prccess launched");

            String torProcessString = mTorProcess.toString();

            int torPID = Integer.parseInt(torProcessString.substring(12, torProcessString.length() - 1));
            Log.i(LOG, "TOR process successfully started: " + torPID);
            mPID = torPID;

            AppController.getInstanceParameterized(null).updateBundlePid(mPID);

            torLogMessages = new Scanner(mTorProcess.getInputStream());

            while (torLogMessages.hasNextLine()) {
                String torData = torLogMessages.nextLine();

                //Waits for the Ready signal: Bootstrapped 100%: Done
                String formattedTorLog = torData.substring(29, torData.length());
                mTorBundleListenerManager.statusMessageReceived(formattedTorLog);

                Log.i(LOG, "TOR process LOG: " + torData);
            }

        } catch (IOException e) {
            Log.i(LOG, "TOR process is closing");

            if (mTorProcess != null) {
                mTorProcess.destroy();
                mPID = 0;
            }

            if (torLogMessages != null) {
                torLogMessages.close();
            }

            Log.e(LOG, "ERROR when launching TOR Bundle" + e.getMessage(), e);
        }
    }

    public void stopTorProcess() {
        Log.i(LOG, "stopTorProcess -> ENTER");

        torLogMessages = null;

        mTorProcess.destroy();
        mPID = 0;

        AppController.getInstanceParameterized(null).updateBundlePid(mPID);

        Log.i(LOG, "stopTorProcess -> LEAVE");
    }

    public Process getmTorProcess() {
        return mTorProcess;
    }

    public int getPID() {
        return mPID;
    }
}
