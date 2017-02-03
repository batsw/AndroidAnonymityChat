package com.batsw.anonimitychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcessManager;
import com.batsw.anonimitychat.tor.bundle.TorPublisher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MainActivity extends AppCompatActivity {

    protected static final String MAIN_ACTIVITY_TAG = MainActivity.class.getSimpleName();

    Button torStopButton, connectToTorClient, mOpenPortButton, mStartTorButton;

    TextView mPatnerHostname, mTorStatusTextView;

    private TorProcessManager mTorProcessManager;

    private static ConcurrentHashMap<Integer, TorPublisher> mContactedPartnerHostnames = null;
    private static Integer mPartnersIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        torStopButton = (Button) findViewById(R.id.btn_tor_stop);
        mStartTorButton = (Button) findViewById(R.id.btn_tor_start);
        connectToTorClient = (Button) findViewById(R.id.btn_tor_connect);
        mOpenPortButton = (Button) findViewById(R.id.open_port);

        mPatnerHostname = (TextView) findViewById(R.id.partner_hostname);

        mTorStatusTextView = (TextView) findViewById(R.id.tor_status);
        mTorStatusTextView.setTextColor(getResources().getColor(R.color.colorStoppedTorStatus));
        mTorStatusTextView.setText(TorConstants.TOR_BUNDLE_STOPPED);

        mTorProcessManager = new TorProcessManager(this, mTorStatusTextView);

        ///////////////////////////////////////////////////////////
        /////////Prerequisites////////////////////////////////////
        ///////////////////////////////////////////////////////////
        mContactedPartnerHostnames = new ConcurrentHashMap<>();


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

                                                      final TorPublisher torPublisher = new TorPublisher(partnerName);

                                                      if (mTorProcessManager.isTorBundleStarted()) {
                                                          Thread thread = new Thread(new Runnable() {

                                                              @Override
                                                              public void run() {

                                                                  torPublisher.run();
                                                              }

                                                          });

                                                          thread.start();
                                                          mPartnersIndex++;
                                                          mContactedPartnerHostnames.put(mPartnersIndex, torPublisher);

//                                                          getIntent().putExtra(partnerName, torPublisher);

                                                          startActivity(ChatActivity.makeIntent(MainActivity.this));

                                                      } else {
                                                          //TODO: show a message on screen TOR did not started yet
                                                          Log.i(MAIN_ACTIVITY_TAG, "TOR Bundle has not started yet");
                                                      }

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

        mStartTorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(MAIN_ACTIVITY_TAG, "Starting TOR from GUI command:");

                if (!mTorProcessManager.isTorBundleStarted() && !mTorStatusTextView.getText().equals(TorConstants.TOR_BUNDLE_IS_STARTING)) {
                    mTorProcessManager.startTorBundle();
                } else {
                    //TODO: show a message on screen TOR Bundle is already stopped
                    Log.i(MAIN_ACTIVITY_TAG, "TOR Bundle is already started OR starting");
                }
            }
        });

        torStopButton.setOnClickListener(new View.OnClickListener()

                                         {
                                             @Override
                                             public void onClick(View v) {
                                                 Log.i(MAIN_ACTIVITY_TAG, "Stopping TOR from GUI command:");

                                                 mTorProcessManager.stopTorBundle();
//                                                     //TODO: show a message on screen TOR Bundle is already stopped
                                                 Log.i(MAIN_ACTIVITY_TAG, "TOR Bundle is stopped");
                                             }
                                         }

        );

    }

    public static ConcurrentHashMap<Integer, TorPublisher> getContactedPartnerHostnames(){
        return mContactedPartnerHostnames;
    }

}
