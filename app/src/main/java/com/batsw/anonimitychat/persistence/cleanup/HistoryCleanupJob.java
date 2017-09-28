package com.batsw.anonimitychat.persistence.cleanup;

import android.os.Handler;
import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.util.KeyValuePair;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tudor on 9/7/2017.
 */

public class HistoryCleanupJob implements Runnable {

    private static final String LOG = HistoryCleanupJob.class.getSimpleName();

    private ConcurrentHashMap<Long, KeyValuePair<Long, Long>> mJobQueue;

    public HistoryCleanupJob(ConcurrentHashMap<Long, KeyValuePair<Long, Long>> jobQueue) {
        mJobQueue = jobQueue;
    }

    @Override
    public void run() {

        while (true) {
            try {
                Log.e(LOG, "HistoryCleanupJob: -> sleep : 50000");
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                Log.e(LOG, "HistoryCleanupJob: cleanup -> Thread sleep error: " + e.getMessage());
            }

            long nowTime = System.currentTimeMillis();

            for (long sessionId : mJobQueue.keySet()) {

                KeyValuePair<Long, Long> valuePair = mJobQueue.get(sessionId);

                long lastRun = valuePair.getK();
                long cleanupFrequency = valuePair.getV();

                Log.i(LOG, "lastRun + cleanupFrequency * 60 * 1000 <= nowTime :::" + lastRun +
                        " + " + lastRun + "__" + cleanupFrequency * 60 * 1000 + "___nowtime: " + nowTime);

                if (lastRun + cleanupFrequency * 60 * 1000 <= nowTime) {
                    AppController.getInstanceParameterized(null).removeMessagesForChat(sessionId);
                    Log.e(LOG, "HistoryCleanupJob: cleanup -> DONE for " + sessionId);

                    updateHistoryCleanupJob(sessionId, nowTime);
                }
            }
        }
    }

    public void addHistoryCleanupJob(long sessionId, long cleanupTime) {
        Log.i(LOG, "addHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", cleanupTime=" + cleanupTime);

        KeyValuePair<Long, Long> timeStartAndPeriod = new KeyValuePair<>(System.currentTimeMillis(), cleanupTime == 0 ? Long.MAX_VALUE : cleanupTime);
        mJobQueue.put(sessionId, timeStartAndPeriod);

        Log.i(LOG, "addHistoryCleanupJob -> LEAVE");
    }

    public void removeHistoryCleanupJob(long sessionId) {
        Log.i(LOG, "removeHistoryCleanupJob -> ENTER sessionId=" + sessionId);

        mJobQueue.remove(sessionId);

        Log.i(LOG, "removeHistoryCleanupJob -> LEAVE");
    }

    public void updateHistoryCleanupJob(long sessionId, long newCleanupTime) {
        Log.i(LOG, "updateHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", cleanupTime=" + newCleanupTime);

        KeyValuePair<Long, Long> keyValuePair = mJobQueue.get(sessionId);
        keyValuePair.setK(newCleanupTime == 0 ? Long.MAX_VALUE : newCleanupTime);

        mJobQueue.remove(sessionId);
        mJobQueue.put(sessionId, keyValuePair);

        Log.i(LOG, "updateHistoryCleanupJob -> LEAVE");
    }
}


