package com.batsw.anonimitychat.persistence.entities;

import com.batsw.anonimitychat.persistence.util.IDbEntity;

/**
 * Created by tudor on 5/1/2017.
 */

public class DBContactEntity implements IDbEntity {
    private long mId;
    private long mSessionId;
    private String mAddress;
    private String mName;
    private String mNickName;
    private String mEmail;

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

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        this.mNickName = nickName;
    }

    @Override
    public String toString() {
        return "DBContactEntity{" +
                "mId=" + mId +
                ", mSessionId=" + mSessionId +
                ", mAddress='" + mAddress + '\'' +
                ", mName='" + mName + '\'' +
                ", mNickName='" + mNickName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                '}';
    }
}
