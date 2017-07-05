package com.batsw.anonimitychat.persistence.util;

/**
 * Created by tudor on 4/30/2017.
 */

public interface PersistenceConstants {

    public static final String DATABASE_ANONYMITY_CHAT = "anonymity_chat.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_CONTACTS = "contacts";
    public static final String TABLE_MY_PROFILE = "my_profile";
    public static final String TABLE_CHATS = "chats";
    public static final String TABLE_CHATS_MESSAGES = "chats_messages";

    //    contacts table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_SESSION_ID = "session_id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_EMAIL = "email";

    //    chats table
    public static final String COLUMN_CHAT_NAME = "chat_name";
    public static final String COLUMN_HISTORY_CLEANUP_TIME = "history_cleanup_time";
    public static final String COLUMN_CONTACT_SESSION_ID = "contact_session_id";

    //    chats_messages
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //  my profile table
    public static final String COLUMN_MY_ADDRESS = "my_address";
    public static final String COLUMN_MY_NAME = "my_name";
    public static final String COLUMN_MY_NICKNAME = "my_nickname";
    public static final String COLUMN_MY_EMAIL = "my_email";
    public static final String COLUMN_TBE_PID = "bundle_pid";
    public static final String COLUMN_TBE_PROCESS = "bundle_process";

}
