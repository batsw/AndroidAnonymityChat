package com.batsw.anonimitychat.appManagement;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.chat.constants.ChatModelConstants;
import com.batsw.anonimitychat.chat.management.ChatController;
import com.batsw.anonimitychat.persistence.DatabaseHelper;
import com.batsw.anonimitychat.persistence.entities.DBChatEntity;
import com.batsw.anonimitychat.persistence.entities.DBContactEntity;
import com.batsw.anonimitychat.persistence.entities.DBMyProfileEntity;
import com.batsw.anonimitychat.persistence.util.IDbEntity;
import com.batsw.anonimitychat.persistence.util.PersistenceConstants;
import com.batsw.anonimitychat.tor.bundle.TorConstants;
import com.batsw.anonimitychat.tor.bundle.TorProcessManager;
import com.batsw.anonimitychat.util.AppConstants;

import java.util.UUID;

/**
 * Created by tudor on 5/13/2017.
 */

public class AppController {
    private static final String LOG = AppController.class.getSimpleName();

    private static AppController mInstance;

    private AppCompatActivity mMainScreenActivity = null;
    private static Context mCurrentActivityContext = null;

    private static boolean isIncomingChatConnection = false;

    private DatabaseHelper mDatabaseHelper;
    private TorProcessManager mTorProcessManager;
    private TextView mTorStatusCarrier;

    private String mMyTorAddress = "";

    private boolean isFirstRun = true;

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
        mDatabaseHelper.triggerInsertDefaultMyProfile();

//mDatabaseHelper.onOpen();

//Network Manager init
        mTorProcessManager = new TorProcessManager(mMainScreenActivity, mTorStatusCarrier);

        handleChatController();

