package com.batsw.anonimitychat.tor.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 12/15/2016.
 */

public class TorBundleListenerManager {

    private List<ITorBundleStatusListener> mTorListenersList = null;

    public TorBundleListenerManager() {
        mTorListenersList = new ArrayList<>();
    }

    public void addTorBundleListener(ITorBundleStatusListener iTorBundleStatusListener) {
        mTorListenersList.add(iTorBundleStatusListener);
    }

    public void statusMessageReceived(String torLogMessage) {
        for (ITorBundleStatusListener torStatusListener : mTorListenersList) {
            torStatusListener.tellStatusMessage(torLogMessage);
        }
    }
}
