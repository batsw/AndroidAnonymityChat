package com.batsw.anonimitychat.persistence.operations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.IEntityDbOperations;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 5/4/2017.
 */

public class DbChatsOperations implements IEntityDbOperations {
    private static final String LOG = DbChatsOperations.class.getSimpleName();

    private SQLiteDatabase mSQLiteDatabase;

    public DbChatsOperations(SQLiteDatabase sqLiteDatabase) {
        mSQLiteDatabase = sqLiteDatabase;
    }

    @Override
    public List<IDbEntity> getAllIDbEntity() {
        Log.i(LOG, "getAllIDbEntity -> ENTER");

        List<IDbEntity> retVal = new ArrayList<>();

        String selectQuery = "SELECT " + PersistenceConstants.COLUMN_ID + ", " +
                PersistenceConstants.COLUMN_CONTACT_SESSION_ID + ", " +
                PersistenceConstants.COLUMN_CHAT_NAME + ", " +
                PersistenceConstants.COLUMN_HISTORY_CLEANUP_TIME +
                " FROM " + PersistenceConstants.TABLE_CHATS;

//        if (!mSQLiteDatabase.isOpen()) {
//            AppController.getInstanceParameterized(null).openDB(mSQLiteDatabase);
//        }

        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    DBChatEntity contact = new DBChatEntity();
                    contact.setId(Long.parseLong(cursor.getString(0)));
                    contact.setSessionId(Long.parseLong(cursor.getString(1)));
                    contact.setChatName(cursor.getString(2));
                    contact.setHistoryCleanupTime(Long.parseLong(cursor.getString(3)));

                    retVal.add(contact);
                } while (cursor.moveToNext());
            }
        }

        Log.i(LOG, "getAllIDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public IDbEntity getIDbEntityById(long sessionId) {
        Log.i(LOG, "getIDbEntityById -> LEAVE sessionId=" + sessionId);

        DBChatEntity retVal = null;

        Cursor cursor = mSQLiteDatabase.query(PersistenceConstants.TABLE_CHATS, new String[]{
                        PersistenceConstants.COLUMN_ID,
                        PersistenceConstants.COLUMN_CHAT_NAME,
                        PersistenceConstants.COLUMN_HISTORY_CLEANUP_TIME
                }, PersistenceConstants.COLUMN_CONTACT_SESSION_ID + "= ?",
                new String[]{String.valueOf(sessionId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            retVal = new DBChatEntity();
            retVal.setId(Long.parseLong(cursor.getString(0)));
            retVal.setSessionId(sessionId);
            retVal.setChatName(cursor.getString(1));
            retVal.setHistoryCleanupTime(Long.parseLong(cursor.getString(2)));
        }

        Log.i(LOG, "getIDbEntityById -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public boolean addDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "addDbEntity -> ENTER dbEntity=" + dbEntity);

        boolean retVal = false;

        if (dbEntity instanceof DBChatEntity) {

            DBChatEntity dbChatEntity = (DBChatEntity) dbEntity;

            ContentValues values = new ContentValues();
            values.put(PersistenceConstants.COLUMN_CHAT_NAME, dbChatEntity.getChatName());
            values.put(PersistenceConstants.COLUMN_HISTORY_CLEANUP_TIME, dbChatEntity.getHistoryCleanupTime());
            values.put(PersistenceConstants.COLUMN_CONTACT_SESSION_ID, dbChatEntity.getSessionId());

            mSQLiteDatabase.insert(PersistenceConstants.TABLE_CHATS, null, values);
//            mSQLiteDatabase.close();

            retVal = true;
        }

        Log.i(LOG, "addDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public int updateDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "updateMyContact -> ENTER myProfile=" + dbEntity);
        int retVal = -1;

        if (dbEntity instanceof DBChatEntity) {

            DBChatEntity chatEntity = (DBChatEntity) dbEntity;

            if (chatEntity.getChatName() != null && !chatEntity.getChatName().isEmpty()) {

                ContentValues values = new ContentValues();
                values.put(PersistenceConstants.COLUMN_CHAT_NAME, chatEntity.getChatName());
                values.put(PersistenceConstants.COLUMN_HISTORY_CLEANUP_TIME, chatEntity.getHistoryCleanupTime());

                retVal = mSQLiteDatabase.update(PersistenceConstants.TABLE_CHATS, values, PersistenceConstants.COLUMN_ID + " = ? and " +
                                PersistenceConstants.COLUMN_CONTACT_SESSION_ID + " = ?",
                        new String[]{String.valueOf(chatEntity.getId()),
                                String.valueOf(chatEntity.getSessionId())
                        });
            } else {
                retVal = -1;
            }

        } else {
            retVal = -1;
        }

        Log.i(LOG, "updateMyContact -> LEAVE retVal=" + retVal);
        return retVal;
    }


    /**
     * TODO: clean direct chats and message history
     * If this is used, means that the chat's message history must be deleted too
     *
     * @param dbEntity
     * @return
     */
    @Override
    public boolean deleteDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "deleteDbEntity -> ENTER dbEntity=" + dbEntity);

        boolean retVal = false;

        if (dbEntity instanceof DBChatEntity) {

            DBChatEntity chatEntity = (DBChatEntity) dbEntity;

            mSQLiteDatabase.delete(PersistenceConstants.TABLE_CHATS, PersistenceConstants.COLUMN_CONTACT_SESSION_ID,
                    new String[]{String.valueOf(chatEntity.getSessionId())
                    });

//            mSQLiteDatabase.close();
        }

        Log.i(LOG, "deleteDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