        Log.i(LOG, "initBackend -> LEAVE");
    }

    public void startNetworkConnection() {
        Log.i(LOG, "startNetworkConnection -> ENTER");

        final String networkStatus = mTorStatusCarrier.getText().toString().trim();

        if (!mTorProcessManager.isTorBundleStarted() && (networkStatus.equals(TorConstants.TOR_BUNDLE_STOPPED))) {
            mTorProcessManager.startTorBundle();

            //        TODO: call this here?
            detectFirstRun();
        }

        Log.i(LOG, "startNetworkConnection -> LEAVE");
    }

    public void stopNetworkConnection() {
        Log.i(LOG, "stopNetworkConnection -> ENTER");

        mTorProcessManager.stopTorBundle();

        Log.i(LOG, "stopNetworkConnection -> LEAVE");
    }

    private void detectFirstRun() {
        Log.i(LOG, "detectFirstRun -> LEAVE");

        mTorStatusCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "detectFirstRun.onTextChanged -> LEAVE charSequence=" + charSequence);

                if (isFirstRun) {
                    final StringBuilder sb = new StringBuilder(charSequence.length());
                    sb.append(charSequence);
                    String textViewText = sb.toString();

                    if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                        handleFirstRun();
                        isFirstRun = false;
                    }
                }
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

                retVal = true;
            }
        }

        Log.i(LOG, "firstRunCheck -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private void handleChatController() {
        Log.i(LOG, "initChatController -> ENTER");
        mTorStatusCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "mTorStatusCarrier.addTextChangedListener -> ENTER");

                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String textViewText = sb.toString();

                if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    Log.i(LOG, "tor bundle has started ... initializing Chat Controller");
                    ChatController.getInstance();
                    ChatController.getInstance().setMyAddress(mTorProcessManager.getTorAddress());
                    ChatController.getInstance().setCurrentActivityContext(mMainScreenActivity.getApplicationContext());
                    ChatController.getInstance().initializeChatConnectionManagement();
                }

                //Meaning that TOR is either starting or Stopped
                if (textViewText.equals(TorConstants.TOR_BUNDLE_STOPPED)) {
                    Log.i(LOG, "tor bundle has stopped ... clearing Chat Controller resources");
                    // ChatController managed resources --- what is to be set to default

                    //TODO: Must clear out the resources held by ChatController .....
                    // when the tor bundle is stopped and ther started and stopped again for each stop the resources must be cleaned

                    ChatController.cleanUp();
                }

                Log.i(LOG, "mTorStatusCarrier.addTextChangedListener -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (mTorStatusCarrier.getText().equals(TorConstants.TOR_BUNDLE_STARTED)) {
            ChatController.getInstance().setCurrentActivityContext(mMainScreenActivity.getApplicationContext());
        }

        Log.i(LOG, "initChatController -> LEAVE");
    }


    // see if it works to remotely update contents of network connection status textViews
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

    //TODO: check this ...
    public void setCurrentActivityContext(Context context) {
        Log.i(LOG, "setCurrentActivityContext -> ENTER context=" + context);

        mCurrentActivityContext = context;

        Log.i(LOG, "setCurrentActivityContext -> LEAVE");
    }

    //    TODO: all DB interaction
    public DBMyProfileEntity getMyProfile() {
        Log.i(LOG, "getMyProfile -> ENTER");
        DBMyProfileEntity retVal = null;

        retVal = mDatabaseHelper.getMyProfileOperations().getAllIDbEntity().isEmpty() ?
                null : (DBMyProfileEntity) mDatabaseHelper.getMyProfileOperations().getAllIDbEntity().get(0);
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
            contact.setSessionId(generateSessionId());
            mDatabaseHelper.getContactsOperations().addDbEntity(contactEntity);

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

        final DBContactEntity contact = (DBContactEntity) mDatabaseHelper.getContactsOperations().getIDbEntityByAddress(contactEntity.getAddress());

        // if the contact to be updated is found
        if (contact != null) {

            contact.setName(contactEntity.getName());
            contact.setNickName(contactEntity.getNickName());
            contact.setEmail(contactEntity.getEmail());

            mDatabaseHelper.getContactsOperations().updateDbEntity(contact);

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
            final boolean deletedAllMessages = mDatabaseHelper.getChatMessagesOperations().deleteAllMessagesForSessionId(contact.getSessionId());
            Log.i(LOG, "all messages deleted -> " + deletedAllMessages);

            if (deletedAllMessages) {
                DBChatEntity chatEntity = new DBChatEntity();
                chatEntity.setSessionId(contact.getSessionId());
                final boolean deletedAllChats = mDatabaseHelper.getChatsOperations().deleteDbEntity(chatEntity);
                Log.i(LOG, "all chats deleted -> " + deletedAllChats);

                if (deletedAllChats) {
                    final boolean deletedContact = mDatabaseHelper.getContactsOperations().deleteDbEntity(contact);
                    Log.i(LOG, "contact deleted -> " + deletedContact);

                    if (deletedContact) {
                        retVal = true;
                    } else {
                        //TODO: popup with ccould not delete the contact
                        Log.i(LOG, "TODO: popup with ccould not delete the contact");
                    }
                } else {
                    //TODO: popup with ccould not delete all chats with contact
                    Log.i(LOG, "TODO: popup with ccould not delete all chats with contact");
                }
            } else {
//TODO: popup with ccould not delete all messages from contact
                Log.i(LOG, "TODO: popup with ccould not delete all messages from contact");
            }
        } else {
            //TODO: popup with contact not found
            Log.i(LOG, "TODO: popup with contact not found");
            retVal = false;
        }

        Log.i(LOG, "deleteContact -> LEAVE retVal=" + retVal);
        return retVal;
    }

    private long generateSessionId() {
        Log.i(LOG, "generateSessionId -> ENTER");

        long retVal = UUID.randomUUID().getMostSignificantBits();

        Log.i(LOG, "generateSessionId -> LEAVE retVal=" + retVal);
        return retVal;
    }

//    TODO create fontAwesome loading method here
}
