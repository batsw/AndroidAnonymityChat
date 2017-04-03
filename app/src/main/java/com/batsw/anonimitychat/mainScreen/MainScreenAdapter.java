package com.batsw.anonimitychat.mainScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by tudor on 3/28/2017.
 */

public class MainScreenAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public MainScreenAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
