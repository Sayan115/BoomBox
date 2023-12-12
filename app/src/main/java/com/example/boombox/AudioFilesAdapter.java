package com.example.boombox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

 class AudioFileAdapter extends RecyclerView.Adapter<AudioFileAdapter.AudioFileViewHolder> {
    // ArrayList to store the audio files
    private ArrayList<AudioFile> audioFiles;

    // Constructor
    public AudioFileAdapter (ArrayList<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }

    // Inner class to hold the views of each item
    public static class AudioFileViewHolder extends RecyclerView.ViewHolder {
        // TextView to display the name of the audio file
        public TextView nameTextView;

        // Constructor
        public AudioFileViewHolder (View itemView) {
            super (itemView);
            // Initialize the TextView
            nameTextView = itemView.findViewById (R.id.audio_name);
        }
    }

    // Method to create a new ViewHolder
    @Override
    public AudioFileViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View itemView = LayoutInflater.from (parent.getContext ()).inflate (R.layout.audio_list_item, parent, false);
        // Return a new ViewHolder
        return new AudioFileViewHolder (itemView);
    }

    // Method to bind the data to the ViewHolder
    @Override
    public void onBindViewHolder (AudioFileViewHolder holder, int position) {
        // Get the audio file at the given position
        AudioFile audioFile = audioFiles.get (position);
        // Set the name of the audio file to the TextView
        holder.nameTextView.setText (audioFile.getName ());
    }

    // Method to get the number of items in the adapter
    @Override
    public int getItemCount () {
        return audioFiles.size ();
    }
}
