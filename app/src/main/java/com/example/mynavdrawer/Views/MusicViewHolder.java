package com.example.mynavdrawer.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.mynavdrawer.Models.MusicFiles;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.databinding.CustomMusicfilesItemBinding;

import java.lang.ref.WeakReference;

/**
 * Created by Yassine Abou on 4/21/2021.
 */
@SuppressWarnings("ALL")
public class MusicViewHolder extends RecyclerView.ViewHolder  {

    CustomMusicfilesItemBinding binding;


    public MusicViewHolder(@NonNull CustomMusicfilesItemBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public void updateWithItemUser(MusicFiles musicItem, RequestManager glide) {
        if (musicItem.getTitle() != null) {
            binding.itemTitle.setText(musicItem.getTitle());
        }
        glide.load(R.drawable.turntable).apply(RequestOptions.circleCropTransform()).into(binding.itemImage);



        }


}
