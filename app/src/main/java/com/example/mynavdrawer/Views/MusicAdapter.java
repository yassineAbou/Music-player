package com.example.mynavdrawer.Views;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.mynavdrawer.Controllers.Activities.MainActivity;
import com.example.mynavdrawer.Controllers.Activities.PlayerActivity;
import com.example.mynavdrawer.Models.MusicFiles;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.databinding.CustomMusicfilesItemBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.mynavdrawer.Controllers.Fragments.MusicsFragment.contacts;

/**
 * Created by Yassine Abou on 4/21/2021.
 */
@SuppressWarnings("ALL")
public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder>    {
    @SuppressWarnings("unused")
    ArrayList<MusicFiles> contactItems = contacts;
    private RequestManager glide;
    private Context musicContext;


    public MusicAdapter() {}

    @SuppressWarnings("unused")
    public MusicAdapter(ArrayList<MusicFiles> contactItems1, RequestManager glide, Context musicContext) {
       contactItems = contactItems1;
        this.glide = glide;
        this.musicContext = musicContext;

    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomMusicfilesItemBinding binding = CustomMusicfilesItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        MusicViewHolder holder = new MusicViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.updateWithItemUser(contactItems.get(position), glide);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(musicContext, PlayerActivity.class);
                intent.putExtra("index",position);
                musicContext.startActivity(intent);
            }
        });

        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(musicContext, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                deleteFile(position, v);
                                break;
                        }

                        return true;

                    }
                });

               popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
       return (contactItems == null) ? 0 : contactItems.size();
    }

    //--------------
    // Delete File
    //--------------

    private void deleteFile(int position, View v) {

        scanaddedFile(contactItems.get(position).getPath());
        contactItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, contactItems.size());
        Snackbar.make(v, "File Deleted!", Snackbar.LENGTH_LONG).show();

    }

    private void scanaddedFile(String path) {
        try {
            MediaScannerConnection.scanFile(musicContext, new String[] { path },
                    null, new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            musicContext.getContentResolver()
                                    .delete(uri, null, null);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
