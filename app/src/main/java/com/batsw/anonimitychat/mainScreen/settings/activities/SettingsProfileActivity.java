package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.MainScreenActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.persistence.entities.DBMyProfileEntity;

/**
 * Created by tudor on 4/25/2017.
 */

public class SettingsProfileActivity extends AppCompatActivity {
    private static final String LOG = SettingsProfileActivity.class.getSimpleName();

    private TextView mBackIcon, mMyAddress, mSave;
    private EditText mMyName;
    //    private EditText mMyNickname;
    private DBMyProfileEntity mMyProfileEntity;


//    private Fragment mThis = null;
//
//    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

//        mThis = this;

        setContentView(R.layout.settings_profile_activity);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);

        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
                "font_awesome/fontawesome.ttf");

        // load my profile
        mMyProfileEntity = AppController.getInstanceParameterized(null).getMyProfile();

        mBackIcon = (TextView) findViewById(R.id.settings_profile_back);
        mBackIcon.setTypeface(fontAwesome);

        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mMyName = (EditText) findViewById(R.id.my_name_edit);
        mMyName.setText(mMyProfileEntity.getMyName());
//        mMyNickname = (EditText) mView.findViewById(R.id.my_nickname_edit);
//        mMyNickname.setText(mMyProfileEntity.getMyNickName());

        mMyAddress = (TextView) findViewById(R.id.my_address_edit);
//        Without the suffix
        mMyAddress.setText(mMyProfileEntity.getMyAddress().substring(0, 16));

//        mMyEmail = (EditText) findViewById(R.id.email_contact_edit);
//        mMyEmail.setText(mMyProfileEntity.getMyEmail());

        mSave = (TextView) findViewById(R.id.settings_my_profile_save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myName = getTextFromEditText(mMyName);
                String myNickName = myName;
//                String myNickName = getTextFromEditText(mMyNickname);
//                String myEmail = getTextFromEditText(mMyEmail);

//                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
//                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString()) ||
//                        !myEmail.equals(mMyProfileEntity.getMyEmail().toString())) {

                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString())) {

                    mMyProfileEntity.setMyName(myName);
                    mMyProfileEntity.setMyNickName(myNickName);
//                    mMyProfileEntity.setMyEmail(myEmail);
                    mMyProfileEntity.setMyEmail(null);

                    AppController.getInstanceParameterized(null).updateMyProfile(mMyProfileEntity);

                    finish();
                }
            }
        });

        Log.i(LOG, "onCreate -> LEAVE");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        Log.i(LOG, "onCreateView -> ENTER");
