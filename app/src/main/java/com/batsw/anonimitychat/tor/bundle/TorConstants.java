package com.batsw.anonimitychat.tor.bundle;

/**
 * Created by tudor on 11/6/2016.
 */

public interface TorConstants {
    // TOR Expert Bundle resources
    public static final String FOLDER_NAME_FOR_RESOURCES_IN_ASSETS = "tor_bundle";
    public static final String INTERNAL_RESOURCE_FOLDER_NAME = "/torBundle";
    public static final String TOR_READY_STATUS_MESSAGE = "Bootstrapped 100%: Done";
    public static final String TOR_HIDDEN_SERVICE_NAME = "hostname";

    //Port related constants
    public static final int TOR_BUNDLE_EXTERNAL_PORT = 80;
    public static final int TOR_BUNDLE_INTERNAL_SOCKS_PORT = 11158;
    public static final int TOR_BUNDLE_INTERNAL_HIDDEN_SERVICES_PORT = 44444;

    //TOR Bundle Status constants
    public static final String TOR_BUNDLE_STOPPED = "TOR STOPPED";
    public static final String TOR_BUNDLE_IS_STARTING = "TOR STARTING";
    public static final String TOR_BUNDLE_STARTED = "TOR STARTED";
}