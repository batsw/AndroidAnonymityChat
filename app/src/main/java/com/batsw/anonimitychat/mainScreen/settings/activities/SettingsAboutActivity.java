package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsAboutActivity extends AppCompatActivity {
    private static final String LOG = SettingsAboutActivity.class.getSimpleName();

    private Fragment mThis = null;

    //    private View mView;
    private TextView mBackIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

//        mThis = this;

        setContentView(R.layout.settings_about_activity);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(),
                "font_awesome/fontawesome.ttf");

////        Hiding the floating Add contact button
//        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
//        if (contactsFragment != null) {
//            contactsFragment.getView().setVisibility(View.INVISIBLE);
//        }

        mBackIcon = (TextView) findViewById(R.id.about_back_icon);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mBackIcon.onClick -> ENTER");
//                getActivity().getFragmentManager().beginTransaction().remove(mThis).commit();
//
//                TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
//                if (contactsFragment != null) {
//                    contactsFragment.getView().setVisibility(View.VISIBLE);
//                }

                finish();
                Log.i(LOG, "mBackIcon.onClick -> LEAVE");
            }

        });

        Log.i(LOG, "onCreate -> LEAVE");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Log.i(LOG, "onCreateView -> ENTER");
//        mView = inflater.inflate(R.layout.settings_about_activity, container, false);
//
//        //Loading fontAwesome
//        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
//                "font_awesome/fontawesome.ttf");
//
////        Hiding the floating Add contact button
//        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
//        if (contactsFragment != null) {
//            contactsFragment.getView().setVisibility(View.INVISIBLE);
//        }
//
//        mBackIcon = (TextView) mView.findViewById(R.id.about_back_icon);
//        mBackIcon.setTypeface(fontAwesome);
//
//        mBackIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getFragmentManager().beginTransaction().remove(mThis).commit();
//
//                TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
//                if (contactsFragment != null) {
//                    contactsFragment.getView().setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        Log.i(LOG, "onCreateView -> LEAVE");
//        return mView;
//    }

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
