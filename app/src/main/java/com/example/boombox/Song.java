package com.example.boombox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Song extends AppCompatActivity{
    ImageView prev, play, next;
    TextView song_title;
    ArrayList<AudioFile> songs;
    MediaPlayer mediaPlayer;
    String songName;
    String[] position;
    SeekBar seekBar;
    Thread updateSeek;
    int fileNo;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        song_title = findViewById(R.id.song_name);
        prev = findViewById(R.id.prev);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);

        seekBar=findViewById(R.id.seekBar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songs");
        songName = intent.getStringExtra("current song");
        song_title.setText(songName);
        Bundle bundle1=intent.getExtras();
        position = (String[]) bundle1.getStringArray("position");
        fileNo=intent.getIntExtra("fileNo",0);
        //Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, Uri.parse(position[fileNo]));
        seekBar.setMax(mediaPlayer.getDuration());

        if (mediaPlayer != null)
            mediaPlayer.start();
        else
            Log.d("TAG", "onCreate:"+position);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek=new Thread(){
            @Override
            public void run() {
                int start=0;
                try {
                    while(start<mediaPlayer.getDuration()){
                        start=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(start);
                        sleep(300);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                }
                else{
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(fileNo!=0)
                {
                    fileNo--;
                }
                else {
                    fileNo=songs.size()-1;
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(position[fileNo]));
                if (mediaPlayer != null)
                    mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                song_title.setText(songs.get(fileNo).getName());

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(fileNo!=songs.size()-1)
                {//Log.d("TAG", "onCreate:before string.valueof");
                    fileNo=fileNo+1;

                }
                else {
                    fileNo=0;
                }

                Log.d("TAG", "onCreate:"+fileNo);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(position[fileNo]));
                Log.d("YourTag", "onCreate:before:"+fileNo);
                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());

                song_title.setText(songs.get(fileNo).getName());

            }
        });
    }
}