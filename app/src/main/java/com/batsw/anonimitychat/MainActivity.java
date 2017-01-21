package com.batsw.anonimitychat;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.batsw.anonimitychat.tor.bundle.CopyTorResource;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcess;
import com.batsw.anonimitychat.tor.bundle.TorPublisher;
import com.batsw.anonimitychat.tor.bundle.TorReceiver;
import com.batsw.anonimitychat.tor.listener.TorBundleListenerManager;
import com.batsw.anonimitychat.tor.listener.TorBundleStatusListenerImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    protected static final String MAIN_ACTIVITY_TAG = MainActivity.class.getSimpleName();

    Button torStopButton, connectToTorClient, mOpenPortButton;

    TextView mPatnerHostname, mPortToOpen, mTorStatusTextView;

    private Process mTorProcess;
    private int mTorPID = 0;
    private String mTorHostnamee = "";

    private TorBundleListenerManager mTorBundleListenerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        torStopButton = (Button) findViewById(R.id.btn_tor_stop);
        connectToTorClient = (Button) findViewById(R.id.btn_tor_connect);
        mOpenPortButton = (Button) findViewById(R.id.open_port);

        mPatnerHostname = (TextView) findViewById(R.id.partner_hostname);

        mTorStatusTextView = (TextView) findViewById(R.id.tor_status);
        mTorStatusTextView.setTextColor(getResources().getColor(R.color.colorStoppedTorStatus));

        ////////////////////////////////////////
        /////////TODO: Tudor refactoring////////
        /////////Prerequisites//////////////////
        ////////////////////////////////////////

        mTorBundleListenerManager = new TorBundleListenerManager();
        TorBundleStatusListenerImpl torBundleStatusListener = new TorBundleStatusListenerImpl();
        mTorBundleListenerManager.addTorBundleListener(torBundleStatusListener);

        //////////////////////////////

        AssetManager assetManager = this.getAssets();

        CopyTorResource copyTorResource = new CopyTorResource();
        String torResourceLocation = copyTorResource.provideTorResource(getFilesDir().toString(), assetManager, MAIN_ACTIVITY_TAG);
        Log.i(MAIN_ACTIVITY_TAG, "tor resources successfully copied to ::: torResourceLocation = " + torResourceLocation);

        Log.i(MAIN_ACTIVITY_TAG, "starting tor bundle ::: ");

        File torFile = new File(torResourceLocation + "/tor");
        File torrcFile = new File(torResourceLocation + "/torrc");

        File hiddenServicesDir = new File(torResourceLocation + "/hidden_service");
        hiddenServicesDir.mkdir();

        String chmodCmd = "chmod " + 777 + ' ' + torFile.getAbsolutePath();

        String cmd = torFile.getAbsolutePath() + " DataDirectory " + torResourceLocation + " -f " + torrcFile.getAbsolutePath();

        Log.i(MAIN_ACTIVITY_TAG, ":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        TorProcess torProcess = null;
        try {

            Runtime.getRuntime().exec(chmodCmd);

            File fileTest = new File(torFile.getAbsolutePath());
            Log.i(MAIN_ACTIVITY_TAG, "testing if tor is executable: " + fileTest.canExecute());

            torProcess = new TorProcess(cmd, mTorBundleListenerManager);
            new Thread(torProcess).start();

        } catch (IOException e) {
            Log.e(MAIN_ACTIVITY_TAG, "error: " + e);
        }

        ////////////////////////////////////////////////////////////
        /////////TODO: Tudor move it in backend////////
        /////////Halting the main thread to start the Bundle////////
        ////////////////////////////////////////////////////////////
        boolean torReady = false;
        while (!torReady) {
            if (torBundleStatusListener.getStatusMessage().equals(TorConstants.TOR_READY_STATUS_MESSAGE)) {
                torReady = true;
            }
        }

        Log.i(MAIN_ACTIVITY_TAG, "TOR Bundle is Ready: " + torReady);

        if (torProcess != null) {
            mTorProcess = torProcess.getTorProcess();
            mTorPID = torProcess.getPID();
        }
        ////////////////////////////////////////////////////////////

        listFilesForFolder(torResourceLocation);
        Log.i(MAIN_ACTIVITY_TAG, "TOR Bundle provided hostname is: " + mTorHostnamee);


        //Starting the TorReceiver and waiting for incoming connections and messages

        Log.i(MAIN_ACTIVITY_TAG, "TOR Receiver is Launching !!!");
        new Thread(new Runnable() {
            public void run() {
                TorReceiver torReceiver = new TorReceiver();
                torReceiver.run();
            }
        }).start();
        Log.i(MAIN_ACTIVITY_TAG, "TOR Receiver is waiting for messages !!!");

        ///////////////////////////////////////////////////////////
        /////////Buttons//////////////////////////////////////////
        ///////////////////////////////////////////////////////////

        connectToTorClient.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Log.i(MAIN_ACTIVITY_TAG, "Accessing TOR Publisher");

                                                      final String partnerName = mPatnerHostname.getText().toString();
                                                      Log.i(MAIN_ACTIVITY_TAG, "entered partner host name: " + partnerName);

                                                      Thread thread = new Thread(new Runnable() {

                                                          @Override
                                                          public void run() {
                                                              TorPublisher torPublisher = new TorPublisher(partnerName);
                                                              torPublisher.run();
                                                          }

                                                      });

                                                      thread.start();

                                                  }
                                              }

        );

        mOpenPortButton.setOnClickListener(new View.OnClickListener()

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   Log.i(MAIN_ACTIVITY_TAG, "It does nothing now");

                                               }
                                           }

        );

        torStopButton.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 Log.i(MAIN_ACTIVITY_TAG, "Stopping TOR from GUI command:");
                                                 mTorProcess.destroy();
                                                 Log.i(MAIN_ACTIVITY_TAG, "TOR bundle successfully stopped!_" + mTorProcess.toString());
                                             }
                                         }

        );

    }

    private void listFilesForFolder(String dirPath) {

        final File folder = new File(dirPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fileName = listOfFiles[i].getName();

                if (fileName.equals(TorConstants.TOR_HIDDEN_SERVICE_NAME)) {
                    mTorHostnamee = hiddenServiceReader(listOfFiles[i]);
                }

                Log.i(MAIN_ACTIVITY_TAG, "File " + fileName);
            } else if (listOfFiles[i].isDirectory()) {
                Log.i(MAIN_ACTIVITY_TAG, "Directory " + listOfFiles[i].getName());
            }
        }
    }

    private String hiddenServiceReader(File hiddenServicesDir) {

        Log.i(MAIN_ACTIVITY_TAG, "Hidden Services directory name is" + hiddenServicesDir);
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
            Log.i(MAIN_ACTIVITY_TAG, "Hidden Services directory contents::: " + fileContent);
            return fileContent;
        } catch (IOException e) {
            Log.e(MAIN_ACTIVITY_TAG, "Error while reading HIDDEN_SERVICE_NAME:" + e.getMessage(), e);
            return null;
        }
    }
}
