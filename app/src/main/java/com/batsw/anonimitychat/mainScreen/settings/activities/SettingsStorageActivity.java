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

public class SettingsStorageActivity extends AppCompatActivity {
    private static final String LOG = SettingsStorageActivity.class.getSimpleName();

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, SettingsStorageActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
