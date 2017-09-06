package com.batsw.anonimitychat.mainScreen.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;
import com.batsw.anonimitychat.mainScreen.settings.activities.ChatsDetailsActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabChats;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.util.AppConstants;

import java.util.List;

/**
 * Created by tudor on 4/12/2017.
 */

public class ChatsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String LOG = ChatsHolder.class.getSimpleName();

    private TabChats mTabChatsActivity;
    private ChatEntity mChatEntity;
    private ImageView mAvatarImageView, mAvailabilityImageView;
    private TextView mNameTextView;
    private List<ChatEntity> mChatEntitiesList;

    public ChatsHolder(View itemView, List<ChatEntity> chatsList, TabChats tabChatsActivity) {
        super(itemView);

        mTabChatsActivity = tabChatsActivity;
        mChatEntitiesList = chatsList;

        mAvatarImageView = (ImageView) itemView.findViewById(R.id.current_user_avatar);
//        mAvailabilityImageView = (ImageView) itemView.findViewById(R.id.current_user_availability);

        mNameTextView = (TextView) itemView.findViewById(R.id.current_chat_name);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "OnClickListener.onClick -> ENTER");
                int position = getLayoutPosition();
                ChatEntity chatEntity = mChatEntitiesList.get(position);

                if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STARTED)) {

                    DBContactEntity dbContactEntity = AppController.getInstanceParameterized(null).getContactEntity(chatEntity.getSessionId());
                    ChatController.getInstance().startChatActivity(AppController.getInstanceParameterized(null).getCurrentActivityContext(), dbContactEntity.getAddress());

                } else {
                    //TODO: show a message on screen
                    // or tell user to use Offline mode ....
                    Log.i(LOG, "cannot start chat when network not connected");
                }

                Log.i(LOG, "OnClickListener.onClick -> LEAVE chatEntity.contactName=" + chatEntity.getContactName());
            }

        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.i(LOG, "OnLongClickListener -> ENTER");

                int position = getLayoutPosition();
                ChatEntity chatEntity = mChatEntitiesList.get(position);

                Intent chatsDetailsActivityIntent = ChatsDetailsActivity.makeIntent(mTabChatsActivity.getActivity());
                chatsDetailsActivityIntent.putExtra(AppConstants.CHAT_ITEM_PUT_EXTRA, chatEntity.getSessionId());

                chatsDetailsActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mTabChatsActivity.getActivity().startActivity(chatsDetailsActivityIntent);

                Log.i(LOG, "OnLongClickListener -> LEAVE chatEntity.contactName=" + chatEntity.getContactName());
                return false;
            }
        });
    }

    public void bindData(ChatEntity ce, boolean isAvailable) {
        mChatEntity = ce;
        //TODO: to load User's custom image

//        if (isAvailable) {
//            mAvailabilityImageView.setImageResource(R.drawable.userstatus_online);
//        } else {
//            mAvailabilityImageView.setImageResource(R.drawable.userstatus_offline);
//        }

        mNameTextView.setText(ce.getContactName());
    }

    /**
     * Todo: This is not working as expected .... the above onClickListener is useful ....
     */
    @Override
    public void onClick(View view) {
        Log.i(LOG, "OnClickListener.onClick -> ENTER view=" + view);

        Log.i(LOG, "DO NOTHING yet !");

        Log.i(LOG, "OnClickListener.onClick -> LEAVE");
    }
}
