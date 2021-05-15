package com.example.mynavdrawer.Controllers.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.mynavdrawer.Controllers.Activities.MainActivity;
import com.example.mynavdrawer.Models.MusicFiles;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Views.MusicAdapter;
import com.example.mynavdrawer.databinding.FragmentMusicsBinding;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class MusicsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final int REQUEST_PERMISSIONS_CODE_WRITE_STORAGE = 5;
    private FragmentMusicsBinding binding;
    private static MusicAdapter mAdapter;
    private static final String TAG = "MusicsFragment";
    public static ArrayList<MusicFiles> contacts ;
    private ImageView mImageView;


    public MusicsFragment() {
        // Required empty public constructor
    }


    public static MusicsFragment newInstance() {
        MusicsFragment fragment = new MusicsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMusicsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        permission();
        mAdapter = new MusicAdapter(contacts, Glide.with(this), getContext());
        setHasOptionsMenu(true); // Add this!

        return view;
    }



    private void configureRecyclerView() {
        binding.mainRv.setHasFixedSize(true);
        binding.mainRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.mainRv.setAdapter(new MusicAdapter(contacts, Glide.with(this), getContext()));
    }

    //------------------
    // PERMISSION
    //------------------

    private void permission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( //Method of Fragment
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_CODE_WRITE_STORAGE
            );
        } else {
            new MyTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE_WRITE_STORAGE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new MyTask().execute();
            }
        }
    }

    //--Get all audios from contentProvider
    private ArrayList<MusicFiles> getAllAudio(Context context) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // Filter only mp3s, only those marked by the MediaStore to be music and longer than 1 minute
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        // +" AND " + MediaStore.Audio.Media.MIME_TYPE + "= 'audio/mpeg'"

        // `+ " AND " + MediaStore.Audio.Media.DURATION + " > 60000";


        String sortOrder = MediaStore.Audio.AudioColumns.TITLE
                + " COLLATE LOCALIZED ASC";
        Cursor c = context.getContentResolver().query(uri, null, selection, null, sortOrder);

        contacts = new ArrayList<MusicFiles>();

        try{
            c.moveToFirst();
            while (c.moveToNext()) {
                MusicFiles songData = new MusicFiles();

                String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String data = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
                String id = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));


                songData.setTitle(title);
                songData.setAlbum(album);
                songData.setArtist(artist);
                songData.setDuration(duration);
                songData.setPath(data);
                contacts.add(songData);
            }
            c.close();
            Log.d("SIZE", "SIZE: " + contacts.size());

        }catch (Exception e){
            Log.d(TAG,"listOfSongs() "+e.getMessage());
        }

        return contacts;

    }


    //-------------------
    // SEARCH VIEW
    //-------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this); // Fragment implements SearchView.OnQueryTextListener
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement the filter logic
        //mAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    
    //-----------------
    // ASYNCTASK
    //-----------------

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAllAudio(getContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.pb.setVisibility(View.GONE);
            configureRecyclerView();
        }
    }


}