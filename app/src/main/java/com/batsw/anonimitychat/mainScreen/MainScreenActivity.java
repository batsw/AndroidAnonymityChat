package com.batsw.anonimitychat.mainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.NavigationDrawerMenuFragment;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerDivider;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerEntry;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItem;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItemAndImg;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerToogle;
import com.batsw.anonimitychat.mainScreen.popup.NetworkPopupActivity;
import com.batsw.anonimitychat.mainScreen.tabs.TabChats;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.mainScreen.util.MainScreenConstants;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 3/27/2017.
 */

public class MainScreenActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private static final String LOG = MainScreenActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private TabHost mTabHost;

    private NetworkPopupActivity mNetworkPopupActivity;
    private TextView mTextView;

    private boolean mReceivedIntent = false;

    NavigationDrawerMenuFragment mDrawerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i(LOG, "onCreate -> ENTER");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        initLayout();
        initTabs();
        initNavigationDrawerMenu();

        initBackend();

        mTextView = new TextView(this);
        AppController.getInstanceParameterized(null).updateWithNetworkConnectionStatus(mTextView);

        mNetworkPopupActivity = new NetworkPopupActivity(this);

        AppController.getInstanceParameterized(null).setCurrentActivityContext(this);

        Log.i(LOG, "onCreate -> LEAVE");
    }

    private void initLayout() {
        Log.i(LOG, "init -> ENTER");

        mViewPager = (ViewPager) findViewById(R.id.main_screen_view_pager);

        mTabHost = (TabHost) findViewById(R.id.main_screen_tab_host);
        mTabHost.setup();

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new TabContacts());
//        listFragments.add(new TabChats());

        MainScreenAdapter mainScreenAdapter = new MainScreenAdapter(getSupportFragmentManager(), listFragments);
        mViewPager.setAdapter(mainScreenAdapter);

        mViewPager.setOnPageChangeListener(this);

        Log.i(LOG, "init -> LEAVE");
    }

    private void initTabs() {
        Log.i(LOG, "initTabs -> ENTER");

//        String[] tabNames = {"Contacts", "Chat List"};
        String[] tabNames = {"Contacts"};

        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = mTabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new TabContent(getApplicationContext()));
            mTabHost.addTab(tabSpec);
        }

        mTabHost.setOnTabChangedListener(this);

        Log.i(LOG, "initTabs -> LEAVE");
    }

    private void initNavigationDrawerMenu() {
        Log.i(LOG, "initNavigationDrawerMenu -> ENTER");

        List<NavigationDrawerEntry> drawerEntries = new ArrayList<>();
//        drawerEntries.add(new NavigationDrawerToogle(MainScreenConstants.NAVIGATION_TOOGLE));
        drawerEntries.add(new NavigationDrawerDivider());
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_PROFILE, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_NETWORK, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_STORAGE, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerDivider());
        drawerEntries.add(new NavigationDrawerItem(MainScreenConstants.NAVIGATION_ABOUT));

        mDrawerFragment = (NavigationDrawerMenuFragment) getSupportFragmentManager().findFragmentById(R.id.main_screen_fragment_navigation_drawer);
        mDrawerFragment.init((android.support.v4.widget.DrawerLayout) findViewById(R.id.main_screen_layout),
                null, drawerEntries);

        Log.i(LOG, "initNavigationDrawerMenu -> LEAVE");
    }

    private void initBackend() {
        Log.i(LOG, "initBackend -> ENTER");

        AppController.getInstanceParameterized(this);

        Log.i(LOG, "initBackend -> LEAVE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG, "onCreateOptionsMenu -> ENTER");

        MenuInflater actionBarMenuInflater = getMenuInflater();
        actionBarMenuInflater.inflate(R.menu.main_screen_toolbar, menu);

        final MenuItem networkItem = menu.getItem(0);

        mTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(LOG, "onCreateOptionsMenu.mTextView.onTextChanged -> ENTER charSequence=" + charSequence);

                final StringBuilder sb = new StringBuilder(charSequence.length());
                sb.append(charSequence);
                String textViewText = sb.toString();

                if (textViewText.equals(TorConstants.TOR_BUNDLE_STARTED)) {
                    networkItem.setIcon(R.drawable.simple_onion_green);
                } else if (textViewText.equals(TorConstants.TOR_BUNDLE_IS_STARTING)) {
                    networkItem.setIcon(R.drawable.simple_onion_blue);
                } else if (textViewText.equals(TorConstants.TOR_BUNDLE_STOPPED)) {
                    networkItem.setIcon(R.drawable.simple_onion_white);
                }

                Log.i(LOG, "onCreateOptionsMenu.mTextView.onTextChanged -> LEAVE");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Log.i(LOG, "onCreateOptionsMenu -> LEAVE");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(LOG, "onOptionsItemSelected -> ENTER item=" + item);
        int id = item.getItemId();

        switch (id) {
            case R.id.action_network:
                Log.i(LOG, "action_network");

                mNetworkPopupActivity.show();

                break;

//            case R.id.action_search:
//                Log.i(LOG, "action_search");
//                break;

            default:
                Log.i(LOG, "default toolbar ... never here");
                break;
        }

        Log.i(LOG, "onOptionsItemSelected -> LEAVE");
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedPage) {
        mTabHost.setCurrentTab(selectedPage);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String s) {
        int selectedPage = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(selectedPage);

        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.main_screen_scroll_view);
        View tabView = mTabHost.getCurrentTabView();
        int scrollPosition = tabView.getLeft() - (horizontalScrollView.getWidth() - tabView.getWidth()) / 2;
        horizontalScrollView.smoothScrollBy(scrollPosition, 0);
    }

    private class TabContent implements TabHost.TabContentFactory {
        Context context;

        public TabContent(Context context) {
            this.context = context;
        }

        @Override
        public View createTabContent(String s) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumWidth(0);
            return fakeView;
        }
    }

    public void moveToChatsTab() {
        Log.i(LOG, "moveToChatsTab -> ENTER");
        mViewPager.setCurrentItem(1, true);
        Log.i(LOG, "moveToChatsTab -> LEAVE");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(LOG, "onNewIntent -> LEAVE");
        super.onNewIntent(intent);

        mReceivedIntent = true;

        Log.i(LOG, "onNewIntent -> LEAVE");
    }

    @Override
    protected void onStart() {
        Log.i(LOG, "onStart -> ENTER");
        super.onStart();
        Log.i(LOG, "do nothing");
        Log.i(LOG, "onStart -> LEAVE");
    }

    @Override
    protected void onResume() {
        Log.i(LOG, "onResume -> ENTER");
        super.onResume();

        Log.i(LOG, "do nothing");
        Log.i(LOG, "onResume -> LEAVE");
    }

    @Override
    protected void onStop() {
        Log.i(LOG, "onStop -> ENTER");

//        if (isHomeButtonPressed) {
//            Log.i(LOG, "HOME button detected");
//
//            //means that HOME was pressed
//            AppController.getInstanceParameterized(null).stopNetworkConnection();
//
//            isHomeButtonPressed = false;
//        } else {
//            isHomeButtonPressed = false;
//        }

        super.onStop();
        Log.i(LOG, "after super.onStop() --- do nothing");
        Log.i(LOG, "onStop -> LEAVE");
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG, "onDestroy -> ENTER");

        AppController.getInstanceParameterized(null).stopNetworkConnection();
        Log.i(LOG, "TOR Bundle is stopped");

        super.onDestroy();

        Log.i(LOG, "onDestroy -> LEAVE");
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        Log.i(LOG, "onActionModeFinished -> ENTER");
        super.onActionModeFinished(mode);
        Log.i(LOG, "do nothing");
        Log.i(LOG, "onActionModeFinished -> LEAVE");
    }

    @Override
    protected void onPause() {
        //TODO: this is called when HOME button is pressed
        Log.i(LOG, "onPause -> ENTER");

//        isHomeButtonPressed = true;

        super.onPause();
        Log.i(LOG, "after super.onPause() --- do nothing");
        Log.i(LOG, "onPause -> LEAVE");
    }

    @Override
    public void finish() {
        //TODO: this is called when Back button is called (closing the app)
        Log.i(LOG, "finish -> ENTER");
        super.finish();
        Log.i(LOG, "do nothing");
        Log.i(LOG, "finish -> LEAVE");
    }

    @Override
    public void finishActivity(int requestCode) {
        Log.i(LOG, "finishActivity -> ENTER");
        super.finishActivity(requestCode);
        Log.i(LOG, "do nothing");
        Log.i(LOG, "finishActivity -> LEAVE");
    }

    /**
     * Creating Intent for the activity of this class
     *
     * @param context
     * @return Intent
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, MainScreenActivity.class);
    }
}
