package com.batsw.anonimitychat.mainScreen.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;

import java.util.List;

/**
 * Created by tudor on 4/7/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactHolder> {

    private static final String LOG = ContactsAdapter.class.getSimpleName();

    private Activity mActivity;
    private List<ContactEntity> mContactsList;

    public ContactsAdapter(List<ContactEntity> contactsList, Activity activity) {

        mContactsList = contactsList;
        mActivity = activity;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(LOG, "onCreateViewHolder -> ENTER");
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_tab_item, parent, false);

        Log.i(LOG, "onCreateViewHolder -> LEAVE");
        return new ContactHolder(view, mContactsList);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        Log.i(LOG, "onBindViewHolder -> ENTER position=" + position);
        ContactEntity ce = mContactsList.get(position);

//        TextView textView = holder.getNameTextView();
//        textView.setText(ce.getName());

        holder.bindData(ce);
        Log.i(LOG, "onBindViewHolder -> LEAVE");
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }
}
