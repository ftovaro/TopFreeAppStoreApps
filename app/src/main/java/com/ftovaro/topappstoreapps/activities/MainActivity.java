package com.ftovaro.topappstoreapps.activities;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.ftovaro.topappstoreapps.utils.CategoriesOptions;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private MenuItem mItemSelected;
    private AppsListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupDrawer();
        setupFragment();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.top20Apps));
        MenuItem item = navigationView.getMenu().findItem(R.id.top20Apps);
        item.setChecked(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item == null || drawerLayout == null) return false;
        if (mItemSelected != null ) mItemSelected.setChecked(false);
        mItemSelected = item;

        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.top20Apps:
                fragment.updateInfo(CategoriesOptions.TOP20APPS.ordinal());
                toolbar.setTitle(getString(R.string.top20apps));
                return true;
            case R.id.education:
                fragment.updateInfo(CategoriesOptions.EDUCATION.ordinal());
                toolbar.setTitle(getString(R.string.education));
                return true;
            case R.id.entertainment:
                fragment.updateInfo(CategoriesOptions.ENTERTAINMENT.ordinal());
                toolbar.setTitle(getString(R.string.entertainment));
                return true;
            case R.id.games:
                fragment.updateInfo(CategoriesOptions.GAMES.ordinal());
                toolbar.setTitle(getString(R.string.games));
                return true;
            case R.id.music:
                fragment.updateInfo(CategoriesOptions.MUSIC.ordinal());
                toolbar.setTitle(getString(R.string.music));
                return true;
            case R.id.navigation:
                fragment.updateInfo(CategoriesOptions.NAVIGATION.ordinal());
                toolbar.setTitle(getString(R.string.navigation));
                return true;
            case R.id.photo_and_video:
                fragment.updateInfo(CategoriesOptions.PHOTO_AND_VIDEO.ordinal());
                toolbar.setTitle(getString(R.string.photo_and_video));
                return true;
            case R.id.social_networking:
                fragment.updateInfo(CategoriesOptions.SOCIAL_NETWORKING.ordinal());
                toolbar.setTitle(getString(R.string.social_networking));
                return true;
            case R.id.travel:
                fragment.updateInfo(CategoriesOptions.TRAVEL.ordinal());
                toolbar.setTitle(getString(R.string.travel));
                return true;
        }
        return false;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        navigationView = (NavigationView) findViewById(R.id.drawer_nav_view);
        if (navigationView != null) navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupFragment() {
        fragment = new AppsListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }
}
