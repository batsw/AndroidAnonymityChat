package com.batsw.anonimitychat.mainScreen.addContact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.batsw.anonimitychat.R;

/**
 * Created by tudor on 4/15/2017.
 */

public class ContactAddActivity extends AppCompatActivity {

    private static final String LOG = ContactAddActivity.class.getSimpleName();

    private EditText mContactName, mContactNickname, mContactAddress, mContactEmail;
    private TextView mBackIcon, mEmailIcon, mChangeContactAvatar;
    private Button mAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(LOG, "onCreate -> ENTER");
        setContentView(R.layout.contact_tab_add_contact);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        mContactName = (EditText) findViewById(R.id.name_contact_add);
        mContactNickname = (EditText) findViewById(R.id.nickname_contact_add);

        mContactAddress = (EditText) findViewById(R.id.address_contact_add);
        mContactEmail = (EditText) findViewById(R.id.email_contact_add);

        mAdd = (Button) findViewById(R.id.contact_add_button);

        mBackIcon = (TextView) findViewById(R.id.contact_back_icon_add);
        mBackIcon.setTypeface(fontAwesome);

        mEmailIcon = (TextView) findViewById(R.id.contact_email_icon_add);
        mEmailIcon.setTypeface(fontAwesome);

        //TODO: to add a listener that allows loading pictures for avatars
        mChangeContactAvatar = (TextView) findViewById(R.id.change_avatar_contact_add);
        mChangeContactAvatar.setTypeface(fontAwesome);

        Log.i(LOG, "onCreate -> LEAVE");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(LOG, "onPause -> ENTER");

        Log.i(LOG, "Do nothing !");

        Log.i(LOG, "onPause -> LEAVE");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(LOG, "onStart -> ENTER");

        Log.i(LOG, "Do nothing !");

        Log.i(LOG, "onStart -> LEAVE");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(LOG, "onStop -> ENTER");

        Log.i(LOG, "Do nothing !");

        Log.i(LOG, "onStop -> LEAVE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(LOG, "onDestroy -> ENTER");

        Log.i(LOG, "Do nothing !");

        Log.i(LOG, "onDestroy -> LEAVE");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, ContactAddActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
