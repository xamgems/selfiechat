package com.amgems.selfiechat;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavigationDrawerFragment extends Fragment {

    /** Remembers the previous selected drawer item on restoring saved instance state **/
    public static final String STATE_SELECTED_ITEM = "state_selected_item";

    /** Preference entry to determine if the user has learned how to expand the nav drawer **/
    public static final String PREF_DRAWER_LEARNED = "pref_drawer_learned";
    /** State determining if the user has learned to expand the nav drawer **/
    private boolean mDrawerLearned;

    /** Manages state and functionality that is shared between the ActionBar and DrawerLayout **/
    ActionBarDrawerToggle mDrawerToggle;

    private boolean mFromSavedInstanceState;
    /** Position of the navigation drawer's currently selected item **/
    private int mSelectedItem;

    /** Root container view of this drawer Fragment **/
    private View mDrawerRootView;

    private DrawerLayout mDrawerLayout;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDrawerLearned = preferences.getBoolean(PREF_DRAWER_LEARNED, false);

        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(STATE_SELECTED_ITEM);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Allow the nav drawer to interact with the action bar
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_ITEM, mSelectedItem);
    }

    public void setUp(int fragmendId, DrawerLayout drawerLayout, ActionBar actionBar) {
        mDrawerRootView = getActivity().findViewById(fragmendId);

        mDrawerLayout = drawerLayout;
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.string.nav_drawer_open, R.string.nav_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mDrawerLearned) {
                    mDrawerLearned = true;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    preferences.edit().putBoolean(PREF_DRAWER_LEARNED, true).apply();
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mDrawerLearned && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mDrawerRootView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Delay syncing state until after the caller has completed its Looper time.
        // This is done so that if the caller is the onCreate of an activity, the state syncing
        // can be done after onCreate has returned.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
