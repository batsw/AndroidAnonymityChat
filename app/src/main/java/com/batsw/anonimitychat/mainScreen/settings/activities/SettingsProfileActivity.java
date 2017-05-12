package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.batsw.anonimitychat.R;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsProfileActivity extends AppCompatActivity {
    private static final String LOG = SettingsProfileActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_profile_activity);

        Log.i(LOG, "onCreate -> ENTER");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, SettingsProfileActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
