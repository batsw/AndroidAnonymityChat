package com.batsw.anonimitychat.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.batsw.anonimitychat.persistence.entities.DBMyProfileEntity;
import com.batsw.anonimitychat.persistence.operations.DbChatMessagesOperations;
import com.batsw.anonimitychat.persistence.operations.DbChatsOperations;
import com.batsw.anonimitychat.persistence.operations.DbContactsOperations;
import com.batsw.anonimitychat.persistence.operations.DbMyProfileOperations;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;
import com.batsw.anonimitychat.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 4/27/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = DatabaseHelper.class.getSimpleName();

    private static final String CREATE_TABLE_MY_PROFILE = "create table " + PersistenceConstants.TABLE_MY_PROFILE + " (\n" +
            PersistenceConstants.COLUMN_ID + " integer primary key autoincrement,\n" +
            PersistenceConstants.COLUMN_MY_ADDRESS + " text not null,\n" +
            PersistenceConstants.COLUMN_MY_NAME + " text null,\n" +
            PersistenceConstants.COLUMN_MY_NICKNAME + " text null,\n" +
            PersistenceConstants.COLUMN_MY_EMAIL + " text null,\n" +
            PersistenceConstants.COLUMN_TBE_PID + " integer not null," +
            PersistenceConstants.COLUMN_TBE_PROCESS + " text null);";

    private static final String CREATE_TABLE_CONTACTS = "create table " + PersistenceConstants.TABLE_CONTACTS + " (\n" +
            PersistenceConstants.COLUMN_ID + " integer primary key autoincrement,\n" +
            PersistenceConstants.COLUMN_SESSION_ID + " integer not null unique,\n" +
            PersistenceConstants.COLUMN_NAME + " text null,\n" +
            PersistenceConstants.COLUMN_ADDRESS + " text not null,\n" +
            PersistenceConstants.COLUMN_NICKNAME + " text null,\n" +
            PersistenceConstants.COLUMN_EMAIL + " text null);";

    private static final String CREATE_TABLE_CHATS = "create table " + PersistenceConstants.TABLE_CHATS + " (\n" +
            PersistenceConstants.COLUMN_ID + " integer primary key autoincrement,\n" +
            PersistenceConstants.COLUMN_CONTACT_SESSION_ID + " integer not null unique,\n" +
            PersistenceConstants.COLUMN_CHAT_NAME + " text not null,\n" +
            PersistenceConstants.COLUMN_HISTORY_CLEANUP_TIME + " integer not null,\n" +
            " FOREIGN KEY (" + PersistenceConstants.COLUMN_CONTACT_SESSION_ID + ") REFERENCES " +
            PersistenceConstants.TABLE_CONTACTS + "(" + PersistenceConstants.COLUMN_SESSION_ID + "));";

    private static final String CREATE_TABLE_CHATS_MESSAGES = "create table " + PersistenceConstants.TABLE_CHATS_MESSAGES + " (\n" +
            PersistenceConstants.COLUMN_ID + " integer primary key autoincrement,\n" +
            PersistenceConstants.COLUMN_CONTACT_SESSION_ID + " integer not null,\n" +
            PersistenceConstants.COLUMN_MESSAGE + " text not null,\n" +
            PersistenceConstants.COLUMN_TIMESTAMP + " integer not null,\n" +
            " FOREIGN KEY (" + PersistenceConstants.COLUMN_CONTACT_SESSION_ID + ") REFERENCES " +
            PersistenceConstants.TABLE_CONTACTS + "(" + PersistenceConstants.COLUMN_SESSION_ID + "));";

    // all entities operations members
    private DbMyProfileOperations mMyProfileOperations;
    private DbContactsOperations mContactsOperations;
    private DbChatMessagesOperations mChatMessagesOperations;
    private DbChatsOperations mChatsOperations;

    private SQLiteDatabase mSqLiteDatabase;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.i(LOG, "constructor -> ENTER");

        Log.i(LOG, "constructor -> LEAVE");
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i(LOG, "onOpen -> ENTER db=" + db);
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
//            //(OR)
//            db.setForeignKeyConstraintsEnabled(true);

            Log.i(LOG, "Enabled FK constraints");
        }
        Log.i(LOG, "onOpen -> LEAVE");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG, "onCreate -> ENTER");

        mSqLiteDatabase = sqLiteDatabase;

        mSqLiteDatabase.execSQL(CREATE_TABLE_MY_PROFILE);
        mSqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
        mSqLiteDatabase.execSQL(CREATE_TABLE_CHATS);
        mSqLiteDatabase.execSQL(CREATE_TABLE_CHATS_MESSAGES);

        Log.i(LOG, "Database Created !!!");

        this.onOpen(mSqLiteDatabase);

