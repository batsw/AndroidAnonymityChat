package com.batsw.anonimitychat.common;

import android.util.Log;

import java.io.File;

/**
 * Created by tudor on 12/26/2016.
 */

public class NetworkingPermissions {

    private static final String LOG = "NetworkingPermissions";

    private String ruleArguments1 = " -I OUTPUT -p tcp -d 127.0.0.1 --dport";
    private String ruleArguments2 = " -j ACCEPT";

    private int mPort;

    public NetworkingPermissions(int port) {
        mPort = port;
    }

    public String ipTableRuleMaker() {
        String ipTables = checkIpTablsAvailability();

        String ruleCommand = ipTables + ruleArguments1 + " " + mPort + ruleArguments2;

        return ruleCommand;
    }

    private String checkIpTablsAvailability() {
        String mSysIptables = "";

//        if (mSysIptables != null) {
//            return mSysIptables;
//        } else {

        //if the user wants us to use the built-in iptables, then we have to find it
        File fileIpt = new File("/system/xbin/iptables");

        if (fileIpt.exists())
            mSysIptables = fileIpt.getAbsolutePath();
        else {

            fileIpt = new File("/system/bin/iptables");

            if (fileIpt.exists())
                mSysIptables = fileIpt.getAbsolutePath();
        }
//        }
        Log.i(LOG, "checkIpTablsAvailability: ipTables location__" + mSysIptables);

        return mSysIptables;
    }
}
