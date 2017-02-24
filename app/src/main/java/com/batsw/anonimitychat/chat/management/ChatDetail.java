package com.batsw.anonimitychat.chat.management;

import com.batsw.anonimitychat.chat.util.ConnectionType;
import com.batsw.anonimitychat.tor.connections.ITorConnection;

/**
 * Created by tudor on 2/13/2017.
 */

public class ChatDetail {

    private String mPartnerAddress;
    private String mPartnerName;
    private ITorConnection mTorConnection;
    private ConnectionType mConnectionType;
    private long mSessionId;
    private boolean mIsAlive;

    public ChatDetail(String partnerAddress, String partnerName, ITorConnection torConnection, ConnectionType connectionType, long sessionId, boolean isAlive) {
        mPartnerAddress = partnerAddress;
        mPartnerName = partnerName;
        mTorConnection = torConnection;
        mConnectionType = connectionType;
        mSessionId = sessionId;
        mIsAlive = isAlive;
    }

    public boolean isAlive() {
        return mIsAlive;
    }

    public ITorConnection getTorConnection() {
        return mTorConnection;
    }

    public String getPartnerAddress() {
        return mPartnerAddress;
    }

    public void setPartnerAddress(String mPartnerAddress) {
        this.mPartnerAddress = mPartnerAddress;
    }

    public void setTorConnection(ITorConnection mTorConnection) {
        this.mTorConnection = mTorConnection;
    }

    public void setIsAlive(boolean mIsAlive) {
        this.mIsAlive = mIsAlive;
    }

    public long getSessionId() {
        return mSessionId;
    }

    public void setSessionId(long sessionId) {
        this.mSessionId = sessionId;
    }

    public String getPartnerName() {
        return mPartnerName;
    }

    public void setPartnerName(String partnerName) {
        this.mPartnerName = partnerName;
    }

    public ConnectionType getConnectionType() {
        return mConnectionType;
    }

    public void setmConnectionType(ConnectionType mConnectionType) {
        this.mConnectionType = mConnectionType;
    }

    @Override
    public String toString() {
        return "ChatDetail{" +
                "mPartnerAddress='" + mPartnerAddress + '\'' +
                ", mPartnerName='" + mPartnerName + '\'' +
                ", mTorConnection=" + mTorConnection +
                ", mConnectionType=" + mConnectionType +
                ", mSessionId=" + mSessionId +
                ", mIsAlive=" + mIsAlive +
                '}';
    }
}
