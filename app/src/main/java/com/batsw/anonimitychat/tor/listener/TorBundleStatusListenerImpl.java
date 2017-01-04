package com.batsw.anonimitychat.tor.listener;

/**
 * Created by tudor on 12/15/2016.
 */

public class TorBundleStatusListenerImpl implements ITorBundleStatusListener {

    private String mStatusMessage = "";

    @Override
    public void tellStatusMessage(String torLogMessage) {
        mStatusMessage = torLogMessage;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }
}
