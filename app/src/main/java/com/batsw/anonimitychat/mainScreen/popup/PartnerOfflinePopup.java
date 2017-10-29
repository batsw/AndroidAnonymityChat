package com.batsw.anonimitychat.mainScreen.popup;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.chat.management.activity.IChatActivityManager;

import java.util.List;

/**
 * Created by tudor on 5/13/2017.
 */

public class PartnerOfflinePopup extends Dialog implements View.OnClickListener {

    private static final String LOG = PartnerOfflinePopup.class.getSimpleName();

    private Context mContext;
    private IChatActivityManager mChatActivityManager;

    private Button mRetryButton;

    public PartnerOfflinePopup(@NonNull Context context, IChatActivityManager chatActivityManager) {
        super(context);
        mContext = context;
        mChatActivityManager = chatActivityManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.partner_offline_popup);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(getContext());

        mRetryButton = (Button) findViewById(R.id.pop_retry_button);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mRetryButton.onClick -> ENTER");

                mChatActivityManager.onCreate();

                Log.i(LOG, "mRetryButton.onClick -> LEAVE");
            }
        });

        Log.i(LOG, "onCreate -> LEAVE");
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG, "onClick -> ENTER");
        Log.i(LOG, "nothing");
        Log.i(LOG, "onClick -> LEAVE");
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }
}
