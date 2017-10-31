package com.batsw.anonimitychat.appManagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.mainScreen.MainScreenActivity;
import com.batsw.anonimitychat.mainScreen.entities.ChatEntity;
import com.batsw.anonimitychat.mainScreen.entities.ContactEntity;
import com.batsw.anonimitychat.mainScreen.tabs.TabChats;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.persistence.DatabaseHelper;
import com.batsw.anonimitychat.persistence.cleanup.HistoryCleanupJob;
import com.batsw.anonimitychat.persistence.cleanup.HistoryCleanupManager;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBChatMessageEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.persistence.entities.DBMyProfileEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcessManager;
import com.batsw.anonimitychat.util.AppConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by tudor on 5/13/2017.
 */

public class AppController {
    private static final String LOG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private AppCompatActivity mMainScreenActivity = null;

    private boolean mIsBackended = false;

    private static Context mCurrentActivityContext = null;

    private TabContacts mContactsTab;
    private TabChats mChatsTab;

    private DatabaseHelper mDatabaseHelper;
    private TorProcessManager mTorProcessManager;
    private TextView mTorStatusCarrier;

    private boolean isFirstRun = true;

    private long mBundlePid;
    private String mMyAddress;

    private HistoryCleanupManager mHistoryCleanupManager;

    private ExecutorService mNSNotifTriggExec;

    private AppController(AppCompatActivity mainActivity) {
        mMainScreenActivity = mainActivity;
//        binding the textView to the MainScreen
        mTorStatusCarrier = new TextView(mMainScreenActivity.getApplicationContext());

        initBackend();
    }

    /**
     * To be used with non-null Context the first time this object is created in the most early app time
     * In rest to be used with null Context parameter
     */
    public static synchronized AppController getInstanceParameterized(AppCompatActivity mainActivity) {
        if (mInstance == null) {
            mInstance = new AppController(mainActivity);
        }
        return mInstance;
    }

    /**
     * binding the backend functionality to MainScreen context
     */
    private void initBackend() {
        Log.i(LOG, "initBackend -> ENTER");

//        Database init
        mDatabaseHelper = new DatabaseHelper(mMainScreenActivity.getApplicationContext(), PersistenceConstants.DATABASE_ANONYMITY_CHAT,
                null, PersistenceConstants.DATABASE_VERSION);
        mDatabaseHelper.initOperations();
        mDatabaseHelper.triggerInsertDefaultMyProfile();
        Log.i(LOG, "init -> mDatabaseHelper - DONE");

        mHistoryCleanupManager = new HistoryCleanupManager();

        final AppController appController = this;
        mHistoryCleanupManager.init(appController);
        Log.i(LOG, "init -> mHistoryCleanupManager - DONE");

        mBundlePid = mDatabaseHelper.getMyProfileOperations().getBundlePid(1);
        Log.i(LOG, "bundlePidLong=" + mBundlePid);

//        handling the previous instance of the app which left the bundle process open
        if (mBundlePid > 0) {
            Log.i(LOG, "the previous bundle process needs to be cleaned: mBundlePid=" + mBundlePid);

            Log.e(LOG, "Killing the previous TorProcess");
            android.os.Process.killProcess((int) getBundlePid());
            Log.e(LOG, "succeded. Now updating the current PID");

            updateBundlePid(0);
        }

        mTorProcessManager = new TorProcessManager(mMainScreenActivity, mTorStatusCarrier);
//        TODO: activate automatic TOR connection
//        Automatic TOR connection start
//        final String networkStatus = mTorStatusCarrier.getText().toString().trim();
//
//        if (!mTorProcessManager.isTorBundleStarted() && (networkStatus.equals(TorConstants.TOR_BUNDLE_STOPPED))) {
//            mTorProcessManager.startTorBundle();
//
//            detectFirstRun();
//        }

        Log.i(LOG, "init -> mTorProcessManager - DONE");

// initializing the ChatController and implicitly the TorReceiver
        mMyAddress = getMyProfile().getMyAddress();
        handleChatController();
        Log.i(LOG, "init -> ChatController - DONE");

        triggerNSNotificationThread();
        Log.i(LOG, "init -> triggerNSNotificationThread - DONE");

        Log.i(LOG, "initBackend -> LEAVE");
    }

    public void startNetworkConnection() {
        Log.i(LOG, "startNetworkConnection -> ENTER");

        final String networkStatus = mTorStatusCarrier.getText().toString().trim();

        if (!mTorProcessManager.isTorBundleStarted() && (networkStatus.equals(TorConstants.TOR_BUNDLE_STOPPED))) {
            mTorProcessManager.startTorBundle();

            detectFirstRun();
        }

        Log.i(LOG, "startNetworkConnection -> LEAVE");
    }

