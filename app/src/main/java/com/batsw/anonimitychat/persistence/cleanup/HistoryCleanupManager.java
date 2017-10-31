package com.batsw.anonimitychat.persistence.cleanup;

import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.util.KeyValuePair;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tudor on 9/11/2017.
 */

public class HistoryCleanupManager {
    private static final String LOG = HistoryCleanupManager.class.getSimpleName();

    //    private ConcurrentHashMap<Long, HistoryCleanupJob> mJobQueue;
//    private ConcurrentHashMap<Long, HistoryCleanupJob> mJobQueue;

    private ConcurrentHashMap<Long, KeyValuePair<Long, Long>> mJobQueue;

    private ExecutorService mCleanupJobThread = null;
    private HistoryCleanupJob mHistoryCleanupJob = null;

    public HistoryCleanupManager() {

        mJobQueue = new ConcurrentHashMap<>();

        Log.i(LOG, "HistoryCleanupManager -> created");
    }

    public void init(AppController appController) {

        long timeStart = System.currentTimeMillis();

        List<IDbEntity> chatList = appController.getChatList();
        for (IDbEntity chatEntity : chatList) {
            DBChatEntity dbCE = (DBChatEntity) chatEntity;

            KeyValuePair<Long, Long> timeStartAndPeriod = new KeyValuePair<>(timeStart, dbCE.getHistoryCleanupTime());

            mJobQueue.put(dbCE.getSessionId(), timeStartAndPeriod);
            Log.i(LOG, "History cleanup job queue created");
        }

        mHistoryCleanupJob = new HistoryCleanupJob(mJobQueue);

        mCleanupJobThread = Executors.newSingleThreadScheduledExecutor();
        mCleanupJobThread.submit(mHistoryCleanupJob);

        Log.i(LOG, "mJobQueue -> created");
    }

    public void addHistoryCleanupJob(long sessionId, long cleanupTime) {
        Log.i(LOG, "addHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", cleanupTime=" + cleanupTime);

//        HistoryCleanupJob historyCleanupJob = new HistoryCleanupJob(sessionId, cleanupTime == 0 ? Long.MAX_VALUE : cleanupTime * 60 * 1000);

        mHistoryCleanupJob.addHistoryCleanupJob(sessionId, cleanupTime);

//        KeyValuePair<Long, Long> timeStartAndPeriod = new KeyValuePair<>(System.currentTimeMillis(),
//                cleanupTime == 0 ? Long.MAX_VALUE : cleanupTime * 60 * 1000);
//
//        mJobQueue.put(sessionId, timeStartAndPeriod);

        Log.i(LOG, "addHistoryCleanupJob -> LEAVE");
    }

    public void removeHistoryCleanupJob(long sessionId) {
        Log.i(LOG, "removeHistoryCleanupJob -> ENTER sessionId=" + sessionId);

//        mHandler.removeCallbacks(mJobQueue.get(sessionId));

        mHistoryCleanupJob.removeHistoryCleanupJob(sessionId);

//        mJobQueue.remove(sessionId);

        Log.i(LOG, "removeHistoryCleanupJob -> LEAVE");
    }

    public void updateHistoryCleanupJob(long sessionId, long newCleanupTime) {
        Log.i(LOG, "updateHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", cleanupTime=" + newCleanupTime);

        mHistoryCleanupJob.updateHistoryCleanupJob(sessionId, newCleanupTime);

//        mJobQueue.remove(sessionId);
//
//        KeyValuePair<Long, Long> timeStartAndPeriod = new KeyValuePair<>(System.currentTimeMillis(),
//                newCleanupTime == 0 ? Long.MAX_VALUE : newCleanupTime * 60 * 1000);
//        mJobQueue.put(sessionId, timeStartAndPeriod);

        Log.i(LOG, "updateHistoryCleanupJob -> LEAVE");
    }

    public void stopAllCleanupJobs() {
        Log.i(LOG, "stopAllCleanupJobs -> ENTER");

//        for (long sessionId : mJobQueue.keySet()) {
//            removeHistoryCleanupJob(sessionId);
//        }

        mHistoryCleanupJob = null;

        if (mCleanupJobThread != null) {
            mCleanupJobThread.shutdownNow();
        }
        mCleanupJobThread = null;

        Log.i(LOG, "stopAllCleanupJobs -> LEAVE");
    }
}
