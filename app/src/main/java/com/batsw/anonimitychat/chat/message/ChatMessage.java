package com.batsw.anonimitychat.chat.message;

/**
 * Created by tudor on 10/15/2016.
 */

public class ChatMessage {
    private String mMessage;
    private ChatMessageType mChatMessageType;
    private long mTimeStamp;

    public ChatMessage(String message, ChatMessageType messageType, long timeStamp) {
        mMessage = message;
        mChatMessageType = messageType;
        mTimeStamp = timeStamp;
    }

    public String getMessage() {
        return mMessage;
    }

    public ChatMessageType getChatMessageType() {
        return mChatMessageType;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }
}