    public void stopNSTrigNotifThread(){
        Log.i(LOG, "stopNSTrigNotifThread -> ENTER");
        mNSNotifTriggExec.shutdown();
        mNSNotifTriggExec.shutdownNow();

        mNSNotifTriggExec = null;
        Log.i(LOG, "stopNSTrigNotifThread -> LEAVE");
    }

    public void stopHistoryCleanupJob() {
        Log.i(LOG, "stopHistoryCleanupJob -> ENTER");

        mHistoryCleanupManager.stopAllCleanupJobs();

        Log.i(LOG, "stopHistoryCleanupJob -> LEAVE");
    }

    public void stopNetworkConnection() {
        Log.i(LOG, "stopNetworkConnection -> ENTER");

        mTorProcessManager.stopTorBundle();

        Log.i(LOG, "stopNetworkConnection -> LEAVE");
    }

    private void detectFirstRun() {
        Log.i(LOG, "detectFirstRun -> ENTER");

        mTorStatusCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "detectFirstRun.onTextChanged -> LEAVE charSequence=" + charSequence);

                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String textViewText = sb.toString();

                if (isFirstRun) {
                    if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                        handleFirstRun();
                        isFirstRun = false;

                        mMyAddress = getMyProfile().getMyAddress();
                    }
                }
                if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
//                    handleChatController();
                    ChatController.getInstance().setMyAddress(mMyAddress);
                }

