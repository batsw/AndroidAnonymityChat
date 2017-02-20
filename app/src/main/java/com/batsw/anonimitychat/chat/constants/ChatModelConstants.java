package com.batsw.anonimitychat.chat.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tudor on 10/15/2016.
 */

public interface ChatModelConstants {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    public static final String MESSAGE_EOL = System.getProperty("line.separator");

    public static final String CHAT_ACTIVITY_INTENT_EXTRA_KEY = "SESSION_ID";

    public static final long DEFAULT_SESSION_ID = 0L;

}
