package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.batsw.anonimitychat.R;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsNetworkActivity extends AppCompatActivity {
    private static final String LOG = SettingsNetworkActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_network_activity);

        Log.i(LOG, "onCreate -> ENTER");
    }
}