//                if (textViewText.equals(TorConstants.TOR_BUNDLE_STOPPED)) {
//                    Log.i(LOG, "tor bundle has stopped ... clearing Chat Controller resources");
//                    // ChatController managed resources --- what is to be set to default
//
//                    ChatController.cleanUp();
//                }

                Log.i(LOG, "detectFirstRun.onTextChanged -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Log.i(LOG, "detectFirstRun -> LEAVE");
    }

    private boolean handleFirstRun() {
        Log.i(LOG, "firstRunCheck -> ENTER");
        boolean retVal = false;

        if (mTorProcessManager.isTorBundleStarted()) {

            String myAddress = mTorProcessManager.getTorAddress().substring(0, 16);

            if (myAddress.length() == AppConstants.ADDRESS_SIZE) {

                mDatabaseHelper.getMyProfileOperations().updateAddressOnFirstNetworkConnection(myAddress);

                ChatController.getInstance().setMyAddress(mMyAddress);

                retVal = true;
            }
        }

        Log.i(LOG, "firstRunCheck -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private void handleChatController() {
        Log.i(LOG, "handleChatController -> ENTER");

        ChatController.getInstance();
        ChatController.getInstance().setMyAddress(mMyAddress);
        ChatController.getInstance().setCurrentActivityContext(mMainScreenActivity.getApplicationContext());
        ChatController.getInstance().initializeChatConnectionManagement();

        ChatController.getInstance().setCurrentActivityContext(mMainScreenActivity.getApplicationContext());

        Log.i(LOG, "handleChatController -> LEAVE");
    }

    private void triggerNSNotificationThread() {
        Log.i(LOG, "triggerNSNotificationThread -> ENTER");

        mNSNotifTriggExec = Executors.newSingleThreadScheduledExecutor();
        mNSNotifTriggExec.submit(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(AppConstants.NET_STAT_NOTIF_TRIG_INTERVAL_MILIS);
                    } catch (InterruptedException e) {
                        Log.i(LOG, "net status notification triggering loop error: " + e.getMessage(), e);
                    }

                    Log.i(LOG, "triggerNSNotificationThread: -> repeat");

                    if (mIsBackended && !(mTorStatusCarrier.getText()).equals(TorConstants.TOR_BUNDLE_STARTED)) {

                        ((MainScreenActivity) mMainScreenActivity).triggerNotification("Network disconnected", "Network connection unavailable. Please connect to network!");
                        Log.i(LOG, "triggerNSNotificationThread: -> network is not active");
                    }

                }
            }
        });

        Log.i(LOG, "triggerNSNotificationThread -> LEAVE");
    }

    public void updateBundlePid(long newPid) {
        Log.i(LOG, "updateBundlePid -> ENTER newPid=" + newPid);

        mBundlePid = newPid;

        long currentPid = mDatabaseHelper.getMyProfileOperations().getBundlePid(1);

        if (currentPid != newPid) {
            mDatabaseHelper.getMyProfileOperations().updateBundlePid(newPid);
        }

        Log.i(LOG, "updateBundlePid -> LEAVE");
    }

    public long getBundlePid() {
        Log.i(LOG, "getBundlePid -> ENTER");

        Log.i(LOG, "getBundlePid -> LEAVE mBundlePid=" + mBundlePid);
        return mBundlePid;
    }

    public void setContactsTab(TabContacts contactsTab) {
        Log.i(LOG, "setContactsTab -> ENTER");
        mContactsTab = contactsTab;
        Log.i(LOG, "setContactsTab -> LEAVE");
    }

    public void addNewContactToTab(ContactEntity newContactEntity) {
        Log.i(LOG, "addNewContactToTab -> ENTER");
        mContactsTab.addContactToList(newContactEntity);
        Log.i(LOG, "addNewContactToTab -> LEAVE");
    }

    public void removeContactFromTab(ContactEntity newContactEntity) {
        Log.i(LOG, "removeContactFromTab -> ENTER");
        mContactsTab.removeContactFromList(newContactEntity);
        Log.i(LOG, "removeContactFromTab -> LEAVE");
    }

    public void setChatsTab(TabChats chatsTab) {
        Log.i(LOG, "setChatsTab -> ENTER");
        mChatsTab = chatsTab;
        Log.i(LOG, "setChatsTab -> LEAVE");
    }

    private void addNewChatToTab(ChatEntity newChatEntity) {
        Log.i(LOG, "addNewChatToTab -> ENTER newChatEntity=" + newChatEntity);
        mChatsTab.addChatToList(newChatEntity);
        Log.i(LOG, "addNewChatToTab -> LEAVE");
    }

    public void editChatToTab(ChatEntity chatEntity) {
        Log.i(LOG, "editChatToTab -> ENTER chatEntity=" + chatEntity);
        mChatsTab.updateContactList(chatEntity);
        Log.i(LOG, "editChatToTab -> LEAVE");
    }

    public void editContactToTab(ContactEntity contactEntity) {
        Log.i(LOG, "editContactToTab -> ENTER contactEntity=" + contactEntity);
        mContactsTab.updateContactList(contactEntity);
        Log.i(LOG, "editContactToTab -> LEAVE");
    }

    public void addMessageToChatHistory(DBChatMessageEntity chatMessageEntity) {
        Log.i(LOG, "addMessageToChatHistory -> ENTER chatMessageEntity=" + chatMessageEntity);
        mDatabaseHelper.getChatMessagesOperations().addDbEntity(chatMessageEntity);
        Log.i(LOG, "addMessageToChatHistory -> LEAVE");
    }

    public List<IDbEntity> getMessageHistoryForSessionId(long contactSessionId) {
        Log.i(LOG, "getMessageHistory -> ENTER contactSessionId=" + contactSessionId);
        List<IDbEntity> retVal;

        retVal = mDatabaseHelper.getChatMessagesOperations().getAllMessagesForSessionId(contactSessionId);

        Log.i(LOG, "getMessageHistory -> LEAVE retVal=" + retVal + ", retVal.size()=" + retVal.size());
        return retVal;
    }

    public void removeMessagesForChat(long contactSessionId) {
        Log.i(LOG, "removeMessagesForChat -> ENTER contactSessionId=" + contactSessionId);
        mDatabaseHelper.getChatMessagesOperations().deleteAllMessagesForSessionId(contactSessionId);
        Log.i(LOG, "removeMessagesForChat -> LEAVE");
    }

