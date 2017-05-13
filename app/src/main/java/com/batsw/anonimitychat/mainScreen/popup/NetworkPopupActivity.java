package com.batsw.anonimitychat.mainScreen.popup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.List;

import com.batsw.anonimitychat.R;

/**
 * Created by tudor on 5/13/2017.
 */

public class NetworkPopupActivity extends Dialog implements View.OnClickListener {

    private static final String LOG = NetworkPopupActivity.class.getSimpleName();

    private Context mContext;

    private TextView mCheckIcon;

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

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(mContext.getAssets(), "font_awesome/fontawesome.ttf");

        mCheckIcon = (TextView) findViewById(R.id.network_popup_check);
        mCheckIcon.setTypeface(fontAwesome);

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
