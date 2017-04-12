package com.batsw.anonimitychat.mainScreen.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;

import java.util.List;

/**
 * Created by tudor on 4/12/2017.
 */

public class ChatsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String LOG = ChatsHolder.class.getSimpleName();

    private ChatEntity mChatEntity;
    private ImageView mAvatarImageView, mAvailabilityImageView;
    private TextView mNameTextView;
    private List<ChatEntity> mChatEntitiesList;

    public ChatsHolder(View itemView, List<ChatEntity> chatsList) {
        super(itemView);

        mChatEntitiesList = chatsList;

        mAvatarImageView = (ImageView) itemView.findViewById(R.id.current_user_avatar);
        mAvailabilityImageView = (ImageView) itemView.findViewById(R.id.current_user_availability);

        mNameTextView = (TextView) itemView.findViewById(R.id.current_user_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "OnClickListener.onClick -> ENTER");
                int position = getLayoutPosition();
                //TODO: go to user's ChatList
                ChatEntity chatEntity = mChatEntitiesList.get(position);
                //From hete go to ChatList

                //todo log the clicked item
                Log.i(LOG, "OnClickListener.onClick -> LEAVE chatEntity.contactName=" + chatEntity.getContactName());
            }

        });
    }

    public void bindData(ChatEntity ce, boolean isAvailable) {
        mChatEntity = ce;
        //TODO 2: to load User's custom image
        //TODO 1: to load the proper availability bubble
        //TODO 3: to load the saved avatar

        if (isAvailable) {
            mAvailabilityImageView.setImageResource(R.drawable.userstatus_online);
        } else {
            mAvailabilityImageView.setImageResource(R.drawable.userstatus_offline);
        }

        mNameTextView.setText(ce.getContactName());
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG, "OnClickListener.onClick -> ENTER view=" + view);

        Log.i(LOG, "DO NOTHING yet !");

        Log.i(LOG, "OnClickListener.onClick -> LEAVE");
    }
}
