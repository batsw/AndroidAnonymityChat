package com.batsw.anonimitychat.mainScreen.entities;

/**
 * Created by tudor on 4/7/2017.
 */

public class ContactEntity {
    private String mName;
    private long mSessionId;

    public ContactEntity(String mName, long mSessionId) {
        this.mName = mName;
        this.mSessionId = mSessionId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public long getSessionId() {
        return mSessionId;
    }

    public void setSessionId(long mSessionId) {
        this.mSessionId = mSessionId;
    }

    @Override
    public String toString() {
        return "ContactEntity{" +
                "mName='" + mName + '\'' +
                ", mSessionId=" + mSessionId +
                '}';
    }
}
