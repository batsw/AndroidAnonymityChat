package com.batsw.anonimitychat.appManagement;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.batsw.anonimitychat.persistence.DatabaseHelper;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcessManager;

/**
 * Created by tudor on 5/13/2017.
 */

public class AppController {
    private static final String LOG = AppController.class.getSimpleName();


    private static AppController mInstance;

    private AppCompatActivity mMainScreenActivity = null;

    private static boolean isIncomingChatConnection = false;

    private DatabaseHelper mDatabaseHelper;
    private TorProcessManager mTorProcessManager;
    private TextView mTorStatusCarrier;

    private String mMyTorAddress = "";

    private AppController(AppCompatActivity mainActivity) {
        mMainScreenActivity = mainActivity;
//        binding the textView to the MainScreen
        mTorStatusCarrier = new TextView(mMainScreenActivity.getApplicationContext());
        
        init();
    }

    /**
     * To be used with non-null Context the first time this object is created in the most early app time
     * In rest to be used with null Context parameter
     */
    public static synchronized AppController getInstanceParameterized(AppCompatActivity mainActivity) {
        if (mInstance == null) {
            mInstance = new AppController(mainActivity);
        }
        return mInstance;
    }

    /**
     * preparing the concurrent resources
     */
    private void init() {
        Log.i(LOG, "init -> ENTER");

        initBackend();

        Log.i(LOG, "init -> LEAVE");
    }

    /**
     * binding the backend functionality to MainScreen context
     */
    private void initBackend() {
        Log.i(LOG, "initBackend -> ENTER");

//        Database init
        mDatabaseHelper = new DatabaseHelper(mMainScreenActivity.getApplicationContext(), PersistenceConstants.DATABASE_ANONYMITY_CHAT,
                null, PersistenceConstants.DATABASE_VERSION);
//mDatabaseHelper.onOpen();

//Network connection init
        mTorProcessManager = new TorProcessManager(mMainScreenActivity, mTorStatusCarrier);

        Log.i(LOG, "initBackend -> LEAVE");
    }

    private boolean firstRun() {
        return false;
    }


//    Network connection commands interfaced with TorProcessManager HERE
    // start , stop connection ... here


    // DB operations interfaced in this section

    // create my profile first run
    // update my profile (only name, nickname and email)

//    .... etc


    //    public void updateContext(Context context) {
//        Log.i(LOG, "updateContext -> ENTER context=" + context);
//
//
//        Log.i(LOG, "updateContext -> LEAVE");
//    }


    // see if it works to remotely update contents of network connection status textViews
    public void updateWithNetworkConnectionStatus(final TextView networkConnStatusLabel) {
        Log.i(LOG, "getNetworkConnectionStatus -> ENTER networkConnStatusLabel=" + networkConnStatusLabel);

        mTorStatusCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                networkConnStatusLabel.setText(mTorStatusCarrier.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Log.i(LOG, "getNetworkConnectionStatus -> LEAVE");
    }

    public String getNetworkConnectionStatus() {
        Log.i(LOG, "getNetworkConnectionStatus -> ENTER");

        String retVal = mTorStatusCarrier.getText().toString();

        Log.i(LOG, "getNetworkConnectionStatus -> LEAVE retVal=" + retVal);
        return retVal;
    }

}
