package com.batsw.anonimitychat.mainScreen.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.util.List;

/**
 * Created by tudor on 5/13/2017.
 */

public class NoNetworkPopup extends Dialog implements View.OnClickListener {

    private static final String LOG = NoNetworkPopup.class.getSimpleName();

    private Context mContext;
    private Button mConnectButton;

    public NoNetworkPopup(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.no_network_popup);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(getContext());

        mConnectButton = (Button) findViewById(R.id.nnp_connect_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mConnectButton.onClick -> ENTER");

                AppController.getInstanceParameterized(null).startNetworkConnection();

                Log.i(LOG, "mConnectButton.onClick -> LEAVE");
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
