package com.batsw.anonimitychat.mainScreen.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.adapters.ChatsAdapter;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tudor on 3/29/2017.
 */

public class TabChats extends Fragment {
    private static final String LOG = TabChats.class.getSimpleName();

    private List<ChatEntity> mChatsTestList;
    private String[] mContactsTestNamesList = {"Bob", "Snack", "Jessie", "John", "Doe", "Drill", "Bet"};

    private ChatsAdapter mChatsAdapter;
    private RecyclerView mChatsRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");

        mChatsTestList = new ArrayList<>();
        for (int i = 0; i < mContactsTestNamesList.length; i++) {
            ChatEntity contactEntity = new ChatEntity(UUID.randomUUID().getMostSignificantBits(), mContactsTestNamesList[i], false);
            mChatsTestList.add(contactEntity);
        }

        Log.i(LOG, "onCreate -> LEAVE");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onCreateView -> ENTER");
        View rootView = inflater.inflate(R.layout.chats_tab, container, false);

        Log.i(LOG, "onCreateView -> LEAVE");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onViewCreated -> ENTER");
        super.onViewCreated(view, savedInstanceState);

        mChatsRecyclerView = (RecyclerView) view.findViewById(R.id.chats_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mChatsRecyclerView.setLayoutManager(linearLayoutManager);

        mChatsAdapter = new ChatsAdapter(mChatsTestList, this);
        mChatsRecyclerView.setAdapter(mChatsAdapter);

        Log.i(LOG, "onViewCreated -> LEAVE");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onActivityCreated -> ENTER");
        super.onActivityCreated(savedInstanceState);

        Log.i(LOG, "onActivityCreated -> LEAVE");
    }
}
