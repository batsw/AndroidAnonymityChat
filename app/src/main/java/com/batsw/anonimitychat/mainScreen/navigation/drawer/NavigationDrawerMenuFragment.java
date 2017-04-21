package com.batsw.anonimitychat.mainScreen.navigation.drawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerEntry;

import java.util.List;

/**
 * Created by tudor on 4/20/2017.
 */

public class NavigationDrawerMenuFragment extends Fragment {
    private View root;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.main_screen_navigation_drawer_menu, container, false);
        return root;
    }

    public void init(DrawerLayout drawerLayout, final Toolbar toolbar, List<NavigationDrawerEntry> drawerEntries) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),
                drawerLayout, toolbar, R.string.main_screen_drawer_open, R.string.main_screen_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mRecyclerView = (RecyclerView) root.findViewById(R.id.main_screen_navigation_items_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        NavigationDrawerMenuAdapter adapter = new NavigationDrawerMenuAdapter(getActivity(), drawerEntries);
        mRecyclerView.setAdapter(adapter);
    }
}
