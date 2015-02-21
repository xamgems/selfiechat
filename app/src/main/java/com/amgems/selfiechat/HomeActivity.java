package com.amgems.selfiechat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amgems.selfiechat.view.CameraFragment;


public class HomeActivity extends ActionBarActivity {

    /** Root view of this activity housing the navigation drawer **/
    private DrawerLayout mDrawerLayout;
    /** Fragment that manages the navigation drawer **/
    private NavigationDrawerFragment mDrawerFragment;
    /** Friends List fragment **/
    private FriendsListFragment mFriendsFragment;
    /** Camera fragment **/
    private CameraFragment mCameraFragment;
    /** Pager to page fragments **/
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        setUp();
    }

    public void setUp() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mDrawerFragment = (NavigationDrawerFragment)
                fragmentManager.findFragmentById(R.id.navigation_fragment);
        mDrawerFragment.setUp(R.id.navigation_fragment, mDrawerLayout, getSupportActionBar());

        mPager = (ViewPager) findViewById(R.id.fragment_pager);
        mPager.setAdapter(new FragmentPager(fragmentManager));
        mFriendsFragment = (FriendsListFragment)
                fragmentManager.findFragmentById(R.id.friends_list_fragment);

        mCameraFragment = (CameraFragment)
                fragmentManager.findFragmentById(R.id.camera_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        setTitle(R.string.app_name);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class FragmentPager extends FragmentPagerAdapter {
        private static int TAB_COUNT = 2;
        private Fragment[] fragments;

        public FragmentPager(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[TAB_COUNT];
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                if (position == 0) {
                    fragments[0] = CameraFragment.newInstance();
                } else if (position == 1) {
                    fragments[1] = FriendsListFragment.newInstance();
                }
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
