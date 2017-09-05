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
import com.batsw.anonimitychat.mainScreen.MainScreenActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsStorageActivity extends Fragment {
    private static final String LOG = SettingsStorageActivity.class.getSimpleName();

    private Fragment mThis = null;

    private View mView;
    private TextView mBackIcon;

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
        mView = inflater.inflate(R.layout.settings_storage_activity, container, false);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
                "font_awesome/fontawesome.ttf");

//        Hiding the floating Add contact button
        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
        if (contactsFragment != null) {
            contactsFragment.getView().setVisibility(View.INVISIBLE);
        }

        mBackIcon = (TextView) mView.findViewById(R.id.settings_storage_back);
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

        Log.i(LOG, "onCreateView -> LEAVE");
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

        Intent retVal = new Intent(context, SettingsStorageActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
