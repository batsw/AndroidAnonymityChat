package com.batsw.anonimitychat.mainScreen.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;

import java.util.List;

/**
 * Created by tudor on 4/7/2017.
 */

public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String LOG = ContactHolder.class.getSimpleName();

    private ContactEntity mContactEntity;
    private ImageView mImageView;
    private TextView mNameTextView;
    private Activity mActivity;
    private List<ContactEntity> mContactEntitiesList;

    public ContactHolder(View itemView, List<ContactEntity> contactsList) {
        super(itemView);

        mContactEntitiesList = contactsList;

        mImageView = (ImageView) itemView.findViewById(R.id.current_user_avatar);
        mNameTextView = (TextView) itemView.findViewById(R.id.current_user_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "OnClickListener.onClick -> ENTER");
                int position = getLayoutPosition();
                //TODO: go to user's ChatList
                ContactEntity contactEntity = mContactEntitiesList.get(position);
                //From hete go to ChatList

                //todo log the clicked item
                Log.i(LOG, "OnClickListener.onClick -> LEAVE contactEntity.name=" + contactEntity.getName());
            }

        });
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG, "OnClickListener.onClick -> ENTER view=" + view);

        Log.i(LOG, "DO NOTHING yet !");

        Log.i(LOG, "OnClickListener.onClick -> LEAVE");
    }

    public void bindData(ContactEntity ce) {
        mContactEntity = ce;
        //TODO: to add custom user image
//        mImageView.setImageResource(getR.id.ic_earth_white);
        mNameTextView.setText(ce.getName());
    }

    public TextView getNameTextView() {
        return mNameTextView;
    }
}

