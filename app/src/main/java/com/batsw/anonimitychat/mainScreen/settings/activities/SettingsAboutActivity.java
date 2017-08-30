package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsAboutActivity extends Fragment {
    private static final String LOG = SettingsAboutActivity.class.getSimpleName();

//    private Fragment mThis = null;

    private View mView;
    private TextView mBackIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

//        //Loading fontAwesome
//        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
//                "font_awesome/fontawesome.ttf");
//
//        mBackIcon = (TextView) AppController.getInstanceParameterized(null).getCurrentActivityContext().findViewById(R.id.about_back_icon);
//        mBackIcon.setTypeface(fontAwesome);
//
//        mBackIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

//        mThis = this;

        Log.i(LOG, "onCreate -> ENTER");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.settings_about_activity, container, false);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
                "font_awesome/fontawesome.ttf");

        mBackIcon = (TextView) mView.findViewById(R.id.about_back_icon);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getFragmentManager().beginTransaction().remove(mThis).commit();
                getActivity().getFragmentManager().popBackStack();
            }
        });

        return mView;
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
