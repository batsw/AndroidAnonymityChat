package com.batsw.anonimitychat.chat.listener;

import android.app.Activity;

import com.batsw.anonimitychat.MainActivity;
import com.batsw.anonimitychat.chat.ChatActivity;

/**
 * Created by tudor on 2/5/2017.
 */

public class IncomingConnectionListenerImpl implements IIncomingConnectionListener{

    private Activity mActivity;

    public IncomingConnectionListenerImpl (Activity activity){
        mActivity = activity;
    }

    @Override
    public void startNewCatActivity() {
        mActivity.startActivity(ChatActivity.makeIntent(mActivity.getApplicationContext()));
    }
}
