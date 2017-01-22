package com.batsw.anonimitychat.tor.bundle;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.batsw.anonimitychat.MainActivity;
import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.tor.listener.TorBundleListenerManager;
import com.batsw.anonimitychat.tor.listener.TorBundleStatusListenerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by tudor on 1/21/2017.
 */

public class TorProcessManager {

    protected static final String TOR_PROCESS_MANAGER_TAG = TorProcessManager.class.getSimpleName();

    private TorProcess mTorProcessCommander = null;
    private TorBundleListenerManager mTorBundleListenerManager;
    private TorBundleStatusListenerImpl mTorBundleStatusListener;

    private int mProcessId = 0;
    private String mTorResourceLocation;

    private AppCompatActivity mMainActivity;
    private AssetManager mAssetManager;
    private TextView mTorStatusTextView;

    private String mTorHostnamee;
    private boolean mTorBundleStarted = false;


    public TorProcessManager(AppCompatActivity mainActivity, TextView torStatusTextView) {

        mMainActivity = mainActivity;
        mAssetManager = mMainActivity.getAssets();

        mTorBundleListenerManager = new TorBundleListenerManager();

        mTorStatusTextView = torStatusTextView;
    }

    public void startTorBundle() {

        Log.i(TOR_PROCESS_MANAGER_TAG, "ENTER -> startTorBundle");

        mTorStatusTextView.setTextColor(mMainActivity.getResources().getColor(R.color.colorLightBlue));
        mTorStatusTextView.setText(TorConstants.TOR_BUNDLE_IS_STARTING);

        if (mProcessId == 0 && mTorProcessCommander == null) {
            String torStartCommand = createTorProcessStartingPrerequisites();

            if (!torStartCommand.isEmpty()) {
                mTorBundleStatusListener = new TorBundleStatusListenerImpl();
                mTorBundleListenerManager.addTorBundleListener(mTorBundleStatusListener);

                mTorProcessCommander = new TorProcess(torStartCommand, mTorBundleListenerManager);
                new Thread(mTorProcessCommander).start();

                updateTorBundleStatus();
            }

        } else {
            //TODO: Show a message that TOR Bundle is already running
            //Do nothing for now

            Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Bundle already started");
        }

        Log.i(TOR_PROCESS_MANAGER_TAG, "LEAVE -> startTorBundle");
    }

