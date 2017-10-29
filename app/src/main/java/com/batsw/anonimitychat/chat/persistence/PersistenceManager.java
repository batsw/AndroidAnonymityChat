package com.batsw.anonimitychat.chat.persistence;

import android.content.Context;
import android.util.Log;

import com.batsw.anonimitychat.chat.management.ChatDetail;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tudor on 2/20/2017.
 */

public class PersistenceManager {

    private static final String PERSISTENCE_MANAGER_LOG = PersistenceManager.class.getSimpleName();

    private ConcurrentLinkedQueue<ChatDetail> mPartnerList;

    public PersistenceManager() {
        init();
    }

    //TODO I must load the Partner list from DB
    private void init() {
        mPartnerList = new ConcurrentLinkedQueue();
    }

    public ConcurrentLinkedQueue<ChatDetail> getPartnerList() {
        return mPartnerList;
    }

    public boolean isPartnerInTheList(String partnerAddress) {
        Log.i(PERSISTENCE_MANAGER_LOG, "isPartnerInTheList -> ENTER partnerAddress=" + partnerAddress);

        boolean retVal = false;

        Iterator<ChatDetail> iterator = mPartnerList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getPartnerAddress() == partnerAddress) {
                retVal = true;
                break;
            }
        }

        Log.i(PERSISTENCE_MANAGER_LOG, "isPartnerInTheList -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public boolean isPartnerInTheList(long sessionId) {
        Log.i(PERSISTENCE_MANAGER_LOG, "isPartnerInTheList -> ENTER sessionId=" + sessionId);

        boolean retVal = false;

        Iterator<ChatDetail> iterator = mPartnerList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getSessionId() == sessionId) {
                retVal = true;
                break;
            }
        }

        Log.i(PERSISTENCE_MANAGER_LOG, "isPartnerInTheList -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public ChatDetail getPartnerDetail(long sessionId) {
        Log.i(PERSISTENCE_MANAGER_LOG, "getPartnerDetail -> ENTER sessionId=" + sessionId);

        ChatDetail retVal = null;

        Iterator<ChatDetail> iterator = mPartnerList.iterator();
        while (iterator.hasNext()) {
            ChatDetail chatDetail = iterator.next();
            if (chatDetail.getSessionId() == sessionId) {
                retVal = chatDetail;
                break;
            }
        }

        Log.i(PERSISTENCE_MANAGER_LOG, "getPartnerDetail -> LEAVE retVal=" + retVal.toString());
        return retVal;
    }

    public ChatDetail getPartnerDetail(String partnerHostname) {
        Log.i(PERSISTENCE_MANAGER_LOG, "getPartnerDetail -> ENTER partnerHostname=" + partnerHostname);

        ChatDetail retVal = null;

        Iterator<ChatDetail> iterator = mPartnerList.iterator();
        while (iterator.hasNext()) {
            ChatDetail chatDetail = iterator.next();
            if (chatDetail.getPartnerAddress().equals(partnerHostname)) {
                retVal = chatDetail;
                break;
            }
        }

        Log.i(PERSISTENCE_MANAGER_LOG, "getPartnerDetail -> LEAVE retVal=" + retVal.toString());
        return retVal;
    }

    public void addPartnerToList(ChatDetail chatDetail) {
        Log.i(PERSISTENCE_MANAGER_LOG, "addPartnerToList -> ENTER chatDetail=" + chatDetail);

        mPartnerList.add(chatDetail);

        Log.i(PERSISTENCE_MANAGER_LOG, "addPartnerToList -> LEAVE");
    }

    public void deletePartnerFromList() {

    }

    public void updatePartnerDetails(ChatDetail chatDetail) {
        Log.i(PERSISTENCE_MANAGER_LOG, "updatePartnerDetails -> ENTER chatDetail=" + chatDetail);

        ChatDetail found = null;
        for (ChatDetail cd : mPartnerList) {
            if (chatDetail.getSessionId() == cd.getSessionId()) {
                found = cd;
                break;
            }
        }

        mPartnerList.remove(found);
        mPartnerList.add(chatDetail);

        Log.i(PERSISTENCE_MANAGER_LOG, "updatePartnerDetails -> LEAVE");
    }
}
