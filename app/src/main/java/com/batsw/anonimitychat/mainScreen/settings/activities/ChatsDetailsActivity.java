package com.batsw.anonimitychat.mainScreen.settings.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.util.AppConstants;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by woman on 4/30/2017.
 */
public class ChatsDetailsActivity extends AppCompatActivity {
    private static final String LOG = ChatsDetailsActivity.class.getSimpleName();

    private Spinner historyCleanupSpinner;
    private LinearLayout historyCleanupLayout;
    private TextView trashIcon, spinnerArrow, mBack, mSave, mContactAddress;
    private EditText mChatName, mHistoryCleanupTime;
    private List<String> list = new ArrayList<>();

    private DBChatEntity mChatEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_tab_chat_details);

        AppController.getInstanceParameterized(null).setChatControllerCurrentActivityContext(this);

        list.add(0, "never");

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        Long chatSessionId = getIntent().getLongExtra(AppConstants.CHAT_ITEM_PUT_EXTRA, 0l);
        if (chatSessionId != 0l) {
            mChatEntity = AppController.getInstanceParameterized(null).getChatEntity(chatSessionId);
        }

        mChatName = (EditText) findViewById(R.id.chat_details_name_edit);
        mHistoryCleanupTime = (EditText) findViewById(R.id.chat_details_history_cleanup_time);
        mContactAddress = (TextView) findViewById(R.id.chat_details_address);

        if (mChatEntity != null) {
            mChatName.setText(mChatEntity.getChatName());
            mHistoryCleanupTime.setText(String.valueOf(mChatEntity.getHistoryCleanupTime()));

            final DBContactEntity contactEntity = AppController.getInstanceParameterized(null).getContactEntity(chatSessionId);
            if (contactEntity != null) {
                mContactAddress.setText(contactEntity.getAddress());
            }
        }

        mBack = (TextView) findViewById(R.id.chat_details_back);
        mBack.setTypeface(fontAwesome);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mBack.setOnClickListener -> ENTER");
                finish();
                Log.i(LOG, "mBack.setOnClickListener -> LEAVE");
            }
        });

        historyCleanupLayout = (LinearLayout) findViewById(R.id.history_cleanup_layout);

        trashIcon = (TextView) findViewById(R.id.chat_details_trash_icon);
        trashIcon.setTypeface(fontAwesome);

        mSave = (TextView) findViewById(R.id.chat_details_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatName = mChatName.getText().toString();
                long historyCleanupTime = 0;
                try {
                    historyCleanupTime = Long.parseLong(mHistoryCleanupTime.getText().toString());
                } catch (NumberFormatException nfe) {
                    //TODO: pop-up history cleanup time numeric only - if <= 0 never delete
                }

                if ((chatName.length() > 0 && !chatName.equals(mChatEntity.getChatName()))
                        || !(historyCleanupTime == mChatEntity.getHistoryCleanupTime())) {

                    mChatEntity.setChatName(chatName);
                    mChatEntity.setHistoryCleanupTime(historyCleanupTime);
                    final boolean updatedChatDetails = AppController.getInstanceParameterized(null).updateChat(mChatEntity);
                    if (updatedChatDetails) {
                        finish();
                    } else {
//                        TODO: pop-up please check entered data
                    }
                }
            }
        });


        //spinnerArrow = (TextView) findViewById(R.id.spinner_arrow);
        //  spinnerArrow.setTypeface(fontAwesome);

        //historyCleanupLayout.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //    public void onClick(View historyCleanupView) {
        //         historyCleanupSpinner.performClick();
        //     }
        //  });

        Log.i(LOG, "onCreate -> LEAVE");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, ChatsDetailsActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
