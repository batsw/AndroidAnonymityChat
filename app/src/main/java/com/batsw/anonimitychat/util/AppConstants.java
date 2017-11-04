package com.batsw.anonimitychat.util;

/**
 * Created by tudor on 5/15/2017.
 */

public interface AppConstants {
    //    Default My Profile value
    public static final String MY_DEFAULT_ADDRESS = "ConnectToNetwork";
    public static final String MY_DEFAULT_NAME = "AnonimousUser";
    public static final String MY_DEFAULT_NICKNAME = "AnonimousUser";

    public static final String CHAT_ITEM_PUT_EXTRA = "Chat_Item";
    public static final String CONTACT_ITEM_PUT_EXTRA = "Contact_Item";

    public static final int ADDRESS_SIZE = 16;

    public static final long NET_STAT_NOTIF_TRIG_INTERVAL_MILIS = 60000*5;

    public static final String NO_TOR_NET_CONN_MSG = "No TOR network \nconnection!";
    public static final String NO_INET_CONN_MSG = "No internet \nconnection!";

}
