package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.chat.ChatActivity;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsAboutActivity extends AppCompatActivity {
    private static final String LOG = SettingsAboutActivity.class.getSimpleName();

    private TextView mBackIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_about_activity);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        mBackIcon = (TextView) findViewById(R.id.about_back_icon);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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

        Intent retVal = new Intent(context, SettingsAboutActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
