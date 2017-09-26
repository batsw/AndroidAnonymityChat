package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.MainScreenActivity;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.io.File;
import java.util.List;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsStorageActivity extends AppCompatActivity {
    private static final String LOG = SettingsStorageActivity.class.getSimpleName();

//    private Fragment mThis = null;

    //    private View mView;
    private TextView mBackIcon, mDbSize;
    //    private TextView mClockIcon;
    private Button mDeleteEverything;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

//        mThis = this;

        setContentView(R.layout.settings_storage_activity);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
                "font_awesome/fontawesome.ttf");

        mBackIcon = (TextView) findViewById(R.id.settings_storage_back);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mDbSize = (TextView) findViewById(R.id.storage_settings_db_size);

        File f = AppController.getInstanceParameterized(null).getCurrentActivityContext().getDatabasePath(PersistenceConstants.DATABASE_ANONYMITY_CHAT);
        long dbSize = f.length();
        Log.i(LOG, "mDbSize -> " + dbSize);

        mDbSize.setText(String.valueOf(dbSize / 1024) + " KB");

        mDeleteEverything = (Button) findViewById(R.id.settings_storage_clear_all);
        mDeleteEverything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mDeleteEverything.onClick -> ENTER");

                List<IDbEntity> contactList = AppController.getInstanceParameterized(null).getContactList();

                for (IDbEntity dbEntity : contactList) {
                    DBContactEntity dbContactEntity = (DBContactEntity) dbEntity;

                    AppController.getInstanceParameterized(null).deleteContact(dbContactEntity.getAddress());

                }

                Log.i(LOG, "mDeleteEverything.onClick -> LEAVE");
            }
        });

        Log.i(LOG, "onCreate -> LEAVE");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Log.i(LOG, "onCreateView -> ENTER");
//        mView = inflater.inflate(R.layout.settings_storage_activity, container, false);
//
//        //Loading fontAwesome
//        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
//                "font_awesome/fontawesome.ttf");
//
//////        Hiding the floating Add contact button
////        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
////        if (contactsFragment != null) {
////            contactsFragment.getView().setVisibility(View.INVISIBLE);
////        }
//
////        mClockIcon = (TextView) mView.findViewById(R.id.settings_storage_clock_icon);
////        mClockIcon.setTypeface(fontAwesome);
//
//        mBackIcon = (TextView) mView.findViewById(R.id.settings_storage_back);
//        mBackIcon.setTypeface(fontAwesome);
//
//        mBackIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().getFragmentManager().beginTransaction().remove(mThis).commit();
//
////                TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
////                if (contactsFragment != null) {
////                    contactsFragment.getView().setVisibility(View.VISIBLE);
////                }
//            }
//        });
//
//        mDbSize = (TextView) mView.findViewById(R.id.storage_settings_db_size);
//
//        File f = mView.getContext().getDatabasePath(PersistenceConstants.DATABASE_ANONYMITY_CHAT);
//        long dbSize = f.length();
//        Log.i(LOG, "mDbSize -> " + dbSize);
//
//        mDbSize.setText(String.valueOf(dbSize / 1024) + " KB");
//
//        mDeleteEverything = (Button) mView.findViewById(R.id.settings_storage_clear_all);
//        mDeleteEverything.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(LOG, "mDeleteEverything.onClick -> ENTER");
//
//                List<IDbEntity> contactList = AppController.getInstanceParameterized(null).getContactList();
//
//                for (IDbEntity dbEntity : contactList) {
//                    DBContactEntity dbContactEntity = (DBContactEntity) dbEntity;
//
//                    AppController.getInstanceParameterized(null).deleteContact(dbContactEntity.getAddress());
//                }
//
//                Log.i(LOG, "mDeleteEverything.onClick -> LEAVE");
//            }
//        });
//
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

        Intent retVal = new Intent(context, SettingsStorageActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
