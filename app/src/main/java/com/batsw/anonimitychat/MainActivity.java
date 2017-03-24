package com.batsw.anonimitychat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcessManager;
import com.batsw.anonimitychat.tor.connections.TorPublisher;

import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity {

    protected static final String MAIN_ACTIVITY_TAG = MainActivity.class.getSimpleName();

    Button torStopButton, connectToTorClient, mOpenPortButton, mStartTorButton;

    TextView mPartnerHostname, mTorStatusTextView, mMyTorAddressLabel;

    private TorProcessManager mTorProcessManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(MAIN_ACTIVITY_TAG, "onCreate -> ENTER");

        setContentView(R.layout.activity_main);

        torStopButton = (Button) findViewById(R.id.btn_tor_stop);
        mStartTorButton = (Button) findViewById(R.id.btn_tor_start);
        connectToTorClient = (Button) findViewById(R.id.btn_tor_connect);
        mOpenPortButton = (Button) findViewById(R.id.open_port);

        mMyTorAddressLabel = (TextView) findViewById(R.id.tor_address);
        mMyTorAddressLabel.setText(ChatModelConstants.MY_TOR_ADDRESS_NA_YET);
        mMyTorAddressLabel.setTextColor(getResources().getColor(R.color.colorStoppedTorStatus));

        mPartnerHostname = (TextView) findViewById(R.id.partner_hostname);

        mTorStatusTextView = (TextView) findViewById(R.id.tor_status);
        mTorStatusTextView.setTextColor(getResources().getColor(R.color.colorStoppedTorStatus));
        mTorStatusTextView.setText(TorConstants.TOR_BUNDLE_STOPPED);

        mTorProcessManager = new TorProcessManager(this, mTorStatusTextView);


        ///////////////////////////////////////////////////////////
        /////////Buttons//////////////////////////////////////////
        ///////////////////////////////////////////////////////////

        connectToTorClient.setOnClickListener(new View.OnClickListener()

                                              {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Log.i(MAIN_ACTIVITY_TAG, "Accessing TOR Publisher");

                                                      final String partnerHostName = mPartnerHostname.getText().toString();
                                                      Log.i(MAIN_ACTIVITY_TAG, "entered partner host name: " + partnerHostName);

                                                      if (mTorProcessManager.isTorBundleStarted()) {

                                                          ChatController.getInstance().startChatActivity(getApplicationContext(), partnerHostName);

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

                CharSequence torStatusText = mTorStatusTextView.getText().toString().trim();

                if (!mTorProcessManager.isTorBundleStarted() && torStatusText.equals(TorConstants.TOR_BUNDLE_STOPPED)) {
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

        mTorStatusTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(MAIN_ACTIVITY_TAG, "mTorStatusTextView.addTextChangedListener -> ENTER");

                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String textViewText = sb.toString();

                if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    ChatController.getInstance();
                    ChatController.getInstance().setMyAddress(mTorProcessManager.getTorHostnamee());
                    ChatController.getInstance().setCurrentActivityContext(getApplicationContext());
                    ChatController.getInstance().initializeChatConnectionManagement();

                    mMyTorAddressLabel.setText(ChatController.getInstance().getMyAddress());
                    mMyTorAddressLabel.setTextColor(getResources().getColor(R.color.colorStartedTorStatus));

                }

                //Meaning that TOR is either starting or Stopped
                if (!textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    // ChatController managed resources --- what is to be set to default

                    mMyTorAddressLabel.setText(ChatModelConstants.MY_TOR_ADDRESS_NA_YET);
                    mMyTorAddressLabel.setTextColor(getResources().getColor(R.color.colorStoppedTorStatus));

                    //TODO: Must clear out the resources held by ChatController .....
                    // when the tor bundle is stopped and ther started and stopped again for each stop the resources must be cleaned
                }

                Log.i(MAIN_ACTIVITY_TAG, "mTorStatusTextView.addTextChangedListener -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (mTorStatusTextView.getText().equals(TorConstants.TOR_BUNDLE_STARTED)) {

            ChatController.getInstance().setCurrentActivityContext(getApplicationContext());
        }

        Log.i(MAIN_ACTIVITY_TAG, "onCreate -> LEAVE");
    }

    @Override
    protected void onStart() {
        Log.i(MAIN_ACTIVITY_TAG, "onStart -> ENTER");
        super.onStart();
        Log.i(MAIN_ACTIVITY_TAG, "do nothing");
        Log.i(MAIN_ACTIVITY_TAG, "onStart -> LEAVE");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
