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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

/**
 * Created by tudor on 5/13/2017.
 */

public class NetworkPopupActivity extends Dialog implements View.OnClickListener {

    private static final String LOG = NetworkPopupActivity.class.getSimpleName();

    private Context mContext;

    private TextView mMyAddress;
    //    private TextView mDoneLabel, mCheckIcon;
    private TextView mNetworkConnectionStatusLabel;
    private Switch mNetworkConnectionSwitch;

    public NetworkPopupActivity(@NonNull Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.network_popup);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(getContext());

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(mContext.getAssets(), "font_awesome/fontawesome.ttf");

//        mCheckIcon = (TextView) findViewById(R.id.network_popup_check);
//        mCheckIcon.setTypeface(fontAwesome);

        mMyAddress = (TextView) findViewById(R.id.network_my_address);
        mMyAddress.setText(AppController.getInstanceParameterized(null).getMyProfile().getMyAddress().substring(0, 16));

        mNetworkConnectionStatusLabel = (TextView) findViewById(R.id.network_popup_status);
        AppController.getInstanceParameterized(null).updateWithNetworkConnectionStatus(mNetworkConnectionStatusLabel);
        mNetworkConnectionStatusLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "mNetworkConnectionSwitch.onCheckedChanged -> ENTER charSequence=" + charSequence);
                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String textViewText = sb.toString();

                if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    mNetworkConnectionStatusLabel.setTextColor(mContext.getResources().getColor(R.color.colorStartedTorStatus));
                    mNetworkConnectionSwitch.setChecked(true);

                    mMyAddress.setText(AppController.getInstanceParameterized(null).getMyProfile().getMyAddress().substring(0, 16));
                } else if (textViewText.equals(TorConstants.TOR_BUNDLE_IS_STARTING)) {
                    mNetworkConnectionStatusLabel.setTextColor(mContext.getResources().getColor(R.color.colorStartingTorStatus));
                } else {
                    mNetworkConnectionStatusLabel.setTextColor(mContext.getResources().getColor(R.color.colorStoppedTorStatus));
                    mNetworkConnectionSwitch.setChecked(false);

                    mMyAddress.setText(AppController.getInstanceParameterized(null).getMyProfile().getMyAddress().substring(0, 16));
                }

                Log.i(LOG, "mNetworkConnectionSwitch.onCheckedChanged -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

        mNetworkConnectionSwitch = (Switch) findViewById(R.id.network_popup_toogle);
        mNetworkConnectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.i(LOG, "mNetworkConnectionSwitch.onCheckedChanged -> ENTER");

                if (isChecked) {
                    AppController.getInstanceParameterized(null).startNetworkConnection();
                } else {
                    AppController.getInstanceParameterized(null).stopNetworkConnection();
                }

                Log.i(LOG, "mNetworkConnectionSwitch.onCheckedChanged -> LEAVE");
            }
        });

//        mDoneLabel = (TextView) findViewById(R.id.network_popup_done);
//        mDoneLabel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        Log.i(LOG, "onCreate -> LEAVE");
    }

//    @Override
//    public void create() {
//
//        super.create();
//
//    }

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
