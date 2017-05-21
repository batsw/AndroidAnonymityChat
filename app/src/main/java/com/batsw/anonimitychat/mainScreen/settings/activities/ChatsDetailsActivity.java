package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.batsw.anonimitychat.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by woman on 4/30/2017.
 */


public class ChatsDetailsActivity extends AppCompatActivity {
    private static final String LOG = ChatsDetailsActivity.class.getSimpleName();

    private Spinner historyCleanupSpinner;
    private LinearLayout historyCleanupLayout;
    private TextView trashIcon, spinnerArrow, mBack;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_tab_chat_details);
        list.add(0, "never");

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        mBack = (TextView) findViewById(R.id.chat_details_back);
        mBack.setTypeface(fontAwesome);

        historyCleanupLayout = (LinearLayout) findViewById(R.id.history_cleanup_layout);
        trashIcon = (TextView) findViewById(R.id.chat_details_trash_icon);
        trashIcon.setTypeface(fontAwesome);
        //spinnerArrow = (TextView) findViewById(R.id.spinner_arrow);
        //  spinnerArrow.setTypeface(fontAwesome);

        //historyCleanupLayout.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //    public void onClick(View historyCleanupView) {
        //         historyCleanupSpinner.performClick();
        //     }
        //  });
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, ChatsDetailsActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
