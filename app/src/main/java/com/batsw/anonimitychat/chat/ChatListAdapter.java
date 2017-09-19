package com.batsw.anonimitychat.chat;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.message.ChatMessage;
import com.batsw.anonimitychat.chat.message.ChatMessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 10/15/2016.
 */

public class ChatListAdapter extends BaseAdapter {

    private static final String LOG = ChatListAdapter.class.getSimpleName();

    private ArrayList<ChatMessage> mChatMessageList;
    private Context mContext;

    public ChatListAdapter(ArrayList<ChatMessage> chatMessageList, Context context) {
        mChatMessageList = chatMessageList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mChatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Log.i(LOG, "getView -> ENTER position=" + position + ", convertView=" + convertView + ", viewGroup=" + viewGroup);
        View retVal;
        ChatMessage message = mChatMessageList.get(position);

        if (message.getChatMessageType().equals(ChatMessageType.USER)) {
            retVal = displayMessage(convertView, message, R.layout.user_chat_item);
        } else {
            retVal = displayMessage(convertView, message, R.layout.partner_chat_item);
        }

        Log.i(LOG, "getView -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private View displayMessage(View convertView, ChatMessage message, int messageViewLayout) {
        Log.i(LOG, "getView -> ENTER convertView=" + convertView + ", message=" + message + ", messageViewLayout=" + messageViewLayout);
        View retVal;
        ViewHolder viewHolder;
        if (convertView == null) {
            retVal = LayoutInflater.from(mContext).inflate(messageViewLayout, null, false);

            viewHolder = new ViewHolder();
            viewHolder.messageTextView = (TextView) retVal.findViewById(R.id.textview_message);
            viewHolder.timeTextView = (TextView) retVal.findViewById(R.id.textview_time);

            retVal.setTag(viewHolder);
        } else {
            retVal = convertView;
            viewHolder = (ViewHolder) retVal.getTag();
        }

        //TODO: Html thing is for api24 amd greater ... reshape the ListView up
//        viewHolder.messageTextView.setText(Html.fromHtml(message.getMessage(), viewHolder.messageTextView.getPaintFlags()));
        viewHolder.messageTextView.setText(message.getMessage());

        viewHolder.timeTextView.setText(ChatModelConstants.SIMPLE_DATE_FORMAT.format(message.getTimeStamp()));

        Log.i(LOG, "getView -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public void addMessageToList(List<ChatMessage> chatMessagesList) {
        Log.i(LOG, "addMessageToList -> ENTER chatMessagesList=" + chatMessagesList);

        mChatMessageList.addAll(chatMessagesList);
        this.notifyDataSetChanged();
//        notifyItemRangeChanged(mContactsList.size() / 2, 1);
//        mContactsAdapter.notifyDataSetChanged();

        Log.i(LOG, "addMessageToList -> LEAVE");
    }

    public void removeMessageFromList(List<ChatMessage> chatMessagesList) {
        Log.i(LOG, "removeMessageFromList -> ENTER chatMessagesList=" + chatMessagesList);

        mChatMessageList.removeAll(chatMessagesList);
        this.notifyDataSetChanged();
//        notifyItemRangeChanged(mContactsList.size() / 2, 1);
//        mContactsAdapter.notifyDataSetChanged();

        Log.i(LOG, "removeMessageFromList -> LEAVE");
    }

    private class ViewHolder {
        public TextView messageTextView;
        public TextView timeTextView;
    }
}
