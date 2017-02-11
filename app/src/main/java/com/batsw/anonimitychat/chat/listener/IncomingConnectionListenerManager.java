package com.batsw.anonimitychat.chat.listener;

import com.batsw.anonimitychat.tor.listener.ITorBundleStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 2/5/2017.
 */

public class IncomingConnectionListenerManager {
    private List<IIncomingConnectionListener> mIncomingConnectionListenerList = null;

    private int mPartnersIndex = 0;

    public IncomingConnectionListenerManager(int partnersIndex) {
        mIncomingConnectionListenerList = new ArrayList<>();
        mPartnersIndex = partnersIndex;
    }

    public void trigger() {

// first increase the index then
// add the connection to map

        /// must change the map to accept the Sockets/Connections with a tag PARTNER/USER type

        for (IIncomingConnectionListener torStatusListener : mIncomingConnectionListenerList) {


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
