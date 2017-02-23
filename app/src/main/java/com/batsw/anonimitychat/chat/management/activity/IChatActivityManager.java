package com.batsw.anonimitychat.chat.management.activity;

import com.batsw.anonimitychat.chat.ChatActivity;
import com.batsw.anonimitychat.chat.management.ChatDetail;

/**
 * Created by tudor on 2/10/2017.
 */

public interface IChatActivityManager extends IViewModel {

    /**
     * Chat related methods
     */


//public void manageMessages();

    public void sendMessage(String message);

    public void loadHistory();

    public void setChatActivity(ChatActivity chatActivity);

    public void configureChatDetail(long sessionId);
}
