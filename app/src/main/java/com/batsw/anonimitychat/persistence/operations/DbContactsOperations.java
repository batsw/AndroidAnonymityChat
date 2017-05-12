package com.batsw.anonimitychat.persistence.operations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.IEntityDbOperations;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 5/4/2017.
 */

public class DbContactsOperations implements IEntityDbOperations {
    private static final String LOG = DbContactsOperations.class.getSimpleName();

    private SQLiteDatabase mSQLiteDatabase;

    public DbContactsOperations(SQLiteDatabase sqLiteDatabase) {
        mSQLiteDatabase = sqLiteDatabase;
    }

    @Override
    public List<IDbEntity> getAllIDbEntity() {
        Log.i(LOG, "getAllIDbEntity -> ENTER");

        List<IDbEntity> retVal = new ArrayList<>();

        String selectQuery = "SELECT " + PersistenceConstants.COLUMN_ID + ", " +
                PersistenceConstants.COLUMN_SESSION_ID + ", " +
                PersistenceConstants.COLUMN_ADDRESS + ", " +
                PersistenceConstants.COLUMN_NAME + ", " +
                PersistenceConstants.COLUMN_NICKNAME + ", " +
                PersistenceConstants.COLUMN_EMAIL +
                " FROM " + PersistenceConstants.TABLE_CONTACTS;

        Cursor cursor = mSQLiteDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DBContactEntity contact = new DBContactEntity();
                contact.setId(Long.parseLong(cursor.getString(0)));
                contact.setSessionId(Long.parseLong(cursor.getString(1)));
                contact.setAddress(cursor.getString(2));
                contact.setName(cursor.getString(3));
                contact.setNickName(cursor.getString(4));
                contact.setEmail(cursor.getString(5));

                retVal.add(contact);
            } while (cursor.moveToNext());
        }

        Log.i(LOG, "getAllIDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public IDbEntity getIDbEntityById(long sessionId) {
        Log.i(LOG, "getIDbEntityById -> LEAVE sessionId=" + sessionId);

        Cursor cursor = mSQLiteDatabase.query(PersistenceConstants.TABLE_CONTACTS, new String[]{
                        PersistenceConstants.COLUMN_ID,
                        PersistenceConstants.COLUMN_ADDRESS,
                        PersistenceConstants.COLUMN_NAME,
                        PersistenceConstants.COLUMN_NICKNAME,
                        PersistenceConstants.COLUMN_EMAIL
                }, PersistenceConstants.COLUMN_SESSION_ID + " = ?",
                new String[]{String.valueOf(sessionId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DBContactEntity contact = new DBContactEntity();
        contact.setId(Long.parseLong(cursor.getString(0)));
        contact.setSessionId(sessionId);
        contact.setAddress(cursor.getString(1));
        contact.setName(cursor.getString(2));
        contact.setNickName(cursor.getString(3));
        contact.setEmail(cursor.getString(4));

        Log.i(LOG, "getIDbEntityById -> LEAVE contact=" + contact);
        return contact;
    }

    @Override
    public boolean addDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "addDbEntity -> ENTER dbEntity=" + dbEntity);

        boolean retVal = false;

        if (dbEntity instanceof DBContactEntity) {

            DBContactEntity dbContactEntity = (DBContactEntity) dbEntity;

            ContentValues values = new ContentValues();
            values.put(PersistenceConstants.COLUMN_SESSION_ID, dbContactEntity.getSessionId());
            values.put(PersistenceConstants.COLUMN_ADDRESS, dbContactEntity.getAddress());
            values.put(PersistenceConstants.COLUMN_NAME, dbContactEntity.getName());
            values.put(PersistenceConstants.COLUMN_NICKNAME, dbContactEntity.getNickName());
            values.put(PersistenceConstants.COLUMN_EMAIL, dbContactEntity.getEmail());

            mSQLiteDatabase.insert(PersistenceConstants.TABLE_CONTACTS, null, values);
            mSQLiteDatabase.close();

            retVal = true;
        }

        Log.i(LOG, "addDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    @Override
    public int updateDbEntity(IDbEntity dbEntity) {
        Log.i(LOG, "updateMyContact -> ENTER myProfile=" + dbEntity);
        int retVal = -1;

        if (dbEntity instanceof DBContactEntity) {

            DBContactEntity contactEntity = (DBContactEntity) dbEntity;

            if (contactEntity.getName() != null && !contactEntity.getName().isEmpty()) {
                if (contactEntity.getNickName() == null || contactEntity.getNickName().isEmpty()) {
                    contactEntity.setNickName(contactEntity.getName());
                }

                ContentValues values = new ContentValues();
                values.put(PersistenceConstants.COLUMN_NAME, contactEntity.getName());
                values.put(PersistenceConstants.COLUMN_NICKNAME, contactEntity.getNickName());
                values.put(PersistenceConstants.COLUMN_EMAIL, contactEntity.getEmail());

                retVal = mSQLiteDatabase.update(PersistenceConstants.TABLE_CONTACTS, values, PersistenceConstants.COLUMN_ID +
                                " = ? and " + PersistenceConstants.COLUMN_SESSION_ID + " = ?",
                        new String[]{String.valueOf(contactEntity.getId()),
                                String.valueOf(contactEntity.getSessionId())
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

        if (dbEntity instanceof DBContactEntity) {

            DBContactEntity contactEntity = (DBContactEntity) dbEntity;

            mSQLiteDatabase.delete(PersistenceConstants.TABLE_CONTACTS, PersistenceConstants.COLUMN_ID + " = ? and " +
                            PersistenceConstants.COLUMN_SESSION_ID + " = ? and " +
                            PersistenceConstants.COLUMN_ADDRESS + " = ? ",
                    new String[]{String.valueOf(contactEntity.getId()),
                            String.valueOf(contactEntity.getSessionId()),
                            String.valueOf(contactEntity.getAddress())
                    });

            mSQLiteDatabase.close();
        }

        Log.i(LOG, "deleteDbEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }
}
