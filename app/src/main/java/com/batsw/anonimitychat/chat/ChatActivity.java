package com.batsw.anonimitychat.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.batsw.anonimitychat.MainActivity;
import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;
import com.batsw.anonimitychat.tor.connections.TorPublisher;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tudor on 1/27/2017.
 */

public class ChatActivity extends AppCompatActivity {

    // =================
    // === VARIABLES ===
    // =================

    private static final String LOG = "ChatActivity";

    private RelativeLayout reltiveLayout;

    private ListView chatListView;
    private ChatListAdapter mChatListAdapter;
    private ArrayList<ChatMessage> mChatMessageList;

    private ConcurrentHashMap<Integer, TorPublisher> mContactedPartnerHostnames = null;
    private TorPublisher mTorPublisher = null;

    private EditText chatEditText;

    private ImageView mEnterChatMessage;

    private ImageView.OnClickListener mClickForEnterChatView = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.equals(mEnterChatMessage)) {
                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
                mChatMessageList.add(message);

                //TODO: I send the message HERE
                mTorPublisher.sendMessage(message.getMessage());

            }
            chatEditText.setText("");
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

        mChatMessageList = new ArrayList<>();

        mChatListAdapter = new ChatListAdapter(mChatMessageList, this);
        chatListView = (ListView) findViewById(R.id.chat_list_view);
        chatListView.setAdapter(mChatListAdapter);

        mEnterChatMessage = (ImageView) findViewById(R.id.enter_chat1);
        mEnterChatMessage.setOnClickListener(mClickForEnterChatView);

        // set image on click listener

        chatEditText = (EditText) findViewById(R.id.chat_edit_text1);
        chatEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                return processChatEditTextKey(view, keyCode, keyEvent);
            }
        });

        chatEditText.addTextChangedListener(mChatEditTextWatcher);

        Integer contactIndex = 0;
        mContactedPartnerHostnames = MainActivity.getContactedPartnerHostnames();

        if (mContactedPartnerHostnames != null && !mContactedPartnerHostnames.isEmpty()) {

            if (mContactedPartnerHostnames.size() > 0) {
                Set<Integer> keys = mContactedPartnerHostnames.keySet();
                Object[] integersArray = keys.toArray();
                contactIndex = (Integer) integersArray[0];
            }
        }

        //Tudor: after finding the needed partner connection we are REMOVING it from the hashMap
        if (contactIndex > 0) {
            mTorPublisher = mContactedPartnerHostnames.get(contactIndex);
            mTorPublisher.setChatActivity(this);

            if (mTorPublisher != null) {
                mContactedPartnerHostnames.remove(contactIndex);
            }
        }
    }

    public void addPartnerMessageToMessageList(ChatMessage partnerMessage) {
        mChatMessageList.add(partnerMessage);
        if (mChatListAdapter != null)
            mChatListAdapter.notifyDataSetChanged();
    }


    /**
     * This method is used to clean the communication resources when the USER has finished chatting
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG, "closeCommunication -> ENTER");

        if (mTorPublisher != null) mTorPublisher.closeCommunication();
        Log.i(LOG, "closeCommunication -> ENTER");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, ChatActivity.class);
    }


    private boolean processChatEditTextKey(View view, int keyCode, KeyEvent keyEvent){
        Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> ENTER");

        // key-down event for "ENTER" key pressed
        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

            Log.i(LOG, "enter key pressed");

            EditText editText = (EditText) view;

            //checking to make sure that the text comes from the actual chatEditText
            if (editText.getText().toString().equals(chatEditText.getText().toString())) {

                if (chatEditText.getText().toString().isEmpty())
                    return false;

//                    final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
//                    mChatMessageList.add(message);

                // whi is PARNTER switched with USER type message ....
//                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.PARTNER, System.currentTimeMillis());
                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
                mChatMessageList.add(message);

                //TODO: I send the message HERE
                mTorPublisher.sendMessage(message.getMessage());


                if (mChatListAdapter != null)
                    mChatListAdapter.notifyDataSetChanged();
            }

            //TODO: call the send message method here ---
            editText.setText("");

            Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> LEAVE");
            return true;
        }

        Log.i(LOG, "chatEditTextKeyListener.OnKeyListener:onKey -> LEAVE");
        return false;
    }

}



