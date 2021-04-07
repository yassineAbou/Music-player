package com.example.mynavdrawer.Controllers.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.Controllers.Fragments.NewsFragment;
import com.example.mynavdrawer.Controllers.Fragments.ParamsFragment;
import com.example.mynavdrawer.Controllers.Fragments.ProfileFragment;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding mMainBinding;

    //FOR FRAGMENTS
    //Declare fragment handled by Navigation Drawer
    private Fragment fragmentNews;
    private Fragment fragmentProfile;
    private Fragment fragmentParams;

    //FOR DATAS
    //Identify each fragment with a number
    private static final int FRAGMENT_NEWS = 0;
    private static final int FRAGMENT_PROFILE = 1;
    private static final int FRAGMENT_PARAMS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view =mMainBinding.getRoot();
        setContentView(view);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        // 2 - Show First Fragment
        this.showFirstFragment();
    }

    @Override
    public void onBackPressed() {
        //Handle back click to close menu
        if (mMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.drawer_news :
                showFragment(FRAGMENT_NEWS);
                break;
            case R.id.drawer_profile:
                showFragment(FRAGMENT_PROFILE);
                break;
            case R.id.drawer_settings:
                showFragment(FRAGMENT_PARAMS);
                break;
            default:
                break;
        }

        mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------


    //Configure Toolbar
    private void configureToolBar() {
        setSupportActionBar(mMainBinding.toolbar);
    }

    //Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mMainBinding.drawerLayout, mMainBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mMainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Configure NavigationView
    private void configureNavigationView(){
        mMainBinding.navView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    //Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (visibleFragment == null){
            //Show News Fragment
            this.showFragment(FRAGMENT_NEWS);
            //Mark as selected the menu item corresponding to NewsFragment
            mMainBinding.navView.getMenu().getItem(0).setChecked(true);
        }
    }

    //Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_NEWS :
                this.showNewsFragment();
                break;
            case FRAGMENT_PROFILE:
                this.showProfileFragment();
                break;
            case FRAGMENT_PARAMS:
                this.showParamsFragment();
                break;
            default:
                break;
        }
    }

    //Create each fragment page and show it
    private void showNewsFragment() {
        if (this.fragmentNews == null) {
            this.fragmentNews = NewsFragment.newInstance();
            this.startTransactionFragment(this.fragmentNews);
        }
    }

    private void showParamsFragment(){
        if (this.fragmentParams == null) {
            this.fragmentParams = ParamsFragment.newInstance();
            this.startTransactionFragment(this.fragmentParams);
        }
    }

    private void showProfileFragment() {
        if (this.fragmentProfile == null) {
            this.fragmentProfile = ProfileFragment.newInstance();
        this.startTransactionFragment(this.fragmentProfile);
       }
    }

    //Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();
        }
    }

}