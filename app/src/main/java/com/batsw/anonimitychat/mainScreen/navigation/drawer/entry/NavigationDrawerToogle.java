package com.batsw.anonimitychat.mainScreen.navigation.drawer.entry;

/**
 * Created by tudor on 4/20/2017.
 */

public class NavigationDrawerToogle extends NavigationDrawerEntry {
    private String title;
    private boolean checked;

    public NavigationDrawerToogle(String title) {
        this.setTitle(title);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }
}
