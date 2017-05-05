package com.batsw.anonimitychat.persistence.entities;

import com.batsw.anonimitychat.persistence.util.IDbEntity;

/**
 * Created by tudor on 5/1/2017.
 */

public class DBMyProfileEntity implements IDbEntity {
    private long mId;
    private String mMyAddress;
    private String mMyName;
    private String mMyNickName;
    private String mMyEmail;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getMyAddress() {
        return mMyAddress;
    }

    public void setMyAddress(String myAddress) {
        this.mMyAddress = myAddress;
    }

    public String getMyName() {
        return mMyName;
    }

    public void setMyName(String myName) {
        this.mMyName = myName;
    }

    public String getMyNickName() {
        return mMyNickName;
    }

    public void setMyNickName(String myNickName) {
        this.mMyNickName = myNickName;
    }

    public String getMyEmail() {
        return mMyEmail;
    }

    public void setMyEmail(String myEmail) {
        this.mMyEmail = myEmail;
    }

    @Override
    public String toString() {
        return "DBMyProfileEntity{" +
                "mId=" + mId +
                ", mMyAddress='" + mMyAddress + '\'' +
                ", mMyName='" + mMyName + '\'' +
                ", mMyNickName='" + mMyNickName + '\'' +
                ", mMyEmail='" + mMyEmail + '\'' +
                '}';
    }
}
