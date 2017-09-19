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
import com.batsw.anonimitychat.mainScreen.contactEditor.ContactEditorActivity;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;
import com.batsw.anonimitychat.mainScreen.settings.activities.ChatsDetailsActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.util.AppConstants;

import java.util.List;

/**
 * Created by tudor on 4/7/2017.
 */

public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String LOG = ContactHolder.class.getSimpleName();

    private TabContacts mTabContActivity;
    private ContactEntity mContactEntity;
    private ImageView mEditImageView, mAvailabilityImageView;
    private TextView mNameTextView;
    private List<ContactEntity> mContactEntitiesList;

    public ContactHolder(View itemView, List<ContactEntity> contactsList, TabContacts tcActivity) {
        super(itemView);

        mTabContActivity = tcActivity;

        mContactEntitiesList = contactsList;

        mAvailabilityImageView = (ImageView) itemView.findViewById(R.id.current_user_availability);

        mEditImageView = (ImageView) itemView.findViewById(R.id.current_user_edit);
        mNameTextView = (TextView) itemView.findViewById(R.id.contact_name);

//        Chats and Contacts tabs together
//         from click go directly to chatActivity with selected Contact
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG, "item.onClick -> ENTER");
                int position = getLayoutPosition();
                ContactEntity contactEntity = mContactEntitiesList.get(position);
                DBContactEntity dbContactEntity = AppController.getInstanceParameterized(null).getContactEntity(contactEntity.getSessionId());

//                final DBChatEntity chatEntity = AppController.getInstanceParameterized(null).getChatEntity(contactEntity.getSessionId());
//                if (chatEntity == null) {
//                    AppController.getInstanceParameterized(null).addNewChatForContact(dbContactEntity);
////                    AppController.getInstanceParameterized(null).moveToChatsTab();
//                }
//                } else {
//                    AppController.getInstanceParameterized(null).moveToChatsTab();
//                }

                Log.i(LOG, "Start chat activity");
//                if (AppController.getInstanceParameterized(null).getNetworkConnectionStatus().equals(TorConstants.TOR_BUNDLE_STARTED)) {

                    ChatController.getInstance().startChatActivity(AppController.getInstanceParameterized(null).getCurrentActivityContext(), dbContactEntity.getAddress());

//                } else {
//                    //TODO: show a message on screen
//                    // or tell user to use Offline mode ....
//                    Log.i(LOG, "cannot start chat when network not connected");
//                }

                Log.i(LOG, "item.onClick -> LEAVE contactEntity.name=" + contactEntity.getName());
            }
        });

//      Contact and Chat apart (there were 2 tabs one for chats and one for contacts)

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(LOG, "item.onClick -> ENTER");
//                int position = getLayoutPosition();
//                ContactEntity contactEntity = mContactEntitiesList.get(position);
//                DBContactEntity dbContactEntity = AppController.getInstanceParameterized(null).getContactEntity(contactEntity.getSessionId());
//
//                final DBChatEntity chatEntity = AppController.getInstanceParameterized(null).getChatEntity(contactEntity.getSessionId());
//                if (chatEntity == null) {
//                    AppController.getInstanceParameterized(null).addNewChatForContact(dbContactEntity);
//                    AppController.getInstanceParameterized(null).moveToChatsTab();
//                } else {
//                    AppController.getInstanceParameterized(null).moveToChatsTab();
//                }
//
//                Log.i(LOG, "item.onClick -> LEAVE contactEntity.name=" + contactEntity.getName());
//            }
//        });

        mEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(LOG, "mEditImageView.onClick -> ENTER");
                int position = getLayoutPosition();
                ContactEntity contactEntity = mContactEntitiesList.get(position);

                Intent editContactActivityIntent = ContactEditorActivity.makeIntent(mTabContActivity.getActivity());
                editContactActivityIntent.putExtra(AppConstants.CONTACT_ITEM_PUT_EXTRA, contactEntity.getSessionId());
                editContactActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mTabContActivity.getActivity().startActivity(editContactActivityIntent);
                Log.i(LOG, "mEditImageView.onClick -> LEAVE contactEntity.name=" + contactEntity.getName());
            }
        });
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

    public void bindData(ContactEntity ce) {
        mContactEntity = ce;
        //TODO: to add custom user image
//        mImageView.setImageResource(getR.id.ic_earth_white);
        mNameTextView.setText(ce.getName());
    }
}