//    public void moveToChatsTab() {
//        Log.i(LOG, "moveToChatsTab -> ENTER");
//        ((MainScreenActivity) mMainScreenActivity).moveToChatsTab();
//        Log.i(LOG, "moveToChatsTab -> LEAVE");
//    }

    public void setChatControllerCurrentActivityContext(Context context) {
        Log.i(LOG, "setChatControllerCurrentActivityContext -> ENTER context=" + context);
        ChatController.getInstance().setCurrentActivityContext(context);
        Log.i(LOG, "setChatControllerCurrentActivityContext -> LEAVE");
    }

    public Context getCurrentActivityContext() {
        return mCurrentActivityContext;
    }

    public void updateWithNetworkConnectionStatus(final TextView networkConnStatusLabel) {
        Log.i(LOG, "getNetworkConnectionStatus -> ENTER networkConnStatusLabel=" + networkConnStatusLabel);

        mTorStatusCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                networkConnStatusLabel.setText(mTorStatusCarrier.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Log.i(LOG, "getNetworkConnectionStatus -> LEAVE");
    }

    public String getNetworkConnectionStatus() {
        Log.i(LOG, "getNetworkConnectionStatus -> ENTER");

        String retVal = mTorStatusCarrier.getText().toString();

        Log.i(LOG, "getNetworkConnectionStatus -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public void triggerNotification(String partnerAddress) {
        Log.i(LOG, "triggerNotification -> ENTER");

        if (mIsBackended) {
            IDbEntity iDbEntityByAddress = mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(partnerAddress);

            String title = "";

            if (iDbEntityByAddress != null) {
                DBContactEntity dbContactEntity = (DBContactEntity) iDbEntityByAddress;
                title = dbContactEntity.getName() + " contacted you!";
            } else {
                title = partnerAddress.substring(0, 16) + " contacted you!";
            }

            ((MainScreenActivity) mMainScreenActivity).triggerNotification(title, "Incoming chat request from your contacts!");

        }

        Log.i(LOG, "triggerNotification -> LEAVE");
    }

    public void setCurrentActivityContext(Context context) {
        Log.i(LOG, "setCurrentActivityContext -> ENTER context=" + context);

        mCurrentActivityContext = context;

        Log.i(LOG, "setCurrentActivityContext -> LEAVE");
    }

    //    TODO: all DB interaction
    public void openDB(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG, "openDB -> ENTER");

        mDatabaseHelper.onOpen(sqLiteDatabase);

        Log.i(LOG, "openDB -> LEAVE");
    }

    public void closeDB() {
        Log.i(LOG, "closeDB -> ENTER");

        mDatabaseHelper.close();

        Log.i(LOG, "closeDB -> LEAVE");
    }

    public DBMyProfileEntity getMyProfile() {
        Log.i(LOG, "getMyProfile -> ENTER");
        DBMyProfileEntity retVal = null;

//        retVal = mDatabaseHelper.getMyProfileOperations().getAllIDbEntity().isEmpty() ?
//                null : (DBMyProfileEntity) mDatabaseHelper.getMyProfileOperations().getAllIDbEntity().get(0);

        retVal = (DBMyProfileEntity) mDatabaseHelper.getMyProfileOperations().getAllIDbEntity().get(0);

        Log.i(LOG, "getMyProfile -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public void updateMyProfile(DBMyProfileEntity myProfileEntity) {
        Log.i(LOG, "updateMyProfile -> ENTER");
        mDatabaseHelper.getMyProfileOperations().updateDbEntity(myProfileEntity);
        Log.i(LOG, "updateMyProfile -> LEAVE");
    }

    public boolean addNewContact(DBContactEntity contactEntity) {
        Log.i(LOG, "addNewContact -> ENTER contactEntity=" + contactEntity);
        boolean retVal;

        final DBContactEntity contact = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(contactEntity.getAddress());

        if (contact == null) {
            contactEntity.setSessionId(generateSessionId());
            mDatabaseHelper.getContactsOperations().addDbEntity(contactEntity);

            addNewChatForContact(contactEntity);

            retVal = true;

        } else {
            retVal = false;
        }

        Log.i(LOG, "addNewContact -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public boolean updateContact(DBContactEntity contactEntity) {
        Log.i(LOG, "addNewContact -> ENTER contactEntity=" + contactEntity);
        boolean retVal;

//        final DBContactEntity contact = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(contactEntity.getAddress());

        final int updateSuccesfull = mDatabaseHelper.getContactsOperations().updateDbEntity(contactEntity);

        if (updateSuccesfull > 0) {
            retVal = true;
        } else {
            retVal = false;
        }

        Log.i(LOG, "addNewContact -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public boolean deleteContact(String contactAddress) {
        Log.i(LOG, "deleteContact -> ENTER contactAddress=" + contactAddress);
        boolean retVal = false;

        final DBContactEntity contact = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(contactAddress);

        // if the contact to be updated is found
        if (contact != null) {
            mDatabaseHelper.getChatMessagesOperations().deleteAllMessagesForSessionId(contact.getSessionId());
            Log.i(LOG, "all messages deleted");

            DBChatEntity chatEntity = new DBChatEntity();
            chatEntity.setSessionId(contact.getSessionId());
            boolean chatDeleted = mDatabaseHelper.getChatsOperations().deleteDbEntity(chatEntity);

            if (chatDeleted) {
                Log.i(LOG, "chat deleted -> ");

                retVal = mDatabaseHelper.getContactsOperations().deleteDbEntity(contact);
                Log.i(LOG, "contact deleted -> ");

                mHistoryCleanupManager.removeHistoryCleanupJob(chatEntity.getSessionId());
                Log.i(LOG, "history cleanup job deleted -> ");

                ContactEntity contactEntity = new ContactEntity(contact.getName(), contact.getSessionId());
                removeContactFromTab(contactEntity);
            }
        }

        Log.i(LOG, "deleteContact -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public DBContactEntity getContactEntity(long sessionId) {
        Log.i(LOG, "getContactEntity -> ENTER sessionId=" + sessionId);
        DBContactEntity retVal = null;

        retVal = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityById(sessionId);

        Log.i(LOG, "getContactEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public DBContactEntity getContactEntity(String address) {
        Log.i(LOG, "getContactEntity -> ENTER address=" + address);
        DBContactEntity retVal = null;

        retVal = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(address);

        Log.i(LOG, "getContactEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private boolean addNewChatForContact(DBContactEntity contactEntity) {
        Log.i(LOG, "addNewChatForContact -> ENTER contactEntity=" + contactEntity);
        boolean retVal;

        DBChatEntity chatEntity = new DBChatEntity();
        chatEntity.setSessionId(contactEntity.getSessionId());
        chatEntity.setChatName(contactEntity.getAddress());
        chatEntity.setHistoryCleanupTime(0);

        retVal = mDatabaseHelper.getChatsOperations().addDbEntity(chatEntity);

        mHistoryCleanupManager.addHistoryCleanupJob(chatEntity.getSessionId(), chatEntity.getHistoryCleanupTime());


//        if (retVal) {
////            TODO: fix chat availability
//            ChatEntity tabChatEntity = new ChatEntity(chatEntity.getSessionId(), chatEntity.getChatName(), false);
//            addNewChatToTab(tabChatEntity);
//        }

        Log.i(LOG, "addNewChatForContact -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public DBChatEntity getChatEntity(long sessionId) {
        Log.i(LOG, "getChatEntity -> ENTER sessionId=" + sessionId);
        DBChatEntity retVal = null;

        retVal = (DBChatEntity) mDatabaseHelper.getChatsOperations().getIDbEntityById(sessionId);

        Log.i(LOG, "getChatEntity -> LEAVE retVal=" + retVal);
        return retVal;
    }

//    public DBChatEntity getChatEntity(String address) {
//        Log.i(LOG, "getChatEntity -> ENTER address=" + address);
//        DBChatEntity retVal = null;
//
//        retVal = (DBChatEntity) mDatabaseHelper.getChatsOperations().getIDbEntityById(sessionId);
//
//        Log.i(LOG, "getChatEntity -> LEAVE retVal=" + retVal);
//        return retVal;
//    }

    public boolean updateChat(DBChatEntity chatEntity) {
        Log.i(LOG, "updateChat -> ENTER chatEntity=" + chatEntity);
        boolean retVal = false;

        if (chatEntity != null) {

            mDatabaseHelper.getChatsOperations().updateDbEntity(chatEntity);

            mHistoryCleanupManager.updateHistoryCleanupJob(chatEntity.getSessionId(), chatEntity.getHistoryCleanupTime());

            retVal = true;
        }

        Log.i(LOG, "updateChat -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public List<IDbEntity> getChatList() {
        Log.i(LOG, "getChatList -> ENTER");
        List<IDbEntity> retVal = new ArrayList();

        retVal = mDatabaseHelper.getChatsOperations().getAllIDbEntity();

        Log.i(LOG, "getChatList -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public List<IDbEntity> getContactList() {
        Log.i(LOG, "getContactList -> ENTER");
        List<IDbEntity> retVal = new ArrayList();

        retVal = mDatabaseHelper.getContactsOperations().getAllIDbEntity();

        Log.i(LOG, "getContactList -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private long generateSessionId() {
        Log.i(LOG, "generateSessionId -> ENTER");

        long retVal = UUID.randomUUID().getMostSignificantBits();

        Log.i(LOG, "generateSessionId -> LEAVE retVal=" + retVal);
        return retVal;
    }

    public void setIsBackended(boolean isBackended) {
        Log.i(LOG, "setIsBackended -> ENTER isBackended=" + isBackended);
        mIsBackended = isBackended;
        Log.i(LOG, "setIsBackended -> LEAVE");
    }

//    TODO create fontAwesome loading method here
//    a generic FA loading method used throughout the app
}
