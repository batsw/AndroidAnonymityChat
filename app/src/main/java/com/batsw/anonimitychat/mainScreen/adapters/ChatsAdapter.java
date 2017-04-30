package com.batsw.anonimitychat.mainScreen.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;
import com.batsw.anonimitychat.mainScreen.tabs.TabChats;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;

import java.util.List;

/**
 * Created by tudor on 4/12/2017.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ChatsHolder> {

    private static final String LOG = ChatsAdapter.class.getSimpleName();

    private TabChats mTabChatsActivity;
    private List<ChatEntity> mChatsList;

    public ChatsAdapter(List<ChatEntity> chatsList, TabChats tcActivity) {

        mTabChatsActivity = tcActivity;
        mChatsList = chatsList;
    }

    @Override
    public ChatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG, "onCreateViewHolder -> ENTER");
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.chats_tab_item, parent, false);

        Log.i(LOG, "onCreateViewHolder -> LEAVE");
        return new ChatsHolder(view, mChatsList, mTabChatsActivity);
    }

    @Override
    public void onBindViewHolder(ChatsHolder holder, int position) {
        Log.i(LOG, "onBindViewHolder -> ENTER position=" + position);
        ChatEntity ce = mChatsList.get(position);

        //TODO: bind the Contact availabiliy status HERE ...
        // availability logic

        //TODO: change to production after testing
//        ce.isAvailable()
        if (position / 2 == 0) {
            holder.bindData(ce, true);
        } else {
            holder.bindData(ce, false);
        }

        Log.i(LOG, "onBindViewHolder -> LEAVE");
    }

    @Override
    public int getItemCount() {
        return mChatsList.size();
    }
}
