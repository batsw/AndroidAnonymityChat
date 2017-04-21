package com.batsw.anonimitychat.mainScreen.navigation.drawer.entry;

/**
 * Created by tudor on 4/20/2017.
 */

public class NavigationDrawerItem extends NavigationDrawerEntry {
    private String title;

    public NavigationDrawerItem(String title) {
        this.setTitle(title);
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }
}
