package com.ftovaro.topappstoreapps.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ftovaro.topappstoreapps.R;
import com.ftovaro.topappstoreapps.fragments.AppsListFragment;
import com.ftovaro.topappstoreapps.interfaces.CommunicatorListener;
import com.ftovaro.topappstoreapps.utils.CategoriesOptions;
import com.ftovaro.topappstoreapps.utils.InfoShower;

/**
 * Main activity of the application.
 */
public class MainActivity extends AppCompatActivity implements CommunicatorListener,
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private MenuItem mItemSelected;
    private AppsListFragment mFragment;
    private boolean isRefreshing;
    private boolean isScreenLarge;
    private CoordinatorLayout mCoordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

        setupScreenOrientation();
        setupToolbar();
        setupDrawer();
        setupFragment();
        setupCoordinator();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        isRefreshing = false;
        onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.top20Apps));
        MenuItem item = mNavigationView.getMenu().findItem(R.id.top20Apps);
        item.setChecked(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                isRefreshing = true;
                onNavigationItemSelected(mNavigationView.getMenu().findItem(R.id.top20Apps));
                MenuItem itemNav = mNavigationView.getMenu().findItem(R.id.top20Apps);
                itemNav.setChecked(true);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item == null || mDrawerLayout == null) return false;
        if (mItemSelected != null ) mItemSelected.setChecked(false);
        mItemSelected = item;

        mDrawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.top20Apps:
                mFragment.updateInfo(CategoriesOptions.TOP20APPS.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.top20apps));
                isRefreshing = false;
                return true;
            case R.id.education:
                mFragment.updateInfo(CategoriesOptions.EDUCATION.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.education));
                return true;
            case R.id.entertainment:
                mFragment.updateInfo(CategoriesOptions.ENTERTAINMENT.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.entertainment));
                return true;
            case R.id.games:
                mFragment.updateInfo(CategoriesOptions.GAMES.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.games));
                return true;
            case R.id.music:
                mFragment.updateInfo(CategoriesOptions.MUSIC.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.music));
                return true;
            case R.id.navigation:
                mFragment.updateInfo(CategoriesOptions.NAVIGATION.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.navigation));
                return true;
            case R.id.photo_and_video:
                mFragment.updateInfo(CategoriesOptions.PHOTO_AND_VIDEO.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.photo_and_video));
                return true;
            case R.id.social_networking:
                mFragment.updateInfo(CategoriesOptions.SOCIAL_NETWORKING.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.social_networking));
                return true;
            case R.id.travel:
                mFragment.updateInfo(CategoriesOptions.TRAVEL.ordinal(), isRefreshing);
                mToolbar.setTitle(getString(R.string.travel));
                return true;
        }
        return false;
    }

    @Override
    public void showInfo() {
        InfoShower.showSnack(mCoordinatorLayout, getString(R.string.connect_to_internet));
    }

    /**
     * Set up a the toolbar of the activity.
     */
    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    /**
     * Set up the NavigationView of the activity.
     **/
    private void setupDrawer() {
        mNavigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        if (mNavigationView != null) mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.open,
                R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    /**
     * Set up the fragment with the list of data.
     */
    private void setupFragment() {
        mFragment = new AppsListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsScreenLarge", isScreenLarge);
        mFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mFragment);
        fragmentTransaction.commit();
    }

    /**
     * Set up the screen orientation depending of the screen size.
     */
    private void setupScreenOrientation(){
        if(isScreenLarge()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            isScreenLarge = true;
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            isScreenLarge = false;
        }
    }

    /**
     * Set up the coordinator layout to make possible to show the Snackbar.
     */
    private void setupCoordinator(){
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    /**
     * Determine the screen size.
     * @return true if the screen is large or extra large.
     */
    private boolean isScreenLarge() {
        final int screenSize = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE
                || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }
}
