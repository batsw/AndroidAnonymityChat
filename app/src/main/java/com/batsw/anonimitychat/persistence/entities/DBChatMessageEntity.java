package com.batsw.anonimitychat.persistence.entities;

import com.batsw.anonimitychat.persistence.util.IDbEntity;

/**
 * Created by tudor on 5/1/2017.
 */

public class DBChatMessageEntity implements IDbEntity {
    private long mId;
    private long mSessionId;
    private String mMessage;
    private long mTimestamp;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public long getSessionId() {
        return mSessionId;
    }

    public void setSessionId(long sessionId) {
        this.mSessionId = sessionId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    @Override
    public String toString() {
        return "DBChatMessageEntity{" +
                "mId=" + mId +
                ", mSessionId=" + mSessionId +
                ", mMessage='" + mMessage + '\'' +
                ", mTimestamp=" + mTimestamp +
                '}';
    }
}
