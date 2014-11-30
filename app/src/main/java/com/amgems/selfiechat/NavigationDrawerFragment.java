package com.amgems.selfiechat;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavigationDrawerFragment extends Fragment {

    /** Manages state and functionality that is shared between the ActionBar and DrawerLayout **/
    ActionBarDrawerToggle mDrawerToggle;

    /** Remembers the previous selected drawer item on restoring saved instance state **/
    public static final String STATE_SELECTED_ITEM = "state_selected_item";

    /** Preference entry to determine if the user has learned how to expand the nav drawer **/
    public static final String PREF_DRAWER_LEARNED = "pref_drawer_learned";

    /** State determining if the user has learned to expand the nav drawer **/
    private boolean mDrawerLearned;

    /** Position of the navigation drawer's currently selected item **/
    private int mSelectedItem;

    private boolean mFromSavedInstanceState;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean mDrawerLearned = preferences.getBoolean(PREF_DRAWER_LEARNED, false);

        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(STATE_SELECTED_ITEM);
            mFromSavedInstanceState = true;
        }

        selectDrawerItem(mSelectedItem);
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
        return inflater.inflate(R.layout.fragment_navigation_drawer_fragment, container, false);
    }

    private void selectDrawerItem(int position) {

    }

}
