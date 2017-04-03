package com.batsw.anonimitychat.mainScreen.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.batsw.anonimitychat.R;

/**
 * Created by tudor on 3/29/2017.
 */

public class TabContats extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_tab, container, false);

        return rootView;
    }
}
