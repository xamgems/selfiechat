package com.amgems.selfiechat;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

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

    private ColorMatrix mSaturationMatrix;

    /** Root container view of this drawer Fragment **/
    private View mDrawerRootView;

    private ImageView mUserCover;
    private BitmapDrawable mCoverDrawable;

    private ImageView mUserAvatar;
    private RoundedBitmapDrawable mAvatarDrawable;

    private DrawerLayout mDrawerLayout;

    public NavigationDrawerFragment() {
        // Required empty public constructor
        mSaturationMatrix = new ColorMatrix();
        mSaturationMatrix.setSaturation(0.25f);
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
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mUserCover = (ImageView) layout.findViewById(R.id.drawer_cover);
        mUserAvatar = (ImageView) layout.findViewById(R.id.image_user_avatar);
        setUpAvatar(mUserAvatar, R.drawable.stub_avatar);
        setUpCover(mUserCover, R.drawable.drawer_cover);
        return layout;
    }

    private void setUpCover(ImageView host, int drawableResId) {
        mCoverDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), drawableResId));
        mCoverDrawable.setAntiAlias(true);
        mSaturationMatrix.setSaturation(0.1f);
        mCoverDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        host.setImageDrawable(mCoverDrawable);
    }

    //TODO(zac): Create an ImageView subclass that can preform this mask automagically
    private void setUpAvatar(ImageView host, int drawableResId) {
        mAvatarDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), drawableResId));
        mAvatarDrawable.setAntiAlias(true);
        mAvatarDrawable.setCornerRadius(mAvatarDrawable.getIntrinsicWidth() / 2);
        mSaturationMatrix.setSaturation(0.25f);
        mAvatarDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
        host.setImageDrawable(mAvatarDrawable);
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

                TimeInterpolator fastOutLinearIn = new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float x) {
                        return (float) (6 * Math.pow(x, 2) - 8 * Math.pow(x, 3) + 3 * Math.pow(x, 4));
                    }
                };
                // Animates saturation of avatar
                ValueAnimator avatarSatAnim = ValueAnimator.ofFloat(0.25f, 0.95f);
                avatarSatAnim.setDuration(800);
                avatarSatAnim.setInterpolator(fastOutLinearIn);
                avatarSatAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float satValue = (float) animation.getAnimatedValue();
                        mSaturationMatrix.setSaturation(satValue);
                        mAvatarDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
                    }
                });

                ValueAnimator coverSatAnim = ValueAnimator.ofFloat(0.1f, 0.60f);
                coverSatAnim.setDuration(1000);
                coverSatAnim.setInterpolator(fastOutLinearIn);
                coverSatAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float satValue = (float) animation.getAnimatedValue();
                        mSaturationMatrix.setSaturation(satValue);
                        mCoverDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
                    }
                });

                AnimatorSet satAnimationSet = new AnimatorSet();
                satAnimationSet.playTogether(avatarSatAnim, coverSatAnim);
                satAnimationSet.start();

                // Animates opacity of avatar
                ViewPropertyAnimator opacityAnim = mUserAvatar.animate().alpha(1.0f).translationX(0);
                opacityAnim.setDuration(700);
                opacityAnim.setInterpolator(fastOutLinearIn);
                opacityAnim.start();

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                // Resets state of animated views
                mSaturationMatrix.setSaturation(0.25f);
                mAvatarDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
                mSaturationMatrix.setSaturation(0.1f);
                mCoverDrawable.setColorFilter(new ColorMatrixColorFilter(mSaturationMatrix));
                mUserAvatar.setTranslationX(-mUserAvatar.getRight());

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
