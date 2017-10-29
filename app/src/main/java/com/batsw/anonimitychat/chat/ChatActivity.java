package com.batsw.anonimitychat.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.batsw.anonimitychat.MainActivity;
import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.chat.management.ChatDetail;
import com.batsw.anonimitychat.chat.management.activity.ChatActivityManagerImpl;
import com.batsw.anonimitychat.chat.management.activity.IChatActivityManager;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;
import com.batsw.anonimitychat.mainScreen.popup.EmptyHistoryPopup;
import com.batsw.anonimitychat.mainScreen.popup.NoNetworkPopup;
import com.batsw.anonimitychat.mainScreen.popup.PartnerOfflinePopup;
import com.batsw.anonimitychat.persistence.entities.DBChatMessageEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.connections.TorPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tudor on 1/27/2017.
 */

public class ChatActivity extends AppCompatActivity {

    // =================
    // === VARIABLES ===
    // =================

    private static final String LOG = ChatActivity.class.getSimpleName();

    private RelativeLayout reltiveLayout;

    private ListView chatListView;
    private ChatListAdapter mChatListAdapter;
    private ArrayList<ChatMessage> mChatMessageList;

    private long mSessionId = 0;

    private EditText chatEditText;

    private TextView mHistory, mChatName, mBack, mNetworkConnection;
    private TextView mConnectionStatus;

    private boolean mHistoryLoaded = false;

    private ImageView mEnterChatMessage;

    private IChatActivityManager mChatActivityManager = new ChatActivityManagerImpl();

    private NoNetworkPopup mNoNetworkPopup;
    private EmptyHistoryPopup mEmptyHistoryPopup;
    private PartnerOfflinePopup mPartnerOfflinePopup;

    /**
     * Sending the message when clicking on Send image
     */
    private ImageView.OnClickListener mClickForEnterChatView = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            Log.i(LOG, "mClickForEnterChatView.onClick -> ENTER");
            if (view.equals(mEnterChatMessage)) {
                if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STARTED)) {

                    if (mChatActivityManager.getChatDetail().getTorConnection() != null) {

                        Log.i(LOG, "sending message to partner");

                        final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());

                        //sending the message HERE
                        mChatActivityManager.sendMessage(message.getMessage());

                        mChatMessageList.add(message);
                    } else {
                        mNetworkConnection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.userstatus_busy_outline));

                        //                    means starting the connection to the partner
                        mChatActivityManager.onCreate();

                        mPartnerOfflinePopup.show();
                    }
                } else {
                    Log.i(LOG, "showing NoNetworkPopup");
                    mNoNetworkPopup.show();
                }
            }
            chatEditText.setText("");

            Log.i(LOG, "mClickForEnterChatView.onClick -> LEAVE");
        }
    };

    private final TextWatcher mChatEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!chatEditText.getText().toString().isEmpty()) {
                mEnterChatMessage.setImageResource(R.drawable.input_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mEnterChatMessage.setImageResource(R.drawable.input_send);
        }
    };

//    ========================
//    ===     METHODS      ===
//    ========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        reltiveLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        ChatController.getInstance().setCurrentActivityContext(this);

        mChatActivityManager.setChatActivity(this);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        mSessionId = getIntent().getLongExtra(ChatModelConstants.CHAT_ACTIVITY_INTENT_EXTRA_KEY, ChatModelConstants.DEFAULT_SESSION_ID);
        if (mSessionId != ChatModelConstants.DEFAULT_SESSION_ID) {
            mChatActivityManager.configureChatDetail(mSessionId);
        }

        mChatMessageList = new ArrayList<>();

        mChatListAdapter = new ChatListAdapter(mChatMessageList, this);
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatListView.setAdapter(mChatListAdapter);

        mEnterChatMessage = (ImageView) findViewById(R.id.enter_chat1);
        mEnterChatMessage.setOnClickListener(mClickForEnterChatView);

        // set image on click listener
        chatEditText = (EditText) findViewById(R.id.chat_edit_text1);
        /**
         * Sending the message when hitting enter on keyboard
         */
        chatEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                return processChatEditTextKey(view, keyCode, keyEvent);
            }
        });

        chatEditText.addTextChangedListener(mChatEditTextWatcher);

        //TODO:
        //IChatActivityManager.getPartnerAddress (16x.onion)

        mBack = (TextView) findViewById(R.id.chat_back_icon);
        mBack.setTypeface(fontAwesome);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mBack.setOnClickListener -> ENTER");
                finish();
                Log.i(LOG, "mBack.setOnClickListener -> LEAVE");
            }
        });

        DBContactEntity contactEntity = AppController.getInstanceParameterized(null).getContactEntity(mSessionId);
        mChatName = (TextView) findViewById(R.id.chat_name);
        mChatName.setText(contactEntity.getName());

        mNetworkConnection = (TextView) findViewById(R.id.chat_partner_status_icon);
        mNetworkConnection.setTypeface(fontAwesome);
        if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STARTED)) {
            mNetworkConnection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.userstatus_online));
        } else {
            mNetworkConnection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.userstatus_offline));
        }

        mConnectionStatus = new TextView(this);

        AppController.getInstanceParameterized(null).updateWithNetworkConnectionStatus(mConnectionStatus);

        mConnectionStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "mConnectionStatus.onTextChanged -> ENTER");
                if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    Log.i(LOG, "mNetworkConnection.onTextChanged to: " + AppController.getInstanceParameterized(null).getNetworkConnectionStatus());

                    mNetworkConnection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.userstatus_online));

