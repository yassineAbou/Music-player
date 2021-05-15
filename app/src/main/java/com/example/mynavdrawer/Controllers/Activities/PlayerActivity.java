package com.example.mynavdrawer.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.mynavdrawer.Models.MusicFiles;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Views.MusicAdapter;
import com.example.mynavdrawer.databinding.ActivityPlayerBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.example.mynavdrawer.Controllers.Fragments.MusicsFragment.contacts;


@SuppressWarnings("ALL")
public class PlayerActivity extends AppCompatActivity  {

    private ActivityPlayerBinding binding;
    private int mPosition;
    private double current_pos, total_duration;
    private static Uri uri;
    private static MediaPlayer mediaplayer = new MediaPlayer();
    private boolean shuffeBoolean = false, repeatBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (!contacts.isEmpty()) {
            getPosition();
            playAudio(mPosition);
            setAudio();
            prevAudioButton();
            nextAudioButton();
            setPause();
        }

    }





    private void getPosition() {
        mPosition = getIntent().getIntExtra("index",-1);
    }

    //play audio file
    private void playAudio(int pos) {
       //  mediaplayer = new MediaPlayer();
         binding.playPause.setImageResource(R.drawable.ic_pause);
         binding.SongName.setText(contacts.get(mPosition).getTitle());
         binding.singerName.setText(contacts.get(mPosition).getArtist());


        try {
            mediaplayer.stop();
            mediaplayer.reset();
            mediaplayer.setDataSource(contacts.get(pos).getPath());
            mediaplayer.prepareAsync();
            mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        setAudioProgress();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void setAudioProgress() {
       current_pos = mediaplayer.getCurrentPosition();
       total_duration = mediaplayer.getDuration();

       //display the audio duration
       binding.songdurationplayed.setText(timeCoversion((long)current_pos));
       binding.songdurationtotal.setText(timeCoversion((long)total_duration));
       binding.seekbar.setMax((int) total_duration);
       final Handler handler = new Handler();

       Runnable runnable = new Runnable() {
           @Override
           public void run() {
                try {
                    current_pos = mediaplayer.getCurrentPosition();
                    binding.songdurationplayed.setText(timeCoversion((long)current_pos));
                    binding.seekbar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
           }
       };
       handler.postDelayed(runnable, 1000);
    }


    private String timeCoversion(long value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
           audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    /*
      1- Control seebar
      2- Play next audio automatically
      3- Repeat audio
      4- Shuffle mediaPlayer sogns
     */
    private void setAudio() {
        //1
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mediaplayer.seekTo((int)current_pos);
            }
        });

        //2
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextAudio();
            }
        });

        //3
        binding.repeatOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeatBoolean) {
                    repeatBoolean = false;
                    binding.repeatOff.setImageResource(R.drawable.ic_repeat);
                } else {
                    repeatBoolean = true;
                    binding.repeatOff.setImageResource(R.drawable.ic_repeat_on);

                }
            }
        });

        //4
        binding.shuffleOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffeBoolean) {
                    shuffeBoolean = false;
                    binding.shuffleOff.setImageResource(R.drawable.ic_shuffle);
                } else {
                    shuffeBoolean = true;
                    binding.shuffleOff.setImageResource(R.drawable.ic_shuffle_on);
                }
            }
        });
    }

    //Play previous audio
    private void prevAudioButton() {
        binding.skipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffeBoolean &&!repeatBoolean){
                    Random rand=new Random();
                    mPosition =rand.nextInt((contacts.size()-1)+1);
                }
                else if(!shuffeBoolean && !repeatBoolean){
                    if(mPosition-1<0){
                        mPosition = contacts.size()-1;
                    }
                    else{
                        mPosition = mPosition-1;
                    }
                }
                playAudio(mPosition);
            }

        });
    }

    //Play next audio
    private void nextAudioButton() {
        binding.skipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextAudio();
            }
        });
    }

    private void nextAudio() {
        if(shuffeBoolean && !repeatBoolean){
            Random rand=new Random();
            mPosition =rand.nextInt((contacts.size()-1)+1);
        }
        else if(!shuffeBoolean && !repeatBoolean){
            mPosition =((mPosition+1)%contacts.size());
        }
        playAudio(mPosition);
    }

    //pause audio
    private void setPause() {
        binding.playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaplayer.isPlaying()) {
                    mediaplayer.pause();
                    binding.playPause.setImageResource(R.drawable.ic_play_arrow);
                } else {
                    mediaplayer.start();
                    binding.playPause.setImageResource(R.drawable.ic_pause);
                }
            }
        });
    }


}

















