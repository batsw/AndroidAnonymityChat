package com.batsw.anonimitychat.mainScreen.navigation.drawer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.batsw.anonimitychat.R;
import com.batsw.anonimitychat.appManagement.AppController;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerDivider;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerEntry;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItem;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerItemAndImg;
import com.batsw.anonimitychat.mainScreen.navigation.drawer.entry.NavigationDrawerToogle;
import com.batsw.anonimitychat.mainScreen.settings.activities.SettingsAboutActivity;
import com.batsw.anonimitychat.mainScreen.settings.activities.SettingsProfileActivity;
import com.batsw.anonimitychat.mainScreen.util.MainScreenConstants;
import com.batsw.anonimitychat.tor.bundle.TorConstants;

import java.util.List;

/**
 * Created by tudor on 4/20/2017.
 */

public class NavigationDrawerMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG = NavigationDrawerMenuAdapter.class.getSimpleName();

    private Context mContext;

    private List<NavigationDrawerEntry> data;
    private LayoutInflater inflater;


    public NavigationDrawerMenuAdapter(Context context, List<NavigationDrawerEntry> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);

        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemLayoutView;
        switch (viewType) {
            case 0:
                itemLayoutView = inflater.inflate(R.layout.main_screen_navigation_drawer_item_and_img, viewGroup, false);
                ItemAndImgViewHolder holder = new ItemAndImgViewHolder(itemLayoutView);
                return holder;
            case 1:
                itemLayoutView = inflater.inflate(R.layout.main_screen_navigation_drawer_divider, viewGroup, false);
                DividerViewHolder dividerViewHolder = new DividerViewHolder(itemLayoutView);
                return dividerViewHolder;
            case 2:
                itemLayoutView = inflater.inflate(R.layout.main_screen_navigation_drawer_item, viewGroup, false);
                ItemViewHolder itemViewHolder = new ItemViewHolder(itemLayoutView);
                return itemViewHolder;
            case 3:
                itemLayoutView = inflater.inflate(R.layout.main_screen_navigation_drawer_toogle, viewGroup, false);
                ToggleViewHolder toggleViewHolder = new ToggleViewHolder(itemLayoutView);
                return toggleViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NavigationDrawerEntry item = data.get(position);

        if (item instanceof NavigationDrawerItemAndImg) {
            final ItemAndImgViewHolder viewHolder = (ItemAndImgViewHolder) holder;
            viewHolder.mTitle.setText(((NavigationDrawerItemAndImg) item).getTitle());
            viewHolder.mImageView.setImageResource(((NavigationDrawerItemAndImg) item).getIconId());

            viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    msniiOnClickHandling(viewHolder.mTitle.getText().toString());
                }
            });
        }

        if (item instanceof NavigationDrawerItem) {
            final ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.mTitle.setText(((NavigationDrawerItem) item).getTitle());

            viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    msniOnClickHandling(viewHolder.mTitle.getText().toString());
                }
            });
        }

        if (item instanceof NavigationDrawerToogle) {
            final ToggleViewHolder viewHolder = (ToggleViewHolder) holder;

            //TODO: if title MainScreenConstants.NAVIGATION_TOOGLE then
            // I must load the Network connection status and if connected
            // then toogle is Checked else is not checked

            viewHolder.mTitle.setText(((NavigationDrawerToogle) item).getTitle());
            viewHolder.mSwitch.setChecked(((NavigationDrawerToogle) item).isChecked());
            viewHolder.mSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    msntOnClickHandling(viewHolder.mTitle.getText().toString(), viewHolder.mSwitch);
                }
            });
        }
    }

    //main_screen_navigation_drawer_item_and_img
    public void msniiOnClickHandling(String title) {
        Log.i(LOG, "msniiOnClickHandling -> ENTER");

        switch (title) {
            case MainScreenConstants.NAVIGATION_PROFILE:
                Log.i(LOG, "-> start my profile");
                //TODO: start the Profile activity
                Intent settingsProfileActivityIntent = SettingsProfileActivity.makeIntent(mContext);
//                addContactActivityIntent.putExtra(,);
                settingsProfileActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(settingsProfileActivityIntent);
                break;
            case MainScreenConstants.NAVIGATION_NETWORK:
                Log.i(LOG, "-> start network");
                //TODO: start the Network activity
                break;
            case MainScreenConstants.NAVIGATION_STORAGE:
                Log.i(LOG, "-> start storage");
                //TODO: start the Storage activity
//                Intent addContactActivityIntent = SettingsAboutActivity.makeIntent(mContext);
////                addContactActivityIntent.putExtra(,);
//                addContactActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(addContactActivityIntent);
                break;
            default:
                Log.i(LOG, "main_screen_navigation_drawer_item_and_img.msniiOnClickHandling -> DEFAULT - do nothing");
        }
        Log.i(LOG, "msniiOnClickHandling -> LEAVE");
    }

    //main_screen_navigation_drawer_item
    private void msniOnClickHandling(String title) {
        Log.i(LOG, "msniOnClickHandling -> ENTER");

        switch (title) {
            case MainScreenConstants.NAVIGATION_ABOUT:
                Log.i(LOG, "main_screen_navigation_drawer_item.msniOnClickHandling -> start About");
                //TODO: start the About activity
                Intent settingsAboutActivityIntent = SettingsAboutActivity.makeIntent(mContext);
                settingsAboutActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(settingsAboutActivityIntent);

                break;
            default:
                Log.i(LOG, "main_screen_navigation_drawer_item.msniOnClickHandling -> DEFAULT - do nothing");
        }
        Log.i(LOG, "msniOnClickHandling -> LEAVE");
    }

    //main_screen_navigation_drawer_toogle
    public void msntOnClickHandling(String title, Switch toogle) {
        Log.i(LOG, "msntOnClickHandling -> ENTER");

        switch (title) {
            case MainScreenConstants.NAVIGATION_TOOGLE:
                Log.i(LOG, "network connection tootgle used");

                if (toogle.isChecked()) {
                    Log.i(LOG, "toogle ON");

                    AppController.getInstanceParameterized(null).startNetworkConnection();
                } else if (!toogle.isChecked()) {
                    Log.i(LOG, "toogle OFF");

                    AppController.getInstanceParameterized(null).stopNetworkConnection();
                } else {
                    Log.i(LOG, "toogle UNKNOWN");
                }

                break;
            default:
                Log.i(LOG, "main_screen_navigation_drawer_toogle.msntOnClickHandling -> DEFAULT - do nothing");
        }
        Log.i(LOG, "msntOnClickHandling -> LEAVE");
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof NavigationDrawerItemAndImg)
            return 0;

        if (data.get(position) instanceof NavigationDrawerDivider)
            return 1;

        if (data.get(position) instanceof NavigationDrawerItem)
            return 2;

        if (data.get(position) instanceof NavigationDrawerToogle)
            return 3;

        return -1;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemAndImgViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;
        final ImageView mImageView;

        public ItemAndImgViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.main_screen_navigation_item_name);
            mImageView = (ImageView) itemView.findViewById(R.id.main_screen_navigation_item_image);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.main_screen_navigation_item_title);
        }
    }

    class ToggleViewHolder extends RecyclerView.ViewHolder {
        final TextView mTitle;
        final Switch mSwitch;

        public ToggleViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.main_screen_navigation_toogle_name);
            mSwitch = (Switch) itemView.findViewById(R.id.main_screen_navigation_toogle);
        }
    }
}
