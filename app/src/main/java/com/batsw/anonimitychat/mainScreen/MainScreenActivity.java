package com.batsw.anonimitychat.mainScreen;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.NavigationDrawerMenuFragment;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerDivider;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerEntry;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItem;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItemAndImg;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerToogle;
import com.batsw.anonimitychat.mainScreen.tabs.TabChats;
import com.batsw.anonimitychat.mainScreen.tabs.TabContacts;
import com.batsw.anonimitychat.mainScreen.util.MainScreenConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tudor on 3/27/2017.
 */

public class MainScreenActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {

    private static final String LOG = MainScreenActivity.class.getSimpleName();

    private ViewPager mViewPager;

    private TabHost mTabHost;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i(LOG, "onCreate -> ENTER");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_activity);

        //TODO: what for?
        //Loading fontAwesome
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "font_awesome/fontawesome.ttf");

        init();

        initTabs();

        initNavigationDrawerMenu();

        Log.i(LOG, "onCreate -> LEAVE");
    }

    private void init() {
        Log.i(LOG, "init -> ENTER");

        mViewPager = (ViewPager) findViewById(R.id.main_screen_view_pager);

        mTabHost = (TabHost) findViewById(R.id.main_screen_tab_host);
        mTabHost.setup();

        List<Fragment> listFragments = new ArrayList<>();
        listFragments.add(new TabContacts());
        listFragments.add(new TabChats());

        MainScreenAdapter mainScreenAdapter = new MainScreenAdapter(getSupportFragmentManager(), listFragments);
        mViewPager.setAdapter(mainScreenAdapter);

        mViewPager.setOnPageChangeListener(this);

        Log.i(LOG, "init -> LEAVE");
    }

    private void initTabs() {
        Log.i(LOG, "initTabs -> ENTER");

        String[] tabNames = {"Contacts", "Chat List"};

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
        drawerEntries.add(new NavigationDrawerToogle(MainScreenConstants.NAVIGATION_TOOGLE));
        drawerEntries.add(new NavigationDrawerDivider());
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_PROFILE, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_NETWORK, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerItemAndImg(MainScreenConstants.NAVIGATION_STORAGE, R.drawable.ic_info_outline_white));
        drawerEntries.add(new NavigationDrawerDivider());
        drawerEntries.add(new NavigationDrawerItem(MainScreenConstants.NAVIGATION_ABOUT));

        NavigationDrawerMenuFragment drawerFragment = (NavigationDrawerMenuFragment) getSupportFragmentManager().findFragmentById(R.id.main_screen_fragment_navigation_drawer);
        drawerFragment.init((android.support.v4.widget.DrawerLayout) findViewById(R.id.main_screen_layout),
                null, drawerEntries);

        Log.i(LOG, "initNavigationDrawerMenu -> LEAVE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_screen_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
