package com.batsw.anonimitychat.mainScreen.contactEditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.adapters.ContactsAdapter;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.util.AppConstants;

/**
 * Created by tudor on 4/9/2017.
 */

public class ContactEditorActivity extends AppCompatActivity {

    private static final String LOG = ContactEditorActivity.class.getSimpleName();

    private DBContactEntity mContactEntity;
    private DBChatEntity mChatEntity;

    private EditText mContactName, mContactNickname, mHistoryCleanupTime;
    private TextView mContactAddress, mContactEmail, mChangeContactAvatar, mEmailIcon, mBackIcon;
    private ImageView mContactAvatar;
    private TextView mSave;
    private Button mDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contact_tab_editor);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        Long contactSessionId = getIntent().getLongExtra(AppConstants.CONTACT_ITEM_PUT_EXTRA, 0l);
        if (contactSessionId != 0l) {
            mContactEntity = AppController.getInstanceParameterized(null).getContactEntity(contactSessionId);
            mChatEntity = AppController.getInstanceParameterized(null).getChatEntity(contactSessionId);
        }

        mContactName = (EditText) findViewById(R.id.name_contact_edit);
        mContactName.setText(mContactEntity.getName());

        mContactNickname = (EditText) findViewById(R.id.nickname_contact_edit);
        mContactNickname.setText(mContactEntity.getNickName());

        mContactAddress = (TextView) findViewById(R.id.address_contact_edit);

        String contactAddress = mContactEntity.getAddress().substring(0, 16);
        mContactAddress.setText(contactAddress);

//        mContactEmail = (TextView) findViewById(R.id.email_contact_edit);
//        mContactEmail.setText(mContactEntity.getEmail());

        mContactAvatar = (ImageView) findViewById(R.id.contact_avatar_edit);

// Chat details
        mHistoryCleanupTime = (EditText) findViewById(R.id.chat_details_history_cleanup_time);

        if (mChatEntity != null) {
            mHistoryCleanupTime.setText(String.valueOf(mChatEntity.getHistoryCleanupTime()));
        }

        mSave = (TextView) findViewById(R.id.save_contact_edit_button);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mSave.onClick -> ENTER");

//                mContactEntity.setAddress(mContactAddress.getText().toString());
                mContactEntity.setName(mContactName.getText().toString());
                mContactEntity.setNickName(mContactNickname.getText().toString());
                mContactEntity.setEmail(mContactEmail.getText().toString());

                long historyCleanupTime = 0;
                try {
                    historyCleanupTime = Long.parseLong(mHistoryCleanupTime.getText().toString());
                    mChatEntity.setHistoryCleanupTime(historyCleanupTime);
                } catch (NumberFormatException nfe) {
                    //TODO: pop-up history cleanup time numeric only - if <= 0 never delete
                }

                AppController.getInstanceParameterized(null).updateChat(mChatEntity);

                final boolean updateSuccessfull = AppController.getInstanceParameterized(null).updateContact(mContactEntity);

                if (updateSuccessfull) {
                    ContactEntity tabContactEntity = new ContactEntity(
                            (mContactEntity.getNickName() == null || mContactEntity.getNickName().isEmpty()) ? mContactEntity.getName() : mContactEntity.getNickName(),
                            mContactEntity.getSessionId()
                    );

                    AppController.getInstanceParameterized(null).editContactToTab(tabContactEntity);
                    finish();
                }

                Log.i(LOG, "mSave.onClick -> LEAVE");
            }
        });

        mDelete = (Button) findViewById(R.id.contact_delete_edit_button);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mDelete.onClick -> ENTER");
                final String contactAddress = mContactAddress.getText().toString();
                final boolean deletedContact = AppController.getInstanceParameterized(null).deleteContact(contactAddress);
                if (deletedContact) {
                    Log.i(LOG, "contact deleted successfully");
                    finish();
                }

                Log.i(LOG, "mDelete.onClick -> LEAVE");
            }
        });

        mBackIcon = (TextView) findViewById(R.id.contact_back_icon_edit);
        mBackIcon.setTypeface(fontAwesome);
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mBackIcon.setOnClickListener -> ENTER");
                finish();
                Log.i(LOG, "mBackIcon.setOnClickListener -> LEAVE");
            }
        });

//        mEmailIcon = (TextView) findViewById(R.id.contact_email_icon_edit);
//        mEmailIcon.setTypeface(fontAwesome);

        //TODO: to add a listener that allows loading pictures for avatars
        mChangeContactAvatar = (TextView) findViewById(R.id.change_avatar_contact_edit);
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

        Intent retVal = new Intent(context, ContactEditorActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
