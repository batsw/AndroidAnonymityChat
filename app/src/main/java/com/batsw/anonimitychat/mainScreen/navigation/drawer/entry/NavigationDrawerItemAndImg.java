package com.batsw.anonimitychat.mainScreen.navigation.drawer.entry;

/**
 * Created by tudor on 4/20/2017.
 */

public class NavigationDrawerItemAndImg extends NavigationDrawerEntry {
    private String title;
    private int iconId;

    public NavigationDrawerItemAndImg(String title, int iconId) {
        this.setTitle(title);
        this.setIconId(iconId);
    }

    public int getIconId() {
        return iconId;
    }

    private void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }
}
