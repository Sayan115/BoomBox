package com.example.boombox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class AudioList extends AppCompatActivity {

    RecyclerView allAudioRecycler;
    // ArrayList to store the audio files
    ArrayList<AudioFile> audioFiles;

    // Method to scan the device storage for audio files
    private void scanAudioFiles () {
        // Initialize the ArrayList
        audioFiles = new ArrayList<> ();

        // Create a cursor to query the internal audio files
        Cursor cursor = getContentResolver ().query (
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, // Uri to query
                null, // Projection (null for all columns)
                null, // Selection (null for all rows)
                null, // Selection arguments
                null  // Sort order
        );

        // Check if the cursor is not null and has some rows
        if (cursor != null && cursor.moveToFirst ()) {
            // Get the column indices of the data we need
            int nameColumn = cursor.getColumnIndex (MediaStore.Audio.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndex (MediaStore.Audio.Media.DATA);
            int durationColumn = cursor.getColumnIndex (MediaStore.Audio.Media.DURATION);

            // Loop through the rows and add each audio file to the ArrayList
            do {
                // Get the data from the cursor
                String name = cursor.getString (nameColumn).replace(".ogg","");
                String path = cursor.getString (pathColumn);
                long duration = cursor.getLong (durationColumn);

                // Create an AudioFile object and add it to the ArrayList
                AudioFile audioFile = new AudioFile (name, path, duration);
                audioFiles.add (audioFile);
            } while (cursor.moveToNext ()); // Move to the next row

            // Close the cursor
            cursor.close ();
        }

        // Create a cursor to query the external audio files
        Cursor cursor2 = getContentResolver ().query (
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // Uri to query
                null, // Projection (null for all columns)
                null, // Selection (null for all rows)
                null, // Selection arguments
                null  // Sort order
        );

        // Check if the cursor is not null and has some rows
        if (cursor2 != null && cursor2.moveToFirst ()) {
            // Get the column indices of the data we need
            int nameColumn = cursor2.getColumnIndex (MediaStore.Audio.Media.DISPLAY_NAME);
            int pathColumn = cursor2.getColumnIndex (MediaStore.Audio.Media.DATA);
            int durationColumn = cursor2.getColumnIndex (MediaStore.Audio.Media.DURATION);

            // Loop through the rows and add each audio file to the ArrayList
            do {
                // Get the data from the cursor
                String name = cursor2.getString (nameColumn).replace(".mp3","").replace(".ogg","");
                String path = cursor2.getString (pathColumn);
                long duration = cursor2.getLong (durationColumn);

                // Create an AudioFile object and add it to the ArrayList
                AudioFile audioFile = new AudioFile (name, path, duration);
                audioFiles.add (audioFile);
            } while (cursor2.moveToNext ()); // Move to the next row

            // Close the cursor
            cursor2.close ();


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        allAudioRecycler=findViewById(R.id.allAudioRecycler);
                        allAudioRecycler.setLayoutManager(new LinearLayoutManager(AudioList.this));
                        scanAudioFiles();
                        allAudioRecycler.setAdapter(new AudioFileAdapter(audioFiles));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
}