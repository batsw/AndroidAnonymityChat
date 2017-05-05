package com.batsw.anonimitychat.persistence.entities;

import com.batsw.anonimitychat.persistence.util.IDbEntity;

/**
 * Created by tudor on 5/1/2017.
 */

public class DBChatEntity implements IDbEntity {
    private long mId;
    private long mSessionId;
    private long mHistoryCleanupTime;
    private String mChatName;

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

    public long getHistoryCleanupTime() {
        return mHistoryCleanupTime;
    }

    public void setHistoryCleanupTime(long historyCleanupTime) {
        this.mHistoryCleanupTime = historyCleanupTime;
    }

    public String getChatName() {
        return mChatName;
    }

    public void setChatName(String chatName) {
        this.mChatName = chatName;
    }

    @Override
    public String toString() {
        return "DBChatEntity{" +
                "mId=" + mId +
                ", mSessionId=" + mSessionId +
                ", mHistoryCleanupTime=" + mHistoryCleanupTime +
                ", mChatName='" + mChatName + '\'' +
                '}';
    }
}
