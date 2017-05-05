package com.batsw.anonimitychat.persistence.operations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.batsw.anonimitychat.persistence.entities.DBChatMessageEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.IEntityDbOperations;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 5/4/2017.
 */

public class DbChatMessagesOperations implements IEntityDbOperations {
    private static final String LOG = DbChatMessagesOperations.class.getSimpleName();

    private SQLiteDatabase mSQLiteDatabase;

    public DbChatMessagesOperations(SQLiteDatabase sqLiteDatabase) {
        mSQLiteDatabase = sqLiteDatabase;
    }

    @Override
    public List<IDbEntity> getAllIDbEntity() {
        Log.i(LOG, "getAllIDbEntity -> ENTER");

        List<IDbEntity> retVal = new ArrayList<>();

        String selectQuery = "SELECT " + PersistenceConstants.COLUMN_ID + ", " +
                PersistenceConstants.COLUMN_CONTACT_SESSION_ID + ", " +
                PersistenceConstants.COLUMN_MESSAGE + ", " +
                PersistenceConstants.COLUMN_TIMESTAMP +
                " FROM " + PersistenceConstants.TABLE_CHATS_MESSAGES;

        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBChatMessageEntity messageEntity = new DBChatMessageEntity();
                messageEntity.setId(Integer.parseInt(cursor.getString(0)));
                messageEntity.setSessionId(Long.parseLong(cursor.getString(1)));
                messageEntity.setMessage(cursor.getString(2));
                messageEntity.setTimestamp(Long.parseLong(cursor.getString(3)));

                retVal.add(messageEntity);
            } while (cursor.moveToNext());
        }

        Log.i(LOG, "getAllIDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public IDbEntity getIDbEntityById(long sessionId) {
        Log.i(LOG, "getIDbEntityById -> LEAVE sessionId=" + sessionId);

        Cursor cursor = mSQLiteDatabase.query(PersistenceConstants.TABLE_CHATS_MESSAGES, new String[]{
                        PersistenceConstants.COLUMN_ID,
                        PersistenceConstants.COLUMN_MESSAGE,
                        PersistenceConstants.COLUMN_TIMESTAMP
                }, PersistenceConstants.COLUMN_SESSION_ID + " = ?",
                new String[]{String.valueOf(sessionId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBChatMessageEntity messageEntity = new DBChatMessageEntity();
        messageEntity.setId(Long.parseLong(cursor.getString(0)));
        messageEntity.setSessionId(sessionId);
        messageEntity.setMessage(cursor.getString(1));
        messageEntity.setTimestamp(Long.parseLong(cursor.getString(2)));

        Log.i(LOG, "getIDbEntityById -> LEAVE messageEntity=" + messageEntity);
        return messageEntity;
    }

    @Override
    public boolean addDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "addDbEntity -> ENTER dbEntity=" + dbEntity);

        boolean retVal = false;

        if (dbEntity instanceof DBChatMessageEntity) {

            DBChatMessageEntity messageEntity = (DBChatMessageEntity) dbEntity;

            ContentValues values = new ContentValues();
            values.put(PersistenceConstants.COLUMN_CONTACT_SESSION_ID, messageEntity.getSessionId());
            values.put(PersistenceConstants.COLUMN_MESSAGE, messageEntity.getMessage());
            values.put(PersistenceConstants.COLUMN_TIMESTAMP, messageEntity.getTimestamp());

            mSQLiteDatabase.insert(PersistenceConstants.TABLE_CHATS_MESSAGES, null, values);
            mSQLiteDatabase.close();

            retVal = true;
        }

        Log.i(LOG, "addDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    /**
     * Not used for TABLE_CHATS_MESSAGES
     *
     * @param dbEntity
     * @return
     */
    @Override
    public int updateDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "updateDbEntity -> ENTER dbEntity=" + dbEntity);
        int retVal = -1;
        Log.i(LOG, "Not implemented");
        Log.i(LOG, "updateDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }


    /**
     * TODO: see if needed ...
     * Not used separately ....
     *
     * @param dbEntity
     * @return
     */
    @Override
    public boolean deleteDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "deleteDbEntity -> ENTER dbEntity=" + dbEntity);

        boolean retVal = false;

//        if (dbEntity instanceof DBContactEntity) {
//
//            DBContactEntity contactEntity = (DBContactEntity) dbEntity;
//
//            mSQLiteDatabase.delete(PersistenceConstants.TABLE_CONTACTS, PersistenceConstants.COLUMN_ID + " = ? and" +
//                            PersistenceConstants.COLUMN_SESSION_ID + " = ? and" +
//                            PersistenceConstants.COLUMN_ADDRESS + " = ? ",
//                    new String[]{String.valueOf(contactEntity.getId()),
//                            String.valueOf(contactEntity.getSessionId()),
//                            String.valueOf(contactEntity.getAddress())
//                    });
//
//            mSQLiteDatabase.close();
//        }

        Log.i(LOG, "Not impleemnted yet");

        Log.i(LOG, "deleteDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
