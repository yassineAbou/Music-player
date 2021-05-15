package com.example.mynavdrawer.Controllers.Activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mynavdrawer.Controllers.Fragments.MusicsFragment;
import com.example.mynavdrawer.Controllers.Fragments.PlaylistFragment;
import com.example.mynavdrawer.Models.MusicFiles;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Views.MusicAdapter;
import com.example.mynavdrawer.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import static com.example.mynavdrawer.Controllers.Fragments.MusicsFragment.contacts;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    ActivityMainBinding binding;
    //FOR FRAGMENTS
    private Fragment fragmentMusics;
    private Fragment fragmentPlaylist;
    //FOR DATAS
    private static final int FRAGMENT_MUSICS = 1;
    private static final int FRAGMENT_PLAYLIST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        // Show First Fragment
        this.showFirstFragment();

    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id){
            case R.id.drawer_musics:
                this.showFragment(FRAGMENT_MUSICS);
                break;
            case R.id.drawer_playlist:
                this.showFragment(FRAGMENT_PLAYLIST);
                break;

            default:
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }



    // ---------------------
    // CONFIGURATION
    // ---------------------

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_search) return true;
        return super.onOptionsItemSelected(item);

    }

    // Configure Toolbar
    private void configureToolBar(){
        setSupportActionBar(binding.toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView(){
        binding.navView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // Show first fragment when activity is created
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (visibleFragment == null){
            // Show News Fragment
            this.showFragment(FRAGMENT_MUSICS);
            // Mark as selected the menu item corresponding to MusicsFragment
            binding.navView.setCheckedItem(R.id.drawer_musics);
        }
    }

    // Show fragment according an Identifier
    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_MUSICS:
                this.showMusicsFragment();
                break;
            case FRAGMENT_PLAYLIST:
                this.showPlaylistFragment();
                break;
            default:
                break;
        }
    }

    // ---

    // Create each fragment page and show it
    private void showPlaylistFragment(){
        if (this.fragmentPlaylist == null) this.fragmentPlaylist = PlaylistFragment.newInstance();
        this.startTransactionFragment(this.fragmentPlaylist);
        getSupportActionBar().setTitle("Playlist");
    }

    private void showMusicsFragment(){
        if (this.fragmentMusics == null) this.fragmentMusics = MusicsFragment.newInstance();
        this.startTransactionFragment(this.fragmentMusics);
        getSupportActionBar().setTitle("Musics");
    }

    // ---

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();
        }
    }


}