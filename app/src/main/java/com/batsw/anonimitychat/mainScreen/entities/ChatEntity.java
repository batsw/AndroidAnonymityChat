package com.batsw.anonimitychat.mainScreen.entities;

/**
 * Created by tudor on 4/9/2017.
 */

public class ChatEntity {
    private long mSessionId;
    private String mContactName;
    // online/offline property
    private boolean mIsAvailable;

    //Todo: add more descriptive properties
    // history setting
    // last received message

    // contact image


    public ChatEntity(long sessionId, String contactName, boolean isAvailable) {
        this.mSessionId = sessionId;
        this.mContactName = contactName;
        this.mIsAvailable = isAvailable;
    }

    public long getSessionId() {
        return mSessionId;
    }

    public void setSessionId(long mSessionId) {
        this.mSessionId = mSessionId;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public boolean isAvailable() {
        return mIsAvailable;
    }

    public void setIsAvailable(boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
    }

    @Override
    public String toString() {
        return "ChatEntity{" +
                "mSessionId=" + mSessionId +
                ", mContactName='" + mContactName + '\'' +
                ", mIsAvailable=" + mIsAvailable +
                '}';
    }
}