//                    means starting the connection to the partner
                    mChatActivityManager.onCreate();
                } else {
                    Log.i(LOG, "mNetworkConnection.onTextChanged to: " + AppController.getInstanceParameterized(null).getNetworkConnectionStatus());

                    mNetworkConnection.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.userstatus_offline));
                    mChatActivityManager.onDestroy();
                }
                Log.i(LOG, "mConnectionStatus.onTextChanged -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mHistory = (TextView) findViewById(R.id.chat_history_button);
        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mHistory.setOnClickListener -> ENTER");

                List<IDbEntity> messageHistoryForSessionId = AppController.getInstanceParameterized(null).getMessageHistoryForSessionId(mSessionId);

                if (!mHistoryLoaded && messageHistoryForSessionId.size() > 0) {

                    List<ChatMessage> chatMessages = new ArrayList<>();

                    for (IDbEntity ide : messageHistoryForSessionId) {
                        DBChatMessageEntity dbChatMessageEntity = (DBChatMessageEntity) ide;

                        ChatMessage chatMessage = new ChatMessage(dbChatMessageEntity.getMessage(),
                                dbChatMessageEntity.getChatMessageType(),
                                dbChatMessageEntity.getTimestamp()
                        );

                        chatMessages.add(chatMessage);
                    }

                    mChatListAdapter.addMessageToList(chatMessages);

                    mHistoryLoaded = true;
                } else {
                    mEmptyHistoryPopup.show();
                }

                Log.i(LOG, "mHistory.setOnClickListener -> LEAVE");
            }
        });

        mChatActivityManager.onCreate();

        mNoNetworkPopup = new NoNetworkPopup(this);
        mEmptyHistoryPopup = new EmptyHistoryPopup(this);
        mPartnerOfflinePopup = new PartnerOfflinePopup(this, mChatActivityManager);

        Log.i(LOG, "onCreate -> LEAVE");
    }

    /**
     * This method is used to clean the communication resources when the USER has finished chatting
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG, "onDestroy -> ENTER");

        mChatActivityManager.onDestroy();

        Log.i(LOG, "onDestroy -> LEAVE");
    }

    public void showReceivedPartnerMessage(ChatMessage partnerMessage) {
        Log.i(LOG, "showReceivedPartnerMessage -> ENTER");

        mChatMessageList.add(partnerMessage);
        if (mChatListAdapter != null)
            mChatListAdapter.notifyDataSetChanged();

        Log.i(LOG, "showReceivedPartnerMessage -> ENTER");
    }

    private boolean processChatEditTextKey(View view, int keyCode, KeyEvent keyEvent) {
        Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> ENTER");

        // event for "ENTER" key pressed
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

            Log.i(LOG, "enter key pressed");

            EditText editText = (EditText) view;

            //checking to make sure that the text comes from the actual chatEditText
            if (editText.getText().toString().equals(chatEditText.getText().toString())) {

                if (chatEditText.getText().toString().isEmpty())
                    return false;

//                    final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
//                    mChatMessageList.add(message);

                // why is PARNTER switched with USER type message ....
//                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.PARTNER, System.currentTimeMillis());
                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());

                //TODO: I send the message HERE
                mChatActivityManager.sendMessage(message.getMessage());

                mChatMessageList.add(message);

                if (mChatListAdapter != null)
                    mChatListAdapter.notifyDataSetChanged();
            }

            editText.setText("");

            Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> LEAVE");
            return true;
        }

        Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> LEAVE");
        return false;
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        Log.i(LOG, "makeIntent -> ENTER context=" + context);

        Intent retVal = new Intent(context, ChatActivity.class);

        Log.i(LOG, "makeIntent -> LEAVE retVal=" + retVal);
        return retVal;
    }
}



