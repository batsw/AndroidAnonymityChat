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
import com.batsw.anonimitychat.mainScreen.adapters.ContactsAdapter;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tudor on 3/29/2017.
 */

public class TabContacts extends Fragment {

    private static final String LOG = TabContacts.class.getSimpleName();

    private List<ContactEntity> mContactsTestList;
    private String[] mContactsTestNamesList = {"Bob", "Snack", "Jessie", "John", "Doe", "Drill", "Bet"};

    private LinearLayoutManager mLinearLayoutManager;

    private ContactsAdapter mContactsAdapter;
    private RecyclerView mContactsRecyclerView;

    private static int DEFAULT_CONTACT_IMAGE;
//    private int[] image = {R.drawable.....};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onCreate -> ENTER");

        mContactsTestList = new ArrayList<>();
        for (int i = 0; i < mContactsTestNamesList.length; i++) {
            ContactEntity contactEntity = new ContactEntity(mContactsTestNamesList[i], UUID.randomUUID().getMostSignificantBits());
            mContactsTestList.add(contactEntity);
        }

        Log.i(LOG, "onCreate -> LEAVE");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onCreateView -> ENTER");
        View rootView = inflater.inflate(R.layout.contacts_tab, container, false);

        Log.i(LOG, "onCreateView -> LEAVE");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onViewCreated -> ENTER");
        super.onViewCreated(view, savedInstanceState);

        mContactsRecyclerView = (RecyclerView) view.findViewById(R.id.contacts_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsRecyclerView.setLayoutManager(linearLayoutManager);

        mContactsAdapter = new ContactsAdapter(mContactsTestList);
        mContactsRecyclerView.setAdapter(mContactsAdapter);

        Log.i(LOG, "onViewCreated -> LEAVE");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(LOG, "onActivityCreated -> ENTER");
        super.onActivityCreated(savedInstanceState);

//        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        mContactsAdapter = new ContactsAdapter(mContactsTestList, getActivity());
//        mContactsRecyclerView.setAdapter(mContactsAdapter);

        Log.i(LOG, "onActivityCreated -> LEAVE");

//        mContactsRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });

    }

}
