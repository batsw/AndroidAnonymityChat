package com.batsw.anonimitychat.chat.listener;

import com.batsw.anonimitychat.tor.listener.ITorBundleStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 2/5/2017.
 */

public class IncomingConnectionListenerManager {
    private List<IIncomingConnectionListener> mIncomingConnectionListenerList = null;

    public IncomingConnectionListenerManager() {
        mIncomingConnectionListenerList = new ArrayList<>();
    }

    public void triggerPartnerChatRequest(String partnerHostname) {
        for (IIncomingConnectionListener torStatusListener : mIncomingConnectionListenerList) {

            torStatusListener.triggerIncomingPartnerConnectionEvent(partnerHostname);

        }
    }

    public void addIncomingConnectionListener(IIncomingConnectionListener iIncomingConnectionListener) {
        mIncomingConnectionListenerList.add(iIncomingConnectionListener);
    }

    public void removeIncomingConnectionListener(ITorBundleStatusListener iTorBundleStatusListener) {
        if (mIncomingConnectionListenerList.contains(iTorBundleStatusListener)) {
            mIncomingConnectionListenerList.remove(iTorBundleStatusListener);
        }
    }
}