    private String createTorProcessStartingPrerequisites() {

        CopyTorResource copyTorResource = new CopyTorResource();
        mTorResourceLocation = copyTorResource.provideTorResource(mMainActivity.getFilesDir().toString(), mAssetManager, TOR_PROCESS_MANAGER_TAG);
        Log.i(TOR_PROCESS_MANAGER_TAG, "tor resources successfully copied to ::: torResourceLocation = " + mTorResourceLocation);

        Log.i(TOR_PROCESS_MANAGER_TAG, "starting tor bundle ::: ");

        File torFile = new File(mTorResourceLocation + "/tor");
        File torrcFile = new File(mTorResourceLocation + "/torrc");

        File hiddenServicesDir = new File(mTorResourceLocation + "/hidden_service");
        hiddenServicesDir.mkdir();

        String chmodCmd = "chmod " + 777 + ' ' + torFile.getAbsolutePath();

        String cmd = torFile.getAbsolutePath() + " DataDirectory " + mTorResourceLocation + " -f " + torrcFile.getAbsolutePath();

        Log.i(TOR_PROCESS_MANAGER_TAG, ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        try {

            Runtime.getRuntime().exec(chmodCmd);

            File fileTest = new File(torFile.getAbsolutePath());
            Log.i(TOR_PROCESS_MANAGER_TAG, "testing if tor is executable: " + fileTest.canExecute());

        } catch (IOException e) {
            Log.e(TOR_PROCESS_MANAGER_TAG, "error when changing TOR Bundle permissions: " + e);
        }

        return cmd;
    }

    private void updateTorBundleStatus() {
        Log.i(TOR_PROCESS_MANAGER_TAG, "ENTER -> updateTorBundleStatus");

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean torReady = false;
                while (mTorBundleStatusListener != null && !torReady) {
                    if (mTorBundleStatusListener.getStatusMessage().equals(TorConstants.TOR_READY_STATUS_MESSAGE)) {

                        mMainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTorStatusTextView.setTextColor(mMainActivity.getResources().getColor(R.color.colorStartedTorStatus));
                                mTorStatusTextView.setText(TorConstants.TOR_BUNDLE_STARTED);
                            }
                        });

                        mProcessId = mTorProcessCommander.getPID();

                        if (!mTorResourceLocation.isEmpty())
                            mTorHostnamee = listFilesForFolder(mTorResourceLocation);
                        Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Bundle provided hostname is: " + mTorHostnamee);

                        mTorBundleStarted = true;

                        //TODO: UGLYYYY must change
                        //Starting TorReceiver  process
                        Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Receiver is Launching !!!");
                        new Thread(new Runnable() {
                            public void run() {
                                TorReceiver torReceiver = new TorReceiver();
                                torReceiver.run();
                            }
                        }).start();
                        Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Receiver is waiting for messages !!!");

                        torReady = true;

                        Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Bundle is Ready: " + torReady);
                    }
                }
            }
        }).start();

        Log.i(TOR_PROCESS_MANAGER_TAG, "LEAVE -> updateTorBundleStatus");
    }

    public void stopTorBundle() {

        Log.i(TOR_PROCESS_MANAGER_TAG, "ENTER -> stopTorBundle");

        if (mProcessId != 0 && mTorProcessCommander != null && mProcessId == mTorProcessCommander.getPID()) {

            mTorProcessCommander.stopTorProcess();

            mTorBundleListenerManager.removeTorBundleListener(mTorBundleStatusListener);

            cleanTorProcessParameters();

            mTorStatusTextView.setTextColor(mMainActivity.getResources().getColor(R.color.colorStoppedTorStatus));
            mTorStatusTextView.setText(TorConstants.TOR_BUNDLE_STOPPED);

        } else {
            //TODO: Show a message that TOR Bundle is already STOPPED
            //Do nothing for now

            Log.i(TOR_PROCESS_MANAGER_TAG, "TOR Bundle already Stopped");
        }

        Log.i(TOR_PROCESS_MANAGER_TAG, "LEAVE -> stopTorBundle");
    }

    private void cleanTorProcessParameters() {
        Log.i(TOR_PROCESS_MANAGER_TAG, "ENTER -> cleanTorProcessParameters");

        mTorProcessCommander = null;
        mTorBundleStatusListener = null;

        mProcessId = 0;
        mTorResourceLocation = "";
        mTorHostnamee = "";

        mTorBundleStarted = false;

        Log.i(TOR_PROCESS_MANAGER_TAG, "LEAVE -> cleanTorProcessParameters");
    }

    //TODO: when testing is done, to remove the resource files listing
    private String listFilesForFolder(String dirPath) {

        String retVal = "";

        final File folder = new File(dirPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();

                if (fileName.equals(TorConstants.TOR_HIDDEN_SERVICE_NAME)) {
                    retVal = readHiddenServicesFile(listOfFiles[i]);
                }

                Log.i(TOR_PROCESS_MANAGER_TAG, "File " + fileName);
            } else if (listOfFiles[i].isDirectory()) {
                Log.i(TOR_PROCESS_MANAGER_TAG, "Directory " + listOfFiles[i].getName());
            }
        }
        return retVal;
    }

    private String readHiddenServicesFile(File hiddenServicesDir) {

        Log.i(TOR_PROCESS_MANAGER_TAG, "Hidden Services directory name is" + hiddenServicesDir);
        String fileContent = "";

        try {
            FileReader fileReader = new FileReader(hiddenServicesDir);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();

            fileContent = stringBuffer.toString();
            Log.i(TOR_PROCESS_MANAGER_TAG, "Hidden Services directory contents::: " + fileContent);
            return fileContent;
        } catch (IOException e) {
            Log.e(TOR_PROCESS_MANAGER_TAG, "Error while reading HIDDEN_SERVICE_NAME:" + e.getMessage(), e);
            return null;
        }
    }

    public String getTorHostnamee() {
        return mTorHostnamee;
    }

    public boolean isTorBundleStarted() {
        return mTorBundleStarted;
    }
}
