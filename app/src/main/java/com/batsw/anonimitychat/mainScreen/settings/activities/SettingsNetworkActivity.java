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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.MainScreenActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsNetworkActivity extends Fragment {
    private static final String LOG = SettingsNetworkActivity.class.getSimpleName();

    private Fragment mThis = null;

    private View mView;
    private TextView mBackIcon;
    private Switch mNetworkConnectionSwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        mThis = this;

        Log.i(LOG, "onCreate -> LEAVE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG, "onCreateView -> ENTER");
        mView = inflater.inflate(R.layout.settings_network_activity, container, false);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
                "font_awesome/fontawesome.ttf");

//        Hiding the floating Add contact button
        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
        if (contactsFragment != null) {
            contactsFragment.getView().setVisibility(View.INVISIBLE);
        }

        mBackIcon = (TextView) mView.findViewById(R.id.settings_network_back);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(mThis).commit();

                TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
                if (contactsFragment != null) {
                    contactsFragment.getView().setVisibility(View.VISIBLE);
                }
            }
        });

        mNetworkConnectionSwitch = (Switch) mView.findViewById(R.id.settings_network_connection_toogle);
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

        if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STOPPED)) {
            mNetworkConnectionSwitch.setChecked(false);
        } else {
            mNetworkConnectionSwitch.setChecked(true);
        }

        Log.i(LOG, "onCreateView -> LEAVE");
        return mView;
    }

    @Override
    public void onStart() {
        Log.i(LOG, "onStart -> ENTER");
        super.onStart();
        Log.i(LOG, "onStart -> LEAVE");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, SettingsNetworkActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public void onDestroyView() {
        Log.i(LOG, "onDestroyView -> ENTER");
        super.onDestroyView();
        Log.i(LOG, "onDestroyView -> LEAVE");
    }
}