//        insertDefaultMyProfile();
//
//        Log.i(LOG, "My Default Profile Created !!!");

        Log.i(LOG, "onCreate -> LEAVE");
    }

    public void initOperations() {
        Log.i(LOG, "initOperations -> ENTER");

        if (mSqLiteDatabase != null) {
            this.onOpen(mSqLiteDatabase);

            mMyProfileOperations = new DbMyProfileOperations(mSqLiteDatabase);
            mContactsOperations = new DbContactsOperations(mSqLiteDatabase);
            mChatsOperations = new DbChatsOperations(mSqLiteDatabase);
            mChatMessagesOperations = new DbChatMessagesOperations(mSqLiteDatabase);
        } else {
            mMyProfileOperations = new DbMyProfileOperations(this.getWritableDatabase());
            mContactsOperations = new DbContactsOperations(this.getWritableDatabase());
            mChatsOperations = new DbChatsOperations(this.getWritableDatabase());
            mChatMessagesOperations = new DbChatMessagesOperations(this.getWritableDatabase());
        }

        Log.i(LOG, "initOperations -> LEAVE");
    }

    public void triggerInsertDefaultMyProfile() {
        Log.i(LOG, "triggerInsertDefaultMyProfile -> ENTER");

        final String myAddress = mMyProfileOperations.getMyAddress(1);
        if (myAddress == null || myAddress.isEmpty()) {
            insertDefaultMyProfile();
        }

        Log.i(LOG, "triggerInsertDefaultMyProfile -> LEAVE");
    }

    private void insertDefaultMyProfile() {
        Log.i(LOG, "insertDefaultMyProfile -> ENTER");

        DBMyProfileEntity dbMyProfileEntity = new DBMyProfileEntity();
        dbMyProfileEntity.setMyAddress(AppConstants.MY_DEFAULT_ADDRESS);
        dbMyProfileEntity.setMyName(AppConstants.MY_DEFAULT_NAME);
        dbMyProfileEntity.setMyNickName(AppConstants.MY_DEFAULT_NICKNAME);

        final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PersistenceConstants.COLUMN_MY_ADDRESS, dbMyProfileEntity.getMyAddress());
        values.put(PersistenceConstants.COLUMN_MY_NAME, dbMyProfileEntity.getMyName());
        values.put(PersistenceConstants.COLUMN_MY_NICKNAME, dbMyProfileEntity.getMyNickName());
        values.put(PersistenceConstants.COLUMN_TBE_PID, String.valueOf(0));

        sqLiteDatabase.insert(PersistenceConstants.TABLE_MY_PROFILE, null, values);
//        sqLiteDatabase.close();

        Log.i(LOG, "insertDefaultMyProfile -> LEAVE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(LOG, "onUpgrade -> ENTER");
        Log.i(LOG, "Drop all tables");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistenceConstants.TABLE_MY_PROFILE);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistenceConstants.TABLE_CHATS_MESSAGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistenceConstants.TABLE_CHATS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersistenceConstants.TABLE_CONTACTS);

        Log.i(LOG, "Create new version's tables");
//        sqLiteDatabase.close();
//        onCreate(sqLiteDatabase);

        Log.i(LOG, "onUpgrade -> LEAVE");
    }

    public DbMyProfileOperations getMyProfileOperations() {
        return mMyProfileOperations;
    }

    public DbContactsOperations getContactsOperations() {
        return mContactsOperations;
    }

    public DbChatMessagesOperations getChatMessagesOperations() {
        return mChatMessagesOperations;
    }

    public DbChatsOperations getChatsOperations() {
        return mChatsOperations;
    }

}
