package com.batsw.anonimitychat.chat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;

import java.util.ArrayList;

/**
 * Created by tudor on 1/27/2017.
 */

public class ChatActivity extends AppCompatActivity {

    // =================
    // === VARIABLES ===
    // =================

    private static final String MAIN_ACTIVITY_TAG = ChatActivity.class.getSimpleName();

    private RelativeLayout reltiveLayout;

    private ListView chatListView;
    private ChatListAdapter mChatListAdapter;
    private ArrayList<ChatMessage> mChatMessageList;

    // Edit text box for the chat ---> variables
    private EditText chatEditText;
    private EditText.OnKeyListener chatEditTextKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

            Log.i(MAIN_ACTIVITY_TAG, "EditText.OnKeyListener:onKey - ENTER");

            // key-down event for "ENTER" key pressed
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

                Log.i(MAIN_ACTIVITY_TAG, "enter key pressed");

                EditText editText = (EditText) view;

                //checking to make sure that the text comes from the actual chatEditText
                if (editText.getText().toString().equals(chatEditText.getText().toString())) {

                    if (chatEditText.getText().toString().isEmpty())
                        return false;

                    final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
                    mChatMessageList.add(message);

                    final ChatMessage message2 = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.PARTNER, System.currentTimeMillis());
                    mChatMessageList.add(message2);

                    if (mChatListAdapter != null)
                        mChatListAdapter.notifyDataSetChanged();
                }

                //TODO: call the send message method here ---
                editText.setText("");

                return true;
            }

            return false;
        }
    };

    private ImageView mEnterChatView;

    private ImageView.OnClickListener mClickForEnterChatView = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.equals(mEnterChatView)) {
                final ChatMessage message = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.USER, System.currentTimeMillis());
                mChatMessageList.add(message);

                final ChatMessage message2 = new ChatMessage(chatEditText.getText().toString(), ChatMessageType.PARTNER, System.currentTimeMillis());
                mChatMessageList.add(message2);
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
                mEnterChatView.setImageResource(R.drawable.input_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mEnterChatView.setImageResource(R.drawable.input_send);
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

        mEnterChatView = (ImageView) findViewById(R.id.enter_chat1);
        mEnterChatView.setOnClickListener(mClickForEnterChatView);

        // set image on click listener

        chatEditText = (EditText) findViewById(R.id.chat_edit_text1);
        chatEditText.setOnKeyListener(chatEditTextKeyListener);
        chatEditText.addTextChangedListener(mChatEditTextWatcher);

    }

    public static Intent makeIntent (Context context){
        return new Intent(context, ChatActivity.class);
    }

}