//        mView = inflater.inflate(R.layout.settings_profile_activity, container, false);
//
//        Typeface fontAwesome = Typeface.createFromAsset(AppController.getInstanceParameterized(null).getCurrentActivityContext().getAssets(),
//                "font_awesome/fontawesome.ttf");
//
////        Hiding the floating Add contact button
//        TabContacts contactsFragment = ((MainScreenActivity) getActivity()).getContactsFragment();
//        if (contactsFragment != null) {
//            contactsFragment.getView().setVisibility(View.INVISIBLE);
//        }
//        // load my profile
//        mMyProfileEntity = AppController.getInstanceParameterized(null).getMyProfile();
//
//        mBackIcon = (TextView) mView.findViewById(R.id.settings_profile_back);
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
//        mMyName = (EditText) mView.findViewById(R.id.my_name_edit);
//        mMyName.setText(mMyProfileEntity.getMyName());
////        mMyNickname = (EditText) mView.findViewById(R.id.my_nickname_edit);
////        mMyNickname.setText(mMyProfileEntity.getMyNickName());
//
//        mMyAddress = (TextView) mView.findViewById(R.id.my_address_edit);
////        Without the suffix
//        mMyAddress.setText(mMyProfileEntity.getMyAddress().substring(0, 16));
//
////        mMyEmail = (EditText) findViewById(R.id.email_contact_edit);
////        mMyEmail.setText(mMyProfileEntity.getMyEmail());
//
//        mSave = (TextView) mView.findViewById(R.id.settings_my_profile_save);
//
//        mSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String myName = getTextFromEditText(mMyName);
//                String myNickName = myName;
////                String myNickName = getTextFromEditText(mMyNickname);
////                String myEmail = getTextFromEditText(mMyEmail);
//
////                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
////                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString()) ||
////                        !myEmail.equals(mMyProfileEntity.getMyEmail().toString())) {
//
//                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
//                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString())) {
//
//                    mMyProfileEntity.setMyName(myName);
//                    mMyProfileEntity.setMyNickName(myNickName);
////                    mMyProfileEntity.setMyEmail(myEmail);
//                    mMyProfileEntity.setMyEmail(null);
//
//                    AppController.getInstanceParameterized(null).updateMyProfile(mMyProfileEntity);
//                }
//            }
//        });
//
//        Log.i(LOG, "onCreateView -> LEAVE");
//        return mView;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        Log.i(LOG, "onCreate -> ENTER");
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.settings_profile_activity);
//
//        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);
//
//        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");
//
//        // load my profile
//        mMyProfileEntity = AppController.getInstanceParameterized(null).getMyProfile();
//
////        mEmailIcon = (TextView) findViewById(R.id.contact_email_icon_edit);
////        mEmailIcon.setTypeface(fontAwesome);
//
//        mBackIcon = (TextView) findViewById(R.id.settings_profile_back);
//        mBackIcon.setTypeface(fontAwesome);
//        mBackIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i(LOG, "mBackIcon.setOnClickListener -> ENTER");
//                finish();
//                Log.i(LOG, "mBackIcon.setOnClickListener -> LEAVE");
//            }
//        });
//
//        mMyName = (EditText) findViewById(R.id.my_name_edit);
//        mMyName.setText(mMyProfileEntity.getMyName());
//        mMyNickname = (EditText) findViewById(R.id.my_nickname_edit);
//        mMyNickname.setText(mMyProfileEntity.getMyNickName());
//
//        mMyAddress = (TextView) findViewById(R.id.my_address_edit);
////        Without the suffix
//        mMyAddress.setText(mMyProfileEntity.getMyAddress().substring(0, 16));
//
////        mMyEmail = (EditText) findViewById(R.id.email_contact_edit);
////        mMyEmail.setText(mMyProfileEntity.getMyEmail());
//
//        mSave = (TextView) findViewById(R.id.settings_my_profile_save);
//
//        mSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String myName = getTextFromEditText(mMyName);
//                String myNickName = getTextFromEditText(mMyNickname);
////                String myEmail = getTextFromEditText(mMyEmail);
//
////                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
////                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString()) ||
////                        !myEmail.equals(mMyProfileEntity.getMyEmail().toString())) {
//
//                if (!myName.equals(mMyProfileEntity.getMyName().toString()) ||
//                        !myNickName.equals(mMyProfileEntity.getMyNickName().toString())) {
//
//                    mMyProfileEntity.setMyName(myName);
//                    mMyProfileEntity.setMyNickName(myNickName);
////                    mMyProfileEntity.setMyEmail(myEmail);
//                    mMyProfileEntity.setMyEmail(null);
//
//                    AppController.getInstanceParameterized(null).updateMyProfile(mMyProfileEntity);
//                }
//            }
//        });
//
//        Log.i(LOG, "onCreate -> ENTER");
//    }

    private String getTextFromEditText(EditText editText) {
        Log.i(LOG, "getTextFromEditText -> ENTER editText=" + editText);
        String retVal = "";

        final StringBuilder sb = new StringBuilder(editText.getText().length());
        sb.append(editText.getText());
        retVal = sb.toString();

        Log.i(LOG, "getTextFromEditText -> LEAVE retVal=" + retVal);
        return retVal;
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, SettingsProfileActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
