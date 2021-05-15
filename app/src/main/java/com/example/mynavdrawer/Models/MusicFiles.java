package com.example.mynavdrawer.Models;

import android.net.Uri;

/**
 * Created by Yassine Abou on 4/21/2021.
 */
@SuppressWarnings("unused")
public class MusicFiles {
    private String path;
    private String title;
    private String artist;
    private String album;
    private long duration;


    public MusicFiles(String path, String title, String artist, String album, long duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public MusicFiles() {
    }


    //--Getters
    public String getAlbum() { return album; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getPath() { return path; }
    public long getDuration() { return duration; }


    //--Setters
    public void setAlbum(String album) { this.album = album; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setPath(String path) { this.path = path; }
    public void setDuration(long duration) { this.duration = duration; }

}

