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
                long cleanupFrequency = valuePair.getV() == 0 ? Long.MAX_VALUE : valuePair.getV();

                Log.i(LOG, "lastRun + cleanupFrequency * 60 * 1000 <= nowTime :::" + lastRun +
                        " + " + lastRun + "__" + cleanupFrequency * 60 * 1000 + "___nowtime: " + nowTime);

                if ((cleanupFrequency != Long.MAX_VALUE) && (lastRun + cleanupFrequency * 60 * 1000 <= nowTime)) {
                    AppController.getInstanceParameterized(null).removeMessagesForChat(sessionId);
                    Log.e(LOG, "HistoryCleanupJob: cleanup -> DONE for " + sessionId);

                    updateHistoryCleanupJob(sessionId, valuePair.getV());
                }
            }
        }
    }

    public void addHistoryCleanupJob(long sessionId, long cleanupTimeFrequency) {
        Log.i(LOG, "addHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", cleanupTimeFrequency=" + cleanupTimeFrequency);

        KeyValuePair<Long, Long> timeStartAndPeriod = new KeyValuePair<>(System.currentTimeMillis(), cleanupTimeFrequency);
        mJobQueue.put(sessionId, timeStartAndPeriod);

        Log.i(LOG, "addHistoryCleanupJob -> LEAVE");
    }

    public void removeHistoryCleanupJob(long sessionId) {
        Log.i(LOG, "removeHistoryCleanupJob -> ENTER sessionId=" + sessionId);

        mJobQueue.remove(sessionId);

        Log.i(LOG, "removeHistoryCleanupJob -> LEAVE");
    }

    public void updateHistoryCleanupJob(long sessionId, long newCleanupTimeFrequency) {
        Log.i(LOG, "updateHistoryCleanupJob -> ENTER sessionId=" + sessionId + ", newCleanupTimeFrequency=" + newCleanupTimeFrequency);

        KeyValuePair<Long, Long> keyValuePair = mJobQueue.get(sessionId);
        keyValuePair.setK(System.currentTimeMillis());
        keyValuePair.setV(newCleanupTimeFrequency);

        mJobQueue.remove(sessionId);
        mJobQueue.put(sessionId, keyValuePair);

        Log.i(LOG, "updateHistoryCleanupJob -> LEAVE");
    }
}


